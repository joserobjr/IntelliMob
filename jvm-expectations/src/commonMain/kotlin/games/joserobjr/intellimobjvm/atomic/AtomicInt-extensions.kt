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

import games.joserobjr.intellimobjvm.functions.IntUnaryOperator
import kotlin.reflect.KProperty

/**
 * @author joserobjr
 * @since 2021-01-19
 */
public operator fun AtomicInt.getValue(thisRef: Any?, property: KProperty<*>): Int {
    return get()
}

public operator fun AtomicInt.setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
    set(value)
}

public fun AtomicInt.update(updateFunction: IntUnaryOperator) {
    getAndUpdate(updateFunction)
}

public var AtomicInt.value: Int
    get() = get()
    set(value) {
        set(value)
    }
