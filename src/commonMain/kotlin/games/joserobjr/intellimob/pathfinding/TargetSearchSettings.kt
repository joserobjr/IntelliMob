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

package games.joserobjr.intellimob.pathfinding

import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.DoubleVectorXYZ
import games.joserobjr.intellimob.math.IDoubleVectorXYZ
import games.joserobjr.intellimob.math.IEntityPos
import kotlin.math.PI

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal data class TargetSearchSettings(
    val from: RegularEntity,
    val maxHorizontalDistance: Int,
    val maxVerticalDistance: Int,
    var preferredYDifference: Int = 0,
    var preferredAngle: IDoubleVectorXYZ? = null,
    var notInWater: Boolean = true,
    var maxAngleDifference: Double = PI / 2.0,
    var favor: BlockFavorProvider = from.blockFavor,
    var aboveGround: Boolean = false,
    var distanceAboveGroundRange: Int = 0,
    var minDistanceAboveGround: Int = 0,
    var validPositionsOnly: Boolean = true,
) {
    fun resetDefaults() {
        preferredYDifference = 0
        preferredAngle = null
        notInWater = true
        maxAngleDifference = PI / 2.0
        favor = from.blockFavor
        aboveGround = false
        distanceAboveGroundRange = 0
        minDistanceAboveGround = 0
        validPositionsOnly = true
    }
    
    fun groundTarget(preferredYDifference: Int, preferredAngle: DoubleVectorXYZ?, maxAngleDifference: Double) {
        resetDefaults()
        this.preferredYDifference = preferredYDifference
        this.preferredAngle = preferredAngle
        this.maxAngleDifference = maxAngleDifference
        aboveGround = true
        validPositionsOnly = false
    }

    fun groundTarget(favor: BlockFavorProvider = this.favor) {
        resetDefaults()
        notInWater = false
        maxAngleDifference = 0.0
        this.favor = favor
        aboveGround = true
    }
    
    fun airTarget(preferredAngle: DoubleVectorXYZ, maxAngleDifference: Double, distanceAboveGroundRange: Int, minDistanceAboveGround: Int) {
        resetDefaults()
        this.preferredAngle = preferredAngle
        notInWater = false
        this.maxAngleDifference = maxAngleDifference
        this.aboveGround = true
        this.distanceAboveGroundRange = distanceAboveGroundRange
        this.minDistanceAboveGround = minDistanceAboveGround
    }
    
    //TODO find a name, reference method_27929
    fun unnamed(pos: IEntityPos) {
        resetDefaults()
        preferredAngle = pos - from.position
        notInWater = false
        aboveGround = true
    }
    
    fun targetTowards(pos: IEntityPos, maxAngleDifference: Double = PI / 2.0) {
        resetDefaults()
        preferredAngle = pos - from.position
        this.maxAngleDifference = maxAngleDifference
    }

    fun groundTargetTowards(preferredYDifference: Int, pos: IEntityPos, maxAngleDifference: Double) {
        resetDefaults()
        this.preferredYDifference = preferredYDifference 
        preferredAngle = pos - from.position
        notInWater = false
        this.maxAngleDifference = maxAngleDifference
        aboveGround = true
        validPositionsOnly = false
    }
    
    fun targetAwayFrom(pos: IEntityPos) {
        resetDefaults()
        preferredAngle = from.position - pos
        notInWater = false
        aboveGround = true
    }
}
