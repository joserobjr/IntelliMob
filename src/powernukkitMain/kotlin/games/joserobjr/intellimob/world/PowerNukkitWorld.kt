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

import cn.nukkit.block.Block
import cn.nukkit.level.Level
import games.joserobjr.intellimob.block.BlockState
import games.joserobjr.intellimob.block.PowerNukkitBlock
import games.joserobjr.intellimob.block.RegularBlock
import games.joserobjr.intellimob.block.asIntelliMobBlockState
import games.joserobjr.intellimob.coroutines.AI
import games.joserobjr.intellimob.coroutines.Sync
import games.joserobjr.intellimob.entity.EntitySnapshot
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.entity.asRegularEntity
import games.joserobjr.intellimob.math.*
import games.joserobjr.intellimob.metadata.lazyMetadata
import games.joserobjr.intellimob.timesource.ServerTickTimeSource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.util.function.Predicate
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal inline class PowerNukkitWorld(override val powerNukkitLevel: Level): RegularWorld {
    override val world: RegularWorld get() = this
    override val name: String get() = powerNukkitLevel.name
    override val job: Job get() = powerNukkitLevel.job
    override val cache: WorldView get() = this
    
    @ExperimentalTime
    override val timeSource: TimeSource get() = ServerTickTimeSource
    override val updateDispatcher: CoroutineDispatcher get() = Dispatchers.Sync
    
    override suspend fun getBlock(pos: IBlockPos): RegularBlock {
        return PowerNukkitBlock(pos.toBlockLocation(this))
    }

    override suspend fun getEntitySnapshots(): List<EntitySnapshot> {
        return getRegularEntities().map { it.createSnapshot() }
    }

    override suspend fun getRegularEntities(): List<RegularEntity> {
        return powerNukkitLevel.entities.map { it.asRegularEntity() }
    }

    override suspend fun getCollidingBlocks(
        bounds: BoundingBox,
        subject: RegularEntity?,
        limit: Int,
        filter: (WorldView.(pos: IBlockPos, state: BlockState) -> Boolean)?
    ): Map<IBlockPos, BlockState> {
        val pos = MutableBlockPos()
        val condition: Predicate<Block> = if (filter != null)
            Predicate { block ->
                filter(this, pos.setAll(block), block.currentState.asIntelliMobBlockState())
            }
        else
            Predicate { block ->
                block.id != 0
            }
        return powerNukkitLevel.getCollisionBlocks(bounds, limit == 1, false, condition)
            .asSequence()
            .let { 
                if (limit <= Int.MAX_VALUE) {
                    it.take(limit)
                } else {
                    it
                }
            }.associate { it.toBlockPos() to it.currentState.asIntelliMobBlockState() }
    }

    override suspend fun getCollidingEntities(
        bounds: BoundingBox,
        subject: RegularEntity?,
        limit: Int,
        filter: (WorldView.(entity: RegularEntity) -> Boolean)?
    ): List<RegularEntity> {
        TODO("Not yet implemented")
    }
    
    private companion object {
        private val Level.job: Job by lazyMetadata<Level, Job> { SupervisorJob() }
    }

    override suspend fun findClosestPlayer(
        position: EntityPos,
        bounds: BoundingBox?,
        loadChunks: Boolean,
        condition: (suspend (RegularEntity) -> Boolean)?
    ): RegularEntity? {
        val players = withContext(world.updateDispatcher) {
            world.powerNukkitLevel.players.values.toList()
        }
        return withContext(Dispatchers.AI) {
            players.asFlow()
                .let { flow ->
                    bounds?.let { flow.filter { player -> player.toEntityPos() in bounds } } ?: flow
                }.map { it.asRegularEntity() }
                .let { flow ->
                    condition?.let { flow.filter { player -> condition(player) } } ?: flow
                }.toList()
                .minByOrNull { it.position.squaredDistance(position) }
        }
    }

    override suspend fun findClosestEntity(
        position: EntityPos,
        bounds: BoundingBox?,
        loadChunks: Boolean,
        condition: (suspend (RegularEntity) -> Boolean)?
    ): RegularEntity? {
        TODO("Not yet implemented")
    }
}
