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

package games.joserobjr.intellimob.control.api

import games.joserobjr.intellimob.entity.status.EntityStatus
import games.joserobjr.intellimob.math.PitchYaw
import games.joserobjr.intellimob.trait.WithEntityPos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal interface HeadController: Controller {
    /**
     * Moves the entity head toward a position at given speed.
     *
     * This may need to be called multiple times depending on the current [EntityStatus.headSpeed] value and the distance of the position.
     *
     * @return `true` if the head has reached the objective and no more calls are needed to look at the position.
     */
    fun CoroutineScope.lookAt(pos: WithEntityPos, speed: PitchYaw = owner.currentStatus.headSpeed): Job
    suspend fun updateHeadAngle(target: PitchYaw, speed: PitchYaw)
}
