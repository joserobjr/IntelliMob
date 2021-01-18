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

package games.joserobjr.intellimob.block

import games.joserobjr.intellimob.math.BlockLocation
import games.joserobjr.intellimob.math.BoundingBox
import games.joserobjr.intellimob.math.asBlockVector3
import games.joserobjr.intellimob.math.toIntelliMobBoundingBox
import games.joserobjr.intellimob.trait.WithBlockLocation
import games.joserobjr.intellimob.trait.WithBlockPosWorldByLocation
import games.joserobjr.intellimob.world.updateDispatcher
import kotlinx.coroutines.withContext

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal actual data class RegularBlock(
    override val location: BlockLocation
): WithBlockLocation, WithBlockPosWorldByLocation {
    actual suspend fun currentState(layer: Int): BlockState {
        return world.powerNukkitLevel.getBlockStateAt(location.x, location.y, location.z, layer).asIntelliMobBlockState()
    }
    
    actual suspend fun currentBlockEntity(): RegularBlockEntity? { 
        return world.powerNukkitLevel.getBlockEntity(position.asBlockVector3())?.asIntelliMobBlockEntity() 
    }
    
    actual suspend fun createSnapshot(includeBlockEntity: Boolean): BlockSnapshot = withContext(world.updateDispatcher) {
        BlockSnapshot(
            location = location,
            states = currentStates(),
            blockEntity = if (includeBlockEntity) currentBlockEntity()?.createSnapshot() else null
        )
    }

    actual suspend fun currentStates(): LayeredBlockState = withContext(world.updateDispatcher) {
        LayeredBlockState(currentState(0), currentState(1))
    }

    actual suspend fun currentBoundingBox(): BoundingBox = withContext(world.updateDispatcher) {
        world.powerNukkitLevel.getBlock(location.x, location.y, location.z).boundingBox?.toIntelliMobBoundingBox()
            ?: BoundingBox.EMPTY + location
    }
}
