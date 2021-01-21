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

package games.joserobjr.intellimob.block

import games.joserobjr.intellimob.math.BoundingBox
import games.joserobjr.intellimob.trait.WithBlockLocation

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal interface RegularBlock: PlatformBlock, WithBlockLocation {
    suspend fun currentState(layer: Int = 0): BlockState
    suspend fun currentStates(): LayeredBlockState
    suspend fun currentBlockEntity(): RegularBlockEntity?
    suspend fun createSnapshot(includeBlockEntity: Boolean = false): BlockSnapshot
    suspend fun currentBoundingBox(): BoundingBox
    suspend fun currentLiquidState(): LiquidState?
    suspend fun changeBlock(main: BlockState, vararg extra: BlockState, entitySnapshot: BlockEntitySnapshot? = null): Boolean
    suspend fun changeBlock(snapshot: BlockSnapshot): Boolean {
        return changeBlock(snapshot.states.main, *snapshot.states.extra.toTypedArray(), entitySnapshot = snapshot.blockEntity)
    }

    suspend fun up(): RegularBlock = world.getBlock(position.up())

    override val regularBlock: RegularBlock get() = this
}
