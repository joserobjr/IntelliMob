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

package games.joserobjr.intellimob

import cn.nukkit.entity.Entity
import cn.nukkit.plugin.PluginBase
import games.joserobjr.intellimob.annotation.ExperimentalIntelliMobApi
import games.joserobjr.intellimob.entity.EntityIronGolem

/**
 * @author joserobjr
 * @since 2021-01-11
 */
@OptIn(ExperimentalIntelliMobApi::class)
internal class IntelliMobPowerNukkitPlugin: PluginBase() {
    override fun onEnable() {
        instance = this
        Entity.registerEntity("IronGolem", EntityIronGolem::class.java)
    }
    
    companion object {
        internal lateinit var instance: IntelliMobPowerNukkitPlugin; private set
    }
}
