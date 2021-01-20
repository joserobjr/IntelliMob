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

@file:Suppress("FunctionName", "NOTHING_TO_INLINE")
@file:JvmName("EnumMapJvmExtensions")

package games.joserobjr.intellimobjvm.collection

import kotlin.reflect.KClass

/**
 * @author joserobjr
 * @since 2021-01-19
 */
public actual inline fun <K: Enum<K>, V> EnumMap(map: EnumMap<K, V>): EnumMap<K, V> = java.util.EnumMap(map)
public actual inline fun <K: Enum<K>, V> EnumMap(map: Map<K, V>): EnumMap<K, V> = java.util.EnumMap(map)
public actual inline fun <K: Enum<K>, V> EnumMap(type: KClass<K>): EnumMap<K, V> = java.util.EnumMap(type.java)


/**
 * Returns the value corresponding to the given [key], or [defaultValue] if such a key is not present in the map.
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public actual inline fun <K: Enum<K>, V> EnumMap<K, V>.getOrDefault(key: K, defaultValue: @UnsafeVariance V): V = getOrDefault(key, defaultValue)

/**
 * Removes the entry for the specified key only if it is mapped to the specified value.
 *
 * @return true if entry was removed
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
public actual inline fun <K : Enum<K>, V> EnumMap<K, V>.remove(key: K, value: V): Boolean = remove(key, value) 
