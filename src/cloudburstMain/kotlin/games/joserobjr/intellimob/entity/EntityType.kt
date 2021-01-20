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
import games.joserobjr.intellimob.entity.factory.EntityAIFactory
import games.joserobjr.intellimob.entity.factory.GenericEntityAIFactory
import games.joserobjr.intellimob.entity.status.ImmutableEntityStatus
import org.cloudburstmc.server.entity.EntityTypes
import java.util.concurrent.ConcurrentHashMap

/**
 * @author joserobjr
 * @since 2021-01-12
 */
internal actual sealed class EntityType {
    internal abstract val platformType: PlatformEntityType<*>
    
    /**
     * The default status based on the entity type.
     */
    actual val defaultStatus: ImmutableEntityStatus by lazy { aiFactory.createDefaultStatus() }

    actual abstract var aiFactory: EntityAIFactory
    
    internal actual class Vanilla(override val platformType: PlatformEntityType<*>) : EntityType() {
        actual override var aiFactory: EntityAIFactory = EntityAIFactory.fromVanillaType(this)
        init {
            registry[platformType] = this
        }
    }
    
    private class Custom(override val platformType: PlatformEntityType<*>): EntityType() {
        override var aiFactory: EntityAIFactory = GenericEntityAIFactory
    }

    actual companion object {
        private val registry: MutableMap<PlatformEntityType<*>, EntityType> = ConcurrentHashMap()
        internal fun fromEntity(entity: RegularEntity): EntityType {
            return registry.computeIfAbsent(entity.type.platformType, ::Custom)
        }
        
        //-------- Passive Mobs --------// 
        actual val BAT = Vanilla(EntityTypes.BAT)
        actual val CAT = Vanilla(EntityTypes.CAT)
        actual val CHICKEN = Vanilla(EntityTypes.CHICKEN)
        actual val COD = Vanilla(EntityTypes.COD)
        actual val COW = Vanilla(EntityTypes.COW)
        actual val DONKEY = Vanilla(EntityTypes.DONKEY)
        actual val FOX = Vanilla(Fox.TYPE)
        actual val HORSE = Vanilla(EntityTypes.HORSE)
        actual val MOOSHROOM = Vanilla(EntityTypes.MOOSHROOM)
        actual val MULE = Vanilla(EntityTypes.MULE)
        actual val OCELOT = Vanilla(EntityTypes.OCELOT)
        actual val PARROT = Vanilla(EntityTypes.PARROT)
        actual val PIG = Vanilla(EntityTypes.PIG)
        actual val RABBIT = Vanilla(EntityTypes.RABBIT)
        actual val SALMON = Vanilla(EntityTypes.SALMON)
        actual val SHEEP = Vanilla(EntityTypes.SHEEP)
        actual val SKELETON_HORSE = Vanilla(EntityTypes.SKELETON_HORSE)
        actual val SNOW_GOLEM = Vanilla(EntityTypes.SNOW_GOLEM)
        actual val SQUID = Vanilla(EntityTypes.SQUID)
        actual val STRIDER = Vanilla(Strider.TYPE)
        actual val TROPICAL_FISH = Vanilla(EntityTypes.TROPICALFISH)
        actual val TURTLE = Vanilla(EntityTypes.TURTLE)
        actual val VILLAGER = Vanilla(EntityTypes.VILLAGER)
        actual val VILLAGER_V1 = Vanilla(EntityTypes.DEPRECATED_VILLAGER)
        actual val WANDERING_TRADER = Vanilla(EntityTypes.WANDERING_TRADER)

        //-------- Neutral Mobs --------//
        actual val BEE = Vanilla(Bee.TYPE)
        actual val CAVE_SPIDER = Vanilla(EntityTypes.CAVE_SPIDER)
        actual val DOLPHIN = Vanilla(EntityTypes.DOLPHIN)
        actual val ENDERMAN = Vanilla(EntityTypes.ENDERMAN)
        actual val IRON_GOLEM = Vanilla(EntityTypes.IRON_GOLEM)
        actual val LLAMA = Vanilla(EntityTypes.LLAMA)
        actual val PIGLIN = Vanilla(Piglin.TYPE)
        actual val PANDA = Vanilla(EntityTypes.PANDA)
        actual val POLAR_BEAR = Vanilla(EntityTypes.POLAR_BEAR)
        actual val PUFFERFISH = Vanilla(EntityTypes.PUFFERFISH)
        actual val SPIDER = Vanilla(EntityTypes.SPIDER)
        actual val WOLF = Vanilla(EntityTypes.WOLF)
        actual val ZOMBIFED_PIGLIN = Vanilla(EntityTypes.ZOMBIE_PIGMAN)

        //-------- Hostile Mobs --------//
        actual val BLAZE = Vanilla(EntityTypes.BLAZE)
        actual val CREEPER = Vanilla(EntityTypes.CREEPER)
        actual val DROWNED = Vanilla(EntityTypes.DROWNED)
        actual val ELDER_GUARDIAN = Vanilla(EntityTypes.ELDER_GUARDIAN)
        actual val ENDERMITE = Vanilla(EntityTypes.ENDERMITE)
        actual val EVOKER = Vanilla(Evoker.TYPE)
        actual val GHAST = Vanilla(EntityTypes.GHAST)
        actual val GUARDIAN = Vanilla(EntityTypes.GUARDIAN)
        actual val HOGLIN = Vanilla(Hoglin.TYPE)
        actual val HUSK = Vanilla(EntityTypes.HUSK)
        actual val MAGMA_CUBE = Vanilla(EntityTypes.MAGMA_CUBE)
        actual val PHANTOM = Vanilla(EntityTypes.PHANTOM)
        actual val PIGLIN_BRUTE = Vanilla(PiglinBrute.TYPE)
        actual val PILLAGER = Vanilla(EntityTypes.PILLAGER)
        actual val RAVEGER = Vanilla(Raveger.TYPE)
        actual val SHULKER = Vanilla(EntityTypes.SHULKER)
        actual val SILVERFISH = Vanilla(EntityTypes.SILVERFISH)
        actual val SKELETON = Vanilla(EntityTypes.SKELETON)
        actual val SLIME = Vanilla(EntityTypes.SLIME)
        actual val STRAY = Vanilla(EntityTypes.STRAY)
        actual val VEX = Vanilla(EntityTypes.VEX)
        actual val VINDICATOR = Vanilla(EntityTypes.VINDICATOR)
        actual val WITCH = Vanilla(EntityTypes.WITCH)
        actual val WITHER_SKELETON = Vanilla(EntityTypes.WITHER_SKELETON)
        actual val ZOGLIN = Vanilla(Zoglin.TYPE)
        actual val ZOMBIE = Vanilla(EntityTypes.ZOMBIE)
        actual val ZOMBIE_VILLAGER = Vanilla(EntityTypes.ZOMBIE_VILLAGER)
        actual val ZOMBIE_VILLAGER_V1 = Vanilla(EntityTypes.DEPRECATED_ZOMBIE_VILLAGER)

        //-------- Boss Mobs --------//
        actual val ENDER_DRAGON = Vanilla(EntityTypes.ENDER_DRAGON)
        actual val WITHER = Vanilla(EntityTypes.WITHER)
    }
}
