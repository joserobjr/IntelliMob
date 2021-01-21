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
import games.joserobjr.intellimob.trait.WithEntityPos
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal abstract class WishLook: Wish() {
    abstract val quickly: Boolean
    
    override suspend fun EntityControls.start(): Job? {
        val target = targetFor(owner) ?: return null
        val status = owner.currentStatus
        return coroutineScope {
            lookAt(target, speed = if (quickly) status.headFastSpeed else status.headSpeed)
        }
    }

    protected abstract suspend fun targetFor(owner: RegularEntity): WithEntityPos?
}
