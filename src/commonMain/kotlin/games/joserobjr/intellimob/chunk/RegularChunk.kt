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
import games.joserobjr.intellimob.block.BlockState
import games.joserobjr.intellimob.block.RegularBlockEntity
import games.joserobjr.intellimob.entity.EntitySnapshot
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.ChunkPos
import games.joserobjr.intellimob.math.IBlockPos
import games.joserobjr.intellimob.trait.WithWorld
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal expect interface RegularChunk: WithWorld {
    val updateDispatcher: CoroutineDispatcher
    val position: ChunkPos
    suspend fun getEntitySnapshots(): List<EntitySnapshot>
    suspend fun getRegularEntities(): List<RegularEntity>
    suspend fun getBlockState(pos: IBlockPos, layer: Int = 0): BlockState
    suspend fun getBlockEntity(pos: IBlockPos): RegularBlockEntity?
    suspend fun setBlockState(pos: IBlockPos, blockState: BlockState, layer: Int = 0)
    suspend fun createBlockSnapshot(pos: IBlockPos, includeBlockEntity: Boolean = false): BlockSnapshot
}