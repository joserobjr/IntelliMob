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

package games.joserobjr.intellimob.math.position.block

import games.joserobjr.intellimob.trait.WithBlockPos

/**
 * @author joserobjr
 * @since 2021-01-19
 */
internal data class MutableBlockPos (
    override var x: Int,
    override var y: Int,
    override var z: Int
): IBlockPos {
    constructor(pos: IBlockPos): this(pos.x, pos.y, pos.z)
    constructor(withPos: WithBlockPos): this(withPos.position)
    constructor(): this(0, 0, 0)
    
    fun setAll(coordinates: IBlockPos): MutableBlockPos {
        return setAll(coordinates.x, coordinates.y, coordinates.z)
    }

    fun setAll(value: Int): MutableBlockPos {
        return setAll(value, value, value)
    }

    fun update(x: Int = this.x, y: Int = this.y, z: Int = this.z): MutableBlockPos {
        return setAll(x, y, z)
    }
    
    fun setAll(x: Int, y: Int, z: Int): MutableBlockPos {
        this.x = x
        this.y = y
        this.z = z
        return this
    }
}
