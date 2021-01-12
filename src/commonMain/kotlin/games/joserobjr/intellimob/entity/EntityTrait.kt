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

/**
 * All possible characteristics of an entity 
 * 
 * @author joserobjr
 * @since 2021-01-11
 */
public enum class EntityTrait {
    /**
     * Never attacks even when attacked.
     */
    PASSIVE,

    /**
     * May become hostile if a condition is not met. 
     */
    NEUTRAL,

    /**
     * Always hostile.
     */
    HOSTILE,

    /**
     * The entity can jump.
     */
    JUMPER,

    /**
     * The entity can 'step', just like going up a staircase where you teleport up a little when you hit each step, 
     * but in this case, this trait allows the entity to step an entire full block.
     */
    BLOCK_STEPPER,

    /**
     * Can walk on flat terrain.
     */
    WALKER,

    /**
     * Can swim up to breath when under water.
     */
    FLOAT_WATER,

    /**
     * Can swim up to breath when under lava.
     */
    FLOAT_LAVA,

    /**
     * Can swim water.
     */
    WATER_SWIMMER,

    /**
     * Can swim lava.
     */
    LAVA_SWIMMER,

    /**
     * Can walk above water.
     */
    WATER_WALKER,

    /**
     * Can walk above lava.
     */
    LAVA_WALKER,
}
