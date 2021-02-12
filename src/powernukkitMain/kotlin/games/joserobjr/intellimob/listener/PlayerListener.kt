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
import cn.nukkit.entity.Entity
import cn.nukkit.entity.EntityCreature
import cn.nukkit.entity.passive.EntityAnimal
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerInteractEntityEvent
import cn.nukkit.item.RuntimeItems
import cn.nukkit.network.protocol.EntityEventPacket
import cn.nukkit.network.protocol.LevelSoundEventPacket
import cn.nukkit.network.protocol.LevelSoundEventPacketV2
import games.joserobjr.intellimob.entity.EntityFlag
import games.joserobjr.intellimob.entity.asRegularEntity
import games.joserobjr.intellimob.item.asRegularItemStack
import kotlin.time.ExperimentalTime

/**
 * @author joserobjr
 * @since 2021-02-11
 */
internal object PlayerListener: Listener {
    private val Entity.creature get() = (this as? EntityCreature).takeUnless { it is Player }
    
    @OptIn(ExperimentalTime::class)
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onUseBreedingItem(ev: PlayerInteractEntityEvent) {
        if (ev.player.isSpectator) return
        val item = ev.item?.asRegularItemStack() ?: return
        val creature = ev.entity.creature ?: return
        val entity = creature.asRegularEntity()
        if (entity.breedingAge > 0) return
        if (item.type in entity.brain.breedingItems || creature is EntityAnimal && creature.isBreedingItem(item.powerNukkitItemStack)) {
            if (!ev.player.isCreative) {
                ev.player.inventory.itemInHand = item.powerNukkitItemStack.clone().also { it.count-- }
            }
            val isBaby = entity.flagManager[EntityFlag.BABY]
            val packets = arrayOf(
                LevelSoundEventPacket().apply {
                    sound = LevelSoundEventPacketV2.SOUND_EAT
                    entityIdentifier = creature.saveId
                    x = creature.x.toFloat()
                    y = creature.y.toFloat()
                    z = creature.z.toFloat()
                    isBabyMob = isBaby
                },
                EntityEventPacket().apply { 
                    eid = creature.id
                    event = EntityEventPacket.EATING_ITEM
                    data = (RuntimeItems.getNetworkId(RuntimeItems.getRuntimeMapping().getNetworkFullId(item.powerNukkitItemStack)) shl 16) or ev.item.damage
                },
                EntityEventPacket().apply { 
                    eid = creature.id
                    event = if (isBaby) EntityEventPacket.BABY_ANIMAL_FEED else EntityEventPacket.LOVE_PARTICLES
                }
            )
            if (!entity.flagManager[EntityFlag.BABY]) {
                entity.startLoving()
            } else if (entity.breedingAge < 0) {
                entity.breedingAge -= (entity.breedingAge * 0.1).toInt()
            }
            ev.player.server.batchPackets(creature.viewers.values.toTypedArray(), packets)
        }
    }
}
