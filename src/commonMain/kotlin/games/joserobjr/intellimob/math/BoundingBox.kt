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

/**
 * @author joserobjr
 * @since 2021-01-18
 */
internal class BoundingBox(
    override val minPosInclusive: IEntityPos,
    override val maxPosExclusive: IEntityPos,
): IBoundingBox {
    constructor(pos: IEntityPos, width: Double, height: Double): this(
        minPosInclusive = (width / 2.0).let { EntityPos(pos.x - it, pos.y, pos.z - it) },
        maxPosExclusive = (width / 2.0).let { EntityPos(pos.x + it, pos.y + height, pos.z + it) },
    )
    constructor(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): this(
        minPosInclusive = EntityPos(minX, minY, minZ),
        maxPosExclusive = EntityPos(maxX, maxY, maxZ)
    )
    
    companion object {
        val EMPTY: BoundingBox = BoundingBox(IEntityPos.ZERO, IEntityPos.ZERO)
    }
}
