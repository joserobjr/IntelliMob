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

import games.joserobjr.intellimob.trait.WithEntityPos
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal interface IEntityPos: WithEntityPos, IDoubleVectorXYZ {
    fun toBlockPos(): BlockPos = BlockPos(x.toInt(), y.toInt(), z.toInt())
    
    override val position: IEntityPos get() = this
    
    fun target(pos: IEntityPos): PitchYaw {
        val deltaX: Double = pos.x - x
        val deltaY: Double = pos.y - y
        val deltaZ: Double = pos.z - z
        
        val yaw = (atan2(deltaZ, deltaX) * RAD_TO_DEGREE) - 90.0
        
        val pitch = -(atan2(deltaY, sqrt(deltaX.squared() + deltaZ.squared())) * RAD_TO_DEGREE)
        
        return PitchYaw(pitch, yaw)
    }

    fun squaredDistance(position: IEntityPos): Double {
        val x = x - position.x
        val y = y - position.y
        val z = z - position.z
        return x.squared() + y.squared() + z.squared()
    }

    fun squaredHorizontalDistance(position: IEntityPos): Double {
        val x = x - position.x
        val z = z - position.z
        return x.squared() + z.squared()
    }

    operator fun plus(pos: IIntVectorXYZ): EntityPos = EntityPos(x + pos.x, y + pos.y, z + pos.z)
    operator fun minus(pos: IIntVectorXYZ): EntityPos = EntityPos(x - pos.x, y - pos.y, z - pos.z)

    operator fun plus(pos: IDoubleVectorXYZ): EntityPos = EntityPos(x + pos.x, y + pos.y, z + pos.z)
    operator fun minus(pos: IDoubleVectorXYZ): EntityPos = EntityPos(x - pos.x, y - pos.y, z - pos.z)
    
    fun plus(x: Double, y: Double, z: Double): EntityPos = EntityPos(this.x + x, this.y + y, this.z + z)
    fun minus(x: Double, y: Double, z: Double): EntityPos = EntityPos(this.x + x, this.y + y, this.z + z)

    fun plus(x: Float, y: Float, z: Float): EntityPos = EntityPos(this.x + x, this.y + y, this.z + z)
    fun minus(x: Float, y: Float, z: Float): EntityPos = EntityPos(this.x + x, this.y + y, this.z + z)
    
    operator fun unaryMinus(): EntityPos = EntityPos(-x, -y, -z)
    fun toImmutable(): EntityPos = EntityPos(x, y, z)
    
    fun down(distance: Double = 1.0): EntityPos {
        return EntityPos(x, y - distance, z) 
    }

    companion object {
        val ZERO: EntityPos = EntityPos(0.0, 0.0, 0.0)
        val ONE: EntityPos = EntityPos(1.0, 1.0, 1.0)
    }
}
