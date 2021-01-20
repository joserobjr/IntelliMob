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

@file:Suppress("NOTHING_TO_INLINE")

package games.joserobjr.intellimob.world

import games.joserobjr.intellimob.block.BlockState
import games.joserobjr.intellimob.block.GoMintBlock
import games.joserobjr.intellimob.block.RegularBlock
import games.joserobjr.intellimob.block.asIntelliMobBlockState
import games.joserobjr.intellimob.coroutines.Sync
import games.joserobjr.intellimob.entity.EntitySnapshot
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.entity.asRegularEntity
import games.joserobjr.intellimob.math.BoundingBox
import games.joserobjr.intellimob.math.IBlockPos
import games.joserobjr.intellimob.math.toBlockPos
import games.joserobjr.intellimob.timesource.ServerTickTimeSource
import io.gomint.entity.Entity
import io.gomint.world.block.Block
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal inline class GoMintWorld(override val goMintWorld: GMWorld): RegularWorld {
    override val world: RegularWorld get() = this
    override val name: String get() = goMintWorld.name()
    override val job: Job get() = jobs.computeIfAbsent(goMintWorld) { SupervisorJob() }
    override val cache: WorldView get() = this
    
    @ExperimentalTime
    override val timeSource: TimeSource get() = ServerTickTimeSource
    override val updateDispatcher: CoroutineDispatcher get() = Dispatchers.Sync

    private companion object {
        private val jobs = WeakHashMap<GMWorld, Job>()
    }

    override suspend fun getBlock(pos: IBlockPos): RegularBlock {
        return GoMintBlock(pos.toBlockLocation(this))
    }

    override suspend fun getEntitySnapshots(): List<EntitySnapshot> {
        return getRegularEntities().map {
            it.createSnapshot()
        }
    }

    override suspend fun getRegularEntities(): List<RegularEntity> {
        val entities = mutableListOf<RegularEntity>()
        goMintWorld.iterateEntities(Entity::class.java) {
            entities += it.asRegularEntity()
        }
        return entities
    }

    override suspend fun getCollidingBlocks(
        bounds: BoundingBox,
        subject: RegularEntity?,
        limit: Int,
        filter: (WorldView.(pos: IBlockPos, state: BlockState) -> Boolean)?
    ): Map<IBlockPos, BlockState> {
        return goMintWorld.collisionCubes(null, bounds, false)
            .asSequence()
            .mapNotNull { it as? Block }
            .map { it.position().toBlockPos() to it.asIntelliMobBlockState() }
            .let { seq ->
                filter?.let { seq.filter { (pos, state) -> filter(this, pos, state) } } ?: seq
            }.let { seq ->
                limit.takeIf { it < Int.MAX_VALUE }?.let { seq.take(limit) } ?: seq
            }.toMap()
    }

    override suspend fun getCollidingEntities(
        bounds: BoundingBox,
        subject: RegularEntity?,
        limit: Int,
        filter: (WorldView.(entity: RegularEntity) -> Boolean)?
    ): List<RegularEntity> {
        return goMintWorld.collisionCubes(null, bounds, true)
            .asSequence()
            .mapNotNull { it as? Entity<*> }
            .map { it.asRegularEntity() }
            .let { seq ->
                filter?.let { seq.filter { entity -> filter(this, entity) } } ?: seq
            }.let { seq ->
                limit.takeIf { it < Int.MAX_VALUE }?.let { seq.take(limit) } ?: seq
            }.toList()
    }
}
