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

package games.joserobjr.intellimob.entity.status

import games.joserobjr.intellimob.math.DoubleVectorXZ
import games.joserobjr.intellimob.math.PitchYaw

/**
 * @author joserobjr
 * @since 2021-01-11
 */
public interface EntityStatus {
    /**
     * How fast the head can rotate the head in the pitch and yaw axis.
     */
    public val headSpeed: PitchYaw
    public val walkSpeed: DoubleVectorXZ
    public val jumpSpeed: Double
    public val stepHeight: Double
    public val canJump: Boolean

    /**
     * Creates a new mutable copy of this status.
     */
    public fun toMutable(): MutableEntityStatus

    /**
     * Gets an immutable instance of this status. 
     */
    public fun toImmutable(): ImmutableEntityStatus

    /**
     * Gets a view of this entity which cannot be modified externally but the values inside it may change if this is mutable.
     */
    public fun asImmutableView(): EntityStatus = ImmutableEntityStatusView(this)
}