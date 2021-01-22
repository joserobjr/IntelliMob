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

package games.joserobjr.intellimob.entity

import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.api.EntityControls
import games.joserobjr.intellimob.coroutines.Sync
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.entity.status.MutableEntityStatus
import games.joserobjr.intellimob.math.*
import games.joserobjr.intellimob.pathfinding.BlockFavorProvider
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.world.RegularWorld
import games.joserobjr.intellimob.world.asIntelliMobWorld
import io.gomint.entity.Entity
import kotlinx.coroutines.*
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * Wraps a GoMint entity object and tag it as a regular entity.
 */
internal class GoMintEntity<E>(override val goMintEntity: Entity<E>) : RegularEntity {
    override val job: Job = SupervisorJob(world.job)
    override val updateDispatcher: CoroutineDispatcher get() = world.updateDispatcher
    override val type: EntityType = EntityType.fromEntity(this)

    override val baseStatus: MutableEntityStatus = type.aiFactory.createBaseStatus(this)
    override var controls: EntityControls = type.aiFactory.createControls(this)
    override var pathFinder: PathFinder = type.aiFactory.createPathFinder(this)
    override val brain: Brain = type.aiFactory.createBrain(this)

    private val _currentStatus: MutableEntityStatus = baseStatus.copy()
    override val currentStatus: EntityStatus = _currentStatus.asImmutableView()

    @ExperimentalTime
    override val timeSource: TimeSource get() = world.timeSource
    
    override val position: EntityPos get() = with(goMintEntity) { EntityPos(positionX(), positionY(), positionZ()) }
    override val eyePosition: EntityPos get() = with(goMintEntity) { EntityPos(positionX(), positionY() + eyeHeight(), positionZ()) }
    override val world: RegularWorld get() = goMintEntity.world().asIntelliMobWorld()
    override val boundingBox: BoundingBox get() = goMintEntity.boundingBox().toIntelliMobBoundingBox()

    override suspend fun createSnapshot(): EntitySnapshot = withContext(Dispatchers.Sync) {
        with(goMintEntity) {
            EntitySnapshot(
                type = type,
                location = EntityLocation(world, positionX(), positionY(), positionZ()),
                pitchYaw = PitchYaw(pitch(), yaw()),
                status = currentStatus
            )
        }
    }

    override var headPitchYaw: PitchYaw
        get() = TODO("Not yet implemented")
        set(value) {}
    override var bodyPitchYaw: PitchYaw
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun hasPassengers(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun moveTo(nextPos: EntityPos): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun applyMotion(motion: IDoubleVectorXYZ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun applySpeed(vector: IDoubleVectorXYZ, force: IDoubleVectorXYZ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun calculateInertiaMotion(): IDoubleVectorXYZ {
        TODO("Not yet implemented")
    }

    override suspend fun calculateDrag(): IDoubleVectorXYZ {
        TODO("Not yet implemented")
    }

    override suspend fun applyPhysics() {
        TODO("Not yet implemented")
    }

    override var blockFavor: BlockFavorProvider
        get() = TODO("Not yet implemented")
        set(value) {}
}
