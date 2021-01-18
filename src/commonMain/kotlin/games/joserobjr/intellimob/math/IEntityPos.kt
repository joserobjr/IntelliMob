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

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal interface IEntityPos: WithEntityPos {
    val x: Double
    val y: Double
    val z: Double

    fun asBlockPos(): BlockPos = BlockPos(x.toInt(), y.toInt(), z.toInt())
    
    override val position: IEntityPos get() = this

    operator fun plus(pos: IBlockPos): EntityPos = EntityPos(x + pos.x, y + pos.y, z + pos.z)
    operator fun minus(pos: IBlockPos): EntityPos = EntityPos(x - pos.x, y - pos.y, z - pos.z)

    operator fun plus(pos: IEntityPos): EntityPos = EntityPos(x + pos.x, y + pos.y, z + pos.z)
    operator fun minus(pos: IEntityPos): EntityPos = EntityPos(x - pos.x, y - pos.y, z - pos.z)
    
    fun plus(x: Double, y: Double, z: Double): EntityPos = EntityPos(this.x + x, this.y + y, this.z + z)
    fun minus(x: Double, y: Double, z: Double): EntityPos = EntityPos(this.x + x, this.y + y, this.z + z)

    fun plus(x: Float, y: Float, z: Float): EntityPos = EntityPos(this.x + x, this.y + y, this.z + z)
    fun minus(x: Float, y: Float, z: Float): EntityPos = EntityPos(this.x + x, this.y + y, this.z + z)
    
    operator fun unaryMinus(): EntityPos = EntityPos(-x, -y, -z)

    companion object {
        val ZERO: EntityPos = EntityPos(0.0, 0.0, 0.0)
        val ONE: EntityPos = EntityPos(1.0, 1.0, 1.0)
    }
}
