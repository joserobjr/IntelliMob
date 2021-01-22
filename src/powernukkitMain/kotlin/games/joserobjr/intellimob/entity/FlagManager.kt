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

package games.joserobjr.intellimob.entity

import cn.nukkit.entity.Entity

/**
 * @author joserobjr
 * @since 2021-01-22
 */
internal class FlagManager(private val entity: Entity) : IEntityFlagManager {
    override fun set(flag: EntityFlag, value: Boolean) {
        entity.setDataFlag(flag.container, flag.id, value)
    }

    override fun get(flag: EntityFlag): Boolean {
        return entity.getDataFlag(flag.container, flag.id)
    }

    override fun enableFlags(vararg flags: EntityFlag) {
        flags.forEach { entity.setDataFlag(it.container, it.id, true) }
    }

    override fun disableFlags(vararg flags: EntityFlag) {
        flags.forEach { entity.setDataFlag(it.container, it.id, false) }
    }
}
