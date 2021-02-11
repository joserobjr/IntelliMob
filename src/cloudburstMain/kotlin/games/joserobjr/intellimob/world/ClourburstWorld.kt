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

import games.joserobjr.intellimob.block.*
import games.joserobjr.intellimob.coroutines.Sync
import games.joserobjr.intellimob.entity.EntitySnapshot
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.entity.Sound
import games.joserobjr.intellimob.entity.asRegularEntity
import games.joserobjr.intellimob.math.*
import games.joserobjr.intellimob.math.collision.BoundingBox
import games.joserobjr.intellimob.math.position.block.IBlockPos
import games.joserobjr.intellimob.math.position.entity.IEntityPos
import games.joserobjr.intellimob.math.toBlockPos
import games.joserobjr.intellimob.metadata.lazyMetadata
import games.joserobjr.intellimob.timesource.ServerTickTimeSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.cloudburstmc.server.level.Level
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal inline class ClourburstWorld(override val cloudburstWorld: Level): RegularWorld {
    override val name: String get() = cloudburstWorld.name
    override val world: RegularWorld get() = this
    override val job: Job get() = cloudburstWorld.job
    override val cache: WorldView get() = this

    @ExperimentalTime
    override val timeSource: TimeSource get() = ServerTickTimeSource
    override val updateDispatcher: CoroutineDispatcher get() = Dispatchers.Sync
    
    private companion object {
        private val Level.job: Job by lazyMetadata<Level, Job> { SupervisorJob() }
    }

    override suspend fun getBlock(pos: IBlockPos): RegularBlock {
        return CloudburstBlock(pos.toBlockLocation(this))
    }

    override suspend fun getEntitySnapshots(): List<EntitySnapshot> {
        return getRegularEntities().map { it.createSnapshot() }
    }

    override suspend fun getRegularEntities(): List<RegularEntity> {
        return cloudburstWorld.entities.map { it.asRegularEntity() }
    }

    override suspend fun getCollidingBlocks(
        bounds: BoundingBox,
        subject: RegularEntity?,
        limit: Int,
        filter: (WorldView.(pos: IBlockPos, state: BlockState) -> Boolean)?
    ): Map<IBlockPos, BlockState> {
        return cloudburstWorld.getCollisionBlocks(bounds, false)
            .asSequence()
            .map { it.position.toBlockPos() to it.state.asIntelliMobBlockState() }
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
        return cloudburstWorld.getCollidingEntities(bounds)
            .asSequence()
            .map { it.asRegularEntity() }
            .let { seq ->
                filter?.let { seq.filter { entity -> filter(this, entity) } } ?: seq
            }.let { seq ->
                limit.takeIf { it < Int.MAX_VALUE }?.let { seq.take(limit) } ?: seq
            }.toList()
    }

    override suspend fun restoreSnapshot(snapshot: BlockSnapshot): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun playSound(pos: IEntityPos, sound: Sound) {
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
