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

package games.joserobjr.intellimob.math.motion

import games.joserobjr.intellimob.math.generic.IDoubleVectorXZ

/**
 * @author joserobjr
 * @since 2021-02-11
 */
internal data class HorizontalVelocity(
    override val x: Double,
    override val z: Double,
): IHorizontalVelocity {
    constructor(xz: Double): this(xz, xz)
    constructor(vector: IDoubleVectorXZ): this(vector.x, vector.z)
    
    companion object {
        val ONE = HorizontalVelocity(1.0)
    }
}
