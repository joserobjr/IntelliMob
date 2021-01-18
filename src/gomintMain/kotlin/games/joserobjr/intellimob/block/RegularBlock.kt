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
import games.joserobjr.intellimob.trait.WithBlockLocation
import games.joserobjr.intellimob.trait.WithBlockPosWorldByLocation
import games.joserobjr.intellimob.world.updateDispatcher
import io.gomint.world.WorldLayer
import io.gomint.world.block.Block
import kotlinx.coroutines.withContext

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal actual class RegularBlock(override val location: BlockLocation): WithBlockLocation, WithBlockPosWorldByLocation {
    actual suspend fun currentState(layer: Int): BlockState {
        if (layer !in VALID_LAYERS) {
            return BlockState.AIR
        }
        
        return with(location) {
            world.goMintWorld.blockAt<Block>(x, y, z, layer.asWorldLayer()).asIntelliMobBlockState()
        }
    }
    
    actual suspend fun currentStates(): LayeredBlockState = withContext(world.updateDispatcher) {
        with(location) {
            world.goMintWorld.run {
                LayeredBlockState(
                    blockAt<Block>(x, y, z, WorldLayer.NORMAL).asIntelliMobBlockState(),
                    blockAt<Block>(x, y, z, WorldLayer.UNDER_WATER).asIntelliMobBlockState()
                )
            }
        }
    }
    
    actual suspend fun currentBlockEntity(): RegularBlockEntity? = null
    actual suspend fun currentBoundingBox(): BoundingBox = currentState(0).boundingBox
    actual suspend fun createSnapshot(includeBlockEntity: Boolean): BlockSnapshot {
        return BlockSnapshot(
            location = location,
            states = currentStates()
        )
    }
}
