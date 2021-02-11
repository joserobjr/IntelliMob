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

package games.joserobjr.intellimob.math.angle

import games.joserobjr.intellimob.math.extensions.isSimilarTo
import kotlin.math.max
import kotlin.math.min

/**
 * [https://www.touringmachine.com/Articles/aircraft/6/]
 * ![](https://www.touringmachine.com/images/PitchRollYaw.png)
 * @author joserobjr
 * @since 2021-01-11
 */
internal class PitchYaw(
    pitch: Double, 
    yaw: Double
) {
    val pitch = wrapDegrees(pitch)
    val yaw = wrapDegrees(yaw)

    constructor(pitch: Float, yaw: Float): this(pitch.toDouble(), yaw = yaw.toDouble())
    
    fun changeAngle(target: PitchYaw, speed: PitchYawSpeed): PitchYaw {
        val pitch = subtractAngles(pitch, target.pitch)
        val yaw = subtractAngles(yaw, target.yaw)
        
        val deltaPith = pitch.smartCoerceIn(-speed.pitch, speed.pitch)
        val deltaYaw = yaw.smartCoerceIn(-speed.yaw, speed.yaw) 
        return PitchYaw(
            this.pitch + deltaPith,
            this.yaw + deltaYaw
        )
    }
    
    private fun Double.smartCoerceIn(min: Double, max: Double): Double {
        return coerceIn(min(min, max), max(min, max))
    }
    
    fun isSimilarTo(other: PitchYaw, epsilon: PitchYaw = EPSILON) = pitch.isSimilarTo(other.pitch, epsilon.pitch) && yaw.isSimilarTo(other.yaw, epsilon.yaw)
    fun isNotSimilarTo(other: PitchYaw, epsilon: PitchYaw = EPSILON) = !isSimilarTo(other, epsilon)
    
    operator fun minus(other: PitchYaw): PitchYaw {
        return PitchYaw(subtractAngles(pitch, other.pitch), subtractAngles(yaw, other.yaw))
    }
    
    operator fun times(value: Double): PitchYaw {
        return PitchYaw(wrapDegrees(pitch * value), wrapDegrees(yaw * value))
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
    
    operator fun component1() = pitch
    operator fun component2() = yaw
    fun copy(pitch: Double = this.pitch, yaw: Double = this.yaw) = PitchYaw(pitch, yaw)
    override fun toString(): String {
        return "PitchYaw(pitch=$pitch, yaw=$yaw)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PitchYaw

        if (pitch != other.pitch) return false
        if (yaw != other.yaw) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pitch.hashCode()
        result = 31 * result + yaw.hashCode()
        return result
    }


    companion object {
        val ZERO = PitchYaw(0.0, 0.0)
        val EPSILON = PitchYaw(0.0001, 0.0001)
    }
}
