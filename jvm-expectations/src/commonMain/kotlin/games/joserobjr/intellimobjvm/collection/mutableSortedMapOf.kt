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

/**
 * Returns a new [MutableSortedMap] with the specified contents, given as a list of pairs
 * where the first value is the key and the second is the value.
 *
 * The resulting [MutableSortedMap] determines the equality and order of keys according to their natural sorting order.
 */
public expect fun <K : Comparable<K>, V> mutableSortedMapOf(vararg pairs: Pair<K, V>): MutableSortedMap<K, V>

/**
 * Returns a new [MutableSortedMap] with the specified contents, given as a list of pairs
 * where the first value is the key and the second is the value.
 *
 * The resulting [MutableSortedMap] determines the equality and order of keys according to the sorting order provided by the given [comparator].
 */
public expect fun <K, V> mutableSortedMapOf(comparator: Comparator<in K>, vararg pairs: Pair<K, V>): MutableSortedMap<K, V>
