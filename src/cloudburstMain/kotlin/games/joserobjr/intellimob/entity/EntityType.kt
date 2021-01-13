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
import games.joserobjr.intellimob.entity.api.*
import org.cloudburstmc.server.entity.EntityTypes
import java.util.concurrent.ConcurrentHashMap

/**
 * @author joserobjr
 * @since 2021-01-12
 */
@ExperimentalIntelliMobApi
public actual class EntityType(public val platformType: PlatformEntityType<*>) {
    @ExperimentalIntelliMobApi
    public actual companion object {
        private val registry: MutableMap<PlatformEntityType<*>, EntityType> = ConcurrentHashMap()
        internal fun fromEntity(entity: RegularEntity): EntityType {
            return registry.computeIfAbsent(entity.type, ::EntityType)
        }
        
        //-------- Passive Mobs --------// 
        public actual val BAT: EntityType = EntityType(EntityTypes.BAT)
        public actual val CAT: EntityType = EntityType(EntityTypes.CAT)
        public actual val CHICKEN: EntityType = EntityType(EntityTypes.CHICKEN)
        public actual val COD: EntityType = EntityType(EntityTypes.COD)
        public actual val COW: EntityType = EntityType(EntityTypes.COW)
        public actual val DONKEY: EntityType = EntityType(EntityTypes.DONKEY)
        public actual val FOX: EntityType = EntityType(Fox.TYPE)
        public actual val HORSE: EntityType = EntityType(EntityTypes.HORSE)
        public actual val MOOSHROOM: EntityType = EntityType(EntityTypes.MOOSHROOM)
        public actual val MULE: EntityType = EntityType(EntityTypes.MULE)
        public actual val OCELOT: EntityType = EntityType(EntityTypes.OCELOT)
        public actual val PARROT: EntityType = EntityType(EntityTypes.PARROT)
        public actual val PIG: EntityType = EntityType(EntityTypes.PIG)
        public actual val RABBIT: EntityType = EntityType(EntityTypes.RABBIT)
        public actual val SALMON: EntityType = EntityType(EntityTypes.SALMON)
        public actual val SHEEP: EntityType = EntityType(EntityTypes.SHEEP)
        public actual val SKELETON_HORSE: EntityType = EntityType(EntityTypes.SKELETON_HORSE)
        public actual val SNOW_GOLEM: EntityType = EntityType(EntityTypes.SNOW_GOLEM)
        public actual val SQUID: EntityType = EntityType(EntityTypes.SQUID)
        public actual val STRIDER: EntityType = EntityType(Strider.TYPE)
        public actual val TROPICAL_FISH: EntityType = EntityType(EntityTypes.TROPICALFISH)
        public actual val TURTLE: EntityType = EntityType(EntityTypes.TURTLE)
        public actual val VILLAGER: EntityType = EntityType(EntityTypes.VILLAGER)
        public actual val VILLAGER_V1: EntityType = EntityType(EntityTypes.DEPRECATED_VILLAGER)
        public actual val WANDERING_TRADER: EntityType = EntityType(EntityTypes.WANDERING_TRADER)

        //-------- Neutral Mobs --------//
        public actual val BEE: EntityType = EntityType(Bee.TYPE)
        public actual val CAVE_SPIDER: EntityType = EntityType(EntityTypes.CAVE_SPIDER)
        public actual val DOLPHIN: EntityType = EntityType(EntityTypes.DOLPHIN)
        public actual val ENDERMAN: EntityType = EntityType(EntityTypes.ENDERMAN)
        public actual val IRON_GOLEM: EntityType = EntityType(EntityTypes.IRON_GOLEM)
        public actual val LLAMA: EntityType = EntityType(EntityTypes.LLAMA)
        public actual val PIGLIN: EntityType = EntityType(Piglin.TYPE)
        public actual val PANDA: EntityType = EntityType(EntityTypes.PANDA)
        public actual val POLAR_BEAR: EntityType = EntityType(EntityTypes.POLAR_BEAR)
        public actual val PUFFERFISH: EntityType = EntityType(EntityTypes.PUFFERFISH)
        public actual val SPIDER: EntityType = EntityType(EntityTypes.SPIDER)
        public actual val WOLF: EntityType = EntityType(EntityTypes.WOLF)
        public actual val ZOMBIFED_PIGLIN: EntityType = EntityType(EntityTypes.ZOMBIE_PIGMAN)

        //-------- Hostile Mobs --------//
        public actual val BLAZE: EntityType = EntityType(EntityTypes.BLAZE)
        public actual val CREEPER: EntityType = EntityType(EntityTypes.CREEPER)
        public actual val DROWNED: EntityType = EntityType(EntityTypes.DROWNED)
        public actual val ELDER_GUARDIAN: EntityType = EntityType(EntityTypes.ELDER_GUARDIAN)
        public actual val ENDERMITE: EntityType = EntityType(EntityTypes.ENDERMITE)
        public actual val EVOKER: EntityType = EntityType(Evoker.TYPE)
        public actual val GHAST: EntityType = EntityType(EntityTypes.GHAST)
        public actual val GUARDIAN: EntityType = EntityType(EntityTypes.GUARDIAN)
        public actual val HOGLIN: EntityType = EntityType(Hoglin.TYPE)
        public actual val HUSK: EntityType = EntityType(EntityTypes.HUSK)
        public actual val MAGMA_CUBE: EntityType = EntityType(EntityTypes.MAGMA_CUBE)
        public actual val PHANTOM: EntityType = EntityType(EntityTypes.PHANTOM)
        public actual val PIGLIN_BRUTE: EntityType = EntityType(PiglinBrute.TYPE)
        public actual val PILLAGER: EntityType = EntityType(EntityTypes.PILLAGER)
        public actual val RAVEGER: EntityType = EntityType(Raveger.TYPE)
        public actual val SHULKER: EntityType = EntityType(EntityTypes.SHULKER)
        public actual val SILVERFISH: EntityType = EntityType(EntityTypes.SILVERFISH)
        public actual val SKELETON: EntityType = EntityType(EntityTypes.SKELETON)
        public actual val SLIME: EntityType = EntityType(EntityTypes.SLIME)
        public actual val STRAY: EntityType = EntityType(EntityTypes.STRAY)
        public actual val VEX: EntityType = EntityType(EntityTypes.VEX)
        public actual val VINDICATOR: EntityType = EntityType(EntityTypes.VINDICATOR)
        public actual val WITCH: EntityType = EntityType(EntityTypes.WITCH)
        public actual val WITHER_SKELETON: EntityType = EntityType(EntityTypes.WITHER_SKELETON)
        public actual val ZOGLIN: EntityType = EntityType(Zoglin.TYPE)
        public actual val ZOMBIE: EntityType = EntityType(EntityTypes.ZOMBIE)
        public actual val ZOMBIE_VILLAGER: EntityType = EntityType(EntityTypes.ZOMBIE_VILLAGER)
        public actual val ZOMBIE_VILLAGER_V1: EntityType = EntityType(EntityTypes.DEPRECATED_ZOMBIE_VILLAGER)

        //-------- Boss Mobs --------//
        public actual val ENDER_DRAGON: EntityType = EntityType(EntityTypes.ENDER_DRAGON)
        public actual val WITHER: EntityType = EntityType(EntityTypes.WITHER)
    }
}
