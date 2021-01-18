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

import games.joserobjr.intellimob.trait.WithWorld
import games.joserobjr.intellimob.world.World

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal data class ChunkLocation (
    override val world: World,
    override val x: Int,
    override val z: Int
): IChunkPos, WithWorld {
    constructor(withWorld: WithWorld, pos: IChunkPos): this(withWorld.world, pos.x, pos.z)
    constructor(withWorld: WithWorld, x: Int, z: Int): this(withWorld.world, x, z)
}
