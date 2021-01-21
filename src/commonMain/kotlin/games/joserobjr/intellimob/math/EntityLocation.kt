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
import games.joserobjr.intellimob.trait.WithWorld
import games.joserobjr.intellimob.world.RegularWorld

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal data class EntityLocation(
    override val world: RegularWorld,
    override val x: Double,
    override val y: Double,
    override val z: Double
): IEntityPos, WithWorld {
    constructor(withWorld: WithWorld, withPos: WithEntityPos): this(withWorld.world, withPos.position)
    constructor(withWorld: WithWorld, pos: IEntityPos): this(withWorld.world, pos.x, pos.y, pos.z)
    constructor(withWorld: WithWorld, x: Double, y: Double, z: Double): this(withWorld.world, x, y, z)
    constructor(withWorld: WithWorld, x: Float, y: Float, z: Float): this(withWorld.world, x.toDouble(), y.toDouble(), z.toDouble())
    
    suspend fun getBlock() = world.getBlock(this)
    suspend fun getCachedBlock() = world.cache.getBlock(this)
}
