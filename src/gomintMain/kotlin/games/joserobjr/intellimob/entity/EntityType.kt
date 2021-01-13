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
import io.gomint.entity.Entity
import io.gomint.entity.animal.*
import io.gomint.entity.monster.*
import io.gomint.entity.passive.EntityVillager
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * @author joserobjr
 * @since 2021-01-12
 */
@ExperimentalIntelliMobApi
public actual class EntityType private constructor(internal val platformType: KClass<out Entity>?) {
    
    @ExperimentalIntelliMobApi
    public actual companion object {
        private val registry: MutableMap<KClass<out Entity>, EntityType> = ConcurrentHashMap()
        private val missingType = EntityType(null)
        
        internal fun fromEntity(entity: RegularEntity) = fromClass(entity::class)
        internal fun fromClass(entityClass: KClass<out Entity>): EntityType {
            registry[entityClass]?.let { return it }
            registry.entries.firstOrNull { it.key.isSuperclassOf(entityClass) }?.let { (_, type) ->
                return registry.putIfAbsent(entityClass, type) ?: type
            }
            return registry.computeIfAbsent(entityClass, ::EntityType)
        }
        
        //-------- Passive Mobs --------// 
        public actual val BAT: EntityType = fromClass(EntityBat::class)
        public actual val CAT: EntityType = fromClass(EntityCat::class)
        public actual val CHICKEN: EntityType = fromClass(EntityChicken::class)
        public actual val COD: EntityType = fromClass(EntityCod::class)
        public actual val COW: EntityType = fromClass(EntityCow::class)
        public actual val DONKEY: EntityType = fromClass(EntityDonkey::class)
        public actual val FOX: EntityType = fromClass(EntityFox::class)
        public actual val HORSE: EntityType = fromClass(EntityHorse::class)
        public actual val MOOSHROOM: EntityType = fromClass(EntityMooshroom::class)
        public actual val MULE: EntityType = fromClass(EntityMule::class)
        public actual val OCELOT: EntityType = fromClass(EntityOcelot::class)
        public actual val PARROT: EntityType = fromClass(EntityParrot::class)
        public actual val PIG: EntityType = fromClass(EntityPig::class)
        public actual val RABBIT: EntityType = fromClass(EntityRabbit::class)
        public actual val SALMON: EntityType = fromClass(EntitySalmon::class)
        public actual val SHEEP: EntityType = fromClass(EntitySheep::class)
        public actual val SKELETON_HORSE: EntityType = fromClass(EntitySkeletonHorse::class)
        public actual val SNOW_GOLEM: EntityType = fromClass(EntitySnowGolem::class)
        public actual val SQUID: EntityType = fromClass(EntitySquid::class)
        public actual val STRIDER: EntityType = fromClass(EntityStrider::class)
        public actual val TROPICAL_FISH: EntityType = fromClass(EntityTropicalFish::class)
        public actual val TURTLE: EntityType = fromClass(EntityTurtle::class)
        public actual val VILLAGER: EntityType = fromClass(EntityVillager::class)
        public actual val VILLAGER_V1: EntityType = missingType
        public actual val WANDERING_TRADER: EntityType = fromClass(Entity::class)

        //-------- Neutral Mobs --------//
        public actual val BEE: EntityType = fromClass(EntityBee::class)
        public actual val CAVE_SPIDER: EntityType = fromClass(EntityCaveSpider::class)
        public actual val DOLPHIN: EntityType = fromClass(EntityDolphin::class)
        public actual val ENDERMAN: EntityType = fromClass(EntityEnderman::class)
        public actual val IRON_GOLEM: EntityType = fromClass(EntityIronGolem::class)
        public actual val LLAMA: EntityType = fromClass(EntityLama::class) 
        public actual val PIGLIN: EntityType = fromClass(EntityPiglin::class)
        public actual val PANDA: EntityType = fromClass(EntityPanda::class)
        public actual val POLAR_BEAR: EntityType = fromClass(EntityPolarBear::class)
        public actual val PUFFERFISH: EntityType = fromClass(EntityPufferfish::class)
        public actual val SPIDER: EntityType = fromClass(EntitySpider::class)
        public actual val WOLF: EntityType = fromClass(EntityWolf::class)
        public actual val ZOMBIFED_PIGLIN: EntityType = fromClass(EntityZombiePiglin::class)

        //-------- Hostile Mobs --------//
        public actual val BLAZE: EntityType = fromClass(EntityBlaze::class)
        public actual val CREEPER: EntityType = fromClass(EntityCreeper::class)
        public actual val DROWNED: EntityType = fromClass(EntityDrowned::class)
        public actual val ELDER_GUARDIAN: EntityType = fromClass(EntityElderGuardian::class)
        public actual val ENDERMITE: EntityType = fromClass(EntityEndermite::class)
        public actual val EVOKER: EntityType = fromClass(EntityEvoker::class)
        public actual val GHAST: EntityType = fromClass(EntityGhast::class)
        public actual val GUARDIAN: EntityType = fromClass(EntityGuardian::class)
        public actual val HOGLIN: EntityType = fromClass(EntityHoglin::class)
        public actual val HUSK: EntityType = fromClass(EntityHusk::class)
        public actual val MAGMA_CUBE: EntityType = fromClass(EntityMagmaCube::class)
        public actual val PHANTOM: EntityType = fromClass(EntityPhantom::class)
        public actual val PIGLIN_BRUTE: EntityType = fromClass(EntityPiglinBrute::class)
        public actual val PILLAGER: EntityType = fromClass(EntityPillager::class)
        public actual val RAVEGER: EntityType = fromClass(EntityRavager::class)
        public actual val SHULKER: EntityType = fromClass(EntityShulker::class)
        public actual val SILVERFISH: EntityType = fromClass(EntitySilverfish::class)
        public actual val SKELETON: EntityType = fromClass(EntitySkeleton::class)
        public actual val SLIME: EntityType = fromClass(EntitySlime::class)
        public actual val STRAY: EntityType = fromClass(EntityStray::class)
        public actual val VEX: EntityType = fromClass(EntityVex::class)
        public actual val VINDICATOR: EntityType = fromClass(EntityVindicator::class)
        public actual val WITCH: EntityType = fromClass(EntityWitch::class)
        public actual val WITHER_SKELETON: EntityType = fromClass(EntityWitherSkeleton::class)
        public actual val ZOGLIN: EntityType = fromClass(EntityZoglin::class)
        public actual val ZOMBIE: EntityType = fromClass(EntityZombie::class)
        public actual val ZOMBIE_VILLAGER: EntityType = fromClass(EntityZombieVillager::class)
        public actual val ZOMBIE_VILLAGER_V1: EntityType = missingType

        //-------- Boss Mobs --------//
        public actual val ENDER_DRAGON: EntityType = fromClass(EntityEnderDragon::class)
        public actual val WITHER: EntityType = fromClass(EntityWither::class)
    }
}
