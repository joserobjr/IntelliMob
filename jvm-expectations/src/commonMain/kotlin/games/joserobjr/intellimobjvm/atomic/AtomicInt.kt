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

package games.joserobjr.intellimobjvm.atomic

import games.joserobjr.intellimobjvm.functions.IntBinaryOperator
import games.joserobjr.intellimobjvm.functions.IntUnaryOperator

/**
 * @author joserobjr
 * @since 2021-01-19
 */
public expect open class AtomicInt(): Number {
    /**
     * Creates a new AtomicInteger with the given initial value.
     *
     * @param initialValue the initial value
     */
    public constructor(initialValue: Int)
    /**
     * Returns the current value,
     * with memory effects as specified by [VarHandle.getVolatile].
     *
     * @return the current value
     */
    public fun get(): Int
    
    /**
     * Sets the value to `newValue`,
     * with memory effects as specified by [VarHandle.setVolatile].
     *
     * @param newValue the new value
     */
    public fun set(newValue: Int)

    /**
     * Sets the value to `newValue`,
     * with memory effects as specified by [VarHandle.setRelease].
     *
     * @param newValue the new value
     * @since 1.6
     */
    public fun lazySet(newValue: Int)

    /**
     * Atomically sets the value to `newValue` and returns the old value,
     * with memory effects as specified by [VarHandle.getAndSet].
     *
     * @param newValue the new value
     * @return the previous value
     */
    public fun getAndSet(newValue: Int): Int

    /**
     * Atomically sets the value to `newValue`
     * if the current value `== expectedValue`,
     * with memory effects as specified by [VarHandle.compareAndSet].
     *
     * @param expectedValue the expected value
     * @param newValue the new value
     * @return `true` if successful. False return indicates that
     * the actual value was not equal to the expected value.
     */
    public fun compareAndSet(expectedValue: Int, newValue: Int): Boolean

    /**
     * Atomically increments the current value,
     * with memory effects as specified by [VarHandle.getAndAdd].
     *
     *
     * Equivalent to `getAndAdd(1)`.
     *
     * @return the previous value
     */
    public fun getAndIncrement(): Int

    /**
     * Atomically decrements the current value,
     * with memory effects as specified by [VarHandle.getAndAdd].
     *
     *
     * Equivalent to `getAndAdd(-1)`.
     *
     * @return the previous value
     */
    public fun getAndDecrement(): Int

    /**
     * Atomically adds the given value to the current value,
     * with memory effects as specified by [VarHandle.getAndAdd].
     *
     * @param delta the value to add
     * @return the previous value
     */
    public fun getAndAdd(delta: Int): Int

    /**
     * Atomically increments the current value,
     * with memory effects as specified by [VarHandle.getAndAdd].
     *
     *
     * Equivalent to `addAndGet(1)`.
     *
     * @return the updated value
     */
    public fun incrementAndGet(): Int

    /**
     * Atomically decrements the current value,
     * with memory effects as specified by [VarHandle.getAndAdd].
     *
     *
     * Equivalent to `addAndGet(-1)`.
     *
     * @return the updated value
     */
    public fun decrementAndGet(): Int

    /**
     * Atomically adds the given value to the current value,
     * with memory effects as specified by [VarHandle.getAndAdd].
     *
     * @param delta the value to add
     * @return the updated value
     */
    public fun addAndGet(delta: Int): Int

    /**
     * Atomically updates (with memory effects as specified by [ ][VarHandle.compareAndSet]) the current value with the results of
     * applying the given function, returning the previous value. The
     * function should be side-effect-free, since it may be re-applied
     * when attempted updates fail due to contention among threads.
     *
     * @param updateFunction a side-effect-free function
     * @return the previous value
     * @since 1.8
     */
    public fun getAndUpdate(updateFunction: IntUnaryOperator): Int

    /**
     * Atomically updates (with memory effects as specified by [ ][VarHandle.compareAndSet]) the current value with the results of
     * applying the given function, returning the updated value. The
     * function should be side-effect-free, since it may be re-applied
     * when attempted updates fail due to contention among threads.
     *
     * @param updateFunction a side-effect-free function
     * @return the updated value
     * @since 1.8
     */
    public fun updateAndGet(updateFunction: IntUnaryOperator): Int

    /**
     * Atomically updates (with memory effects as specified by [ ][VarHandle.compareAndSet]) the current value with the results of
     * applying the given function to the current and given values,
     * returning the previous value. The function should be
     * side-effect-free, since it may be re-applied when attempted
     * updates fail due to contention among threads.  The function is
     * applied with the current value as its first argument, and the
     * given update as the second argument.
     *
     * @param x the update value
     * @param accumulatorFunction a side-effect-free function of two arguments
     * @return the previous value
     * @since 1.8
     */
    public fun getAndAccumulate(
        x: Int,
        accumulatorFunction: IntBinaryOperator
    ): Int

    /**
     * Atomically updates (with memory effects as specified by [ ][VarHandle.compareAndSet]) the current value with the results of
     * applying the given function to the current and given values,
     * returning the updated value. The function should be
     * side-effect-free, since it may be re-applied when attempted
     * updates fail due to contention among threads.  The function is
     * applied with the current value as its first argument, and the
     * given update as the second argument.
     *
     * @param x the update value
     * @param accumulatorFunction a side-effect-free function of two arguments
     * @return the updated value
     * @since 1.8
     */
    public fun accumulateAndGet(
        x: Int,
        accumulatorFunction: IntBinaryOperator
    ): Int
}
