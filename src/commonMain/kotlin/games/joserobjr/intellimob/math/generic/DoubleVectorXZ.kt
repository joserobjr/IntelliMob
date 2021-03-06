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

package games.joserobjr.intellimob.math.generic

import games.joserobjr.intellimob.math.extensions.isSimilarTo

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal data class DoubleVectorXZ (
    val x: Double,
    val z: Double
) {
    constructor(both: Double): this(both, both)

    fun isSimilarTo(other: DoubleVectorXZ) = x.isSimilarTo(other.x) && z.isSimilarTo(other.z)
    
    operator fun times(other: DoubleVectorXZ): DoubleVectorXZ {
        return DoubleVectorXZ(x * other.x , z * other.z)
    }
}
