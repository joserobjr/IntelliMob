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

import games.joserobjr.intellimob.math.angle.PitchYawSpeed
import games.joserobjr.intellimob.math.motion.HorizontalVelocity
import games.joserobjr.intellimob.math.motion.Velocity

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal data class MutableEntityStatus(
    override var headSpeed: PitchYawSpeed,
    override var headFastSpeed: PitchYawSpeed,
    override var walkSpeed: HorizontalVelocity,
    override var sprintSpeed: HorizontalVelocity,
    override var flySpeed: HorizontalVelocity,
    override var jumpSpeed: Double,
    override var stepHeight: Double,
    override var canJump: Boolean,
    override var followRange: Double,
    override var attackKnockBack: Double,
    override var gravity: Velocity,
    override var drag: Velocity
) : EntityStatus {
    override fun toImmutable(): ImmutableEntityStatus = ImmutableEntityStatus(
        headSpeed = headSpeed,
        headFastSpeed = headFastSpeed,
        walkSpeed = walkSpeed,
        sprintSpeed = sprintSpeed,
        flySpeed = flySpeed,
        jumpSpeed = jumpSpeed,
        stepHeight = stepHeight,
        canJump = canJump,
        followRange = followRange,
        attackKnockBack = attackKnockBack,
        gravity = gravity,
        drag = drag,
    )

    override fun toMutable(): MutableEntityStatus = copy()
}
