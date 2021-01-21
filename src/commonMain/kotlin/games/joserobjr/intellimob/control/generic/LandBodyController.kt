/*
 * IntelliMob, an intelligent plugin to give AI to Java Minecraft Bedrock servers
 * Copyright (C) 2021  José Roberto de Araújo Júnior <joserobjr@powernukkit.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package games.joserobjr.intellimob.control.generic

import games.joserobjr.intellimob.control.api.BodyController
import games.joserobjr.intellimob.coroutines.AI
import games.joserobjr.intellimob.coroutines.CompletedJob
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.*
import games.joserobjr.intellimob.trait.WithEntityPos
import games.joserobjr.intellimob.trait.update
import games.joserobjr.intellimobjvm.atomic.atomic
import kotlinx.coroutines.*
import kotlin.math.sqrt
import kotlin.time.ExperimentalTime

/**
 * @author joserobjr
 * @since 2021-01-21
 */
internal open class LandBodyController(final override val owner: RegularEntity): BodyController {
    private val currentJob = atomic(ActiveWalk(IEntityPos.ZERO, .0, DoubleVectorXZ(.0)) to CompletedJob)

    override fun isMoving(): Boolean = currentJob.get().second.isActive
    
    override fun CoroutineScope.walkTo(pos: WithEntityPos, acceptableDistance: Double, speed: DoubleVectorXZ): Job {
        val (_, job) = currentJob.updateAndGet { oldPair ->
            val (oldActivity, oldJob) = oldPair
            val activity = ActiveWalk(pos.position.toImmutable(), acceptableDistance, speed)
            if (!oldJob.isCompleted && !oldJob.isCancelled) {
                if (oldActivity.isSimilar(activity)) {
                    oldActivity.speed = speed
                    oldActivity.acceptableDistance = acceptableDistance
                    return@updateAndGet oldPair
                }
                oldJob.cancel("Starting another walk")
            }
            activity to launch(Dispatchers.AI) { 
                walk(activity)
            }
        }
        return job
    }
    
    private data class ActiveWalk(val target: EntityPos, var acceptableDistance: Double, var speed: DoubleVectorXZ) {
        fun isSimilar(other: ActiveWalk): Boolean {
            return target.position.toBlockPos() == other.target.position.toBlockPos()
        }
    }
    
    @OptIn(ExperimentalTime::class)
    private suspend fun walk(activity: ActiveWalk) = coroutineScope s@ {
        val path = owner.pathFinder.findPath(owner.world.cache, owner.position.toBlockPos(), activity.target.toBlockPos())
        if (path.isEmpty()) {
            return@s
        }
        while (true) {
            val current = owner.position
            if (current.squaredDistance(activity.target) <= activity.acceptableDistance.squared()) {
                return@s
            }
            val nextNode = path.next(current) ?: return@s
            val nextPos = current.stepTo(nextNode, activity.speed)
            val angle = current.target(nextPos)
            owner.controls.updateHeadAngle(angle, owner.currentStatus.headFastSpeed.apply { PitchYaw(pitch * 3, yaw * 3) })
            owner.update {
                moveTo(nextPos)
            }
            delay(1.ticks)
        }
    }
    
    private fun EntityPos.stepTo(toward: EntityPos, speed: DoubleVectorXZ): EntityPos {
        // Create vector
        var x = x - toward.x
        var z = z - toward.z
        
        // Normalize
        val squaredLen = x.squared() + z.squared()
        if (squaredLen <= 0) {
            return this
        }
        val len = sqrt(squaredLen)
        x /= len
        z /= len

        // Apply speed
        x *= speed.x
        z *= speed.z
        
        // Add to the current position
        return EntityPos(
            this.x + x,
            this.y,
            this.z + z
        )
    }
}
