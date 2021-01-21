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

package games.joserobjr.intellimob.world

import games.joserobjr.intellimob.block.BlockSnapshot
import games.joserobjr.intellimob.block.BlockState
import games.joserobjr.intellimob.block.RegularBlockEntity
import games.joserobjr.intellimob.math.IBlockPos
import games.joserobjr.intellimob.trait.WithTimeSource
import games.joserobjr.intellimob.trait.WithUpdateDispatcher
import kotlinx.coroutines.Job

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal interface RegularWorld: PlatformWorld, WorldView, WithTimeSource, WithUpdateDispatcher {
    val name: String
    val job: Job
    val cache: WorldView
    override val regularWorld: RegularWorld get() = this
    override suspend fun getBlockState(pos: IBlockPos, layer: Int): BlockState = getBlock(pos).currentState(layer)
    override suspend fun getBlockEntity(pos: IBlockPos): RegularBlockEntity? = getBlock(pos).currentBlockEntity()
    suspend fun restoreSnapshot(snapshot: BlockSnapshot): Boolean
}
