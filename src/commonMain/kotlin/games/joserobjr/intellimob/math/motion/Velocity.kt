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

package games.joserobjr.intellimob.math.motion

import games.joserobjr.intellimob.math.generic.IDoubleVectorXYZ
import games.joserobjr.intellimob.math.generic.IDoubleVectorXZ
import games.joserobjr.intellimob.math.generic.IIntVectorXYZ

/**
 * @author joserobjr
 * @since 2021-01-22
 */
internal data class Velocity(
    override val x: Double,
    override val y: Double,
    override val z: Double
) : IVelocity {
    constructor(values: IDoubleVectorXYZ): this(values.x, values.y, values.z)
    constructor(values: IDoubleVectorXZ): this(values.x, 0, values.z)
    constructor(values: IIntVectorXYZ): this(values.x, values.y, values.z)
    constructor(x: Float, y: Float, z: Float): this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(x: Int, y: Int, z: Int): this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(x: Number, y: Number, z: Number): this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(values: Int): this(values, values, values)
    constructor(values: Double): this(values, values, values)
    constructor(values: Float): this(values, values, values)
    constructor(values: Number): this(values, values, values)
    override fun toImmutable() = this
}
