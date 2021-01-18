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

import games.joserobjr.intellimob.entity.status.ImmutableEntityStatus

/**
 * @author joserobjr
 * @since 2021-01-12
 */
internal expect class EntityType {
    /**
     * The default status based on the entity type.
     */
    val defaultStatus: ImmutableEntityStatus
    
    companion object {
        //-------- Passive Mobs --------// 
        val BAT: EntityType
        val CAT: EntityType
        val CHICKEN: EntityType
        val COD: EntityType
        val COW: EntityType
        val DONKEY: EntityType
        val FOX: EntityType
        val HORSE: EntityType
        val MOOSHROOM: EntityType
        val MULE: EntityType
        val OCELOT: EntityType
        val PARROT: EntityType
        val PIG: EntityType
        val RABBIT: EntityType
        val SALMON: EntityType
        val SHEEP: EntityType
        val SKELETON_HORSE: EntityType
        val SNOW_GOLEM: EntityType
        val SQUID: EntityType
        val STRIDER: EntityType
        val TROPICAL_FISH: EntityType
        val TURTLE: EntityType
        val VILLAGER: EntityType
        val VILLAGER_V1: EntityType
        val WANDERING_TRADER: EntityType

        //-------- Neutral Mobs --------//
        val BEE: EntityType
        val CAVE_SPIDER: EntityType
        val DOLPHIN: EntityType
        val ENDERMAN: EntityType
        val IRON_GOLEM: EntityType
        val LLAMA: EntityType
        val PIGLIN: EntityType
        val PANDA: EntityType
        val POLAR_BEAR: EntityType
        val PUFFERFISH: EntityType
        val SPIDER: EntityType
        val WOLF: EntityType
        val ZOMBIFED_PIGLIN: EntityType

        //-------- Hostile Mobs --------//
        val BLAZE: EntityType
        val CREEPER: EntityType
        val DROWNED: EntityType
        val ELDER_GUARDIAN: EntityType
        val ENDERMITE: EntityType
        val EVOKER: EntityType
        val GHAST: EntityType
        val GUARDIAN: EntityType
        val HOGLIN: EntityType
        val HUSK: EntityType
        val MAGMA_CUBE: EntityType
        val PHANTOM: EntityType
        val PIGLIN_BRUTE: EntityType
        val PILLAGER: EntityType
        val RAVEGER: EntityType
        val SHULKER: EntityType
        val SILVERFISH: EntityType
        val SKELETON: EntityType
        val SLIME: EntityType
        val STRAY: EntityType
        val VEX: EntityType
        val VINDICATOR: EntityType
        val WITCH: EntityType
        val WITHER_SKELETON: EntityType
        val ZOGLIN: EntityType
        val ZOMBIE: EntityType
        val ZOMBIE_VILLAGER: EntityType
        val ZOMBIE_VILLAGER_V1: EntityType

        //-------- Boss Mobs --------//
        val ENDER_DRAGON: EntityType
        val WITHER: EntityType
    }
}
