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

package games.joserobjr.intellimob.entity.factory

import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.brain.goal.GoalLookAround
import games.joserobjr.intellimob.brain.goal.GoalLookAtEntity
import games.joserobjr.intellimob.brain.goal.GoalSwimUp
import games.joserobjr.intellimob.brain.goal.GoalWanderAround
import games.joserobjr.intellimob.control.ModularControls
import games.joserobjr.intellimob.control.api.EntityControls
import games.joserobjr.intellimob.control.generic.GenericHeadController
import games.joserobjr.intellimob.control.generic.LandBodyController
import games.joserobjr.intellimob.control.generic.LandJumpController
import games.joserobjr.intellimob.entity.EntityType
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.entity.status.ImmutableEntityStatus
import games.joserobjr.intellimob.math.DoubleVectorXZ
import games.joserobjr.intellimob.math.PitchYawSpeed
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.pathfinding.StraightLinePathFinder

/**
 * @author joserobjr
 * @since 2021-01-13
 */
internal open class LivingEntityAIFactory: EntityAIFactory {
    override fun createControls(regularEntity: RegularEntity): EntityControls {
        return ModularControls(
            regularEntity, 
            LandBodyController(regularEntity),
            LandJumpController(regularEntity),
            GenericHeadController(regularEntity)
        )
    }
    
    override fun createPathFinder(regularEntity: RegularEntity): PathFinder = StraightLinePathFinder()

    override fun createDefaultStatus(): ImmutableEntityStatus = ImmutableEntityStatus(
        headSpeed = PitchYawSpeed(40.0, 10.0),
        headFastSpeed = PitchYawSpeed(80.0, 20.0),
        walkSpeed = DoubleVectorXZ(.7),
        sprintSpeed = DoubleVectorXZ(1.0),
        flySpeed = DoubleVectorXZ(.4),
        jumpSpeed = .42,
        stepHeight = .6,
        canJump = true,
    )

    override fun createBrain(regularEntity: RegularEntity): Brain = Brain(regularEntity).apply {
        normalGoals += GoalSwimUp
        normalGoals += GoalWanderAround(DoubleVectorXZ(1.0), 120)
        normalGoals += GoalLookAtEntity(setOf(EntityType.PLAYER), 6F)
        normalGoals += GoalLookAround
    }
}
