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

package games.joserobjr.intellimob.entity.factory

import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.api.EntityControls
import games.joserobjr.intellimob.entity.IEntityFlagManager
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.entity.status.MutableEntityStatus
import games.joserobjr.intellimob.pathfinding.PathFinder

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal object UninitializedAI: EntityAIFactory {
    private fun fail(): Nothing {
        throw IllegalStateException("The entity AI factory has not been initialized yet")
    }
    
    override fun createBrain(regularEntity: RegularEntity): Brain {
        fail()
    }

    override fun adjustDefaultStatus(status: MutableEntityStatus) {
        fail()
    }

    override fun setDefaultFlags(manager: IEntityFlagManager) {
        fail()
    }

    override fun createControls(regularEntity: RegularEntity): EntityControls {
        fail()
    }

    override fun createPathFinder(regularEntity: RegularEntity): PathFinder {
        fail()
    }

    override fun createBaseStatus(regularEntity: RegularEntity): MutableEntityStatus {
        fail()
    }
}
