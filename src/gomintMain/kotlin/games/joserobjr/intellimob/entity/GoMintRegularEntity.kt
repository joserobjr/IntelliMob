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
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.entity.status.createBaseStatus
import games.joserobjr.intellimob.math.EntityLocation
import games.joserobjr.intellimob.math.EntityPos
import games.joserobjr.intellimob.math.IEntityPos
import games.joserobjr.intellimob.math.PitchYaw
import games.joserobjr.intellimob.pathfinder.createPathFinder
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.world.World
import games.joserobjr.intellimob.world.asIntelliMobWorld
import io.gomint.entity.Entity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class GoMintRegularEntity<E>(override val goMintEntity: Entity<E>) : RegularEntity {
    override val type: EntityType by lazy { EntityType.fromEntity(this) }
    override val controls: EntityControls by lazy { createControls() }
    override val brain: Brain by lazy { createBrain() }
    override val baseStatus: EntityStatus by lazy { createBaseStatus() }
    override val pathFinder: PathFinder by lazy { createPathFinder() }
    override val position: IEntityPos
        get() = with(goMintEntity) { EntityPos(positionX(), positionY(), positionZ()) }
    override val world: World get() = goMintEntity.world().asIntelliMobWorld()
    
    private var _currentStatus: EntityStatus? = null
    public override var currentStatus: EntityStatus
        get() {
            return _currentStatus ?: baseStatus
        }
        private set(value) {
            _currentStatus = value
        }
    
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
}
