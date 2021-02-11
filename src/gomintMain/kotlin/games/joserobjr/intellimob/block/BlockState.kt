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
import games.joserobjr.intellimob.math.toBlockPos
import games.joserobjr.intellimob.math.toExpandedBoundingBox
import games.joserobjr.intellimob.trait.WithBoundingBox
import io.gomint.GoMint
import io.gomint.world.block.Block
import io.gomint.world.block.BlockAir

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal actual class BlockState private constructor(goMintBlock: Block) : WithBoundingBox {
    actual val type: BlockType = BlockType.fromBlock(goMintBlock)
    override val boundingBox: BoundingBox = goMintBlock.boundingBoxes().toExpandedBoundingBox() - goMintBlock.position().toBlockPos()
    
    val goMintBlockReference = GoMint.instance().createBlock(type.goMintBlockClass.java).apply { 
        copyFromBlock(goMintBlock)
    }
    
    actual companion object {
        actual val AIR: BlockState = from(GoMint.instance().createBlock(BlockAir::class.java))
        fun from(block: Block): BlockState {
            return BlockState(block)
        }

        actual val RED_WOOL: BlockState
            get() = TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlockState

        if (type != other.type) return false
        if (boundingBox != other.boundingBox) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + boundingBox.hashCode()
        return result
    }

    override fun toString(): String {
        return "BlockState(type=$type, boundingBox=$boundingBox)"
    }

    actual operator fun contains(tag: BlockTag): Boolean {
        TODO("Not yet implemented")
    }
}
