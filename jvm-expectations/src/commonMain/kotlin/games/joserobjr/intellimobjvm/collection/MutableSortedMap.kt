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
 * A [Map] that further provides a *total ordering* on its keys.
 * The map is ordered according to the [natural][Comparable] of its keys, or by a [Comparator] typically
 * provided at sorted map creation time.  This order is reflected when
 * iterating over the sorted map's collection views (returned by the
 * `entrySet`, `keySet` and `values` methods).
 * Several additional operations are provided to take advantage of the
 * ordering.  (This interface is the map analogue of [MutableSortedSet].)
 *
 *
 * All keys inserted into a sorted map must implement the `Comparable`
 * interface (or be accepted by the specified comparator).  Furthermore, all
 * such keys must be *mutually comparable*: `k1.compareTo(k2)` (or
 * `comparator.compare(k1, k2)`) must not throw a
 * `ClassCastException` for any keys `k1` and `k2` in
 * the sorted map.  Attempts to violate this restriction will cause the
 * offending method or constructor invocation to throw a
 * `ClassCastException`.
 *
 *
 * Note that the ordering maintained by a sorted map (whether or not an
 * explicit comparator is provided) must be *consistent with equals* if
 * the sorted map is to correctly implement the `Map` interface.  (See
 * the `Comparable` interface or `Comparator` interface for a
 * precise definition of *consistent with equals*.)  This is so because
 * the `Map` interface is defined in terms of the `equals`
 * operation, but a sorted map performs all key comparisons using its
 * `compareTo` (or `compare`) method, so two keys that are
 * deemed equal by this method are, from the standpoint of the sorted map,
 * equal.  The behavior of a tree map *is* well-defined even if its
 * ordering is inconsistent with equals; it just fails to obey the general
 * contract of the `Map` interface.
 *
 *
 * All general-purpose sorted map implementation classes should provide four
 * "standard" constructors. It is not possible to enforce this recommendation
 * though as required constructors cannot be specified by interfaces. The
 * expected "standard" constructors for all sorted map implementations are:
 *
 *  1. A void (no arguments) constructor, which creates an empty sorted map
 * sorted according to the natural ordering of its keys.
 *  1. A constructor with a single argument of type `Comparator`, which
 * creates an empty sorted map sorted according to the specified comparator.
 *  1. A constructor with a single argument of type `Map`, which creates
 * a new map with the same key-value mappings as its argument, sorted
 * according to the keys' natural ordering.
 *  1. A constructor with a single argument of type `MutableSortedMap`, which
 * creates a new sorted map with the same key-value mappings and the same
 * ordering as the input sorted map.
 *
 *
 *
 * **Note**: several methods return submaps with restricted key
 * ranges. Such ranges are *half-open*, that is, they include their low
 * endpoint but not their high endpoint (where applicable).  If you need a
 * *closed range* (which includes both endpoints), and the key type
 * allows for calculation of the successor of a given key, merely request
 * the subrange from `lowEndpoint` to
 * `successor(highEndpoint)`.  For example, suppose that `m`
 * is a map whose keys are strings.  The following idiom obtains a view
 * containing all of the key-value mappings in `m` whose keys are
 * between `low` and `high`, inclusive: `val sub: MutableSortedMap<String, V> = m.subMap(low, high+"\0")`
 *
 * A similar technique can be used to generate an *open range*
 * (which contains neither endpoint).  The following idiom obtains a
 * view containing all of the key-value mappings in `m` whose keys
 * are between `low` and `high`, exclusive:`val sub = MutableSortedMap<String, V> = m.subMap(low+"\0", high)`
 *
 * @param K the type of keys maintained by this map
 * @param V the type of mapped values
 */
public expect interface MutableSortedMap<K, V> : MutableMap<K, V> {
    /**
     * Returns the comparator used to order the keys in this map, or
     * `null` if this map uses the [ natural ordering][Comparable] of its keys.
     *
     * @return the comparator used to order the keys in this map,
     * or `null` if this map uses the natural ordering
     * of its keys
     */
    public fun comparator(): Comparator<in K>?

    /**
     * Returns a view of the portion of this map whose keys range from
     * `fromKey`, inclusive, to `toKey`, exclusive.  (If
     * `fromKey` and `toKey` are equal, the returned map
     * is empty.)  The returned map is backed by this map, so changes
     * in the returned map are reflected in this map, and vice-versa.
     * The returned map supports all optional map operations that this
     * map supports.
     *
     *
     * The returned map will throw an `IllegalArgumentException`
     * on an attempt to insert a key outside its range.
     *
     * @param fromKey low endpoint (inclusive) of the keys in the returned map
     * @param toKey high endpoint (exclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys range from
     * `fromKey`, inclusive, to `toKey`, exclusive
     * @throws ClassCastException if `fromKey` and `toKey`
     * cannot be compared to one another using this map's comparator
     * (or, if the map has no comparator, using natural ordering).
     * Implementations may, but are not required to, throw this
     * exception if `fromKey` or `toKey`
     * cannot be compared to keys currently in the map.
     * @throws NullPointerException if `fromKey` or `toKey`
     * is null and this map does not permit null keys
     * @throws IllegalArgumentException if `fromKey` is greater than
     * `toKey`; or if this map itself has a restricted
     * range, and `fromKey` or `toKey` lies
     * outside the bounds of the range
     */
    public fun subMap(fromKey: K, toKey: K): MutableSortedMap<K, V>

    /**
     * Returns a view of the portion of this map whose keys are
     * strictly less than `toKey`.  The returned map is backed
     * by this map, so changes in the returned map are reflected in
     * this map, and vice-versa.  The returned map supports all
     * optional map operations that this map supports.
     *
     *
     * The returned map will throw an `IllegalArgumentException`
     * on an attempt to insert a key outside its range.
     *
     * @param toKey high endpoint (exclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys are strictly
     * less than `toKey`
     * @throws ClassCastException if `toKey` is not compatible
     * with this map's comparator (or, if the map has no comparator,
     * if `toKey` does not implement [Comparable]).
     * Implementations may, but are not required to, throw this
     * exception if `toKey` cannot be compared to keys
     * currently in the map.
     * @throws NullPointerException if `toKey` is null and
     * this map does not permit null keys
     * @throws IllegalArgumentException if this map itself has a
     * restricted range, and `toKey` lies outside the
     * bounds of the range
     */
    public fun headMap(toKey: K): MutableSortedMap<K, V>

    /**
     * Returns a view of the portion of this map whose keys are
     * greater than or equal to `fromKey`.  The returned map is
     * backed by this map, so changes in the returned map are
     * reflected in this map, and vice-versa.  The returned map
     * supports all optional map operations that this map supports.
     *
     *
     * The returned map will throw an `IllegalArgumentException`
     * on an attempt to insert a key outside its range.
     *
     * @param fromKey low endpoint (inclusive) of the keys in the returned map
     * @return a view of the portion of this map whose keys are greater
     * than or equal to `fromKey`
     * @throws ClassCastException if `fromKey` is not compatible
     * with this map's comparator (or, if the map has no comparator,
     * if `fromKey` does not implement [Comparable]).
     * Implementations may, but are not required to, throw this
     * exception if `fromKey` cannot be compared to keys
     * currently in the map.
     * @throws NullPointerException if `fromKey` is null and
     * this map does not permit null keys
     * @throws IllegalArgumentException if this map itself has a
     * restricted range, and `fromKey` lies outside the
     * bounds of the range
     */
    public fun tailMap(fromKey: K): MutableSortedMap<K, V>

    /**
     * Returns the first (lowest) key currently in this map.
     *
     * @return the first (lowest) key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    public fun firstKey(): K

    /**
     * Returns the last (highest) key currently in this map.
     *
     * @return the last (highest) key currently in this map
     * @throws NoSuchElementException if this map is empty
     */
    public fun lastKey(): K
}
