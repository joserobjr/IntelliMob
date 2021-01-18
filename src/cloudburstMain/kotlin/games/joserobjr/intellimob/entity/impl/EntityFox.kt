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

package games.joserobjr.intellimob.entity.impl

import games.joserobjr.intellimob.entity.api.Fox
import org.cloudburstmc.server.entity.EntityType
import org.cloudburstmc.server.entity.impl.passive.Animal
import org.cloudburstmc.server.level.Location

/**
 * @author joserobjr
 * @since 2021-01-12
 */
internal class EntityFox(type: EntityType<*>?, location: Location?) : Animal(type, location), Fox {
    override fun initEntity() {
        super.initEntity()
        maxHealth = 20
    }

    override fun getName(): String = "Fox"
    override fun getWidth(): Float = 0.7f
    override fun getHeight(): Float = 0.6f
}
