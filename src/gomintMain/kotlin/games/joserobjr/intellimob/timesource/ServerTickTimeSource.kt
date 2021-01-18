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

package games.joserobjr.intellimob.timesource

import games.joserobjr.intellimob.trait.WithTimeSource
import kotlinx.atomicfu.atomic
import java.util.concurrent.TimeUnit
import kotlin.time.AbstractLongTimeSource
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * @author joserobjr
 * @since 2021-01-17
 */
@ExperimentalTime
internal object ServerTickTimeSource: AbstractLongTimeSource(TimeUnit.MILLISECONDS), WithTimeSource {
    private val currentTickAtomic = atomic(0L)
    val currentTick by currentTickAtomic
    
    fun increment(ticks: Long = 1L): Long {
        require(ticks > 0L) { "Ticks must be positive. Got $ticks" }
        return currentTickAtomic.addAndGet(ticks)
    }
    
    override fun read() = currentTick * 50

    @ExperimentalTime
    override val timeSource: TimeSource get() = this
}
