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

import games.joserobjr.intellimob.block.RegularBlock
import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.api.EntityControls
import games.joserobjr.intellimob.control.frozen.FrozenControls
import games.joserobjr.intellimob.entity.EntityType
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.entity.factory.passive.PigAI
import games.joserobjr.intellimob.entity.status.ImmutableEntityStatus
import games.joserobjr.intellimob.entity.status.MutableEntityStatus
import games.joserobjr.intellimob.pathfinding.BlockFavorProvider
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.pathfinding.StationaryPathFinder

/**
 * @author joserobjr
 * @since 2021-01-19
 */
internal interface EntityAIFactory {
    fun createControls(regularEntity: RegularEntity): EntityControls = FrozenControls(regularEntity)
    fun createPathFinder(regularEntity: RegularEntity): PathFinder = StationaryPathFinder
    fun createBrain(regularEntity: RegularEntity): Brain
    fun createBaseStatus(regularEntity: RegularEntity): MutableEntityStatus = regularEntity.type.defaultStatus.toMutable()
    fun createDefaultStatus(): ImmutableEntityStatus
    fun createBlockFavor(entity: RegularEntity): BlockFavorProvider = object : BlockFavorProvider {
        override suspend fun computeFavorBlockFavor(block: RegularBlock): Double {
            return 1.0
        }
    }

    companion object {
        fun fromVanillaType(vanillaType: EntityType.Vanilla): EntityAIFactory {
            return when (vanillaType) {
                EntityType.PLAYER -> PlayerAI
                EntityType.PIG -> PigAI()
                else -> GenericEntityAIFactory
            }
        }
    }
}
