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

import games.joserobjr.intellimob.math.collision.BoundingBox
import games.joserobjr.intellimob.math.toIntelliMobBoundingBox
import games.joserobjr.intellimob.trait.WithBoundingBox
import org.cloudburstmc.server.block.BlockTypes
import java.util.concurrent.ConcurrentHashMap

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal actual class BlockState private constructor(val cloudburstBlockState: CBBlockState): WithBoundingBox {
    actual val type: BlockType = BlockType.from(cloudburstBlockState)
    override val boundingBox: BoundingBox = cloudburstBlockState.behavior.boundingBox.toIntelliMobBoundingBox()
    
    actual companion object {
        actual val AIR: BlockState = from(CBBlockState.get(BlockTypes.AIR.id))
        private val states = ConcurrentHashMap<CBBlockState, BlockState>()

        fun from(state: CBBlockState): BlockState {
            return states.computeIfAbsent(state, ::BlockState)
        }

        actual val RED_WOOL: BlockState
            get() = TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlockState

        if (cloudburstBlockState != other.cloudburstBlockState) return false

        return true
    }

    override fun hashCode(): Int {
        return cloudburstBlockState.hashCode()
    }

    override fun toString(): String {
        return "BlockState(cloudburstBlockState=$cloudburstBlockState)"
    }

    actual operator fun contains(tag: BlockTag): Boolean {
        TODO("Not yet implemented")
    }
}
