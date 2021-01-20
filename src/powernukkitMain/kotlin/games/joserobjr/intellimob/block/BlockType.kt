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

import cn.nukkit.block.Block
import cn.nukkit.block.BlockID
import java.util.concurrent.ConcurrentHashMap

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal actual class BlockType private constructor(state: PNBlockState) {
    actual val defaultState: BlockState = state.asIntelliMobBlockState()
    
    actual companion object {
        private val types = ConcurrentHashMap<PNBlockState, BlockType>()
        actual val AIR: BlockType = fromBlockId(BlockID.AIR)

        fun fromPNBlock(block: Block): BlockType {
            return fromBlockId(block.id)
        }
        
        fun fromBlockId(blockId: Int): BlockType {
            val state = PNBlockState.of(blockId)
            return types.computeIfAbsent(state, ::BlockType)
        }
        
        fun fromBlockState(state: PNBlockState): BlockType {
            return if (state.isDefaultState) {
                types.computeIfAbsent(state, ::BlockType)
            } else {
                fromBlockId(state.blockId)
            }
        }
    }
}
