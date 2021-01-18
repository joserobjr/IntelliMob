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

import io.gomint.GoMint
import io.gomint.world.block.Block
import io.gomint.world.block.BlockAir
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * @author joserobjr
 * @since 2021-01-17
 */
internal actual class BlockType private constructor(val goMintBlockClass: KClass<out Block>) {
    actual val defaultState: BlockState by lazy { 
        GoMint.instance().createBlock(goMintBlockClass.java).asIntelliMobBlockState()
    }
    
    actual companion object {
        private val types = ConcurrentHashMap<KClass<out Block>, BlockType>()
        actual val AIR: BlockType = fromClass(BlockAir::class)
        fun fromBlock(block: Block): BlockType = fromClass(block::class) 
        fun fromClass(entityClass: KClass<out Block>): BlockType {
            types[entityClass]?.let { return it }
            types.entries.firstOrNull { it.key.isSuperclassOf(entityClass) }?.let { (_, type) ->
                return types.putIfAbsent(entityClass, type) ?: type
            }
            return types.computeIfAbsent(entityClass, ::BlockType)
        }
    }
}
