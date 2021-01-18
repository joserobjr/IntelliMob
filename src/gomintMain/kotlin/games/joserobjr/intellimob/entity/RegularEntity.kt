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
import games.joserobjr.intellimob.math.EntityPos
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.trait.WithEntityLocation
import games.joserobjr.intellimob.trait.WithTimeSource
import io.gomint.entity.Entity
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal actual interface RegularEntity: WithEntityLocation, WithTimeSource {
    val goMintEntity: Entity<*>

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

    actual override val position: EntityPos

    actual suspend fun createSnapshot(): EntitySnapshot
}

internal fun <E> Entity<E>.asRegularEntity(): RegularEntity = GoMintRegularEntity(this)

private inline fun <D: Any> lazyData(crossinline loader: RegularEntity.()-> D) = object : ReadOnlyProperty<RegularEntity, D> {
    private var mutableProperty: KMutableProperty<D?>? = null
    override fun getValue(thisRef: RegularEntity, property: KProperty<*>): D {
        val mutableProperty: KMutableProperty<D?> = mutableProperty ?: run {
            val name = property.name
            EntityDataStorage::class.declaredMemberProperties
                .first { it.name == name }
                .let { 
                    @Suppress("UNCHECKED_CAST") 
                    it as KMutableProperty<D?> 
                }.also { mutableProperty = it }
        }
        
        val data = thisRef.intelliMobData
        mutableProperty.getter.call(data)?.let { return it }
        val default = thisRef.loader()
        mutableProperty.setter.call(data, default)
        return default
    }
}

private val dataStorage: MutableMap<RegularEntity, EntityDataStorage> = Collections.synchronizedMap(WeakHashMap())

private val RegularEntity.intelliMobData: EntityDataStorage get() {
    return dataStorage.computeIfAbsent(this) {
        EntityDataStorage()
    }
}

private data class EntityDataStorage (
    var type: EntityType? = null,
    var controls: EntityControls? = null,
    var brain: Brain? = null,
    var defaultStatus: EntityStatus? = null,
    var baseStatus: EntityStatus? = null,
    var pathFinder: PathFinder? = null,
)
