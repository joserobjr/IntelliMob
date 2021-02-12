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

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.entity.Entity
import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.EntityEventPacket
import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.api.EntityControls
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.entity.status.MutableEntityStatus
import games.joserobjr.intellimob.item.RegularItemStack
import games.joserobjr.intellimob.item.asRegularItemStack
import games.joserobjr.intellimob.math.angle.PitchYaw
import games.joserobjr.intellimob.math.collision.BoundingBox
import games.joserobjr.intellimob.math.extensions.isSimilarTo
import games.joserobjr.intellimob.math.motion.IVelocity
import games.joserobjr.intellimob.math.motion.Velocity
import games.joserobjr.intellimob.math.position.entity.EntityLocation
import games.joserobjr.intellimob.math.position.entity.EntityPos
import games.joserobjr.intellimob.math.toEntityPos
import games.joserobjr.intellimob.math.toIntelliMobBoundingBox
import games.joserobjr.intellimob.math.toVector3
import games.joserobjr.intellimob.pathfinding.BlockFavorProvider
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.timesource.ServerTickTimeSource
import games.joserobjr.intellimob.trait.update
import games.joserobjr.intellimob.trait.updateAndGet
import games.joserobjr.intellimob.world.RegularWorld
import games.joserobjr.intellimob.world.asIntelliMobWorld
import games.joserobjr.intellimobjvm.atomic.atomic
import kotlinx.coroutines.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.seconds

/**
 * Wraps a PowerNukkit entity object and tag it as a regular entity.
 * @author joserobjr
 */
internal class PowerNukkitEntity(override val powerNukkitEntity: Entity) : RegularEntity {
    override val job: Job = SupervisorJob(world.job)
    override val updateDispatcher: CoroutineDispatcher get() = world.updateDispatcher
    override val type: EntityType = EntityType.fromEntity(this)

    override val flagManager = FlagManager(powerNukkitEntity).also { type.aiFactory.setDefaultFlags(it) }
    override val baseStatus: MutableEntityStatus = type.aiFactory.createBaseStatus(this)
    override var controls: EntityControls = type.aiFactory.createControls(this)
    override var blockFavor: BlockFavorProvider = type.aiFactory.createBlockFavor(this)
    override var pathFinder: PathFinder = type.aiFactory.createPathFinder(this)
    override val brain: Brain = type.aiFactory.createBrain(this)

    private val _currentStatus: MutableEntityStatus = baseStatus.copy()
    override val currentStatus: EntityStatus = _currentStatus.asImmutableView()
        get() {
            updateStatus()
            return field
        }

    @ExperimentalTime
    override val timeSource: TimeSource get() = ServerTickTimeSource
    
    override var position: EntityPos 
        get() = powerNukkitEntity.toEntityPos()
        set(value) {
            powerNukkitEntity.teleport(value.toVector3())
        }
    
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
    
    override var motion: Velocity
        get() = with(powerNukkitEntity) { Velocity(motionX, motionY, motionZ) }
        set(value) {
            powerNukkitEntity.motion = value.toVector3()     
        }

    override val itemInMainHand: RegularItemStack?
        get() = when (val entity = powerNukkitEntity) {
            is Player -> entity.inventory?.itemInHand?.asRegularItemStack()
            else -> null
        }

    override val itemInOffHand: RegularItemStack?
        get() = when (val entity = powerNukkitEntity) {
            is Player -> entity.offhandInventory?.getItem(0)?.asRegularItemStack()
            else -> null
        }

    override val isValid: Boolean
        get() = with (powerNukkitEntity) {
            isAlive && (this !is Player || isConnected)
        }

    override var isUnderAttack: Boolean = false

    override var breedingAge: Int = 0
    
    private val lovingJob = atomic<Job?>(null)

    override suspend fun isTouchingWater(): Boolean {
        return powerNukkitEntity.isTouchingWater
    }

    override fun hasPassengers(): Boolean = powerNukkitEntity.passengers.isNotEmpty()

    override suspend fun playSound(sound: Sound) {
        world.playSound(position, sound)
    }

    override suspend fun moveTo(nextPos: EntityPos): Boolean = updateAndGet {
        with(powerNukkitEntity) {
            move(nextPos.x - x, nextPos.y - y, nextPos.z - z)
        }
    }

    override suspend fun applyMotion(motion: IVelocity): Boolean = updateAndGet {
        with(powerNukkitEntity) {
            powerNukkitEntity.setMotion(Vector3(motionX + motion.x, motionY + motion.y, motionZ + motion.z))
        }
    }

    override suspend fun applySpeed(vector: IVelocity, force: IVelocity): Boolean = updateAndGet {
        with(powerNukkitEntity) {
            motionX = adjustMotion(motionX, vector.x, force.x)
            motionY = adjustMotion(motionY, vector.y, force.y)
            motionZ = adjustMotion(motionZ, vector.z, force.z)
        }
        true
    }
    
    private fun adjustMotion(before: Double, desired: Double, force: Double): Double {
        if (0.0.isSimilarTo(force) || before.isSimilarTo(desired)) {
            return before
        }
        val delta = desired - before
        return before + if (delta >= 0) delta.coerceAtMost(force) else delta.coerceAtLeast(-force)
    }

    override suspend fun calculateInertiaMotion(): IVelocity {
        return currentStatus.gravity
    }

    override suspend fun calculateDrag(): IVelocity {
        with(powerNukkitEntity) {
            if (!isOnGround) {
                return currentStatus.drag
            }
            val friction = .91 * level.getBlock(x.toInt(), (y - 0.5).toInt(), z.toInt()).frictionFactor
            return Velocity(friction, .98, friction)
        }
    }
    
    override suspend fun applyPhysics() = update {
        with(powerNukkitEntity) {
            val inertia = calculateInertiaMotion()
            motionX += inertia.x
            motionY += inertia.y
            motionZ += inertia.z
            if (isInsideOfWater) {
                applySpeed(inertia, Velocity(.1))
            }
            if (!onGround && motionY > -0.5) {
                highestPosition = y
            }
            move(motionX, motionY, motionZ)
            
            val drag = calculateDrag()
            motionX *= drag.x
            motionY *= drag.y
            motionZ *= drag.z
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
    
    private fun updateStatus() {
        with(powerNukkitEntity) {
            with(_currentStatus) {
                canJump = isAlive && (onGround || isInsideOfWater)
            }
        }
    }

    override suspend fun createChild(other: RegularEntity): RegularEntity? {
        return type.childFactory.createChild(this, other)
    }
    
    @ExperimentalTime
    override fun startLoving(duration: Duration) = lovingJob.updateAndGet { old -> 
        old?.cancel("Starting another loving job")
        CoroutineScope(job + updateDispatcher).launch {
            withTimeout(duration) {
                old?.join()
                flagManager[EntityFlag.IN_LOVE] = true
                try {
                    val packet = EntityEventPacket().apply {
                        eid = powerNukkitEntity.id
                        event = EntityEventPacket.LOVE_PARTICLES
                    }
                    
                    while (isValid && flagManager[EntityFlag.IN_LOVE]) {
                        Server.broadcastPacket(powerNukkitEntity.viewers.values, packet)
                        delay(1.seconds)
                    }
                } finally {
                    flagManager[EntityFlag.IN_LOVE] = false
                }
            }
        }
    }
    
    override fun stopLoving() {
        lovingJob.get()?.cancel("Stopped by request")
    }
}
