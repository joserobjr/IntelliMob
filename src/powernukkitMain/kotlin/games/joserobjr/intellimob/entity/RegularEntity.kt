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

package games.joserobjr.intellimob.entity

import cn.nukkit.entity.Entity
import cn.nukkit.metadata.MetadataValue
import cn.nukkit.metadata.Metadatable
import cn.nukkit.plugin.Plugin
import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.brain.createBrain
import games.joserobjr.intellimob.brain.wish.Wishes
import games.joserobjr.intellimob.control.EntityControls
import games.joserobjr.intellimob.control.createControls
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.entity.status.createBaseStatus
import games.joserobjr.intellimob.entity.status.createDefaultStatus
import games.joserobjr.intellimob.metadata.lazyMetadata
import games.joserobjr.intellimob.pathfinder.createPathFinder
import games.joserobjr.intellimob.pathfinding.PathFinder

/**
 * The root interface which compose the entities, with or without IntelliMob AI.
 * 
 * @author joserobjr
 * @since 2021-01-11
 */ 
public actual interface RegularEntity: Metadatable {
    /**
     * The PowerNukkit entity which this interface represents.
     */
    public val powerNukkitEntity: Entity
}

/**
 * Allows to apply physical movements to the entity.
 */
public actual val RegularEntity.controls: EntityControls by lazyMetadata(RegularEntity::createControls)

/**
 * Hold and process all intelligence stuff which doesn't require physical interactions.
 */
public actual val RegularEntity.brain: Brain by lazyMetadata(RegularEntity::createBrain)

/**
 * The default status based on the entity type.
 */
public actual val RegularEntity.defaultStatus: EntityStatus by lazyMetadata(RegularEntity::createDefaultStatus)

/**
 * The current base status, not modified by environmental conditions.
 */
public actual val RegularEntity.baseStatus: EntityStatus by lazyMetadata(RegularEntity::createBaseStatus)

/**
 * The intelligence which visualizes the world to realize movement [Wishes] from the [Brain] using the [EntityControls].
 */
public actual val RegularEntity.pathFinder: PathFinder by lazyMetadata(RegularEntity::createPathFinder)

/**
 * Wraps a PowerNukkit entity object and tag it as a regular entity.
 */
public inline class PowerEntityWrapper(override val powerNukkitEntity: Entity): RegularEntity {
    override fun setMetadata(metadataKey: String?, newMetadataValue: MetadataValue?): Unit = powerNukkitEntity.setMetadata(metadataKey, newMetadataValue)
    override fun getMetadata(metadataKey: String?): List<MetadataValue> = powerNukkitEntity.getMetadata(metadataKey)
    override fun hasMetadata(metadataKey: String?): Boolean = powerNukkitEntity.hasMetadata(metadataKey)
    override fun removeMetadata(metadataKey: String?, owningPlugin: Plugin?): Unit = powerNukkitEntity.removeMetadata(metadataKey, owningPlugin)
}

/**
 * Gets an IntelliMob representation of this entity
 */
public inline fun Entity.asRegularEntity(): RegularEntity = PowerEntityWrapper(this)
