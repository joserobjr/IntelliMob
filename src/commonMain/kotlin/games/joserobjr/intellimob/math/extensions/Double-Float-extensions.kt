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

package games.joserobjr.intellimob.math.extensions

import kotlin.math.abs

private const val EPSILON_D = 0.000001
private const val EPSILON_F = 0.000001F

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal fun Double.isSimilarTo(other: Double, epsilon: Double = EPSILON_D) = abs(this - other) < epsilon 
internal fun Float.isSimilarTo(other: Float, epsilon: Float = EPSILON_F) = abs(this - other) < epsilon
internal fun Float.isSimilarTo(other: Double, epsilon: Double = EPSILON_D) = toDouble().isSimilarTo(other, epsilon)
internal fun Double.isSimilarTo(other: Float, epsilon: Double = EPSILON_D) = isSimilarTo(other.toDouble(), epsilon)

internal fun Double.isNotSimilarTo(other: Double, epsilon: Double = EPSILON_D) = !isSimilarTo(other, epsilon) 
internal fun Float.isNotSimilarTo(other: Float, epsilon: Float = EPSILON_F) = !isSimilarTo(other, epsilon) 
internal fun Float.isNotSimilarTo(other: Double, epsilon: Double = EPSILON_D) = !isSimilarTo(other, epsilon) 
internal fun Double.isNotSimilarTo(other: Float, epsilon: Double = EPSILON_D) = !isSimilarTo(other, epsilon) 
