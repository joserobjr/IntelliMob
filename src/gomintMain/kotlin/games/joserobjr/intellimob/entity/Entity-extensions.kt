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
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.pathfinding.PathFinder
import io.gomint.entity.Entity
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties

/**
 * @author joserobjr
 * @since 2021-01-18
 */
internal fun <E> Entity<E>.asRegularEntity(): RegularEntity = GoMintEntity(this)

private inline fun <D: Any> lazyData(crossinline loader: RegularEntity.()-> D) = object :
    ReadOnlyProperty<RegularEntity, D> {
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
