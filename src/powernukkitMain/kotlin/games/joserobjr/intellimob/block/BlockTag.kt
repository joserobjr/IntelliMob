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

package games.joserobjr.intellimob.block

import cn.nukkit.block.Block
import cn.nukkit.block.BlockLeaves
import cn.nukkit.block.BlockLog
import cn.nukkit.block.BlockStem
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * @author joserobjr
 * @since 2021-01-22
 */
internal actual sealed class BlockTag {
    
    abstract operator fun contains(state: BlockState): Boolean
    
    private class SimpleClass(val kClass: KClass<out Block>, val except: KClass<*>? = null): BlockTag() {
        override fun contains(state: BlockState): Boolean {
            val blockClass = state.powerNukkitBlockState.block::class
            return kClass.isSuperclassOf(blockClass) && except?.isSuperclassOf(blockClass) == true 
        }
    }
    
    actual companion object {
        actual val LEAVES: BlockTag = SimpleClass(BlockLeaves::class)
        actual val LOGS: BlockTag = SimpleClass(BlockLog::class, BlockStem::class)
    }
}
