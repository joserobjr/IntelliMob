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

import games.joserobjr.intellimob.block.RegularBlock
import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.ModularControls
import games.joserobjr.intellimob.control.api.EntityControls
import games.joserobjr.intellimob.control.bat.BatBodyControl
import games.joserobjr.intellimob.control.bat.BatJumpController
import games.joserobjr.intellimob.control.frozen.FrozenHeadController
import games.joserobjr.intellimob.entity.EntityFlag.CAN_FLY
import games.joserobjr.intellimob.entity.IEntityFlagManager
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.entity.factory.AggressiveAIFactory
import games.joserobjr.intellimob.entity.factory.FlyingAIFactory
import games.joserobjr.intellimob.entity.status.MutableEntityStatus
import games.joserobjr.intellimob.pathfinding.BlockFavorProvider
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.pathfinding.StationaryPathFinder

/**
 * @author joserobjr
 * @since 2021-01-21
 */
internal open class BatAI: AggressiveAIFactory, FlyingAIFactory {
    override fun createBrain(regularEntity: RegularEntity): Brain {
        return Brain(regularEntity)
    }

    override fun createControls(regularEntity: RegularEntity): EntityControls {
        return ModularControls(
            regularEntity,
            BatBodyControl(regularEntity),
            BatJumpController(regularEntity),
            FrozenHeadController(regularEntity)
        )
    }

    override fun createPathFinder(regularEntity: RegularEntity): PathFinder {
        return StationaryPathFinder
    }

    override fun createBlockFavor(entity: RegularEntity): BlockFavorProvider {
        return NoFavors
    }

    override fun adjustDefaultStatus(status: MutableEntityStatus) {
        super<AggressiveAIFactory>.adjustDefaultStatus(status)
        super<FlyingAIFactory>.adjustDefaultStatus(status)
    }

    override fun setDefaultFlags(manager: IEntityFlagManager) {
        super<AggressiveAIFactory>.setDefaultFlags(manager)
        super<FlyingAIFactory>.setDefaultFlags(manager)
        manager.enableFlags(CAN_FLY)
    }
    
    private object NoFavors: BlockFavorProvider {
        override suspend fun computeFavorBlockFavor(block: RegularBlock): Double {
            return .0
        }
    }
}
