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

package games.joserobjr.intellimob.brain.wish

import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.generic.IDoubleVectorXYZ
import games.joserobjr.intellimob.math.position.entity.IEntityPos
import games.joserobjr.intellimob.trait.WithEntityPos

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal data class WishLookAtDelta(
    val delta: IDoubleVectorXYZ,
    override val quickly: Boolean = false,
): WishLook() {
    override suspend fun targetFor(owner: RegularEntity) = object : WithEntityPos {
        override val position: IEntityPos get() = owner.eyePosition + delta 
    }
}
