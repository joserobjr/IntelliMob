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
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.server.BatchPacketsEvent
import cn.nukkit.network.protocol.DataPacket
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal object PacketListener: Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPacketSend(ev: BatchPacketsEvent) {
         ev.packets.forEach { 
            if (it.pid() == MoveEntityAbsolutePacket.NETWORK_ID) {
                onMoveEntityAbsolute(ev.players, it as MoveEntityAbsolutePacket)
            }
        }
    }
    
    private fun onMoveEntityAbsolute(players: Array<Player>, packet: MoveEntityAbsolutePacket) {
//        val entity = players.firstOrNull()?.level?.getEntity(packet.eid)?.asRegularEntity() as? PowerNukkitEntity ?: return
//        packet.yaw = entity.headYaw
//        packet.reEncode()
    }
    
    private fun DataPacket.reEncode() {
        if (isEncoded) {
            reset()
            encode()
        }
    }
}
