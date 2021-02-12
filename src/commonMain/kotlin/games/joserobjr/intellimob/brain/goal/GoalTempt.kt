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

import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.item.ItemType
import games.joserobjr.intellimob.math.extensions.squared
import games.joserobjr.intellimob.math.extensions.ticks
import games.joserobjr.intellimob.math.motion.HorizontalVelocity
import games.joserobjr.intellimob.math.position.entity.IEntityPos
import games.joserobjr.intellimob.trait.WithEntityPos
import kotlinx.coroutines.*
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark

/**
 * @author joserobjr
 * @since 2021-01-24
 */
internal open class GoalTempt(val items: Set<ItemType>, val speed: HorizontalVelocity): Goal(MOVE_LOOK) {
    override val defaultPriority: Int get() = 10_900_000
    override val needsMemory: Boolean get() = true
    
    @OptIn(ExperimentalTime::class)
    override suspend fun canStart(entity: RegularEntity, memory: Memory?): Boolean {
        requireNotNull(memory)
        if (isInCooldown(memory)) {
            return false
        }
        
        val closestPlayer = findPlayer(entity, memory) ?: return false
        memory["player"] = closestPlayer
        resetCooldown(entity, memory)
        return true
    }
    
    @OptIn(ExperimentalTime::class)
    private fun isInCooldown(memory: Memory): Boolean {
        val cooldown: TimeMark? = memory["cooldown"]
        return cooldown?.hasNotPassedNow() == true
    }
    
    @OptIn(ExperimentalTime::class)
    private fun resetCooldown(entity: RegularEntity, memory: Memory) {
        memory["cooldown"] = entity.brain.timeSource.markNow() + 100.ticks
    }
    
    protected open suspend fun findPlayer(entity: RegularEntity, memory: Memory): RegularEntity? {
        return entity.world.findClosestPlayer(entity.eyePosition, entity.boundingBox.expandBy(10.00)) {
            it.isValid && isTemptedBy(it)
        }
    }
    
    protected open fun isTemptedBy(player: RegularEntity): Boolean {
        return sequence {
            yield(player.itemInMainHand)
            yield(player.itemInOffHand)
        }.filterNotNull().any { it.type in items }
    } 

    @OptIn(ExperimentalTime::class)
    override fun CoroutineScope.start(entity: RegularEntity, memory: Memory?): Job? {
        requireNotNull(memory)
        var player: RegularEntity = memory["player"] ?: return null
        val dynamicPlayerPos = object : WithEntityPos {
            override val position: IEntityPos get() = player.position
        }
        val dynamicPlayerEye = object : WithEntityPos {
            override val position: IEntityPos get() = player.eyePosition
        }
        return launch {
            val job = coroutineContext.job
            var (move, look) = with(entity.brain.wishes) {
                moveTo(dynamicPlayerPos, speedMultiplier = speed) to lookAt(dynamicPlayerEye) 
            }

            while (true) {
                delay(1.ticks)
                val nextPlayer: RegularEntity? = findPlayer(entity, memory)
                if (nextPlayer == null) {
                    job.cancel("No more targets")
                    break
                }
                
                player = nextPlayer
                if (!player.isValid) {
                    job.cancel("The player is dead")
                    break
                }
                if (!isTemptedBy(player)) {
                    job.cancel("The player is no longer holding the item")
                    break
                }

                resetCooldown(entity, memory)
                
                if (look.isCompleted) {
                    look = with(entity.brain.wishes) {
                        lookAt(dynamicPlayerEye)
                    }
                }
                
                val shouldMove = player.position.squaredDistance(entity.position) > 2.5.squared()
                if (!shouldMove) {
                    if (!move.isCompleted) {
                        move.cancel("Too close")
                    }
                } else if (move.isCompleted) {
                    move = with(entity.brain.wishes) {
                        moveTo(dynamicPlayerPos, speedMultiplier = speed)
                    }
                }
            }
        }
    }
}
