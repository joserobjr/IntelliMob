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

import games.joserobjr.intellimob.trait.WithBlockPos
import games.joserobjr.intellimob.trait.WithBoundingBox
import games.joserobjr.intellimob.trait.WithWorld

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal interface IBlockPos: WithBlockPos, WithBoundingBox, IIntVectorXYZ {
    val chunkX: Int get() = x shr 4
    val chunkSection: Int? get() = y.takeIf { it in 0..255 }?.shr(4) 
    val chunkZ: Int get() = z shr 4
    override val position: IBlockPos get() = this
    
    override val boundingBox: BoundingBox get() = toEntityPos().let { min ->
        BoundingBox(
            minPosInclusive = min,
            maxPosExclusive = min + IEntityPos.ONE
        )
    }
    
    fun toCenteredEntityPos(xInc: Double = 0.5, yInc: Double = 0.0, zInc: Double = 0.5): EntityPos {
        return EntityPos(x.toDouble() + xInc, y.toDouble() + yInc, z.toDouble() + zInc)
    }
    fun toEntityPos(): EntityPos = EntityPos(x.toDouble(), y.toDouble(), z.toDouble())
    
    fun squaredDistance(position: IBlockPos): Int {
        val x = x - position.x
        val y = y - position.y
        val z = z - position.z
        return x.squared() + y.squared() + z.squared()
    }

    fun squaredDistance(position: IEntityPos): Double {
        val x = x + 0.5 - position.x
        val y = y - position.y.toInt()
        val z = z + 0.5 - position.z
        return x.squared() + y.squared() + z.squared()
    }

    operator fun contains(pos: IEntityPos): Boolean {
        return x <= pos.x && pos.x < x + 1 &&
                z <= pos.z && pos.z < z + 1
    }
    
    operator fun plus(pos: IIntVectorXYZ): BlockPos = BlockPos(x + pos.x, y + pos.y, z + pos.z)
    operator fun minus(pos: IIntVectorXYZ): BlockPos = BlockPos(x - pos.x, y - pos.y, z - pos.z)

    fun plus(x: Int, y: Int, z: Int): BlockPos = BlockPos(this.x + x, this.y + y, this.z + z)
    fun minus(x: Int, y: Int, z: Int): BlockPos = BlockPos(this.x + x, this.y + y, this.z + z)
    
    operator fun unaryMinus(): BlockPos = BlockPos(-x, -y, -z)
    
    fun toBlockLocation(world: WithWorld): BlockLocation {
        return BlockLocation(world, this)
    }
    
    fun toMutableBlockPos(): MutableBlockPos = MutableBlockPos(x, y, z)
    fun toImmutableBlockPos(): BlockPos = BlockPos(x, y, z)
    fun up(): IBlockPos {
        return BlockPos(x, y + 1, z)
    }
    
    fun cubeIterator(target: IBlockPos): Iterator<IBlockPos> = iterator {
        val xRange = x toward target.x 
        val yRange = y toward target.y
        val zRange = z toward target.z
        xRange.forEach { x ->
            yRange.forEach { y ->
                zRange.forEach { z ->
                    yield(BlockPos(x, y, z))
                }
            }
        }
    }

    companion object {
        val ZERO: BlockPos = BlockPos(0, 0, 0)
        val ONE: BlockPos = BlockPos(1, 1, 1)
    }
}
