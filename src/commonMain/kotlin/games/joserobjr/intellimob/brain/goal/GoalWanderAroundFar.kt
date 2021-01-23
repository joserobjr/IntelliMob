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

import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.BlockPos
import games.joserobjr.intellimob.math.DoubleVectorXZ
import games.joserobjr.intellimob.pathfinding.findTarget
import kotlin.random.Random

/**
 * @author joserobjr
 * @since 2021-01-22
 */
internal open class GoalWanderAroundFar(
    speed: DoubleVectorXZ,
    chance: Int = 120,
    val probability: Float = 0.001F
) : GoalWanderAround(speed, chance) {
    override val defaultPriority: Int get() = 90_990_000
    override suspend fun findTarget(entity: RegularEntity, memory: GoalMemory): BlockPos? {
        if (entity.isTouchingWater()) {
            val target = entity.pathFinder.findTarget(entity, 15, 7) {
                groundTarget()
            }
            return target ?: super.findTarget(entity, memory)
        } 
        
        return if (Random.nextFloat() >= probability) {
            entity.pathFinder.findTarget(entity, 15, 7) {
                groundTarget()
            }
        } else {
            super.findTarget(entity, memory)
        }
    }
}
