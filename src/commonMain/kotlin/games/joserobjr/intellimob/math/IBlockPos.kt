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

package games.joserobjr.intellimob.math

import games.joserobjr.intellimob.trait.WithBlockPos
import games.joserobjr.intellimob.trait.WithBoundingBox

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal interface IBlockPos: WithBlockPos, WithBoundingBox {
    val x: Int
    val y: Int
    val z: Int
    
    override val boundingBox: BoundingBox get() = toEntityPos().let { min ->
        BoundingBox(
            minPosInclusive = min,
            maxPosExclusive = min + IEntityPos.ONE
        )
    }
    
    fun asCenteredEntityPos(): EntityPos = EntityPos(x.toDouble() + 0.5, y.toDouble(), z.toDouble() + 0.5)
    fun toEntityPos(): EntityPos = EntityPos(x.toDouble(), y.toDouble(), z.toDouble())
    
    override val position: IBlockPos get() = this

    operator fun contains(pos: IEntityPos): Boolean {
        return x <= pos.x && pos.x < x + 1 &&
                z <= pos.z && pos.z < z + 1
    }
    
    operator fun plus(pos: IBlockPos): BlockPos = BlockPos(x + pos.x, y + pos.y, z + pos.z)
    operator fun minus(pos: IBlockPos): BlockPos = BlockPos(x - pos.x, y - pos.y, z - pos.z)

    fun plus(x: Int, y: Int, z: Int): BlockPos = BlockPos(this.x + x, this.y + y, this.z + z)
    fun minus(x: Int, y: Int, z: Int): BlockPos = BlockPos(this.x + x, this.y + y, this.z + z)
    
    operator fun unaryMinus(): BlockPos = BlockPos(-x, -y, -z)
    
    companion object {
        val ZERO: BlockPos = BlockPos(0, 0, 0)
        val ONE: BlockPos = BlockPos(1, 1, 1)
    }
}
