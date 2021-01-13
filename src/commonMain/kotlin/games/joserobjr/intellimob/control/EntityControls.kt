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

package games.joserobjr.intellimob.control

import games.joserobjr.intellimob.annotation.ExperimentalIntelliMobApi
import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.math.DoubleVectorXZ
import games.joserobjr.intellimob.math.EntityPos
import games.joserobjr.intellimob.math.PitchYaw

/**
 * Physical interactions with the entity body.
 * 
 * @author joserobjr
 * @since 2021-01-11
 */
@ExperimentalIntelliMobApi
public interface EntityControls {
    /**
     * The current status affecting the entity. The returned values must consider if the entity is on floor, in water,
     * etc.
     */
    public val currentStatus: EntityStatus

    /**
     * Moves the entity head toward a position at given speed. 
     * 
     * This may need to be called multiple times depending on the current [EntityStatus.headSpeed] value and the distance of the position.
     * 
     * @return `true` if the head has reached the objective and no more calls are needed to look at the position. 
     */
    public fun lookAt(pos: EntityPos, speed: PitchYaw = currentStatus.headSpeed): Boolean

    /**
     * The entity receives an upward motion at the given speed.
     */
    public fun jump(speed: Double = currentStatus.jumpSpeed): Boolean

    /**
     * Moves the entity toward a direction.
     * 
     * This may need to be called multiple times depending on the current [EntityStatus.walkSpeed] value and the distance of the position.
     * 
     * @param pos The position which the entity wants to reach
     * @param acceptableDistance Determine how precise the entity needs to be in the range.
     * @param speed How fast the entity will move in the axis
     * @return `true` if the entity has reached an acceptable distance to the position and no more calls are needed.
     */
    public fun walkTo(pos: EntityPos, acceptableDistance: Double = currentStatus.stepHeight, speed: DoubleVectorXZ = currentStatus.walkSpeed): Boolean
}
