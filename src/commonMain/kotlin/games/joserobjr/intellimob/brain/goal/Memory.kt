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

package games.joserobjr.intellimob.brain.goal

import games.joserobjr.intellimobjvm.ref.WeakReference

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal inline class Memory(val contents: MutableMap<String, Any?> = mutableMapOf()) {
    inline operator fun <reified V> get(key: String): V? {
        val any = getAny(key)
        if (any is WeakReference<*> && V::class != WeakReference::class) {
            val unboxed = any.get()
            if (unboxed is V) {
                return unboxed
            }
        }
        return any as V
    }
    operator fun contains(key: String) = key in contents
    operator fun minusAssign(key: String) {
        contents -= key
    }
    operator fun plusAssign(key: String) {
        contents.getOrPut(key) { null }
    }
    operator fun set(key: String, value: Any?) {
        contents[key] = value
    }
    fun getAny(key: String) = contents[key]
    fun setWeak(key: String, value: Any?) {
        this[key] = WeakReference(value)
    }
}
