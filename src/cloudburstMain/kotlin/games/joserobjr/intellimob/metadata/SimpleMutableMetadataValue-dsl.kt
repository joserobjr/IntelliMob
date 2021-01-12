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

package games.joserobjr.intellimob.metadata

import org.cloudburstmc.server.metadata.Metadatable
import kotlin.reflect.KProperty

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal inline fun <V> metadata(initialValue: V, key: String? = null) = MetadataProperty(initialValue, key)
internal inline fun <reified R: Metadatable, V> lazyMetadata(crossinline initialValue: R.()->V) = lazyMetadata(null, initialValue)
internal inline fun <reified R: Metadatable, V> lazyMetadata(key: String? = null, crossinline initialValue: R.()->V) = object : AbstractMetadataProperty<V>(key) {
    override fun getMissingValue(thisRef: Metadatable, property: KProperty<*>, key: String): V {
        return (thisRef as R).initialValue()
    }
}
