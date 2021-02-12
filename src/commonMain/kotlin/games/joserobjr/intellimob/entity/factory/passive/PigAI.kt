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

package games.joserobjr.intellimob.entity.factory.passive

import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.brain.goal.GoalMate
import games.joserobjr.intellimob.brain.goal.GoalTempt
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.entity.factory.LivingEntityAIFactory
import games.joserobjr.intellimob.entity.status.MutableEntityStatus
import games.joserobjr.intellimob.item.ItemType.Companion.BEETROOT
import games.joserobjr.intellimob.item.ItemType.Companion.CARROT
import games.joserobjr.intellimob.item.ItemType.Companion.CARROT_ON_A_STICK
import games.joserobjr.intellimob.item.ItemType.Companion.POTATO
import games.joserobjr.intellimob.math.motion.HorizontalVelocity

/**
 * @author joserobjr
 * @since 2021-01-19
 */
internal open class PigAI: LivingEntityAIFactory {
    override fun adjustDefaultStatus(status: MutableEntityStatus) = with(status) {
        walkSpeed = HorizontalVelocity(.25)
    }

    override fun createBrain(regularEntity: RegularEntity): Brain {
        return super.createBrain(regularEntity).apply {
            breedingItems += sequenceOf(CARROT, POTATO, BEETROOT)
            
            val twentyPercentFaster = HorizontalVelocity(1.2)
            normalGoals += GoalMate()
            normalGoals += GoalTempt(breedingItems, twentyPercentFaster)
            normalGoals += GoalTempt(setOf(CARROT_ON_A_STICK), twentyPercentFaster)
        }
    }
}

