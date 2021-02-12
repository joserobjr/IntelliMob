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

import com.github.michaelbull.logging.InlineLogger
import games.joserobjr.intellimob.block.BlockSnapshot
import games.joserobjr.intellimob.block.BlockState
import games.joserobjr.intellimob.block.RegularBlock
import games.joserobjr.intellimob.control.api.BodyController
import games.joserobjr.intellimob.coroutines.AI
import games.joserobjr.intellimob.coroutines.CompletedJob
import games.joserobjr.intellimob.entity.EntityFlag
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.*
import games.joserobjr.intellimob.math.angle.PitchYaw
import games.joserobjr.intellimob.math.extensions.squared
import games.joserobjr.intellimob.math.extensions.ticks
import games.joserobjr.intellimob.math.motion.HorizontalVelocity
import games.joserobjr.intellimob.math.motion.IHorizontalVelocity
import games.joserobjr.intellimob.math.motion.IVelocity
import games.joserobjr.intellimob.math.motion.Velocity
import games.joserobjr.intellimob.math.position.entity.EntityPos
import games.joserobjr.intellimob.math.position.entity.IEntityPos
import games.joserobjr.intellimob.pathfinding.Path
import games.joserobjr.intellimob.trait.WithEntityPos
import games.joserobjr.intellimob.trait.update
import games.joserobjr.intellimob.trait.updateAsync
import games.joserobjr.intellimobjvm.atomic.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toSet
import kotlin.coroutines.coroutineContext
import kotlin.math.sqrt
import kotlin.time.ExperimentalTime

/**
 * @author joserobjr
 * @since 2021-01-21
 */
internal open class LandBodyController(final override val owner: RegularEntity): BodyController {
    private val currentJob = atomic(ActiveWalk(IEntityPos.ZERO, .0, HorizontalVelocity(.0)) to CompletedJob)

    override fun isMoving(): Boolean = !currentJob.get().second.isCompleted
    
    override fun CoroutineScope.walkTo(pos: WithEntityPos, acceptableDistance: Double, speed: IHorizontalVelocity): Job? {
        val (_, job) = currentJob.updateAndGet { oldPair ->
            val (oldActivity, oldJob) = oldPair
            val activity = ActiveWalk(pos, acceptableDistance, speed)
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
    
    private data class ActiveWalk(val target: WithEntityPos, var acceptableDistance: Double, var speed: IHorizontalVelocity) {
        fun isSimilar(other: ActiveWalk): Boolean {
            return target.position.toBlockPos() == other.target.position.toBlockPos()
        }
    }
    
    private suspend fun findPath(activity: ActiveWalk): Path? {
        return owner.pathFinder.findPath(
            owner.world.cache,
            owner.position.toBlockPos(),
            activity.target.position.toBlockPos()
        ).takeIf { it.isNotEmpty() }
    }
    
    @OptIn(ExperimentalTime::class)
    private suspend fun walk(activity: ActiveWalk) {
        var path = findPath(activity) ?: return
        var lastNode: BlockSnapshot? = null
        val job = Job(coroutineContext.job)
        job.start()
        CoroutineScope(SupervisorJob(owner.world.job) + owner.world.updateDispatcher).launch { 
            try {
                job.join()
            } catch (ignored: CancellationException) {
                // Does nothing
            } catch (e: Exception) {
                log.error(e) { "Error while awaiting for the job to complete" }
            } finally {
                try {
                    lastNode?.restoreSnapshot()
                } catch (e: Throwable) {
                    log.error(e) { "Error while restoring block snapshot" }
                }
            }
        }
        try {
        var lastTargetPos = activity.target.position.toBlockPos()
        while (true) {
            val current = owner.position
            val currentEntityPos = activity.target.position
            val currentTargetPos = currentEntityPos.toBlockPos()
            if (currentTargetPos isNotSimilarTo lastTargetPos) {
                path = findPath(activity) ?: return
                lastTargetPos = currentTargetPos
            }
            if (!activity.acceptableDistance.isNaN() && current.squaredDistance(currentEntityPos) <= activity.acceptableDistance.squared()) {
                return
            }
            val nextNode = path.next(current) ?: return
            if (!activity.acceptableDistance.isNaN() && nextNode == path.nodes.last() && current.squaredHorizontalDistance(currentEntityPos) <= activity.acceptableDistance.squared()) {
                return
            }
            val nextNodePos = nextNode.toCenteredEntityPos()
            if (nextNodePos.down().toBlockPos() isNotSimilarTo lastNode?.position) {
                lastNode?.restoreSnapshot()
                val nextBlock = owner.world.getBlock(nextNodePos.down())
                lastNode = nextBlock.createSnapshot(includeBlockEntity = true).takeIf { it.states.main != BlockState.RED_WOOL }
                nextBlock.changeBlock(BlockState.RED_WOOL)
            }
            val status = owner.currentStatus
            val angle = current.target(nextNodePos).copy(pitch = 0.0)
            while (true) {
                owner.controls.updateHeadAngle(angle, status.headFastSpeed * 2.5)
                if (owner.headPitchYaw.isSimilarTo(angle, PitchYaw(.01, .01))) {
                    break
                }
                delay(1.ticks)
            }
            
            
            var motion = current.motionToward(nextNodePos, activity.speed)
            val bounds = owner.boundingBox + motion
            val collision = owner.world.getCollidingBlocks(bounds, owner).asSequence()
//            val needsToStep = collision
//                .filter { (pos) -> pos.y == bounds.minPosInclusive.y.toInt() }
//                .filter { (_, state) -> state.boundingBox.maxPosExclusive.y < status.stepHeight + 0.001 }
//                .maxOfOrNull { (pos, state) -> (state.boundingBox.maxPosExclusive + pos).y }
            
            val highestY = collision
                //.filterNot { (pos, state) -> pos.y == bounds.minPosInclusive.y.toInt() && state.boundingBox.maxPosExclusive.y < status.stepHeight + 0.001 }
                .asFlow()
                .map { (pos) -> owner.world.getBlock(pos) }
                .map { block ->
                    var last: RegularBlock
                    var above = block
                    do {
                        last = above
                        above = above.up()
                    } while (above.currentBoundingBox().isNotEmpty())
                    last.currentBoundingBox().maxPosExclusive.y
                }.toSet().minOrNull()
            
            val needsToStep = false//highestY != null && highestY in (current.y)..(current.y + status.stepHeight - 0.001)
            val needsToJump = highestY != null && highestY in (current.y + status.stepHeight)..(current.y + status.stepHeight + status.jumpSpeed)
            
            owner.update {
                if (needsToStep && highestY != null) {
                    moveTo(currentEntityPos.toImmutable().copy(y = currentEntityPos.y + highestY))
                } else if (needsToJump) {
                    owner.controls.jump()
                }
                applySpeed(motion, Velocity(activity.speed))
            }
            delay(1.ticks)
        }
        } finally {
            job.complete()
        }
    }
    
    private fun EntityPos.motionToward(toward: EntityPos, speed: IHorizontalVelocity): IVelocity {
        // Create vector
        var x = toward.x - x
        var z = toward.z - z
        
        // Normalize
        val squaredLen = x.squared() + z.squared()
        if (squaredLen <= 0) {
            return Velocity(this)
        }
        val len = sqrt(squaredLen)
        x /= len
        z /= len

        // Apply speed
        x *= speed.x
        z *= speed.z
        
        // Done
        return Velocity(x, 0.0, z) 
    }

    override suspend fun idleTask() {
        with(owner) {
            updateAsync {
                applyPhysics()
                flagManager[EntityFlag.MOVING] = isMoving()
            }
        }
    }
    
    companion object {
        private val log = InlineLogger()
    }
}
