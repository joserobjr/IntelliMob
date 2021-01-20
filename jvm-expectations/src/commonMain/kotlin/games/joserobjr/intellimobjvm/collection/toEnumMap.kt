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

package games.joserobjr.intellimobjvm.collection

import kotlin.jvm.JvmName

/**
 * @author joserobjr
 * @since 2021-01-19
 */
@JvmName("convertMapToEnumMap")
public inline fun <K: Enum<K>, V> Map<K, V>.toEnumMap(): EnumMap<K, V> = EnumMap(this)

@JvmName("convertIterableToEnumMap")
public inline fun <reified K: Enum<K>, V> Iterable<Pair<K, V>>.toEnumMap(): EnumMap<K, V> {
    return EnumMap<K, V>().also { map ->
        forEach { (key, value) ->
            map[key] = value
        }
    }
}

@JvmName("convertSequenceToEnumMap")
public inline fun <reified K: Enum<K>, V> Sequence<Pair<K, V>>.toEnumMap(): EnumMap<K, V> {
    return EnumMap<K, V>().also { map ->
        forEach { (key, value) ->
            map[key] = value
        }
    }
}

@JvmName("convertArrayToEnumMap")
public inline fun <reified K: Enum<K>, V> Array<out Pair<K, V>>.toEnumMap(): EnumMap<K, V> {
    return EnumMap<K, V>().also { map ->
        forEach { (key, value) ->
            map[key] = value
        }
    }
}

public inline fun <reified K: Enum<K>, V> enumMapOf(vararg pairs: Pair<K, V>): EnumMap<K, V> = pairs.toEnumMap()

@JvmName("convertIterableEntryToEnumMap")
public inline fun <reified K: Enum<K>, V> Iterable<Map.Entry<K, V>>.toEnumMap(): EnumMap<K, V> {
    return EnumMap<K, V>().also { map ->
        forEach { (key, value) ->
            map[key] = value
        }
    }
}

@JvmName("convertSequenceEntryToEnumMap")
public inline fun <reified K: Enum<K>, V> Sequence<Map.Entry<K, V>>.toEnumMap(): EnumMap<K, V> {
    return EnumMap<K, V>().also { map ->
        forEach { (key, value) ->
            map[key] = value
        }
    }
}

@JvmName("convertArrayEntryToEnumMap")
public inline fun <reified K: Enum<K>, V> Array<out Map.Entry<K, V>>.toEnumMap(): EnumMap<K, V> {
    return EnumMap<K, V>().also { map ->
        forEach { (key, value) ->
            map[key] = value
        }
    }
}

public inline fun <reified K: Enum<K>, V> enumMapOf(vararg pairs: Map.Entry<K, V>): EnumMap<K, V> = pairs.toEnumMap()
