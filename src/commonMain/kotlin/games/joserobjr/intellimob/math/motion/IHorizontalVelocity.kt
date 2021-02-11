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

import games.joserobjr.intellimob.math.constants.RAD_TO_DEGREE
import games.joserobjr.intellimob.math.generic.IDoubleVectorXYZ
import games.joserobjr.intellimob.math.generic.IDoubleVectorXZ
import kotlin.math.atan2

/**
 * @author joserobjr
 * @since 2021-02-11
 */
internal interface IHorizontalVelocity: IDoubleVectorXZ {
    operator fun plus(values: IDoubleVectorXZ): IHorizontalVelocity = HorizontalVelocity(x + values.x, z + values.z)
    operator fun minus(values: IDoubleVectorXZ): IHorizontalVelocity = HorizontalVelocity(x - values.x, z - values.z)
    operator fun times(values: IDoubleVectorXZ): IHorizontalVelocity = HorizontalVelocity(x * values.x, z * values.z)
    operator fun div(values: IDoubleVectorXZ): IHorizontalVelocity = HorizontalVelocity(x / values.x, z / values.z)
    operator fun rem(values: IDoubleVectorXZ): IHorizontalVelocity = HorizontalVelocity(x % values.x, z % values.z)
    operator fun unaryMinus(): IHorizontalVelocity = HorizontalVelocity(-x, -z)

    operator fun plus(values: IDoubleVectorXYZ): IVelocity = Velocity(x + values.x, values.y, z + values.z)
    operator fun minus(values: IDoubleVectorXYZ): IVelocity = Velocity(x - values.x, -values.y, z - values.z)
    operator fun times(values: IDoubleVectorXYZ): IVelocity = Velocity(x * values.x, 0, z * values.z)
    operator fun div(values: IDoubleVectorXYZ): IVelocity = Velocity(x / values.x, 0, z / values.z)
    operator fun rem(values: IDoubleVectorXYZ): IVelocity = Velocity(x % values.x, 0 % values.y, z % values.z)

    fun toImmutableHorizontal() = HorizontalVelocity(x, z)

    fun toYaw() = (atan2(z, x) * RAD_TO_DEGREE) - 90.0
}
