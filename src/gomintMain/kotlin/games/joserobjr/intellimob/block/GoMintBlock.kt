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

import games.joserobjr.intellimob.math.position.block.BlockLocation
import games.joserobjr.intellimob.math.collision.BoundingBox
import io.gomint.world.WorldLayer
import io.gomint.world.block.Block
import io.gomint.world.block.BlockLiquid
import kotlinx.coroutines.withContext

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal class GoMintBlock(override val location: BlockLocation): RegularBlock {
    private fun currentGMBlock(layer: Int = 0): Block {
        return with(location) {
            world.goMintWorld.blockAt(x, y, z, layer.asWorldLayer())
        }
    }
    
    override suspend fun currentState(layer: Int): BlockState {
        if (layer !in VALID_LAYERS) {
            return BlockState.AIR
        }
        
        return currentGMBlock(layer).asIntelliMobBlockState()
    }

    override suspend fun currentStates(): LayeredBlockState = withContext(world.updateDispatcher) {
        with(location) {
            world.goMintWorld.run {
                LayeredBlockState(
                    blockAt<Block>(x, y, z, WorldLayer.NORMAL).asIntelliMobBlockState(),
                    blockAt<Block>(x, y, z, WorldLayer.UNDER_WATER).asIntelliMobBlockState()
                )
            }
        }
    }

    override suspend fun currentBlockEntity(): RegularBlockEntity? = null
    override suspend fun currentBoundingBox(): BoundingBox = currentState(0).boundingBox
    override suspend fun createSnapshot(includeBlockEntity: Boolean): BlockSnapshot {
        return BlockSnapshot(
            location = location,
            states = currentStates()
        )
    }

    override suspend fun currentLiquidState(): LiquidState? {
        var layer = 0
        val liquid = currentGMBlock() as? BlockLiquid<*>
            ?: (currentGMBlock(1) as? BlockLiquid<*>)?.also { layer = 1 }
            ?: return null
        
        val height = liquid.fillHeight().toDouble()
        return LiquidState(
            type = BlockType.fromBlock(liquid),
            height = height,
            bounds = with(location) { BoundingBox(x.toDouble(), y.toDouble(), z.toDouble(), x + 1.0, y + height, z + 1.0) },
            pos = location,
            layer = layer,
        )
    }

    override suspend fun changeBlock(
        main: BlockState,
        vararg extra: BlockState,
        entitySnapshot: BlockEntitySnapshot?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun isSolid(): Boolean {
        TODO("Not yet implemented")
    }
}
