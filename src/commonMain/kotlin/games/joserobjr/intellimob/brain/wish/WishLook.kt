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

import games.joserobjr.intellimob.control.api.EntityControls
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.EntityPos
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal abstract class WishLook: Wish() {
    abstract val target: EntityPos
    abstract val targetEntity: RegularEntity?
    abstract val isConstant: Boolean
    abstract val quickly: Boolean

    override suspend fun EntityControls.start(): Job? {
        return coroutineScope {
            val status = owner.currentStatus
            lookAt(targetFor(owner), speed = if (quickly) status.headFastSpeed else status.headSpeed)
        }
    }
    
    protected open fun targetFor(owner: RegularEntity): EntityPos = target
}
