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
import games.joserobjr.intellimob.math.PitchYawSpeed

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal data class ImmutableEntityStatus (
    override val headSpeed: PitchYawSpeed,
    override val headFastSpeed: PitchYawSpeed,
    override val walkSpeed: DoubleVectorXZ,
    override val sprintSpeed: DoubleVectorXZ,
    override val flySpeed: DoubleVectorXZ,
    override val jumpSpeed: Double,
    override val stepHeight: Double,
    override val canJump: Boolean
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
    )

    override fun toImmutable(): ImmutableEntityStatus = this
    override fun asImmutableView(): EntityStatus = this
}
