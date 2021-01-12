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
 * [https://www.touringmachine.com/Articles/aircraft/6/]
 * ![](https://www.touringmachine.com/images/PitchRollYaw.png)
 * @author joserobjr
 * @since 2021-01-11
 */
public inline class PitchYaw (private val vectorXY: DoubleVectorXY) {
    public val pitch: Double get() = vectorXY.x
    public val yaw: Double get() = vectorXY.y

    public operator fun component1(): Double = pitch
    public operator fun component2(): Double = yaw

    public fun copy(pitch: Double = this.pitch, yaw: Double = this.yaw): PitchYaw =
        PitchYaw(vectorXY.copy(x = pitch, y = yaw))

    override fun toString(): String {
        return "PitchYaw(pitch=$pitch, yaw=$yaw)"
    }
}
