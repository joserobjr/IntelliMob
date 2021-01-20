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

package games.joserobjr.intellimob.brain.wish

import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.EntityControls
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.EntityPos
import games.joserobjr.intellimob.math.IDoubleVectorXYZ
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlin.time.*

/**
 * @author joserobjr
 * @since 2021-01-11
 */
@ExperimentalTime
internal class WishesOld (
    private val timeSource: TimeSource,
    look: WishLook? = null,
    move: WishMove? = null,
    jump: Wish? = null,
) {
    private val wishCounter = atomic(0)
    
    private val _look = atomic(look)
    private val _move = atomic(move)
    private val _jump = atomic(jump)

    val look: WishLook? by _look 
    val move: WishMove? by _move 
    val jump: Wish? by _jump 
    
    private var lookTimeOut: TimeMark? = null
    private var moveTimeOut: TimeMark? = null
    private var jumpTimeOut: TimeMark? = null
    
    fun isNotEmpty(): Boolean {
        val currentWishPos = wishCounter.value
        if (look != null || move != null || jump != null) {
            return true
        }
        val newPos = wishCounter.value
        return currentWishPos == newPos
    }
    
    fun moveTo(pos: EntityPos, timeLimit: Duration? = null, sprinting: Boolean = false) {
        _move.update {
            moveTimeOut = timeLimit?.let { timeSource.markNow() + it }
            WishMoveToPos(pos, sprinting)
        }
        wishCounter.incrementAndGet()
    }
    
    fun moveTo(entity: RegularEntity, timeLimit: Duration? = null, sprinting: Boolean = false) {
        _move.update {
            moveTimeOut = timeLimit?.let { timeSource.markNow() + it }
            WishMoveToEntity(entity, sprinting)
        }
        wishCounter.incrementAndGet()
    }
    
    fun stayStill(timeLimit: Duration) {
        _move.update {
            moveTimeOut = timeSource.markNow() + timeLimit
            WishStayStill
        }
        wishCounter.incrementAndGet()
    }
    
    fun lookAt(pos: EntityPos, timeLimit: Duration? = null, quickly: Boolean = false) {
        _look.update {
            lookTimeOut = timeLimit?.let { timeSource.markNow() + it }
            WishLookAtPos(pos, quickly)
        }
        wishCounter.incrementAndGet()
    }
    
    fun lookAt(entity: RegularEntity, timeLimit: Duration? = null, quickly: Boolean = false) {
        _look.update {
            lookTimeOut = timeLimit?.let { timeSource.markNow() + it }
            WishLookAtEntity(entity, quickly)
        }
        wishCounter.incrementAndGet()
    }
    
    fun lookAtDelta(delta: IDoubleVectorXYZ, timeLimit: Duration? = null, quickly: Boolean = false) {
        _look.update {
            lookTimeOut = timeLimit?.let { timeSource.markNow() + it }
            WishLookAtDelta(delta, quickly)
        }
        wishCounter.incrementAndGet()
    }
    
    fun jumpOnce(timeLimit: Duration = 3.seconds) {
        _jump.update { 
            jumpTimeOut = timeSource.markNow() + timeLimit
            WishJumpOnce
        }
    }
    
    fun EntityControls.execute(brain: Brain): Boolean {
        val currentWishPos = wishCounter.value
        var needsUpdate = false
        move?.apply {
            if (moveTimeOut?.hasNotPassedNow() != false && execute(brain)) {
                if (!_move.compareAndSet(this, null)) {
                    needsUpdate = true
                }
            } else {
                needsUpdate = true
            }
        }
        jump?.apply {
            if (jumpTimeOut?.hasNotPassedNow() != false && execute(brain)) {
                if (!_jump.compareAndSet(this, null)) {
                    needsUpdate = true
                }
            } else {
                needsUpdate = true
            }
        }
        look?.apply {
            if (lookTimeOut?.hasNotPassedNow() != false && execute(brain)) {
                if (!_look.compareAndSet(this, null)) {
                    needsUpdate = true
                }
            } else {
                needsUpdate = true
            }
        }
        val pending = wishCounter.addAndGet(-currentWishPos)
        return needsUpdate || pending != 0
    }
}
