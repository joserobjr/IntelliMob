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

package games.joserobjr.intellimob.brain.goal

import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.api.PhysicalControl
import games.joserobjr.intellimob.math.DoubleVectorXYZ
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

/**
 * @author joserobjr
 * @since 2021-01-18
 */
internal object GoalLookAround: Goal(setOf(PhysicalControl.MOVE, PhysicalControl.LOOK)) {
    override val defaultPriority: Int
        get() = 1_000_000
    
    override suspend fun canStart(brain: Brain): Boolean {
        return Random.nextFloat() < .02F
    }

    @OptIn(ExperimentalTime::class)
    override fun CoroutineScope.start(brain: Brain): Job {
        return with(brain.wishes) {
            val delta = PI * 2 * Random.nextDouble()
            val duration = Random.nextInt(1_000, 2_000).milliseconds
            
            launch {
                lookAtDelta(
                    delta = DoubleVectorXYZ(cos(delta), 0.0, sin(delta)),
                    timeLimit = duration
                )
                stayStill(duration)
            }
        }
    }
}
