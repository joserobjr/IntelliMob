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

package games.joserobjr.intellimob.metadata

import games.joserobjr.intellimob.intelliMobContainer
import org.cloudburstmc.server.metadata.Metadatable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal abstract class AbstractMetadataProperty<V>(protected val key: String?): ReadWriteProperty<Metadatable, V> {
    override fun setValue(thisRef: Metadatable, property: KProperty<*>, value: V) {
        val keyName = key ?: property.name
        thisRef.getMetadataValue(keyName)?.let {
            it.value = value
            return
        }

        thisRef.setMetadata(key, SimpleMutableMetadataValue(value))
    }

    override fun getValue(thisRef: Metadatable, property: KProperty<*>): V {
        val keyName = key ?: property.name
        return thisRef.getMetadataValue(keyName)?.value ?: getMissingValue(thisRef, property, keyName)
    }
    
    protected abstract fun getMissingValue(thisRef: Metadatable, property: KProperty<*>, key: String): V
    
    private fun Metadatable.getMetadataValue(key: String): SimpleMutableMetadataValue<V>? {
        return getMetadata(key)
            .firstOrNull { it.owningPlugin == intelliMobContainer }
            ?.let {
                @Suppress("UNCHECKED_CAST")
                it as? SimpleMutableMetadataValue<V>
            }?.takeIf { it.isValid }
    }
}
