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

package games.joserobjr.intellimob.math

import games.joserobjr.intellimob.math.collision.BoundingBox
import io.gomint.math.AxisAlignedBB
import kotlin.math.max
import kotlin.math.min

/**
 * @author joserobjr
 * @since 2021-01-18
 */
internal fun List<AxisAlignedBB>.toExpandedBoundingBox(): BoundingBox {
    when (size) {
        0 -> return BoundingBox.EMPTY
        1 -> return first().toIntelliMobBoundingBox()
    }
    
    val iterator = iterator()
    val first = iterator.next()
    var minX = first.minX()
    var minY = first.minY()
    var minZ = first.minZ()
    var maxX = first.maxX()
    var maxY = first.maxY()
    var maxZ = first.maxZ()
    iterator.forEachRemaining { current -> 
        minX = min(minX, current.minX())
        minY = min(minY, current.minY())
        minZ = min(minZ, current.minZ())
        maxX = max(maxX, current.maxX())
        maxY = max(maxY, current.maxY())
        maxZ = max(maxZ, current.maxZ())
    }
    return BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
}

internal inline fun AxisAlignedBB.toIntelliMobBoundingBox(): BoundingBox {
    return BoundingBox(minX(), minY(), minZ(), maxX(), maxY(), maxZ())
}
