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

@file:Suppress("NOTHING_TO_INLINE")

package games.joserobjr.intellimob.block

import org.cloudburstmc.server.block.BlockTypes
import org.cloudburstmc.server.utils.Identifier
import java.util.concurrent.ConcurrentHashMap

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal actual class BlockType private constructor(blockState: CBBlockState) {
    actual val defaultState: BlockState = blockState.defaultState().asIntelliMobBlockState()
    
    actual companion object {
        private val types = ConcurrentHashMap<Identifier, BlockType>()
        actual val AIR: BlockType = from(CBBlockState.get(BlockTypes.AIR.id))
        
        fun from(state: CBBlockState): BlockType {
            return types.computeIfAbsent(state.type) {
                BlockType(state)
            }
        }
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlockType

        if (defaultState != other.defaultState) return false

        return true
    }

    override fun hashCode(): Int {
        return defaultState.hashCode()
    }

    override fun toString(): String {
        return "BlockType(defaultState=$defaultState)"
    }
}
