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

package games.joserobjr.intellimob.item

import cn.nukkit.item.ItemID
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
 * @author joserobjr
 * @since 2021-01-24
 */
internal actual sealed class ItemType {
    private class CustomItemType(val itemId: Int): ItemType()
    private class VanillaItemType(val itemId: Int): ItemType() {
        init {
            registry[itemId] = this
        }
    }
    
    actual companion object {
        private val registry = Int2ObjectOpenHashMap<ItemType>()
        
        operator fun get(itemId: Int): ItemType {
            return registry.getOrElse(itemId) {
                CustomItemType(itemId)
            }
        }
        
        actual val BEETROOT: ItemType = VanillaItemType(ItemID.BEETROOT)
        actual val POTATO: ItemType = VanillaItemType(ItemID.POTATO)
        actual val CARROT: ItemType = VanillaItemType(ItemID.CARROT)
    }
}
