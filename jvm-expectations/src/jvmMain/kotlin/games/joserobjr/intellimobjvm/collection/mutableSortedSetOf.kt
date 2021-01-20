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
 * Returns a new `SortedSet` with the given elements.
 */
public actual inline fun <T: Comparable<T>> mutableSortedSetOf(vararg elements: T): MutableSortedSet<T> = kotlin.collections.sortedSetOf(*elements)

/**
 * Returns a new [MutableSortedSet] with the given [comparator] and elements.
 */
public actual inline fun <T> mutableSortedSetOf(comparator: Comparator<in T>, vararg elements: T): MutableSortedSet<T> = kotlin.collections.sortedSetOf(comparator, *elements)
