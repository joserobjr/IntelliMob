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

package games.joserobjr.intellimob.entity.factory

import games.joserobjr.intellimob.entity.EntityFlag
import games.joserobjr.intellimob.entity.IEntityFlagManager
import games.joserobjr.intellimob.entity.status.MutableEntityStatus
import games.joserobjr.intellimob.math.Velocity

/**
 * @author joserobjr
 * @since 2021-01-22
 */
internal interface FlyingAIFactory: LivingEntityAIFactory {
    override fun adjustDefaultStatus(status: MutableEntityStatus) = with(status) {
        gravity = Velocity(0.0, .0, 0.0)
        drag = Velocity(.8, .9, .8)
    }

    override fun setDefaultFlags(manager: IEntityFlagManager) {
        super.setDefaultFlags(manager)
        manager.enableFlags(EntityFlag.CAN_FLY)
    }
}
