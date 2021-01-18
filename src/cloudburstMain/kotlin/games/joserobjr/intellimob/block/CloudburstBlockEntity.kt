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

import games.joserobjr.intellimob.math.IBlockPos
import games.joserobjr.intellimob.math.toBlockPos
import games.joserobjr.intellimob.world.RegularWorld
import games.joserobjr.intellimob.world.asIntelliMobWorld
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.cloudburstmc.server.blockentity.BlockEntity
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

internal inline class CloudburstBlockEntity(override val cloudburstBlockEntity: BlockEntity): RegularBlockEntity {
    override val updateDispatcher: CoroutineDispatcher get() = world.updateDispatcher
    override val position: IBlockPos get() = cloudburstBlockEntity.position.toBlockPos()
    override val world: RegularWorld get() = cloudburstBlockEntity.level.asIntelliMobWorld()
    
    override suspend fun createSnapshot(): BlockEntitySnapshot = withContext(updateDispatcher) {
        with(cloudburstBlockEntity) {
            BlockEntitySnapshot(
                location = location,
                type = type,
                content = serverTag
            )
        }
    }

    @ExperimentalTime
    override val timeSource: TimeSource
        get() = world.timeSource
}
