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

package games.joserobjr.intellimob.math.collision

import games.joserobjr.intellimob.math.position.entity.IEntityPos
import org.cloudburstmc.server.math.AxisAlignedBB

/**
 * @author joserobjr
 * @since 2021-01-19
 */
internal actual open class PlatformBoundingBox actual constructor(
    actual val minPosInclusive: IEntityPos,
    actual val maxPosExclusive: IEntityPos
): AxisAlignedBB {
    override fun clone(): AxisAlignedBB {
        // Not cloning because this object is immutable
        return this
    }

    override fun getMinX(): Float {
        return minPosInclusive.x.toFloat()
    }

    override fun getMinY(): Float {
        return minPosInclusive.y.toFloat()
    }

    override fun getMinZ(): Float {
        return minPosInclusive.z.toFloat()
    }

    override fun getMaxX(): Float {
        return maxPosExclusive.x.toFloat()
    }

    override fun getMaxY(): Float {
        return maxPosExclusive.y.toFloat()
    }

    override fun getMaxZ(): Float {
        return maxPosExclusive.z.toFloat()
    }
}
