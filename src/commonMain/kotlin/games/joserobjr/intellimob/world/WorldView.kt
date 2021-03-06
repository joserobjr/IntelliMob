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

package games.joserobjr.intellimob.world

import games.joserobjr.intellimob.block.BlockState
import games.joserobjr.intellimob.block.RegularBlock
import games.joserobjr.intellimob.block.RegularBlockEntity
import games.joserobjr.intellimob.entity.EntitySnapshot
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.collision.BoundingBox
import games.joserobjr.intellimob.math.position.block.IBlockPos
import games.joserobjr.intellimob.math.position.entity.IEntityPos
import games.joserobjr.intellimob.trait.WithBlockPos
import games.joserobjr.intellimob.trait.WithEntityPos
import games.joserobjr.intellimob.trait.WithWorld

/**
 * @author joserobjr
 * @since 2021-01-18
 */
internal interface WorldView: WithWorld {
    suspend fun getBlock(pos: IEntityPos): RegularBlock = getBlock(pos.toBlockPos())
    suspend fun getBlock(pos: IBlockPos): RegularBlock
    suspend fun getBlock(pos: WithBlockPos): RegularBlock = getBlock(pos.position)
    suspend fun getBlock(pos: WithEntityPos): RegularBlock = getBlock(pos.position)
    suspend fun getBlockState(pos: IEntityPos, layer: Int = 0): BlockState = getBlockState(pos.toBlockPos())
    suspend fun getBlockState(pos: IBlockPos, layer: Int = 0): BlockState
    suspend fun getBlockState(pos: WithBlockPos, layer: Int = 0): BlockState = getBlockState(pos.position)
    suspend fun getBlockState(pos: WithEntityPos, layer: Int = 0): BlockState = getBlockState(pos.position)
    suspend fun getBlockEntity(pos: IEntityPos): RegularBlockEntity? = getBlockEntity(pos.toBlockPos())
    suspend fun getBlockEntity(pos: IBlockPos): RegularBlockEntity?
    suspend fun getBlockEntity(pos: WithBlockPos): RegularBlockEntity? = getBlockEntity(pos.position)
    suspend fun getBlockEntity(pos: WithEntityPos): RegularBlockEntity? = getBlockEntity(pos.position)
    
    suspend fun getEntitySnapshots(): List<EntitySnapshot>
    suspend fun getRegularEntities(): List<RegularEntity>
    
    suspend fun getCollidingBlocks(
        bounds: BoundingBox,
        subject: RegularEntity? = null,
        limit: Int = Int.MAX_VALUE,
        filter: (WorldView.(pos: IBlockPos, state: BlockState)->Boolean)? = null
    ): Map<IBlockPos, BlockState>
    
    suspend fun getCollidingEntities(
        bounds: BoundingBox,
        subject: RegularEntity? = null,
        limit: Int = Int.MAX_VALUE,
        filter: (WorldView.(entity: RegularEntity)->Boolean)? = null
    ): List<RegularEntity>

    suspend fun hasPlayerIn(
        bounds: BoundingBox? = null,
        condition: (suspend (RegularEntity) -> Boolean)? = null
    ): RegularEntity? = throw NotImplementedError()
    
    suspend fun findClosestPlayer(
        position: IEntityPos,
        bounds: BoundingBox? = null,
        loadChunks: Boolean = false,
        condition: (suspend (RegularEntity) -> Boolean)? = null
    ): RegularEntity?
    
    suspend fun findClosestEntity(
        position: IEntityPos,
        bounds: BoundingBox,
        loadChunks: Boolean = false,
        condition: (suspend (RegularEntity) -> Boolean)? = null
    ): RegularEntity?
}
