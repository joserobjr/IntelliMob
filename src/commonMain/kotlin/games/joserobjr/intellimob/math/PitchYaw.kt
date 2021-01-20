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
internal data class PitchYaw(
    val pitch: Double, 
    val yaw: Double
) {
    fun changeAngle(target: PitchYaw, speed: PitchYaw): PitchYaw {
        val pitch = subtractAngles(pitch, target.pitch)
        val yaw = subtractAngles(yaw, target.yaw)
        
        val deltaPith = pitch.coerceIn(-speed.pitch, speed.pitch)
        val deltaYaw = yaw.coerceIn(-speed.yaw, speed.yaw) 
        return PitchYaw(
            this.pitch + deltaPith,
            this.yaw + deltaYaw
        )
    }
    
    operator fun minus(other: PitchYaw): PitchYaw {
        return PitchYaw(subtractAngles(pitch, other.pitch), subtractAngles(yaw, other.yaw))
    }

    private fun subtractAngles(start: Double, end: Double): Double {
        return wrapDegrees(end - start)
    }

    private fun wrapDegrees(from: Double): Double {
        var to = from % 360.0
        if (to >= 180.0) {
            to -= 360.0
        }
        if (to < -180.0) {
            to += 360.0
        }
        return to
    }

    constructor(pitch: Float, yaw: Float): this(pitch.toDouble(), yaw = yaw.toDouble())
    companion object {
        val ZERO = PitchYaw(0.0, 0.0)
    }
}
