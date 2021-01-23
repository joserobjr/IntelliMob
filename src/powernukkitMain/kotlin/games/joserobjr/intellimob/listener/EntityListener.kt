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

import cn.nukkit.Player
import cn.nukkit.entity.EntityCreature
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.entity.EntityDespawnEvent
import cn.nukkit.event.entity.EntityEvent
import cn.nukkit.event.entity.EntitySpawnEvent
import games.joserobjr.intellimob.coroutines.AI
import games.joserobjr.intellimob.entity.PowerNukkitEntity
import games.joserobjr.intellimob.entity.asRegularEntity
import games.joserobjr.intellimob.intelliMob
import games.joserobjr.intellimob.math.ticks
import kotlinx.coroutines.*
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal object EntityListener: Listener {
    private val EntityEvent.creature get() = (entity as? EntityCreature).takeUnless { it is Player } 
    
    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityPreSpawn(ev: EntitySpawnEvent) {
        ev.creature?.asRegularEntity() // Setup the AI on first call
    }

    @OptIn(ExperimentalTime::class)
    @EventHandler(priority = EventPriority.MONITOR)
    fun onEntityPostSpawn(ev: EntitySpawnEvent) {
        val creature = ev.creature?.asRegularEntity() ?: return
        CoroutineScope(Dispatchers.AI + creature.job).launch {
            delay(1.ticks)
            creature.brain.startThinking()
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onEntityDespawnEvent(ev: EntityDespawnEvent) {
        ev.entity.takeIf { it.hasMetadata("regularEntity") }?.apply {
            asRegularEntity().job.cancel(CancellationException("The entity despawned"))
            removeMetadata("regularEntity", intelliMob)
        }
    }
    
    @ExperimentalTime
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onEntityAttacked(ev: EntityDamageByEntityEvent) {
        val entity = ev.entity.asRegularEntity() as PowerNukkitEntity
        entity.isUnderAttack = true
        CoroutineScope(entity.updateDispatcher).launch { 
            delay(3.seconds)
            if (ev.entity.lastDamageCause === ev) {
                entity.isUnderAttack = false
            }
        }
    }
}
