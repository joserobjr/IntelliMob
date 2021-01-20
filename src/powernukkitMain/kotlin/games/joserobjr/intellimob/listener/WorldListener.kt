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

package games.joserobjr.intellimob.listener

import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.level.LevelLoadEvent
import games.joserobjr.intellimob.intelliMob
import games.joserobjr.intellimob.world.asIntelliMobWorld

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal object WorldListener: Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onWorldLoad(ev: LevelLoadEvent) {
        check(!ev.level.asIntelliMobWorld().job.isCompleted) {
            "The AI job for the level ${ev.level} is completed on load. This is an IntelliMob bug, pease report."
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onWorldUnload(ev: LevelLoadEvent) {
        ev.level.removeMetadata("job", intelliMob)
    }
}
