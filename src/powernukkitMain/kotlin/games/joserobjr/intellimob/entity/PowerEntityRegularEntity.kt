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

import cn.nukkit.entity.Entity
import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.brain.createBrain
import games.joserobjr.intellimob.control.EntityControls
import games.joserobjr.intellimob.control.createControls
import games.joserobjr.intellimob.coroutines.Sync
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.entity.status.createBaseStatus
import games.joserobjr.intellimob.math.*
import games.joserobjr.intellimob.pathfinder.createPathFinder
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.timesource.ServerTickTimeSource
import games.joserobjr.intellimob.world.World
import games.joserobjr.intellimob.world.asIntelliMobWorld
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * Wraps a PowerNukkit entity object and tag it as a regular entity.
 */
internal class PowerEntityRegularEntity(override val powerNukkitEntity: Entity) : RegularEntity {
    override val type: EntityType get() = EntityType.fromEntity(this)
    override val controls: EntityControls by lazy { createControls() }
    override val brain: Brain by lazy { createBrain() }
    override val baseStatus: EntityStatus by lazy { createBaseStatus() }
    override val pathFinder: PathFinder by lazy { createPathFinder() }
    override val position: EntityPos get() = powerNukkitEntity.asEntityPos()
    override val world: World get() = powerNukkitEntity.level.asIntelliMobWorld()
    override val boundingBox: BoundingBox get() = powerNukkitEntity.boundingBox?.toIntelliMobBoundingBox() ?: BoundingBox.EMPTY
    private var _currentStatus: EntityStatus? = null
    public override var currentStatus: EntityStatus
        get() {
            return _currentStatus ?: baseStatus
        }
        private set(value) {
            _currentStatus = value
        }

    @ExperimentalTime
    override val timeSource: TimeSource get() = ServerTickTimeSource

    override suspend fun createSnapshot(): EntitySnapshot = withContext(Dispatchers.Sync) {
        with(powerNukkitEntity) {
            EntitySnapshot(
                type = type,
                location = EntityLocation(world, x, y, z),
                pitchYaw = PitchYaw(pitch, yaw),
                status = currentStatus
            )
        }
    }
}
