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
 * @since 2021-01-18
 */
internal data class BoundingBox(
    val minPosInclusive: IEntityPos,
    val maxPosExclusive: IEntityPos
): WithBoundingBox {
    constructor(pos: IEntityPos, width: Double, height: Double): this(
        minPosInclusive = (width / 2.0).let { EntityPos(pos.x - it, pos.y, pos.z - it) },
        maxPosExclusive = (width / 2.0).let { EntityPos(pos.x + it, pos.y + height, pos.z + it) },
    )
    constructor(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): this(
        minPosInclusive = EntityPos(minX, minY, minZ),
        maxPosExclusive = EntityPos(maxX, maxY, maxZ)
    )
    constructor(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float): this(
        minPosInclusive = EntityPos(minX, minY, minZ),
        maxPosExclusive = EntityPos(maxX, maxY, maxZ)
    )

    operator fun contains(pos: IEntityPos): Boolean {
        val min = minPosInclusive
        val max = maxPosExclusive
        return min.x <= pos.x && pos.x < max.x &&
                min.z <= pos.z && pos.z < max.z &&
                min.y <= pos.y && pos.y < max.y
    }

    operator fun contains(pos: IBlockPos): Boolean {
        val min = minPosInclusive
        val max = maxPosExclusive
        return min.x <= pos.x && pos.x < max.x &&
                min.z <= pos.z && pos.z < max.z &&
                min.y <= pos.y && pos.y < max.y
    }

    fun intersects(boundingBox: BoundingBox): Boolean {
        val thisMin = minPosInclusive
        val thisMax = maxPosExclusive
        val thatMin = boundingBox.minPosInclusive
        val thatMax = boundingBox.maxPosExclusive
        return thatMin.x < thisMax.x && thatMin.y < thisMax.y && thatMin.z < thisMax.z &&
                thatMax.x > thisMin.x && thatMax.y > thisMin.y && thatMax.z > thisMin.z
    }
    
    operator fun plus(pos: IBlockPos): BoundingBox {
        return BoundingBox(minPosInclusive + pos, maxPosExclusive + pos)
    }

    operator fun plus(pos: IEntityPos): BoundingBox {
        return BoundingBox(minPosInclusive + pos, maxPosExclusive + pos)
    }

    operator fun minus(pos: IEntityPos): BoundingBox {
        return BoundingBox(minPosInclusive - pos, maxPosExclusive - pos)
    }
    
    operator fun minus(pos: IBlockPos): BoundingBox {
        return BoundingBox(minPosInclusive - pos, maxPosExclusive - pos)
    }

    companion object {
        val EMPTY: BoundingBox = BoundingBox(IEntityPos.ZERO, IEntityPos.ZERO)
    }

    override val boundingBox: BoundingBox get() = this
}
