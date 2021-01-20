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
 * Represents an operation on a single operand that produces a result of the
 * same type as its operand.  This is a specialization of `Function` for
 * the case where the operand and result are of the same type.
 *
 *
 * This is a functional interface
 * whose functional method is [apply].
 *
 * @param T the type of the operand and result of the operator
 *
 * @see Function
 */
public expect fun interface UnaryOperator<T> : Function<T, T>
