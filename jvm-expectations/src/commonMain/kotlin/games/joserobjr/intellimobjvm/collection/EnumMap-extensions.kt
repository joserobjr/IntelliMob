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

@file:Suppress("FunctionName", "UNCHECKED_CAST", "NOTHING_TO_INLINE", "EXTENSION_SHADOWED_BY_MEMBER")
@file:JvmName("EnumMapCommonExtensions")

package games.joserobjr.intellimobjvm.collection

import kotlin.jvm.JvmName
import kotlin.reflect.KClass

/**
 * @author joserobjr
 * @since 2021-01-19
 */
public inline fun <reified K: Enum<K>, V> EnumMap(): EnumMap<K, V> = EnumMap(K::class)
public expect fun <K: Enum<K>, V> EnumMap(map: EnumMap<K, V>): EnumMap<K, V>
public expect fun <K: Enum<K>, V> EnumMap(map: Map<K, V>): EnumMap<K, V>
public expect fun <K: Enum<K>, V> EnumMap(type: KClass<K>): EnumMap<K, V>

public inline fun <K: Enum<K>, V> EnumMap<K, V>.asMutableMap(): MutableMap<K, V> {
    return this as MutableMap<K, V>
}

public inline fun <K: Enum<K>, V> EnumMap<K, V>.asMap(): Map<K, V> {
    return this as Map<K, V>
}

public inline fun <K: Enum<K>, V> EnumMap<K, V>.toEnumMap(): EnumMap<K, V> = EnumMap(this)
public inline fun <K: Enum<K>, V> EnumMap<K, V>.toMutableMap(): MutableMap<K, V> = EnumMap(this).asMutableMap()
public inline fun <K: Enum<K>, V> EnumMap<K, V>.toMap(): MutableMap<K, V> = EnumMap(this).asMutableMap()


// Query Operations
/**
 * Returns the number of key/value pairs in the map.
 */
public inline val <K: Enum<K>, V> EnumMap<K, V>.size: Int get() = asMap().size

/**
 * Returns `true` if the map is empty (contains no elements), `false` otherwise.
 */
public inline fun <K: Enum<K>, V> EnumMap<K, V>.isEmpty(): Boolean = asMap().isEmpty()

/**
 * Returns `true` if the map contains the specified [key].
 */
public inline fun <K: Enum<K>, V> EnumMap<K, V>.containsKey(key: K): Boolean = asMap().containsKey(key)

/**
 * Returns `true` if the map maps one or more keys to the specified [value].
 */
public inline fun <K: Enum<K>, V> EnumMap<K, V>.containsValue(value: @UnsafeVariance V): Boolean = asMap().containsValue(value)

/**
 * Returns the value corresponding to the given [key], or `null` if such a key is not present in the map.
 */
public inline operator fun <K: Enum<K>, V> EnumMap<K, V>.get(key: K): V? = asMap()[key]

/**
 * Returns the value corresponding to the given [key], or [defaultValue] if such a key is not present in the map.
 */
public expect fun <K: Enum<K>, V> EnumMap<K, V>.getOrDefault(key: K, defaultValue: @UnsafeVariance V): V

/**
 * Checks if the map contains the given key.
 *
 * This method allows to use the `x in map` syntax for checking whether an object is contained in the map.
 */
public inline operator fun <K: Enum<K>, V> EnumMap<K, V>.contains(key: K): Boolean = asMutableMap().contains(key)

// Modification Operations
/**
 * Associates the specified [value] with the specified [key] in the map.
 *
 * @return the previous value associated with the key, or `null` if the key was not present in the map.
 */
public inline fun <K: Enum<K>, V> EnumMap<K, V>.put(key: K, value: V): V? = asMutableMap().put(key, value)

/**
 * Allows to use the index operator for storing values in a mutable map.
 */
public inline operator fun <K: Enum<K>, V> EnumMap<K, V>.set(key: K, value: V): Unit = asMutableMap().set(key, value)

/**
 * Removes the specified key and its corresponding value from this map.
 *
 * @return the previous value associated with the key, or `null` if the key was not present in the map.
 */
public inline fun <K: Enum<K>, V> EnumMap<K, V>.remove(key: K): V? = asMutableMap().remove(key)

/**
 * Removes the entry for the specified key only if it is mapped to the specified value.
 *
 * @return true if entry was removed
 */
public expect fun <K: Enum<K>, V> EnumMap<K, V>.remove(key: K, value: V): Boolean

// Bulk Modification Operations
/**
 * Updates this map with key/value pairs from the specified map [from].
 */
public inline fun <K: Enum<K>, V> EnumMap<K, V>.putAll(from: Map<out K, V>): Unit = asMutableMap().putAll(from)

/**
 * Removes all elements from this map.
 */
public inline fun <K: Enum<K>, V> EnumMap<K, V>.clear(): Unit = asMutableMap().clear()

// Views
/**
 * Returns a [MutableSet] of all keys in this map.
 */
public inline val <K: Enum<K>, V> EnumMap<K, V>.keys: MutableSet<K> get() = asMutableMap().keys

/**
 * Returns a [MutableCollection] of all values in this map. Note that this collection may contain duplicate values.
 */
public inline val <K: Enum<K>, V> EnumMap<K, V>.values: MutableCollection<V> get() = asMutableMap().values

/**
 * Returns a [MutableSet] of all key/value pairs in this map.
 */
public inline val <K: Enum<K>, V> EnumMap<K, V>.entries: MutableSet<MutableMap.MutableEntry<K, V>> get() = asMutableMap().entries
