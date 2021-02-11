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

@file:Suppress("NOTHING_TO_INLINE")

package games.joserobjr.intellimob.math

import cn.nukkit.block.Block
import cn.nukkit.level.Position
import games.joserobjr.intellimob.block.PNBlockState
import games.joserobjr.intellimob.block.PowerNukkitBlock
import games.joserobjr.intellimob.block.RegularBlock
import games.joserobjr.intellimob.math.position.block.BlockLocation

/**
 * @author joserobjr
 * @since 2021-01-19
 */
internal inline fun BlockLocation.asBlock(): RegularBlock = PowerNukkitBlock(this)
internal inline fun BlockLocation.getPNBlock(layer: Int = 0, load: Boolean = false): Block = world.powerNukkitLevel.getBlock(x, y, z, layer, load)
internal inline fun BlockLocation.getPNState(layer: Int = 0, load: Boolean = false): PNBlockState {
    with(world.powerNukkitLevel) {
        return if (!load && !isChunkLoaded(chunkX, chunkZ)) {
            PNBlockState.AIR
        } else {
            world.powerNukkitLevel.getBlockStateAt(x, y, z, layer)
        }
    }
}

internal fun BlockLocation.toPosition() = Position(x.toDouble(), y.toDouble(), z.toDouble(), world.powerNukkitLevel)
