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

/**
 * @author joserobjr
 * @since 2021-01-18
 */
internal class LayeredBlockState private constructor(layers: MutableList<BlockState>) {
    constructor(layers: Iterable<BlockState>): this(layers.toMutableList())
    constructor(vararg layers: BlockState): this(layers.toMutableList())

    private val layers: List<BlockState> = layers.run {
        asReversed().listIterator().let { iterator ->
            while (iterator.hasNext() && iterator.next() == BlockState.AIR) {
                iterator.remove()
            }
        }
        toList()
    }
    val main: BlockState get() = layers.getOrElse(0) { BlockState.AIR }
    val extra: List<BlockState> get() = if (layers.size <= 1) emptyList() else layers.subList(1, layers.size) 
    
    operator fun get(layer: Int): BlockState {
        return layers.getOrElse(layer) { BlockState.AIR }
    }
    
    fun asSequence() = if(layers.isEmpty()) sequenceOf(BlockState.AIR) else layers.asSequence()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as LayeredBlockState

        if (layers != other.layers) return false

        return true
    }

    override fun hashCode(): Int {
        return layers.hashCode()
    }

    companion object {
        val EMPTY = LayeredBlockState()
    }
}
