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

import games.joserobjr.intellimob.annotation.ExperimentalIntelliMobApi
import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.brain.wish.Wishes
import games.joserobjr.intellimob.control.EntityControls
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.pathfinding.PathFinder

/**
 * The root interface which compose the entities, with or without IntelliMob AI. 
 * 
 * @author joserobjr
 * @since 2021-01-11
 */
@ExperimentalIntelliMobApi
public expect interface RegularEntity

@ExperimentalIntelliMobApi
public expect val RegularEntity.entityType: EntityType

/**
 * Allows to apply physical movements to the entity.
 */
@ExperimentalIntelliMobApi
public expect val RegularEntity.controls: EntityControls

/**
 * Hold and process all intelligence stuff which doesn't require physical interactions.
 */
@ExperimentalIntelliMobApi
public expect val RegularEntity.brain: Brain

/**
 * The default status based on the entity type.
 */
@ExperimentalIntelliMobApi
public expect val RegularEntity.defaultStatus: EntityStatus

/**
 * The current base status, not modified by environmental conditions.
 */
@ExperimentalIntelliMobApi
public expect val RegularEntity.baseStatus: EntityStatus

/**
 * The intelligence which visualizes the world to realize movement [Wishes] from the [Brain] using the [EntityControls].
 */
@ExperimentalIntelliMobApi
public expect val RegularEntity.pathFinder: PathFinder
