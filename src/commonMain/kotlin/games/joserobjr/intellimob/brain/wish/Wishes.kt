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
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal class Wishes (
    look: LookWish? = null,
    move: MoveWish? = null,
    jump: Wish? = null,
) {
    private val _look = atomic(look)
    private val _move = atomic(move)
    private val _jump = atomic(jump)

    val look: LookWish? by _look 
    val move: MoveWish? by _move 
    val jump: Wish? by _jump 
    
    fun moveTo(pos: EntityPos, sprinting: Boolean = false) {
        _move.update { move ->
            move?.takeIf { it.isConstant && it.target == pos }?.let { return }
            MoveToPosWish(pos, sprinting)
        }
    }
    
    fun moveTo(entity: RegularEntity, sprinting: Boolean = false) {
        _move.update { move ->
            move?.takeIf { it.targetEntity == entity }?.let { return }
            MoveToEntityWish(entity, sprinting)
        }
    }
    
    fun lookAt(pos: EntityPos, quickly: Boolean = false) {
        _look.update { look ->
            look?.takeIf { it.isConstant && it.target == pos }?.let { return }
            LookAtPosWish(pos, quickly)
        }
    }
    
    fun lookAt(entity: RegularEntity, quickly: Boolean = false) {
        _look.update { look ->
            look?.takeIf { it.targetEntity == entity }?.let { return }
            LookAtEntityWish(entity, quickly)
        }
    }
    
    fun EntityControls.execute(brain: Brain): Boolean {
        var needsUpdate = false
        move?.apply {
            if (execute(brain)) {
                _move.compareAndSet(this, null)
            } else {
                needsUpdate = true
            }
        }
        jump?.apply {
            if (execute(brain)) {
                _jump.compareAndSet(this, null)
            } else {
                needsUpdate = true
            }
        }
        look?.apply {
            if (execute(brain)) {
                if (!_look.compareAndSet(this, null)) {
                    needsUpdate = look != null
                }
            } else {
                needsUpdate = true
            }
        }
        return needsUpdate
    }
}
