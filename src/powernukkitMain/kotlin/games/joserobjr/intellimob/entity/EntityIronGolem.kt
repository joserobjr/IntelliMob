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

import cn.nukkit.entity.mob.EntityMob
import cn.nukkit.level.format.FullChunk
import cn.nukkit.nbt.tag.CompoundTag

/**
 * @author joserobjr
 * @since 2021-01-12
 */
internal class EntityIronGolem(chunk: FullChunk?, nbt: CompoundTag?) : EntityMob(chunk, nbt) {
    companion object {
        const val NETWORK_ID: Int = 20
    }

    override fun getNetworkId(): Int = NETWORK_ID

    override fun initEntity() {
        super.initEntity()
        maxHealth = 100
    }

    override fun getName(): String = "Iron Golem"
    override fun getWidth(): Float = 1.4f
    override fun getHeight(): Float = 2.9f
}
