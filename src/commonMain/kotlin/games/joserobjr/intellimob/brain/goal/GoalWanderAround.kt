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

import games.joserobjr.intellimob.control.api.PhysicalControl
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.BlockPos
import games.joserobjr.intellimob.math.DoubleVectorXZ
import games.joserobjr.intellimob.pathfinding.findTarget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.random.Random
import kotlin.time.ExperimentalTime

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal open class GoalWanderAround(val speed: DoubleVectorXZ, val chance: Int = 120): Goal(setOf(PhysicalControl.MOVE)) {
    override val defaultPriority: Int get() = 90_980_000
    override val needsMemory: Boolean get() = true

    override suspend fun canStart(entity: RegularEntity, memory: GoalMemory?): Boolean {
        requireNotNull(memory)
        if (entity.hasPassengers() || Random.nextInt(chance) != 0) {
            return false
        }
        val target = findTarget(entity, memory) ?: return false
        memory["target"] = target
        return true
    }
    
    protected open suspend fun findTarget(entity: RegularEntity, memory: GoalMemory): BlockPos? {
        return entity.pathFinder.findTarget(entity, 10, 7)
    } 

    @OptIn(ExperimentalTime::class)
    override fun CoroutineScope.start(entity: RegularEntity, memory: GoalMemory?): Job? {
        requireNotNull(memory)
        val target: BlockPos = memory["target"] ?: return null
        return with(entity.brain.wishes) {
            moveTo(target.toCenteredEntityPos())
        }
    }

}
