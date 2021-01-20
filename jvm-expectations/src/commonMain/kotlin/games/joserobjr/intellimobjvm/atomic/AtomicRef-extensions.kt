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

package games.joserobjr.intellimobjvm.atomic

import games.joserobjr.intellimobjvm.functions.UnaryOperator
import kotlin.reflect.KProperty

/**
 * @author joserobjr
 * @since 2021-01-19
 */
public operator fun <V> AtomicRef<V>.getValue(thisRef: Any?, property: KProperty<*>): V {
    return get()
}

public operator fun <V> AtomicRef<V>.setValue(thisRef: Any?, property: KProperty<*>, value: V) {
    set(value)
}

public fun <V> AtomicRef<V>.update(updateFunction: UnaryOperator<V>) {
    getAndUpdate(updateFunction)
}

public var <V> AtomicRef<V>.value: V
    get() = get()
    set(value) {
        set(value)
    }
