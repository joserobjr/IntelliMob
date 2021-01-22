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

import cn.nukkit.block.BlockLiquid
import games.joserobjr.intellimob.math.*
import kotlinx.coroutines.withContext

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal inline class PowerNukkitBlock(
    override val location: BlockLocation
): RegularBlock {
    override suspend fun currentState(layer: Int): BlockState {
        return location.getPNState(layer).asIntelliMobBlockState()
    }

    override suspend fun currentBlockEntity(): RegularBlockEntity? { 
        return world.powerNukkitLevel.getBlockEntity(position.toBlockVector3())?.asIntelliMobBlockEntity() 
    }

    override suspend fun createSnapshot(includeBlockEntity: Boolean): BlockSnapshot = withContext(world.updateDispatcher) {
        BlockSnapshot(
            location = location,
            states = currentStates(),
            blockEntity = if (includeBlockEntity) currentBlockEntity()?.createSnapshot() else null
        )
    }

    override suspend fun currentStates(): LayeredBlockState = withContext(world.updateDispatcher) {
        LayeredBlockState(currentState(0), currentState(1))
    }

    override suspend fun currentBoundingBox(): BoundingBox = withContext(world.updateDispatcher) {
        world.powerNukkitLevel.getBlock(location.x, location.y, location.z).boundingBox?.toIntelliMobBoundingBox()
            ?: BoundingBox.EMPTY + location
    }

    override suspend fun currentLiquidState(): LiquidState? {
        val liquid = location.getPNBlock() as? BlockLiquid 
            ?: location.getPNBlock(layer = 1) as? BlockLiquid
            ?: return null
        
        return LiquidState(
            type = BlockType.fromPNBlock(liquid),
            height = liquid.fluidHeightPercent.toDouble(),
            pos = location,
            layer = liquid.layer,
            bounds = liquid.collisionBoundingBox.toIntelliMobBoundingBox()
        )
    }

    override suspend fun changeBlock(main: BlockState, vararg extra: BlockState, entitySnapshot: BlockEntitySnapshot?): Boolean {
        return changeBlock(BlockSnapshot(location, LayeredBlockState(main, *extra), entitySnapshot))
    }

    override suspend fun changeBlock(snapshot: BlockSnapshot): Boolean {
        return world.restoreSnapshot(snapshot)
    }

    override suspend fun isSolid(): Boolean {
        return location.getPNBlock().isSolid
    }
}
