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
import games.joserobjr.intellimob.annotation.ExperimentalIntelliMobApi
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.util.function.IntFunction

/**
 * @author joserobjr
 * @since 2021-01-12
 */
@ExperimentalIntelliMobApi
public actual class EntityType(public val networkId: Int) {
    @ExperimentalIntelliMobApi
    public actual companion object {
        private val registry = Int2ObjectOpenHashMap<EntityType>()
        
        internal fun fromEntity(entity: RegularEntity): EntityType {
            return registry.computeIfAbsent(entity.powerNukkitEntity.networkId, IntFunction(::EntityType))
        }
        
        //-------- Passive Mobs --------// 
        public actual val BAT: EntityType = EntityType(EntityBat.NETWORK_ID)
        public actual val CAT: EntityType = EntityType(EntityCat.NETWORK_ID)
        public actual val CHICKEN: EntityType = EntityType(EntityChicken.NETWORK_ID)
        public actual val COD: EntityType = EntityType(EntityCod.NETWORK_ID)
        public actual val COW: EntityType = EntityType(EntityCow.NETWORK_ID)
        public actual val DONKEY: EntityType = EntityType(EntityDonkey.NETWORK_ID)
        public actual val FOX: EntityType = EntityType(EntityFox.NETWORK_ID)
        public actual val HORSE: EntityType = EntityType(EntityHorse.NETWORK_ID)
        public actual val MOOSHROOM: EntityType = EntityType(EntityMooshroom.NETWORK_ID)
        public actual val MULE: EntityType = EntityType(EntityMule.NETWORK_ID)
        public actual val OCELOT: EntityType = EntityType(EntityOcelot.NETWORK_ID)
        public actual val PARROT: EntityType = EntityType(EntityParrot.NETWORK_ID)
        public actual val PIG: EntityType = EntityType(EntityPig.NETWORK_ID)
        public actual val RABBIT: EntityType = EntityType(EntityRabbit.NETWORK_ID)
        public actual val SALMON: EntityType = EntityType(EntitySalmon.NETWORK_ID)
        public actual val SHEEP: EntityType = EntityType(EntitySheep.NETWORK_ID)
        public actual val SKELETON_HORSE: EntityType = EntityType(EntitySkeletonHorse.NETWORK_ID)
        public actual val SNOW_GOLEM: EntityType = EntityType(EntitySnowGolem.NETWORK_ID)
        public actual val SQUID: EntityType = EntityType(EntitySquid.NETWORK_ID)
        public actual val STRIDER: EntityType = EntityType(EntityStrider.NETWORK_ID)
        public actual val TROPICAL_FISH: EntityType = EntityType(EntityTropicalFish.NETWORK_ID)
        public actual val TURTLE: EntityType = EntityType(EntityTurtle.NETWORK_ID)
        public actual val VILLAGER: EntityType = EntityType(EntityVillager.NETWORK_ID)
        public actual val VILLAGER_V1: EntityType = EntityType(EntityVillagerV1.NETWORK_ID)
        public actual val WANDERING_TRADER: EntityType = EntityType(EntityWanderingTrader.NETWORK_ID)

        //-------- Neutral Mobs --------//
        public actual val BEE: EntityType = EntityType(EntityBee.NETWORK_ID)
        public actual val CAVE_SPIDER: EntityType = EntityType(EntityCaveSpider.NETWORK_ID)
        public actual val DOLPHIN: EntityType = EntityType(EntityDolphin.NETWORK_ID)
        public actual val ENDERMAN: EntityType = EntityType(EntityEnderman.NETWORK_ID)
        public actual val IRON_GOLEM: EntityType = EntityType(EntityIronGolem.NETWORK_ID) 
        public actual val LLAMA: EntityType = EntityType(EntityLlama.NETWORK_ID)
        public actual val PIGLIN: EntityType = EntityType(EntityPiglin.NETWORK_ID)
        public actual val PANDA: EntityType = EntityType(EntityPanda.NETWORK_ID)
        public actual val POLAR_BEAR: EntityType = EntityType(EntityPolarBear.NETWORK_ID)
        public actual val PUFFERFISH: EntityType = EntityType(EntityPufferfish.NETWORK_ID)
        public actual val SPIDER: EntityType = EntityType(EntitySpider.NETWORK_ID)
        public actual val WOLF: EntityType = EntityType(EntityWolf.NETWORK_ID)
        public actual val ZOMBIFED_PIGLIN: EntityType = EntityType(EntityZombiePigman.NETWORK_ID)

        //-------- Hostile Mobs --------//
        public actual val BLAZE: EntityType = EntityType(EntityBlaze.NETWORK_ID)
        public actual val CREEPER: EntityType = EntityType(EntityCreeper.NETWORK_ID)
        public actual val DROWNED: EntityType = EntityType(EntityDrowned.NETWORK_ID)
        public actual val ELDER_GUARDIAN: EntityType = EntityType(EntityElderGuardian.NETWORK_ID)
        public actual val ENDERMITE: EntityType = EntityType(EntityEndermite.NETWORK_ID)
        public actual val EVOKER: EntityType = EntityType(EntityEvoker.NETWORK_ID)
        public actual val GHAST: EntityType = EntityType(EntityGhast.NETWORK_ID)
        public actual val GUARDIAN: EntityType = EntityType(EntityGuardian.NETWORK_ID)
        public actual val HOGLIN: EntityType = EntityType(EntityHoglin.NETWORK_ID)
        public actual val HUSK: EntityType = EntityType(EntityHusk.NETWORK_ID)
        public actual val MAGMA_CUBE: EntityType = EntityType(EntityMagmaCube.NETWORK_ID)
        public actual val PHANTOM: EntityType = EntityType(EntityPhantom.NETWORK_ID)
        public actual val PIGLIN_BRUTE: EntityType = EntityType(EntityPiglinBrute.NETWORK_ID)
        public actual val PILLAGER: EntityType = EntityType(EntityPillager.NETWORK_ID)
        public actual val RAVEGER: EntityType = EntityType(EntityRavager.NETWORK_ID)
        public actual val SHULKER: EntityType = EntityType(EntityShulker.NETWORK_ID)
        public actual val SILVERFISH: EntityType = EntityType(EntitySilverfish.NETWORK_ID)
        public actual val SKELETON: EntityType = EntityType(EntitySkeleton.NETWORK_ID)
        public actual val SLIME: EntityType = EntityType(EntitySlime.NETWORK_ID)
        public actual val STRAY: EntityType = EntityType(EntityStray.NETWORK_ID)
        public actual val VEX: EntityType = EntityType(EntityVex.NETWORK_ID)
        public actual val VINDICATOR: EntityType = EntityType(EntityVindicator.NETWORK_ID)
        public actual val WITCH: EntityType = EntityType(EntityWitch.NETWORK_ID)
        public actual val WITHER_SKELETON: EntityType = EntityType(EntityWitherSkeleton.NETWORK_ID)
        public actual val ZOGLIN: EntityType = EntityType(EntityZoglin.NETWORK_ID)
        public actual val ZOMBIE: EntityType = EntityType(EntityZombie.NETWORK_ID)
        public actual val ZOMBIE_VILLAGER: EntityType = EntityType(EntityZombieVillager.NETWORK_ID)
        public actual val ZOMBIE_VILLAGER_V1: EntityType = EntityType(EntityZombieVillagerV1.NETWORK_ID)

        //-------- Boss Mobs --------//
        public actual val ENDER_DRAGON: EntityType = EntityType(EntityEnderDragon.NETWORK_ID)
        public actual val WITHER: EntityType = EntityType(EntityWither.NETWORK_ID)
    }
}
