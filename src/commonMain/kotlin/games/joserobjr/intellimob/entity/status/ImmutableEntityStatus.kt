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

import games.joserobjr.intellimob.math.generic.DoubleVectorXZ
import games.joserobjr.intellimob.math.angle.PitchYawSpeed
import games.joserobjr.intellimob.math.motion.HorizontalVelocity
import games.joserobjr.intellimob.math.motion.Velocity

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal data class ImmutableEntityStatus (
    override val headSpeed: PitchYawSpeed,
    override val headFastSpeed: PitchYawSpeed,
    override val walkSpeed: HorizontalVelocity,
    override val sprintSpeed: HorizontalVelocity,
    override val flySpeed: HorizontalVelocity,
    override val jumpSpeed: Double,
    override val stepHeight: Double,
    override val canJump: Boolean,
    override val followRange: Double,
    override val attackKnockBack: Double,
    override val gravity: Velocity,
    override val drag: Velocity,
): EntityStatus {
    override fun toMutable(): MutableEntityStatus = MutableEntityStatus(
        headSpeed = headSpeed,
        headFastSpeed = headFastSpeed,
        walkSpeed = walkSpeed,
        jumpSpeed = jumpSpeed,
        flySpeed = flySpeed,
        sprintSpeed = sprintSpeed,
        stepHeight = stepHeight,
        canJump = canJump,
        followRange = followRange,
        attackKnockBack = attackKnockBack,
        gravity = gravity,
        drag = drag,
    )

    override fun toImmutable(): ImmutableEntityStatus = this
    override fun asImmutableView(): EntityStatus = this
}
