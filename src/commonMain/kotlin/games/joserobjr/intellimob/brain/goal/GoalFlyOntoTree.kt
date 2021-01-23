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

import games.joserobjr.intellimob.block.BlockState
import games.joserobjr.intellimob.block.BlockTag
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.BlockPos
import games.joserobjr.intellimob.math.Velocity
import games.joserobjr.intellimob.pathfinding.findTarget
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlin.random.Random

/**
 * @author joserobjr
 * @since 2021-01-22
 */
internal open class GoalFlyOntoTree(
    speed: Velocity,
    chance: Int = 120,
    farProbability: Float = 0.001F
) : GoalWanderAroundFar(speed, chance, farProbability) {
    override val defaultPriority: Int get() = 90_970_000
    
    override suspend fun findTarget(entity: RegularEntity, memory: Memory): BlockPos? {
        var target: BlockPos? = null
        
        if (Random.nextFloat() >= probability) {
            target = findTreeTarget(entity)
        } else if (entity.isTouchingWater()) {
            entity.pathFinder.findTarget(entity, 15, 15) {
                groundTarget()
            }
        }
        
        return target ?: super.findTarget(entity, memory)
    }

    private suspend fun findTreeTarget(entity: RegularEntity): BlockPos? {
        val currentPos = entity.position.toBlockPos()
        
        return (currentPos - SCAN_DISTANCE).cubeIterator(currentPos + SCAN_DISTANCE).asFlow()
            .filter { it != currentPos }
            .map { entity.world.getBlock(it) }
            .map { it to it.currentState() }
            .filter { (_, state)-> BlockTag.LEAVES in state || BlockTag.LOGS in state }
            .filter { (block)-> block.currentState() != BlockState.AIR }
            .filter { (block)-> block.up().currentState() != BlockState.AIR }
            .map { (block) -> block.position.toImmutableBlockPos() }
            .firstOrNull()
    }

    companion object {
        private val SCAN_DISTANCE = BlockPos(3, 6, 3)
    }
}
