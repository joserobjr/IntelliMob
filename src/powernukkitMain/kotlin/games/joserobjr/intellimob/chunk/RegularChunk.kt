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
 * @since 2021-01-18
 */
internal actual interface RegularChunk: WithWorld {
    val powerNukkitChunk: PNChunk
    
    actual val updateDispatcher: CoroutineDispatcher
    actual val position: ChunkPos
    
    actual suspend fun getEntitySnapshots(): List<EntitySnapshot>
    actual suspend fun getRegularEntities(): List<RegularEntity>
    actual suspend fun getBlockState(pos: IBlockPos, layer: Int): BlockState
    actual suspend fun getBlockEntity(pos: IBlockPos): RegularBlockEntity?
    actual suspend fun setBlockState(pos: IBlockPos, blockState: BlockState, layer: Int)
    actual suspend fun createBlockSnapshot(pos: IBlockPos, includeBlockEntity: Boolean): BlockSnapshot
}
