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

package games.joserobjr.intellimob.math.position.entity

import games.joserobjr.intellimob.math.generic.IDoubleVectorXYZ
import games.joserobjr.intellimob.trait.WithEntityPos

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal data class EntityPos (
    override val x: Double,
    override val y: Double,
    override val z: Double
): IEntityPos {
    constructor(pos: IDoubleVectorXYZ): this(pos.x, pos. y, pos.z)
    constructor(pos: IEntityPos): this(pos as IDoubleVectorXYZ)
    constructor(withPos: WithEntityPos): this(withPos.position)
    constructor(x: Float, y: Float, z: Float): this(x.toDouble(), y.toDouble(), z.toDouble())

    override fun toImmutable() = this
}
