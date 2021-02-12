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

package games.joserobjr.intellimob.entity.childfactory

import cn.nukkit.entity.Entity
import games.joserobjr.intellimob.entity.EntityFlag
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.entity.asRegularEntity
import games.joserobjr.intellimob.math.toPosition

/**
 * @author joserobjr
 * @since 2021-02-12
 */
internal open class ChildFactory protected constructor() {
    open fun createChild(entityA: RegularEntity, entityB: RegularEntity): RegularEntity? {
        val pos = entityA.location.toPosition()
        return Entity.createEntity(entityA.type.networkId, pos)
            ?.apply { setScale(0.5f) }
            ?.asRegularEntity()?.apply {
                flagManager[EntityFlag.BABY]
                breedingAge = -24000
                powerNukkitEntity.spawnToAll()
            }
    }

    companion object {
        val SIMPLE = ChildFactory()
    }
}
