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
import games.joserobjr.intellimob.entity.EntityFlag.ON_FIRE
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.BlockPos
import games.joserobjr.intellimob.math.IBlockPos
import games.joserobjr.intellimob.math.IIntVectorXYZ
import games.joserobjr.intellimob.math.Velocity
import games.joserobjr.intellimob.pathfinding.findTarget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlin.time.ExperimentalTime

/**
 * @author joserobjr
 * @since 2021-01-23
 */
internal open class GoalEscapeDanger(val speed: Velocity) : Goal(setOf(PhysicalControl.MOVE)) {
    override val defaultPriority: Int get() = -100_100_000
    override val needsMemory: Boolean get() = true

    override suspend fun canStart(entity: RegularEntity, memory: Memory?): Boolean {
        requireNotNull(memory)
        val onFire = entity.flagManager[ON_FIRE]
        
        if (!entity.isUnderAttack && !onFire) {
            return false
        }
        
        var target: IBlockPos? = null
        if (onFire) {
            target = locateClosesWater(entity, memory, SCAN_RANGE)
        }
        
        if (target == null) {
            target = findTarget(entity, memory)
        }
        
        memory["target"] = target
        
        return target != null
    }
    
    protected open suspend fun findTarget(entity: RegularEntity, memory: Memory): IBlockPos? {
        return entity.pathFinder.findTarget(entity, 5, 4)
    }
    
    protected open suspend fun locateClosesWater(entity: RegularEntity, memory: Memory, range: IIntVectorXYZ): IBlockPos? {
        val pos = entity.position.toBlockPos()
        return (pos - range).cubeIterator(pos + range).asSequence().asFlow()
            .mapNotNull { entity.world.getBlock(it).currentLiquidState() }
            .filter { it.isWater }
            .map { it.pos }
            .toList()
            .minByOrNull { it.squaredDistance(pos) }
    }

    @ExperimentalTime
    override fun CoroutineScope.start(entity: RegularEntity, memory: Memory?): Job? {
        requireNotNull(memory)
        val target: IBlockPos = memory["target"] ?: return null
        return with(entity.brain.wishes) {
            moveTo(target.toCenteredEntityPos())
        }
    }
    
    companion object {
        private val SCAN_RANGE: IIntVectorXYZ = BlockPos(5, 4, 5)
    }
}
