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

import games.joserobjr.intellimob.annotation.ExperimentalIntelliMobApi

/**
 * @author joserobjr
 * @since 2021-01-12
 */
@ExperimentalIntelliMobApi
public expect class EntityType {
    
    
    @ExperimentalIntelliMobApi
    public companion object {
        //-------- Passive Mobs --------// 
        public val BAT: EntityType
        public val CAT: EntityType
        public val CHICKEN: EntityType
        public val COD: EntityType
        public val COW: EntityType
        public val DONKEY: EntityType
        public val FOX: EntityType
        public val HORSE: EntityType
        public val MOOSHROOM: EntityType
        public val MULE: EntityType
        public val OCELOT: EntityType
        public val PARROT: EntityType
        public val PIG: EntityType
        public val RABBIT: EntityType
        public val SALMON: EntityType
        public val SHEEP: EntityType
        public val SKELETON_HORSE: EntityType
        public val SNOW_GOLEM: EntityType
        public val SQUID: EntityType
        public val STRIDER: EntityType
        public val TROPICAL_FISH: EntityType
        public val TURTLE: EntityType
        public val VILLAGER: EntityType
        public val VILLAGER_V1: EntityType
        public val WANDERING_TRADER: EntityType

        //-------- Neutral Mobs --------//
        public val BEE: EntityType
        public val CAVE_SPIDER: EntityType
        public val DOLPHIN: EntityType
        public val ENDERMAN: EntityType
        public val IRON_GOLEM: EntityType
        public val LLAMA: EntityType
        public val PIGLIN: EntityType
        public val PANDA: EntityType
        public val POLAR_BEAR: EntityType
        public val PUFFERFISH: EntityType
        public val SPIDER: EntityType
        public val WOLF: EntityType
        public val ZOMBIFED_PIGLIN: EntityType

        //-------- Hostile Mobs --------//
        public val BLAZE: EntityType
        public val CREEPER: EntityType
        public val DROWNED: EntityType
        public val ELDER_GUARDIAN: EntityType
        public val ENDERMITE: EntityType
        public val EVOKER: EntityType
        public val GHAST: EntityType
        public val GUARDIAN: EntityType
        public val HOGLIN: EntityType
        public val HUSK: EntityType
        public val MAGMA_CUBE: EntityType
        public val PHANTOM: EntityType
        public val PIGLIN_BRUTE: EntityType
        public val PILLAGER: EntityType
        public val RAVEGER: EntityType
        public val SHULKER: EntityType
        public val SILVERFISH: EntityType
        public val SKELETON: EntityType
        public val SLIME: EntityType
        public val STRAY: EntityType
        public val VEX: EntityType
        public val VINDICATOR: EntityType
        public val WITCH: EntityType
        public val WITHER_SKELETON: EntityType
        public val ZOGLIN: EntityType
        public val ZOMBIE: EntityType
        public val ZOMBIE_VILLAGER: EntityType
        public val ZOMBIE_VILLAGER_V1: EntityType

        //-------- Boss Mobs --------//
        public val ENDER_DRAGON: EntityType
        public val WITHER: EntityType
    }
}
