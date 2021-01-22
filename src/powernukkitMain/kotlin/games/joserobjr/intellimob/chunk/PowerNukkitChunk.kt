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

import games.joserobjr.intellimob.block.*
import games.joserobjr.intellimob.entity.EntitySnapshot
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.entity.asRegularEntity
import games.joserobjr.intellimob.math.*
import games.joserobjr.intellimob.world.RegularWorld
import games.joserobjr.intellimob.world.WorldView
import games.joserobjr.intellimob.world.asIntelliMobWorld
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal inline class PowerNukkitChunk(override val powerNukkitChunk: PNChunk): RegularChunk {
    override val updateDispatcher: CoroutineDispatcher get() = world.updateDispatcher
    override val world: RegularWorld get() = powerNukkitChunk.provider.level.asIntelliMobWorld()
    override val position: ChunkPos get() = with(powerNukkitChunk) { ChunkPos(x, z) }

    private fun validatePos(pos: IBlockPos) {
        val minX = powerNukkitChunk.x shl 4
        val minZ = powerNukkitChunk.z shl 4
        require(pos.x in minX..(minX + 15) && pos.z in minZ..(minZ + 15)) { 
            "The position $pos is not inside the chunk $position" 
        }
    }
    
    override suspend fun getRegularEntities(): List<RegularEntity> = withContext(updateDispatcher) {
        powerNukkitChunk.entities.values.map { it.asRegularEntity() }
    }

    override suspend fun getEntitySnapshots(): List<EntitySnapshot> = withContext(updateDispatcher) {
        powerNukkitChunk.entities.values.map { it.asRegularEntity().createSnapshot() }
    }

    override suspend fun getBlockState(pos: IBlockPos, layer: Int): BlockState {
        validatePos(pos)
        if (pos.y !in 0..255) {
            return BlockState.AIR
        }
        return getBlockStateUnchecked(pos, layer)
    }
    
    private fun getBlockStateUnchecked(pos: IBlockPos, layer: Int): BlockState {
        return powerNukkitChunk.getBlockState(pos.x and 0xF, pos.y, pos.z and 0xF, layer).asIntelliMobBlockState()
    }

    override suspend fun getBlockEntity(pos: IBlockPos): RegularBlockEntity? {
        validatePos(pos)
        if (pos.y !in 0..255) {
            return null
        }
        return powerNukkitChunk.getTile(pos.x and 0xF, pos.y, pos.z and 0xF)?.asIntelliMobBlockEntity()
    }

    override suspend fun createBlockSnapshot(pos: IBlockPos, includeBlockEntity: Boolean): BlockSnapshot {
        validatePos(pos)
        val location = BlockLocation(world, pos)
        if (pos.y !in 0..255) {
            return BlockSnapshot(location, LayeredBlockState.EMPTY)
        }
        return if (includeBlockEntity) {
            withContext(updateDispatcher) {
                BlockSnapshot(
                    location = location,
                    states = getBlockStateList(pos),
                    blockEntity = getBlockEntity(pos)?.createSnapshot()
                )
            }
        } else {
            BlockSnapshot(
                location = location,
                states = getBlockStateList(pos)
            )
        }
    }
    
    private fun getBlockStateList(pos: IBlockPos): LayeredBlockState {
        return LayeredBlockState(getBlockStateUnchecked(pos, 0), getBlockStateUnchecked(pos, 1))
    }

    override suspend fun getBlock(pos: IBlockPos): RegularBlock {
        return PowerNukkitBlock(pos.toBlockLocation(this))
    }

    override suspend fun getCollidingBlocks(
        bounds: BoundingBox,
        subject: RegularEntity?,
        limit: Int,
        filter: (WorldView.(pos: IBlockPos, state: BlockState) -> Boolean)?
    ): Map<IBlockPos, BlockState> {
        TODO("Not yet implemented")
    }

    override suspend fun getCollidingEntities(
        bounds: BoundingBox,
        subject: RegularEntity?,
        limit: Int,
        filter: (WorldView.(entity: RegularEntity) -> Boolean)?
    ): List<RegularEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun findClosestPlayer(
        position: IEntityPos,
        bounds: BoundingBox?,
        loadChunks: Boolean,
        condition: (suspend (RegularEntity) -> Boolean)?
    ): RegularEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun findClosestEntity(
        position: IEntityPos,
        bounds: BoundingBox?,
        loadChunks: Boolean,
        condition: (suspend (RegularEntity) -> Boolean)?
    ): RegularEntity? {
        TODO("Not yet implemented")
    }
}
