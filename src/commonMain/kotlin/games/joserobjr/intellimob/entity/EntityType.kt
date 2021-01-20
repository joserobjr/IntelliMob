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

import games.joserobjr.intellimob.entity.factory.EntityAIFactory
import games.joserobjr.intellimob.entity.status.ImmutableEntityStatus

/**
 * @author joserobjr
 * @since 2021-01-12
 */
internal expect sealed class EntityType {
    /**
     * The default status based on the entity type.
     */
    val defaultStatus: ImmutableEntityStatus
    
    abstract var aiFactory: EntityAIFactory
    
    internal class Vanilla: EntityType {
        override var aiFactory: EntityAIFactory
    }
    
    companion object {
        //-------- Passive Mobs --------// 
        val BAT: Vanilla
        val CAT: Vanilla
        val CHICKEN: Vanilla
        val COD: Vanilla
        val COW: Vanilla
        val DONKEY: Vanilla
        val FOX: Vanilla
        val HORSE: Vanilla
        val MOOSHROOM: Vanilla
        val MULE: Vanilla
        val OCELOT: Vanilla
        val PARROT: Vanilla
        val PIG: Vanilla
        val RABBIT: Vanilla
        val SALMON: Vanilla
        val SHEEP: Vanilla
        val SKELETON_HORSE: Vanilla
        val SNOW_GOLEM: Vanilla
        val SQUID: Vanilla
        val STRIDER: Vanilla
        val TROPICAL_FISH: Vanilla
        val TURTLE: Vanilla
        val VILLAGER: Vanilla
        val VILLAGER_V1: Vanilla
        val WANDERING_TRADER: Vanilla

        //-------- Neutral Mobs --------//
        val BEE: Vanilla
        val CAVE_SPIDER: Vanilla
        val DOLPHIN: Vanilla
        val ENDERMAN: Vanilla
        val IRON_GOLEM: Vanilla
        val LLAMA: Vanilla
        val PIGLIN: Vanilla
        val PANDA: Vanilla
        val POLAR_BEAR: Vanilla
        val PUFFERFISH: Vanilla
        val SPIDER: Vanilla
        val WOLF: Vanilla
        val ZOMBIFED_PIGLIN: Vanilla

        //-------- Hostile Mobs --------//
        val BLAZE: Vanilla
        val CREEPER: Vanilla
        val DROWNED: Vanilla
        val ELDER_GUARDIAN: Vanilla
        val ENDERMITE: Vanilla
        val EVOKER: Vanilla
        val GHAST: Vanilla
        val GUARDIAN: Vanilla
        val HOGLIN: Vanilla
        val HUSK: Vanilla
        val MAGMA_CUBE: Vanilla
        val PHANTOM: Vanilla
        val PIGLIN_BRUTE: Vanilla
        val PILLAGER: Vanilla
        val RAVEGER: Vanilla
        val SHULKER: Vanilla
        val SILVERFISH: Vanilla
        val SKELETON: Vanilla
        val SLIME: Vanilla
        val STRAY: Vanilla
        val VEX: Vanilla
        val VINDICATOR: Vanilla
        val WITCH: Vanilla
        val WITHER_SKELETON: Vanilla
        val ZOGLIN: Vanilla
        val ZOMBIE: Vanilla
        val ZOMBIE_VILLAGER: Vanilla
        val ZOMBIE_VILLAGER_V1: Vanilla

        //-------- Boss Mobs --------//
        val ENDER_DRAGON: Vanilla
        val WITHER: Vanilla
    }
}
