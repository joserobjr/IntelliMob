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

import games.joserobjr.intellimob.block.LiquidState
import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.brain.wish.Wishes
import games.joserobjr.intellimob.control.api.EntityControls
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.entity.status.MutableEntityStatus
import games.joserobjr.intellimob.item.RegularItemStack
import games.joserobjr.intellimob.math.EntityPos
import games.joserobjr.intellimob.math.IDoubleVectorXYZ
import games.joserobjr.intellimob.math.PitchYaw
import games.joserobjr.intellimob.math.Velocity
import games.joserobjr.intellimob.pathfinding.BlockFavorProvider
import games.joserobjr.intellimob.pathfinding.PathFinder
import games.joserobjr.intellimob.trait.WithBoundingBox
import games.joserobjr.intellimob.trait.WithEntityLocation
import games.joserobjr.intellimob.trait.WithTimeSource
import games.joserobjr.intellimob.trait.WithUpdateDispatcher
import kotlinx.coroutines.Job

/**
 * The root interface which compose the entities, with or without IntelliMob AI. 
 * 
 * @author joserobjr
 * @since 2021-01-11
 */
internal interface RegularEntity: PlatformEntity, WithEntityLocation, WithTimeSource, WithBoundingBox, WithUpdateDispatcher {
    val job: Job
    
    val type: EntityType

    /**
     * Allows to apply physical movements to the entity.
     */
    var controls: EntityControls

    /**
     * Hold and process all intelligence stuff which doesn't require physical interactions.
     */
    val brain: Brain

    /**
     * The current base status, not modified by environmental conditions.
     */
    val baseStatus: MutableEntityStatus

    /**
     * The current status, affected environmental conditions.
     */
    val currentStatus: EntityStatus

    /**
     * The intelligence which visualizes the world to realize movement [Wishes] from the [Brain] using the [EntityControls].
     */
    var pathFinder: PathFinder

    override val position: EntityPos
    
    val eyePosition: EntityPos

    var headPitchYaw: PitchYaw

    var bodyPitchYaw: PitchYaw

    var motion: Velocity
    
    val flagManager: IEntityFlagManager

    val isUnderAttack: Boolean

    val itemInMainHand: RegularItemStack?
    val itemInOffHand: RegularItemStack?

    /**
     * Is alive and is online.
     */
    val isValid: Boolean

    fun hasPassengers(): Boolean
    
    suspend fun createSnapshot(): EntitySnapshot
    
    suspend fun currentLiquidOnEyes(): LiquidState? {
        return world.cache.getBlock(eyePosition).currentLiquidState()
    }

    suspend fun isEyeUnderWater(): Boolean {
        val liquid = currentLiquidOnEyes() ?: return false
        return eyePosition.y < liquid.bounds.maxPosExclusive.y
    }
    
    suspend fun isTouchingWater(): Boolean

    suspend fun moveTo(nextPos: EntityPos): Boolean
    
    suspend fun applyMotion(motion: IDoubleVectorXYZ): Boolean
    suspend fun applySpeed(vector: IDoubleVectorXYZ, force: IDoubleVectorXYZ = vector): Boolean
    
    suspend fun calculateInertiaMotion(): IDoubleVectorXYZ
    
    suspend fun calculateDrag(): IDoubleVectorXYZ
    
    suspend fun applyPhysics()

    suspend fun playSound(sound: Sound)

    override val regularEntity: RegularEntity get() = this

    var blockFavor: BlockFavorProvider
}
