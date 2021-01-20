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

package games.joserobjr.intellimobjvm.collection

/**
 * Converts this [Map] to a [MutableSortedMap]. 
 * The resulting [MutableSortedMap] determines the equality and order of keys according to their natural sorting order.
 *
 * Note that if the natural sorting order of keys considers any two keys of this map equal
 * (this could happen if the equality of keys according to [Comparable.compareTo] is inconsistent with the equality according to [Any.equals]),
 * only the value associated with the last of them gets into the resulting map.
 */
public actual inline fun <K : Comparable<K>, V> Map<out K, V>.toMutableSortedMap(): MutableSortedMap<K, V> = toSortedMap()

/**
 * Converts this [Map] to a [MutableSortedMap]. 
 * The resulting [MutableSortedMap] determines the equality and order of keys according to the sorting order provided by the given [comparator].
 *
 * Note that if the `comparator` considers any two keys of this map equal, only the value associated with the last of them gets into the resulting map.
 */
public actual inline fun <K, V> Map<out K, V>.toMutableSortedMap(comparator: Comparator<in K>): MutableSortedMap<K, V> = toSortedMap(comparator)
