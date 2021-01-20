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

package games.joserobjr.intellimob

import cn.nukkit.entity.Entity
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.entity.EntityDespawnEvent
import cn.nukkit.event.entity.EntitySpawnEvent
import cn.nukkit.event.level.LevelLoadEvent
import cn.nukkit.plugin.PluginBase
import games.joserobjr.intellimob.coroutines.AI
import games.joserobjr.intellimob.entity.EntityIronGolem
import games.joserobjr.intellimob.entity.asRegularEntity
import games.joserobjr.intellimob.world.asIntelliMobWorld
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal class IntelliMobPowerNukkitPlugin: PluginBase() {
    override fun onEnable() {
        instance = this
        registerTemporaryEntities()
        server.pluginManager.let { manager ->
            manager.registerEvents(WorldListener, this)
            manager.registerEvents(EntityListener, this)
        }
    }
    
    private object WorldListener: Listener {
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
    
    private object EntityListener: Listener {
        @EventHandler(priority = EventPriority.HIGHEST)
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
    
    companion object {
        internal lateinit var instance: IntelliMobPowerNukkitPlugin; private set
    }
    
    private fun registerTemporaryEntities() {
        Entity.registerEntity("IronGolem", EntityIronGolem::class.java)
    }
}
