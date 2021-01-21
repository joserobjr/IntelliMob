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

package games.joserobjr.intellimob.entity

import cn.nukkit.entity.Entity
import cn.nukkit.math.Vector3
import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.api.EntityControls
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.entity.status.MutableEntityStatus
import games.joserobjr.intellimob.math.*
import games.joserobjr.intellimob.pathfinding.BlockFavorProvider
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.timesource.ServerTickTimeSource
import games.joserobjr.intellimob.trait.update
import games.joserobjr.intellimob.trait.updateAndGet
import games.joserobjr.intellimob.world.RegularWorld
import games.joserobjr.intellimob.world.asIntelliMobWorld
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * Wraps a PowerNukkit entity object and tag it as a regular entity.
 * @author joserobjr
 */
internal class PowerNukkitEntity(override val powerNukkitEntity: Entity) : RegularEntity {
    override val job: Job = SupervisorJob(world.job)
    override val updateDispatcher: CoroutineDispatcher get() = world.updateDispatcher
    override val type: EntityType = EntityType.fromEntity(this)
    
    override val baseStatus: MutableEntityStatus = type.aiFactory.createBaseStatus(this)
    override var controls: EntityControls = type.aiFactory.createControls(this)
    override var blockFavor: BlockFavorProvider = type.aiFactory.createBlockFavor(this)
    override var pathFinder: PathFinder = type.aiFactory.createPathFinder(this)
    override val brain: Brain = type.aiFactory.createBrain(this)

    private val _currentStatus: MutableEntityStatus = baseStatus.copy()
    override val currentStatus: EntityStatus = _currentStatus.asImmutableView()

    @ExperimentalTime
    override val timeSource: TimeSource get() = ServerTickTimeSource
    
    override val position: EntityPos get() = powerNukkitEntity.toEntityPos()
    override val eyePosition: EntityPos get() = with(powerNukkitEntity) { EntityPos(x, y + eyeHeight, z) }
    override val world: RegularWorld get() = powerNukkitEntity.level.asIntelliMobWorld()
    override val boundingBox: BoundingBox get() = powerNukkitEntity.boundingBox?.toIntelliMobBoundingBox() ?: BoundingBox.EMPTY
    
    private var bodyPitch = 0.0
    
    override var headPitchYaw: PitchYaw 
        get() = with(powerNukkitEntity) { PitchYaw(pitch, yaw) }
        set(value) {
            with(powerNukkitEntity) {
                pitch = value.pitch
                yaw = value.yaw
            }
        }

    override var bodyPitchYaw: PitchYaw
        get() = with(powerNukkitEntity) { PitchYaw(bodyPitch, yaw) }
        set(value) {
            bodyPitch = value.pitch
            powerNukkitEntity.yaw = value.yaw
        }

    override fun hasPassengers(): Boolean = powerNukkitEntity.passengers.isNotEmpty()

    override suspend fun moveTo(nextPos: EntityPos): Boolean = updateAndGet {
        with(powerNukkitEntity) {
            move(nextPos.x - x, nextPos.y - y, nextPos.z - z)
        }
    }

    override suspend fun applyMotion(motion: IDoubleVectorXYZ): Boolean = updateAndGet {
        with(powerNukkitEntity) {
            powerNukkitEntity.setMotion(Vector3(motionX + motion.x, motionY + motion.y, motionZ + motion.z))
        }
    }

    override suspend fun applySpeed(vector: IDoubleVectorXYZ, force: IDoubleVectorXYZ): Boolean = updateAndGet {
        with(powerNukkitEntity) {
            motionX = vector.x
            motionY = vector.y
            motionZ = vector.z
//            // Create vector
//            var x = motionX - vector.x
//            var y = motionY - vector.y
//            var z = motionZ - vector.z
//
//            // Normalize
//            val squaredLen = x.squared() + y.squared() + z.squared()
//            if (squaredLen > 0) {
//                val len = sqrt(squaredLen)
//                x /= len
//                y /= len
//                z /= len
//
//                // Apply speed
//                x *= force.x
//                y *= force.y
//                z *= force.z
//
//                powerNukkitEntity.motion = Vector3(motionX + x, motionY + y, motionZ + z)
//            }
        }
        true
    }

    override suspend fun calculateInertiaMotion(): IDoubleVectorXYZ {
        return DEFAULT_GRAVITY
    }

    override suspend fun calculateDrag(): IDoubleVectorXYZ {
        return DEFAULT_DRAG
    }
    
    override suspend fun applyPhysics() = update {
        with(powerNukkitEntity) {
            val inertia = calculateInertiaMotion()
            motionX += inertia.x
            motionY += inertia.y
            motionZ += inertia.z
            move(motionX, motionY, motionZ)
            
            val drag = calculateDrag()
            motionX *= 1 - drag.x
            motionY *= 1 - drag.y
            motionZ *= 1 - drag.z
            updateMovement()
        }
    }
    
    override suspend fun createSnapshot(): EntitySnapshot = updateAndGet {
        with(powerNukkitEntity) {
            EntitySnapshot(
                type = type,
                location = EntityLocation(world, x, y, z),
                pitchYaw = PitchYaw(pitch, yaw),
                status = currentStatus
            )
        }
    }
    
    companion object {
        private val DEFAULT_GRAVITY = DoubleVectorXYZ(0.0, -0.04, 0.0) 
        private val DEFAULT_DRAG = DoubleVectorXYZ(0.02) 
    }
}
