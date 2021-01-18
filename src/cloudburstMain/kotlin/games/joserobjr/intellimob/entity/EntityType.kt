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

import games.joserobjr.intellimob.entity.api.*
import games.joserobjr.intellimob.entity.status.ImmutableEntityStatus
import games.joserobjr.intellimob.entity.status.createDefaultStatus
import org.cloudburstmc.server.entity.EntityTypes
import java.util.concurrent.ConcurrentHashMap

/**
 * @author joserobjr
 * @since 2021-01-12
 */
internal actual class EntityType(val platformType: PlatformEntityType<*>) {
    /**
     * The default status based on the entity type.
     */
    actual val defaultStatus: ImmutableEntityStatus by lazy { createDefaultStatus() }
    
    actual companion object {
        private val registry: MutableMap<PlatformEntityType<*>, EntityType> = ConcurrentHashMap()
        internal fun fromEntity(entity: RegularEntity): EntityType {
            return registry.computeIfAbsent(entity.type.platformType, ::EntityType)
        }
        
        //-------- Passive Mobs --------// 
        actual val BAT: EntityType = EntityType(EntityTypes.BAT)
        actual val CAT: EntityType = EntityType(EntityTypes.CAT)
        actual val CHICKEN: EntityType = EntityType(EntityTypes.CHICKEN)
        actual val COD: EntityType = EntityType(EntityTypes.COD)
        actual val COW: EntityType = EntityType(EntityTypes.COW)
        actual val DONKEY: EntityType = EntityType(EntityTypes.DONKEY)
        actual val FOX: EntityType = EntityType(Fox.TYPE)
        actual val HORSE: EntityType = EntityType(EntityTypes.HORSE)
        actual val MOOSHROOM: EntityType = EntityType(EntityTypes.MOOSHROOM)
        actual val MULE: EntityType = EntityType(EntityTypes.MULE)
        actual val OCELOT: EntityType = EntityType(EntityTypes.OCELOT)
        actual val PARROT: EntityType = EntityType(EntityTypes.PARROT)
        actual val PIG: EntityType = EntityType(EntityTypes.PIG)
        actual val RABBIT: EntityType = EntityType(EntityTypes.RABBIT)
        actual val SALMON: EntityType = EntityType(EntityTypes.SALMON)
        actual val SHEEP: EntityType = EntityType(EntityTypes.SHEEP)
        actual val SKELETON_HORSE: EntityType = EntityType(EntityTypes.SKELETON_HORSE)
        actual val SNOW_GOLEM: EntityType = EntityType(EntityTypes.SNOW_GOLEM)
        actual val SQUID: EntityType = EntityType(EntityTypes.SQUID)
        actual val STRIDER: EntityType = EntityType(Strider.TYPE)
        actual val TROPICAL_FISH: EntityType = EntityType(EntityTypes.TROPICALFISH)
        actual val TURTLE: EntityType = EntityType(EntityTypes.TURTLE)
        actual val VILLAGER: EntityType = EntityType(EntityTypes.VILLAGER)
        actual val VILLAGER_V1: EntityType = EntityType(EntityTypes.DEPRECATED_VILLAGER)
        actual val WANDERING_TRADER: EntityType = EntityType(EntityTypes.WANDERING_TRADER)

        //-------- Neutral Mobs --------//
        actual val BEE: EntityType = EntityType(Bee.TYPE)
        actual val CAVE_SPIDER: EntityType = EntityType(EntityTypes.CAVE_SPIDER)
        actual val DOLPHIN: EntityType = EntityType(EntityTypes.DOLPHIN)
        actual val ENDERMAN: EntityType = EntityType(EntityTypes.ENDERMAN)
        actual val IRON_GOLEM: EntityType = EntityType(EntityTypes.IRON_GOLEM)
        actual val LLAMA: EntityType = EntityType(EntityTypes.LLAMA)
        actual val PIGLIN: EntityType = EntityType(Piglin.TYPE)
        actual val PANDA: EntityType = EntityType(EntityTypes.PANDA)
        actual val POLAR_BEAR: EntityType = EntityType(EntityTypes.POLAR_BEAR)
        actual val PUFFERFISH: EntityType = EntityType(EntityTypes.PUFFERFISH)
        actual val SPIDER: EntityType = EntityType(EntityTypes.SPIDER)
        actual val WOLF: EntityType = EntityType(EntityTypes.WOLF)
        actual val ZOMBIFED_PIGLIN: EntityType = EntityType(EntityTypes.ZOMBIE_PIGMAN)

        //-------- Hostile Mobs --------//
        actual val BLAZE: EntityType = EntityType(EntityTypes.BLAZE)
        actual val CREEPER: EntityType = EntityType(EntityTypes.CREEPER)
        actual val DROWNED: EntityType = EntityType(EntityTypes.DROWNED)
        actual val ELDER_GUARDIAN: EntityType = EntityType(EntityTypes.ELDER_GUARDIAN)
        actual val ENDERMITE: EntityType = EntityType(EntityTypes.ENDERMITE)
        actual val EVOKER: EntityType = EntityType(Evoker.TYPE)
        actual val GHAST: EntityType = EntityType(EntityTypes.GHAST)
        actual val GUARDIAN: EntityType = EntityType(EntityTypes.GUARDIAN)
        actual val HOGLIN: EntityType = EntityType(Hoglin.TYPE)
        actual val HUSK: EntityType = EntityType(EntityTypes.HUSK)
        actual val MAGMA_CUBE: EntityType = EntityType(EntityTypes.MAGMA_CUBE)
        actual val PHANTOM: EntityType = EntityType(EntityTypes.PHANTOM)
        actual val PIGLIN_BRUTE: EntityType = EntityType(PiglinBrute.TYPE)
        actual val PILLAGER: EntityType = EntityType(EntityTypes.PILLAGER)
        actual val RAVEGER: EntityType = EntityType(Raveger.TYPE)
        actual val SHULKER: EntityType = EntityType(EntityTypes.SHULKER)
        actual val SILVERFISH: EntityType = EntityType(EntityTypes.SILVERFISH)
        actual val SKELETON: EntityType = EntityType(EntityTypes.SKELETON)
        actual val SLIME: EntityType = EntityType(EntityTypes.SLIME)
        actual val STRAY: EntityType = EntityType(EntityTypes.STRAY)
        actual val VEX: EntityType = EntityType(EntityTypes.VEX)
        actual val VINDICATOR: EntityType = EntityType(EntityTypes.VINDICATOR)
        actual val WITCH: EntityType = EntityType(EntityTypes.WITCH)
        actual val WITHER_SKELETON: EntityType = EntityType(EntityTypes.WITHER_SKELETON)
        actual val ZOGLIN: EntityType = EntityType(Zoglin.TYPE)
        actual val ZOMBIE: EntityType = EntityType(EntityTypes.ZOMBIE)
        actual val ZOMBIE_VILLAGER: EntityType = EntityType(EntityTypes.ZOMBIE_VILLAGER)
        actual val ZOMBIE_VILLAGER_V1: EntityType = EntityType(EntityTypes.DEPRECATED_ZOMBIE_VILLAGER)

        //-------- Boss Mobs --------//
        actual val ENDER_DRAGON: EntityType = EntityType(EntityTypes.ENDER_DRAGON)
        actual val WITHER: EntityType = EntityType(EntityTypes.WITHER)
    }
}
