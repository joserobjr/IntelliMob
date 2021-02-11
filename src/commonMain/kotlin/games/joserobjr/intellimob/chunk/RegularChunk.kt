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

package games.joserobjr.intellimob.chunk

import games.joserobjr.intellimob.block.BlockSnapshot
import games.joserobjr.intellimob.math.position.chunk.ChunkPos
import games.joserobjr.intellimob.math.position.block.IBlockPos
import games.joserobjr.intellimob.trait.WithUpdateDispatcher
import games.joserobjr.intellimob.world.WorldView

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal interface RegularChunk: PlatformChunk, WorldView, WithUpdateDispatcher {
    val position: ChunkPos
    suspend fun createBlockSnapshot(pos: IBlockPos, includeBlockEntity: Boolean = false): BlockSnapshot
    override val regularChunk: RegularChunk get() = this
}
