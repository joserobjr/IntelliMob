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

package games.joserobjr.intellimob.world

import cn.nukkit.level.GameRules
import cn.nukkit.level.GameRule as PNGameRule


/**
 * @author joserobjr
 * @since 2021-02-11
 */
internal actual sealed class GameRule<T>(val powerNukkit: PNGameRule, val type: GameRules.Type) {
    abstract fun getValue(gameRules: GameRules): T
    
    private class BooleanGameRule(powerNukkit: PNGameRule) : GameRule<Boolean>(powerNukkit, GameRules.Type.BOOLEAN) {
        override fun getValue(gameRules: GameRules): Boolean {
            return gameRules.getBoolean(powerNukkit)
        }
    }

    private class IntGameRule(powerNukkit: PNGameRule) : GameRule<Int>(powerNukkit, GameRules.Type.INTEGER) {
        override fun getValue(gameRules: GameRules): Int {
            return gameRules.getInteger(powerNukkit)
        }
    }

    private class FloatGameRule(powerNukkit: PNGameRule) : GameRule<Float>(powerNukkit, GameRules.Type.FLOAT) {
        override fun getValue(gameRules: GameRules): Float {
            return gameRules.getFloat(powerNukkit)
        }
    }

    actual companion object {
        actual val DO_MOB_LOOT: GameRule<Boolean> = BooleanGameRule(PNGameRule.DO_MOB_LOOT)
    }
}
