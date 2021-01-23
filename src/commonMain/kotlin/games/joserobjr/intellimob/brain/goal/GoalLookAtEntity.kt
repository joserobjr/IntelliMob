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

package games.joserobjr.intellimob.brain.goal

import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.api.PhysicalControl
import games.joserobjr.intellimob.entity.EntityType
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimobjvm.atomic.atomic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal class GoalLookAtEntity(val types: Set<EntityType>, val range: Float, val chance: Float = .2F, val condition: (suspend (Brain, RegularEntity)->Boolean)? = null): Goal(setOf(PhysicalControl.LOOK)) {
    override val defaultPriority: Int get() = 100_800_000
    override val needsMemory: Boolean get() = true
    
    var target = atomic<RegularEntity?>(null)

    override suspend fun canStart(entity: RegularEntity, memory: Memory?): Boolean {
        requireNotNull(memory)
        if (types.isEmpty() || Random.nextFloat() >= chance) {
            return false
        }
        
        val target = with(entity) {
            val area = boundingBox.expandBy(range, 3F, range)
            val condition = condition
            if (types.size == 1 && types.first() == EntityType.PLAYER) {
                var predicate: (suspend (RegularEntity) -> Boolean)? = null
                if (condition != null) {
                    predicate = { player -> condition(brain, player) }
                }
                world.cache.findClosestPlayer(eyePosition, area, condition = predicate)
            } else {
                world.cache.findClosestEntity(eyePosition, area) { entity ->
                    entity.type in types && (condition == null || condition(brain, entity))
                }
            }
        }
        
        if (target != null) {
            memory.setWeak("target", target)
            return true
        }
        return false
    }

    @OptIn(ExperimentalTime::class)
    override fun CoroutineScope.start(entity: RegularEntity, memory: Memory?): Job? {
        memory ?: return null
        val target: RegularEntity = memory["target"] ?: return null
        val duration = Random.nextInt(2_000, 4_000).milliseconds
        return launch {
            with(entity.brain.wishes) {
                lookAt(
                    entity = target,
                    timeLimit = duration
                )
                stayStill(duration)
            }
        }
    }
}
