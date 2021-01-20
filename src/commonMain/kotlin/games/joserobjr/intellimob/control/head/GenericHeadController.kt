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

package games.joserobjr.intellimob.control.head

import games.joserobjr.intellimob.control.api.HeadController
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.EntityPos
import games.joserobjr.intellimob.math.PitchYaw
import games.joserobjr.intellimob.math.ticks
import games.joserobjr.intellimobjvm.atomic.atomic
import kotlinx.coroutines.*
import kotlin.time.ExperimentalTime

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal open class GenericHeadController(final override val owner: RegularEntity) : HeadController {
    var keepAlignedToHorizon = false
    var activeTask = atomic<Job>(Job().apply { complete() })
    
    @OptIn(ExperimentalTime::class)
    override fun CoroutineScope.lookAt(pos: EntityPos, speed: PitchYaw): Job {
        return activeTask.updateAndGet { old ->
            old.cancel(CancellationException("A new task has started"))
            launch(owner.updateDispatcher) {
                while (true) {
                    var previous = owner.headPitchYaw
                    
                    if (keepAlignedToHorizon && previous.pitch != 0.0) {
                        previous = previous.copy(pitch = 0.0)
                    }
                    
                    owner.headPitchYaw = previous.changeAngle(owner.eyePosition.target(pos), speed)
                    delay(1.ticks)
                }
            }
        }
    }
    
    protected suspend fun idleTask() {
        
    }
}
