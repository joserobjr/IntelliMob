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
import games.joserobjr.intellimob.brain.wish.Wishes
import games.joserobjr.intellimob.control.EntityControls
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.trait.WithBoundingBox
import games.joserobjr.intellimob.trait.WithEntityLocation
import games.joserobjr.intellimob.trait.WithTimeSource
import org.cloudburstmc.server.entity.Entity

/**
 * The root interface which compose the entities, with or without IntelliMob AI.
 * 
 * @author joserobjr
 * @since 2021-01-11
 */
internal actual interface RegularEntity: WithEntityLocation, WithTimeSource, WithBoundingBox {
    val cloudburstEntity: Entity

    actual val type: EntityType

    /**
     * Allows to apply physical movements to the entity.
     */
    actual val controls: EntityControls

    /**
     * Hold and process all intelligence stuff which doesn't require physical interactions.
     */
    actual val brain: Brain

    /**
     * The current base status, not modified by environmental conditions.
     */
    actual val baseStatus: EntityStatus

    /**
     * The current status, affected environmental conditions.
     */
    actual val currentStatus: EntityStatus

    /**
     * The intelligence which visualizes the world to realize movement [Wishes] from the [Brain] using the [EntityControls].
     */
    actual val pathFinder: PathFinder

    actual suspend fun createSnapshot(): EntitySnapshot
}


