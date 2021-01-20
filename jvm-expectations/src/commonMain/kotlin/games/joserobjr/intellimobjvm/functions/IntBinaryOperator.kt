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

package games.joserobjr.intellimobjvm.functions


/**
 * Represents an operation upon two `int`-valued operands and producing an
 * `int`-valued result.   This is the primitive type specialization of
 * [BinaryOperator] for `int`.
 *
 *
 * This is a [functional interface](package-summary.html)
 * whose functional method is [.applyAsInt].
 *
 * @see BinaryOperator
 *
 * @see IntUnaryOperator
 *
 * @since 1.8
 */
public expect fun interface IntBinaryOperator {
    /**
     * Applies this operator to the given operands.
     *
     * @param left the first operand
     * @param right the second operand
     * @return the operator result
     */
    public fun applyAsInt(left: Int, right: Int): Int
}
