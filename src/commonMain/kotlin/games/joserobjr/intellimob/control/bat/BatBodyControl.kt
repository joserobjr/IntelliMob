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

package games.joserobjr.intellimob.control.bat

import games.joserobjr.intellimob.block.BlockState
import games.joserobjr.intellimob.block.RegularBlock
import games.joserobjr.intellimob.control.api.BodyController
import games.joserobjr.intellimob.entity.EntityFlag
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.entity.Sound
import games.joserobjr.intellimob.math.*
import games.joserobjr.intellimob.math.angle.PitchYaw
import games.joserobjr.intellimob.math.extensions.squared
import games.joserobjr.intellimob.math.generic.DoubleVectorXYZ
import games.joserobjr.intellimob.math.motion.IHorizontalVelocity
import games.joserobjr.intellimob.math.motion.Velocity
import games.joserobjr.intellimob.math.position.entity.EntityPos
import games.joserobjr.intellimob.trait.WithEntityPos
import games.joserobjr.intellimob.trait.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.math.sign
import kotlin.random.Random

/**
 * @author joserobjr
 * @since 2021-01-21
 */
internal class BatBodyControl(override val owner: RegularEntity) : BodyController {
    private var hangingBlock: RegularBlock? = null
    private var isRoosting
        get() = owner.flagManager[EntityFlag.RESTING]
        set(value) {
            owner.flagManager.toggleFlags(value, EntityFlag.RESTING, EntityFlag.MOVING)
        }

    override fun CoroutineScope.walkTo(pos: WithEntityPos, acceptableDistance: Double, speed: IHorizontalVelocity): Job? {
        return null
    }

    override suspend fun idleTask() {
        if (isRoosting) {
            updateRoosting()
        } else {
            updateNotRoosting()
        }

        owner.applyPhysics()
    }
    
    private suspend fun updateRoosting() {
        if (hangingBlock?.isSolid() != true) {
            owner.playSound(Sound.MOB_BAT_TAKEOFF)
            return
        }
        
        if (Random.nextInt(200) == 0) {
            owner.update {
                headPitchYaw = PitchYaw(0.0, Random.nextDouble(360.0))
            }
        }
        
        if (owner.world.hasPlayerIn(owner.boundingBox.expandBy(4.0)) != null) {
            owner.update {
                isRoosting = false
            }
            owner.playSound(Sound.MOB_BAT_TAKEOFF)
            return
        }
    }
    
    private suspend fun updateNotRoosting() {
        var hangingBlock = hangingBlock
        if (hangingBlock != null && (hangingBlock.position.y < 1 || hangingBlock.currentState() != BlockState.AIR)) {
            hangingBlock = null
        }

        val current = owner.position
        val hangingBlockPos = hangingBlock?.position
        val nextPos: EntityPos =
            if (hangingBlockPos == null || hangingBlockPos.squaredDistance(current) < 2.squared() || Random.nextInt(30) == 0) {
                val pos = current + EntityPos(
                    Random.nextInt(-6, 7).toDouble(),
                    Random.nextInt(-2, 4).toDouble(),
                    Random.nextInt(-6, 7).toDouble(),
                )
                hangingBlock = owner.world.getBlock(pos)
                this.hangingBlock = hangingBlock
                pos
            } else hangingBlockPos.toCenteredEntityPos(yInc = .1)

        val speed = nextPos + CONSTANT - current
        val currentSpeed = owner.motion
        val adjusted = currentSpeed + Velocity(
            (sign(speed.x) * 0.5 - currentSpeed.x) * 0.1,
            (sign(speed.y) * 0.7 - currentSpeed.y) * 0.1,
            (sign(speed.z) * 0.5 - currentSpeed.z) * 0.1
        )
        
        
        val currentYaw = owner.headPitchYaw.yaw
        val yaw = adjusted.toYaw()
        val adjustedYaw = PitchYaw(pitch = 0.0, yaw = currentYaw + yaw)
        val attemptToRest = Random.nextInt(200) == 0
        owner.update {
            motion = adjusted.toImmutable()
            headPitchYaw = adjustedYaw
            if (attemptToRest) {
                val above = owner.world.getBlock(current.toBlockPos().up())
                if (above.isSolid()) {
                    this@BatBodyControl.hangingBlock = above
                    isRoosting = true
                }
            }
        }
    }

    override fun isMoving(): Boolean {
        return true
    }

    companion object {
        val CONSTANT = DoubleVectorXYZ(.5, .1, .5)
    }
}
