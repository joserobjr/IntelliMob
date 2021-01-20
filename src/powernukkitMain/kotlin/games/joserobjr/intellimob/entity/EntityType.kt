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

import cn.nukkit.entity.mob.*
import cn.nukkit.entity.passive.*
import games.joserobjr.intellimob.entity.factory.EntityAIFactory
import games.joserobjr.intellimob.entity.factory.GenericEntityAIFactory
import games.joserobjr.intellimob.entity.status.ImmutableEntityStatus
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.util.function.IntFunction

/**
 * @author joserobjr
 * @since 2021-01-12
 */
internal actual sealed class EntityType(val networkId: Int) {
    /**
     * The default status based on the entity type.
     */
    actual val defaultStatus: ImmutableEntityStatus by lazy { aiFactory.createDefaultStatus() }

    actual abstract var aiFactory: EntityAIFactory
    
    internal actual class Vanilla(networkId: Int): EntityType(networkId) {
        actual override var aiFactory = EntityAIFactory.fromVanillaType(this)
        init {
            registry[networkId] = this
        }
    }
    
    private class Custom(networkId: Int): EntityType(networkId) {
        override var aiFactory: EntityAIFactory = GenericEntityAIFactory
    }
    
    actual companion object {
        private val registry = Int2ObjectOpenHashMap<EntityType>()
        
        internal fun fromEntity(entity: RegularEntity): EntityType {
            return registry.computeIfAbsent(entity.powerNukkitEntity.networkId, IntFunction(::Custom))
        }
        
        //-------- Passive Mobs --------// 
        actual val BAT = Vanilla(EntityBat.NETWORK_ID)
        actual val CAT = Vanilla(EntityCat.NETWORK_ID)
        actual val CHICKEN = Vanilla(EntityChicken.NETWORK_ID)
        actual val COD = Vanilla(EntityCod.NETWORK_ID)
        actual val COW = Vanilla(EntityCow.NETWORK_ID)
        actual val DONKEY = Vanilla(EntityDonkey.NETWORK_ID)
        actual val FOX = Vanilla(EntityFox.NETWORK_ID)
        actual val HORSE = Vanilla(EntityHorse.NETWORK_ID)
        actual val MOOSHROOM = Vanilla(EntityMooshroom.NETWORK_ID)
        actual val MULE = Vanilla(EntityMule.NETWORK_ID)
        actual val OCELOT = Vanilla(EntityOcelot.NETWORK_ID)
        actual val PARROT = Vanilla(EntityParrot.NETWORK_ID)
        actual val PIG = Vanilla(EntityPig.NETWORK_ID)
        actual val RABBIT = Vanilla(EntityRabbit.NETWORK_ID)
        actual val SALMON = Vanilla(EntitySalmon.NETWORK_ID)
        actual val SHEEP = Vanilla(EntitySheep.NETWORK_ID)
        actual val SKELETON_HORSE = Vanilla(EntitySkeletonHorse.NETWORK_ID)
        actual val SNOW_GOLEM = Vanilla(EntitySnowGolem.NETWORK_ID)
        actual val SQUID = Vanilla(EntitySquid.NETWORK_ID)
        actual val STRIDER = Vanilla(EntityStrider.NETWORK_ID)
        actual val TROPICAL_FISH = Vanilla(EntityTropicalFish.NETWORK_ID)
        actual val TURTLE = Vanilla(EntityTurtle.NETWORK_ID)
        actual val VILLAGER = Vanilla(EntityVillager.NETWORK_ID)
        actual val VILLAGER_V1 = Vanilla(EntityVillagerV1.NETWORK_ID)
        actual val WANDERING_TRADER = Vanilla(EntityWanderingTrader.NETWORK_ID)

        //-------- Neutral Mobs --------//
        actual val BEE = Vanilla(EntityBee.NETWORK_ID)
        actual val CAVE_SPIDER = Vanilla(EntityCaveSpider.NETWORK_ID)
        actual val DOLPHIN = Vanilla(EntityDolphin.NETWORK_ID)
        actual val ENDERMAN = Vanilla(EntityEnderman.NETWORK_ID)
        actual val IRON_GOLEM = Vanilla(EntityIronGolem.NETWORK_ID) 
        actual val LLAMA = Vanilla(EntityLlama.NETWORK_ID)
        actual val PIGLIN = Vanilla(EntityPiglin.NETWORK_ID)
        actual val PANDA = Vanilla(EntityPanda.NETWORK_ID)
        actual val POLAR_BEAR = Vanilla(EntityPolarBear.NETWORK_ID)
        actual val PUFFERFISH = Vanilla(EntityPufferfish.NETWORK_ID)
        actual val SPIDER = Vanilla(EntitySpider.NETWORK_ID)
        actual val WOLF = Vanilla(EntityWolf.NETWORK_ID)
        actual val ZOMBIFED_PIGLIN = Vanilla(EntityZombiePigman.NETWORK_ID)

        //-------- Hostile Mobs --------//
        actual val BLAZE = Vanilla(EntityBlaze.NETWORK_ID)
        actual val CREEPER = Vanilla(EntityCreeper.NETWORK_ID)
        actual val DROWNED = Vanilla(EntityDrowned.NETWORK_ID)
        actual val ELDER_GUARDIAN = Vanilla(EntityElderGuardian.NETWORK_ID)
        actual val ENDERMITE = Vanilla(EntityEndermite.NETWORK_ID)
        actual val EVOKER = Vanilla(EntityEvoker.NETWORK_ID)
        actual val GHAST = Vanilla(EntityGhast.NETWORK_ID)
        actual val GUARDIAN = Vanilla(EntityGuardian.NETWORK_ID)
        actual val HOGLIN = Vanilla(EntityHoglin.NETWORK_ID)
        actual val HUSK = Vanilla(EntityHusk.NETWORK_ID)
        actual val MAGMA_CUBE = Vanilla(EntityMagmaCube.NETWORK_ID)
        actual val PHANTOM = Vanilla(EntityPhantom.NETWORK_ID)
        actual val PIGLIN_BRUTE = Vanilla(EntityPiglinBrute.NETWORK_ID)
        actual val PILLAGER = Vanilla(EntityPillager.NETWORK_ID)
        actual val RAVEGER = Vanilla(EntityRavager.NETWORK_ID)
        actual val SHULKER = Vanilla(EntityShulker.NETWORK_ID)
        actual val SILVERFISH = Vanilla(EntitySilverfish.NETWORK_ID)
        actual val SKELETON = Vanilla(EntitySkeleton.NETWORK_ID)
        actual val SLIME = Vanilla(EntitySlime.NETWORK_ID)
        actual val STRAY = Vanilla(EntityStray.NETWORK_ID)
        actual val VEX = Vanilla(EntityVex.NETWORK_ID)
        actual val VINDICATOR = Vanilla(EntityVindicator.NETWORK_ID)
        actual val WITCH = Vanilla(EntityWitch.NETWORK_ID)
        actual val WITHER_SKELETON = Vanilla(EntityWitherSkeleton.NETWORK_ID)
        actual val ZOGLIN = Vanilla(EntityZoglin.NETWORK_ID)
        actual val ZOMBIE = Vanilla(EntityZombie.NETWORK_ID)
        actual val ZOMBIE_VILLAGER = Vanilla(EntityZombieVillager.NETWORK_ID)
        actual val ZOMBIE_VILLAGER_V1 = Vanilla(EntityZombieVillagerV1.NETWORK_ID)

        //-------- Boss Mobs --------//
        actual val ENDER_DRAGON = Vanilla(EntityEnderDragon.NETWORK_ID)
        actual val WITHER = Vanilla(EntityWither.NETWORK_ID)
    }
}
