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

import games.joserobjr.intellimob.trait.WithBoundingBox

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal interface IChunkPos: WithBoundingBox, IIntVectorXZ {
    val minBlockPos: BlockPos get() = BlockPos(x shl 4, 0, z shl 4)
    val maxBlockPos: BlockPos get() = BlockPos((x shl 4) + 15, 255, (z shl 4) + 15)
    
    val minEntityPosInclusive: EntityPos get() = EntityPos((x shl 4).toDouble(), Double.MIN_VALUE, (z shl 4).toDouble())
    val maxEntityPosExclusive: EntityPos get() = EntityPos((x shl 4) + 16.0, Double.MAX_VALUE, (z shl 4) + 16.0)

    override val boundingBox: BoundingBox get() {
        return BoundingBox(
            minPosInclusive = minEntityPosInclusive,
            maxPosExclusive = maxEntityPosExclusive
        )
    }
    
    operator fun contains(pos: IBlockPos): Boolean {
        val minX = x shl 4
        val minZ = z shl 4
        return pos.x in minX..(minX + 15) && pos.z in minZ..(minZ + 15)
    }

    operator fun contains(pos: IEntityPos): Boolean {
        val minX = (x shl 4).toDouble()
        val minZ = (z shl 4).toDouble()
        return minX <= pos.x && pos.x < minX + 16 &&
                minZ <= pos.z && pos.z < minZ + 16
    }
}
