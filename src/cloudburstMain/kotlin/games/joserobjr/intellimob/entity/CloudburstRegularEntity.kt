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
import games.joserobjr.intellimob.brain.createBrain
import games.joserobjr.intellimob.control.EntityControls
import games.joserobjr.intellimob.control.createControls
import games.joserobjr.intellimob.coroutines.Sync
import games.joserobjr.intellimob.entity.EntityType.Companion.fromEntity
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.entity.status.createBaseStatus
import games.joserobjr.intellimob.math.*
import games.joserobjr.intellimob.pathfinder.createPathFinder
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.world.World
import games.joserobjr.intellimob.world.asIntelliMobWorld
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.cloudburstmc.server.entity.Entity
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal class CloudburstRegularEntity(override val cloudburstEntity: Entity) : RegularEntity {
    override val type: EntityType by lazy { fromEntity(this) }
    override val controls: EntityControls by lazy { createControls() }
    override val brain: Brain by lazy { createBrain() }
    override val baseStatus: EntityStatus by lazy { createBaseStatus() }
    override val pathFinder: PathFinder by lazy { createPathFinder() }
    override val position: IEntityPos get() = cloudburstEntity.position.toEntityPos()
    override val world: World get() = cloudburstEntity.level.asIntelliMobWorld()
    override val boundingBox: BoundingBox get() = cloudburstEntity.boundingBox.toIntelliMobBoundingBox()
    
    @ExperimentalTime
    override val timeSource: TimeSource get() = world.timeSource

    private var _currentStatus: EntityStatus? = null
    override var currentStatus: EntityStatus
        get() = _currentStatus ?: baseStatus
        private set(value) { _currentStatus = value }
    
    override suspend fun createSnapshot(): EntitySnapshot = withContext(Dispatchers.Sync) {
        with(cloudburstEntity) {
            EntitySnapshot(
                type = this@CloudburstRegularEntity.type,
                location = EntityLocation(world, x, y, z),
                pitchYaw = PitchYaw(pitch, yaw),
                status = currentStatus
            )
        }
    }
}
