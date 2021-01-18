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

import cn.nukkit.level.Level
import games.joserobjr.intellimob.coroutines.Sync
import games.joserobjr.intellimob.timesource.ServerTickTimeSource
import games.joserobjr.intellimob.trait.WithTimeSource
import games.joserobjr.intellimob.trait.WithWorld
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal actual interface World: WithWorld, WithTimeSource {
    val powerNukkitLevel: Level
}

internal inline class PowerWorldWrapper(override val powerNukkitLevel: Level): World {
    override val world: World get() = this
    
    @ExperimentalTime
    override val timeSource: TimeSource get() = ServerTickTimeSource
}

internal fun Level.asIntelliMobWorld(): World = PowerWorldWrapper(this)

internal actual val World.updateDispatcher: CoroutineDispatcher get() = Dispatchers.Sync
