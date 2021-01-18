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

import games.joserobjr.intellimob.math.BlockLocation
import games.joserobjr.intellimob.math.BoundingBox
import games.joserobjr.intellimob.math.toVector3i
import kotlinx.coroutines.withContext

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal class CloudburstBlock(override val location: BlockLocation): RegularBlock {
    override suspend fun currentState(layer: Int): BlockState {
        return with(location) {
            world.cloudburstWorld.getBlock(x, y, z).getState(layer).asIntelliMobBlockState()
        }
    }

    override suspend fun currentStates(): LayeredBlockState = withContext(world.updateDispatcher) {
        with(location) {
            world.cloudburstWorld.getBlock(x, y, z).toLayeredBlockState()
        }
    }

    override suspend fun currentBlockEntity(): RegularBlockEntity? {
        return world.cloudburstWorld.getBlockEntity(location.toVector3i())?.asRegularBlockEntity()
    }

    override suspend fun currentBoundingBox(): BoundingBox {
        return currentState(1).boundingBox + location
    }

    override suspend fun createSnapshot(includeBlockEntity: Boolean): BlockSnapshot {
        val level = world.cloudburstWorld
        return if (includeBlockEntity) {
            withContext(world.updateDispatcher) {
                with(location) {
                    BlockSnapshot(
                        location = location,
                        states = level.getBlock(x, y, z).toLayeredBlockState(),
                        blockEntity = level.getBlockEntity(location.toVector3i())?.asRegularBlockEntity()?.createSnapshot()
                    )
                }
            }
        } else {
            with(location) {
                BlockSnapshot(
                    location = location,
                    states = level.getBlock(x, y, z).toLayeredBlockState()
                )
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegularBlock

        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        return location.hashCode()
    }

    override fun toString(): String {
        return "RegularBlock(location=$location)"
    }
}
