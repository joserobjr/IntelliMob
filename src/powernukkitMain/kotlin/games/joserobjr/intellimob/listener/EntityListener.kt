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
import cn.nukkit.event.entity.EntityDespawnEvent
import cn.nukkit.event.entity.EntitySpawnEvent
import games.joserobjr.intellimob.coroutines.AI
import games.joserobjr.intellimob.entity.asRegularEntity
import games.joserobjr.intellimob.intelliMob
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal object EntityListener: Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityPreSpawn(ev: EntitySpawnEvent) {
        ev.entity.asRegularEntity() // Setup the AI on first call
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onEntityPostSpawn(ev: EntitySpawnEvent) {
        val entity = ev.entity.asRegularEntity()
        CoroutineScope(Dispatchers.AI + entity.job).launch {
            entity.brain.startThinking()
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onEntityDespawnEvent(ev: EntityDespawnEvent) {
        ev.entity.asRegularEntity().job.cancel(CancellationException("The entity despawned"))
        ev.entity.removeMetadata("regularEntity", intelliMob)
    }
}
