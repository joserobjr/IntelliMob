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
import games.joserobjr.intellimob.entity.status.createDefaultStatus
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
internal actual class EntityType private constructor(internal val platformType: KClass<out Entity<*>>?) {
    /**
     * The default status based on the entity type.
     */
    actual val defaultStatus: ImmutableEntityStatus by lazy { createDefaultStatus() }
    
    actual companion object {
        private val registry: MutableMap<KClass<out Entity<*>>, EntityType> = ConcurrentHashMap()
        private val missingType = EntityType(null)
        
        internal fun fromEntity(entity: RegularEntity) = fromClass(entity.goMintEntity::class)
        internal fun fromClass(entityClass: KClass<out Entity<*>>): EntityType {
            registry[entityClass]?.let { return it }
            registry.entries.firstOrNull { it.key.isSuperclassOf(entityClass) }?.let { (_, type) ->
                return registry.putIfAbsent(entityClass, type) ?: type
            }
            return registry.computeIfAbsent(entityClass, ::EntityType)
        }
        
        //-------- Passive Mobs --------// 
        actual val BAT: EntityType = fromClass(EntityBat::class)
        actual val CAT: EntityType = fromClass(EntityCat::class)
        actual val CHICKEN: EntityType = fromClass(EntityChicken::class)
        actual val COD: EntityType = fromClass(EntityCod::class)
        actual val COW: EntityType = fromClass(EntityCow::class)
        actual val DONKEY: EntityType = fromClass(EntityDonkey::class)
        actual val FOX: EntityType = fromClass(EntityFox::class)
        actual val HORSE: EntityType = fromClass(EntityHorse::class)
        actual val MOOSHROOM: EntityType = fromClass(EntityMooshroom::class)
        actual val MULE: EntityType = fromClass(EntityMule::class)
        actual val OCELOT: EntityType = fromClass(EntityOcelot::class)
        actual val PARROT: EntityType = fromClass(EntityParrot::class)
        actual val PIG: EntityType = fromClass(EntityPig::class)
        actual val RABBIT: EntityType = fromClass(EntityRabbit::class)
        actual val SALMON: EntityType = fromClass(EntitySalmon::class)
        actual val SHEEP: EntityType = fromClass(EntitySheep::class)
        actual val SKELETON_HORSE: EntityType = fromClass(EntitySkeletonHorse::class)
        actual val SNOW_GOLEM: EntityType = fromClass(EntitySnowGolem::class)
        actual val SQUID: EntityType = fromClass(EntitySquid::class)
        actual val STRIDER: EntityType = fromClass(EntityStrider::class)
        actual val TROPICAL_FISH: EntityType = fromClass(EntityTropicalFish::class)
        actual val TURTLE: EntityType = fromClass(EntityTurtle::class)
        actual val VILLAGER: EntityType = fromClass(EntityVillager::class)
        actual val VILLAGER_V1: EntityType = missingType
        actual val WANDERING_TRADER: EntityType = fromClass(Entity::class)

        //-------- Neutral Mobs --------//
        actual val BEE: EntityType = fromClass(EntityBee::class)
        actual val CAVE_SPIDER: EntityType = fromClass(EntityCaveSpider::class)
        actual val DOLPHIN: EntityType = fromClass(EntityDolphin::class)
        actual val ENDERMAN: EntityType = fromClass(EntityEnderman::class)
        actual val IRON_GOLEM: EntityType = fromClass(EntityIronGolem::class)
        actual val LLAMA: EntityType = fromClass(EntityLama::class) 
        actual val PIGLIN: EntityType = fromClass(EntityPiglin::class)
        actual val PANDA: EntityType = fromClass(EntityPanda::class)
        actual val POLAR_BEAR: EntityType = fromClass(EntityPolarBear::class)
        actual val PUFFERFISH: EntityType = fromClass(EntityPufferfish::class)
        actual val SPIDER: EntityType = fromClass(EntitySpider::class)
        actual val WOLF: EntityType = fromClass(EntityWolf::class)
        actual val ZOMBIFED_PIGLIN: EntityType = fromClass(EntityZombiePiglin::class)

        //-------- Hostile Mobs --------//
        actual val BLAZE: EntityType = fromClass(EntityBlaze::class)
        actual val CREEPER: EntityType = fromClass(EntityCreeper::class)
        actual val DROWNED: EntityType = fromClass(EntityDrowned::class)
        actual val ELDER_GUARDIAN: EntityType = fromClass(EntityElderGuardian::class)
        actual val ENDERMITE: EntityType = fromClass(EntityEndermite::class)
        actual val EVOKER: EntityType = fromClass(EntityEvoker::class)
        actual val GHAST: EntityType = fromClass(EntityGhast::class)
        actual val GUARDIAN: EntityType = fromClass(EntityGuardian::class)
        actual val HOGLIN: EntityType = fromClass(EntityHoglin::class)
        actual val HUSK: EntityType = fromClass(EntityHusk::class)
        actual val MAGMA_CUBE: EntityType = fromClass(EntityMagmaCube::class)
        actual val PHANTOM: EntityType = fromClass(EntityPhantom::class)
        actual val PIGLIN_BRUTE: EntityType = fromClass(EntityPiglinBrute::class)
        actual val PILLAGER: EntityType = fromClass(EntityPillager::class)
        actual val RAVEGER: EntityType = fromClass(EntityRavager::class)
        actual val SHULKER: EntityType = fromClass(EntityShulker::class)
        actual val SILVERFISH: EntityType = fromClass(EntitySilverfish::class)
        actual val SKELETON: EntityType = fromClass(EntitySkeleton::class)
        actual val SLIME: EntityType = fromClass(EntitySlime::class)
        actual val STRAY: EntityType = fromClass(EntityStray::class)
        actual val VEX: EntityType = fromClass(EntityVex::class)
        actual val VINDICATOR: EntityType = fromClass(EntityVindicator::class)
        actual val WITCH: EntityType = fromClass(EntityWitch::class)
        actual val WITHER_SKELETON: EntityType = fromClass(EntityWitherSkeleton::class)
        actual val ZOGLIN: EntityType = fromClass(EntityZoglin::class)
        actual val ZOMBIE: EntityType = fromClass(EntityZombie::class)
        actual val ZOMBIE_VILLAGER: EntityType = fromClass(EntityZombieVillager::class)
        actual val ZOMBIE_VILLAGER_V1: EntityType = missingType

        //-------- Boss Mobs --------//
        actual val ENDER_DRAGON: EntityType = fromClass(EntityEnderDragon::class)
        actual val WITHER: EntityType = fromClass(EntityWither::class)
    }
}
