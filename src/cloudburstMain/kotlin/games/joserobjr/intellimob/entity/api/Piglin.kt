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

package games.joserobjr.intellimob.entity.api

import games.joserobjr.intellimob.entity.PlatformEntityType
import org.cloudburstmc.server.entity.Entity
import org.cloudburstmc.server.entity.EntityType

/**
 * @author joserobjr
 * @since 2021-01-12
 */
internal interface Piglin: Entity {
    companion object {
        val TYPE: EntityType<Piglin> = PlatformEntityType.from("piglin", Piglin::class.java)
    }
}
