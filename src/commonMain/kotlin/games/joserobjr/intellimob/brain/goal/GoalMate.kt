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

import games.joserobjr.intellimob.entity.EntityFlag.BABY
import games.joserobjr.intellimob.entity.EntityFlag.IN_LOVE
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.extensions.ticks
import games.joserobjr.intellimob.math.motion.HorizontalVelocity
import games.joserobjr.intellimob.world.GameRule
import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

/**
 * @author joserobjr
 * @since 2021-02-11
 */
internal class GoalMate(val speed: HorizontalVelocity = HorizontalVelocity.ONE): Goal(MOVE) {
    override val defaultPriority: Int get() = 9_000_000
    override val needsMemory: Boolean get() = true
    override suspend fun canStart(entity: RegularEntity, memory: Memory?): Boolean {
        requireNotNull(memory)
        if (!entity.flagManager[IN_LOVE]) {
            return false
        }
        
        val mate = findMate(entity) ?: return false
        memory["mate"] = mate
        return true
    }
    
    private fun canBreed(entity: RegularEntity, with: RegularEntity): Boolean {
        return entity.type == with.type && with.flagManager[IN_LOVE]
    }
    
    private suspend fun findMate(entity: RegularEntity): RegularEntity? {
        return entity.world.findClosestEntity(entity.position, entity.boundingBox.expandBy(8.0)) {
            it != entity && canBreed(entity, with = it)
        }
    }

    @OptIn(ExperimentalTime::class)
    override fun CoroutineScope.start(entity: RegularEntity, memory: Memory?): Job? {
        requireNotNull(memory)
        val mate: RegularEntity = memory["mate"] ?: return null
        return launch { 
            with(entity.brain.wishes) {
                fun stop(reason: String) {
                    cancel(reason)
                }
                
                launch(entity.updateDispatcher) { 
                    delay(3.seconds)
                    while (!breed(entity, mate)) {
                        delay(1.ticks)
                    }
                    stop("Breed successfully")
                }
                
                launch { 
                    while (true) {
                        if (!entity.isValid || !mate.isValid || !entity.flagManager[IN_LOVE] || !mate.flagManager[IN_LOVE]) {
                            stop("They are no longer in love")
                            break
                        }
                        delay(1.ticks)
                    }
                }

                lookAt(mate.trackingEyePosition, quickly = true, timeLimit = 3.5.seconds)
                try {
                    withTimeout(3.5.seconds) {
                        while (true) {
                            moveTo(mate, speedMultiplier = speed).join()
                        }
                    }
                } finally {
                    stop("Finalizing")
                }
            }
        }
    }
    
    suspend fun breed(entityA: RegularEntity, entityB: RegularEntity): Boolean {
        if (!entityA.flagManager[IN_LOVE] || !entityB.flagManager[IN_LOVE]) {
            return false
        }
        
        val child: RegularEntity = entityA.createChild(entityB) ?: return false
        child.flagManager[BABY] = true
        child.position = entityA.position
        
        entityA.flagManager[IN_LOVE] = false
        entityB.flagManager[IN_LOVE] = false
        
        entityA.breedingAge = 6_000
        entityB.breedingAge = 6_000
        
        if (entityA.world[GameRule.DO_MOB_LOOT]) {
            entityA.world.spawnExperienceOrbs(entityA.position, Random.nextInt(1, 8))
        }
        return true
    }
}
