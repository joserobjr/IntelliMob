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
import games.joserobjr.intellimob.entity.RegularEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal abstract class Goal(val physicalControl: Set<PhysicalControl>) {
    /**
     * Default priorities:
     * * -100.000.000 SwimUp
     * 
     * * 090.980.000 WanderAround 
     * * 090.990.000 WanderAroundFar 
     * 
     * * 100.700.000 LookAround
     * * 100.800.000 LookAtEntity
     * * 100.900.000 LookAround
     */
    abstract val defaultPriority: Int
    open val needsMemory: Boolean get() = false
    
    open fun canBeAddedTo(brain: Brain, selector: EntityGoalSelector): Boolean {
        return true
    }
    
    open fun adjustPriority(brain: Brain, selector: EntityGoalSelector, proposedPriority: Int): Int {
        return proposedPriority
    } 
    
    open fun addedTo(brain: Brain, selector: EntityGoalSelector) {} 
    
    abstract suspend fun canStart(entity: RegularEntity, memory: GoalMemory?): Boolean
    
    abstract fun CoroutineScope.start(entity: RegularEntity, memory: GoalMemory?): Job?
    
    open suspend fun canStop(entity: RegularEntity): Boolean {
        return true
    }
    
    open suspend fun canBeReplacedBy(entity: RegularEntity, other: Goal, thisPriority: Int, otherPriority: Int): Boolean {
        return canStop(entity) && otherPriority < thisPriority
    }
}
