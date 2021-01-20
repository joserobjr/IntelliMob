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

package games.joserobjr.intellimob.brain.wish

import games.joserobjr.intellimob.control.api.EntityControls
import games.joserobjr.intellimob.entity.RegularEntity
import kotlinx.coroutines.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal abstract class Wish {
    /**
     * @return `true` if the wish has been fulfilled and no more executions are needed
     */
    abstract suspend fun EntityControls.start(): Job?
    
    @OptIn(ExperimentalTime::class)
    protected fun CoroutineScope.runOnEveryTick(entity: RegularEntity, action: suspend EntityControls.()->Boolean): Job {
        return launch(entity.updateDispatcher) {
            while (true) {
                val elapsedTime = measureTime {
                    if(action(entity.controls)) {
                        return@launch
                    }
                }
                delay(50 - elapsedTime.toLongMilliseconds())
            }
        }
    }
    
    protected suspend fun EntityControls.keepTryingTo(action: suspend EntityControls.()->Boolean): Job? {
        if (action()) {
            return null
        }
        return coroutineScope {
            runOnEveryTick(owner, action)
        }
    }
}
