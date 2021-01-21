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

import games.joserobjr.intellimob.control.api.HeadController
import games.joserobjr.intellimob.coroutines.AI
import games.joserobjr.intellimob.coroutines.CompletedJob
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.PitchYaw
import games.joserobjr.intellimob.math.PitchYawSpeed
import games.joserobjr.intellimob.math.ticks
import games.joserobjr.intellimob.trait.WithEntityPos
import games.joserobjr.intellimob.trait.update
import games.joserobjr.intellimobjvm.atomic.atomic
import kotlinx.coroutines.*
import kotlin.time.ExperimentalTime

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal open class GenericHeadController(final override val owner: RegularEntity, val returnSpeed: PitchYawSpeed = DEFAULT_RETURN_SPEED) : HeadController {
    var keepAlignedToHorizon = false
    private val activeTask = atomic(CompletedJob)
    private val delayHeadReturn = atomic(CompletedJob)
    
    @OptIn(ExperimentalTime::class)
    override fun CoroutineScope.lookAt(pos: WithEntityPos, speed: PitchYawSpeed): Job {
        return activeTask.updateAndGet { old ->
            old.cancel(CancellationException("A new task has started"))
            launch(Dispatchers.AI) {
                while (true) {
                    if (!owner.controls.isMoving()) {
                        updateHeadAngle(owner.eyePosition.target(pos.position), speed)
                    }
                    delay(1.ticks)
                }
            }
        }
    }
    
    @OptIn(ExperimentalTime::class)
    override suspend fun idleTask() {
        if (activeTask.get().isCompleted && !owner.controls.isMoving()) {
            updateHeadAngle(owner.bodyPitchYaw, returnSpeed)
        }
    }
    
    override suspend fun updateHeadAngle(target: PitchYaw, speed: PitchYawSpeed) {
        val previous = owner.headPitchYaw

        var force = false
        var updated = previous.changeAngle(target, speed)
        if (keepAlignedToHorizon && updated.pitch != 0.0) {
            updated = updated.copy(pitch = 0.0)
            force = true
        }
        
        if (force || previous.isNotSimilarTo(updated)) {
            owner.update {
                headPitchYaw = updated
            }
        }
    }
    
    companion object {
        private val DEFAULT_RETURN_SPEED = PitchYawSpeed(10.0, 10.0)
    }
}
