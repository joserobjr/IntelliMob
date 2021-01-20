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
import games.joserobjr.intellimob.entity.factory.GenericEntityAIFactory
import games.joserobjr.intellimob.entity.factory.UninitializedAI
import games.joserobjr.intellimob.entity.status.ImmutableEntityStatus
import games.joserobjr.intellimob.entity.status.createDefaultStatus
import io.gomint.entity.Entity
import io.gomint.entity.EntityPlayer
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
internal actual sealed class EntityType {
    internal abstract val platformType: KClass<out Entity<*>>?
    actual abstract var aiFactory: EntityAIFactory
    
    /**
     * The default status based on the entity type.
     */
    actual val defaultStatus: ImmutableEntityStatus by lazy { createDefaultStatus() }
    
    internal actual class Vanilla(override val platformType: KClass<out Entity<*>>?): EntityType() {
        actual override var aiFactory: EntityAIFactory = UninitializedAI
            get() {
                field.takeUnless { it == UninitializedAI }?.let { return it }
                field = EntityAIFactory.fromVanillaType(this)
                return field
            }
        init {
            if (platformType != null) {
                registry[platformType] = this
            }
        }
    }
    
    private class Custom(override val platformType: KClass<out Entity<*>>): EntityType() {
        override var aiFactory: EntityAIFactory = GenericEntityAIFactory
    }
    
    actual companion object {
        private val registry: MutableMap<KClass<out Entity<*>>, EntityType> = ConcurrentHashMap()
        
        internal fun fromEntity(entity: RegularEntity) = fromClass(entity.goMintEntity::class)
        internal fun fromClass(entityClass: KClass<out Entity<*>>): EntityType {
            registry[entityClass]?.let { return it }
            registry.entries.firstOrNull { it.key.isSuperclassOf(entityClass) }?.let { (_, type) ->
                return registry.putIfAbsent(entityClass, type) ?: type
            }
            return registry.computeIfAbsent(entityClass, ::Custom)
        }

        //-------- Human Players --------//
        actual val PLAYER = Vanilla(EntityPlayer::class)
        
        //-------- Passive Mobs --------// 
        actual val BAT = Vanilla(EntityBat::class)
        actual val CAT = Vanilla(EntityCat::class)
        actual val CHICKEN = Vanilla(EntityChicken::class)
        actual val COD = Vanilla(EntityCod::class)
        actual val COW = Vanilla(EntityCow::class)
        actual val DONKEY = Vanilla(EntityDonkey::class)
        actual val FOX = Vanilla(EntityFox::class)
        actual val HORSE = Vanilla(EntityHorse::class)
        actual val MOOSHROOM = Vanilla(EntityMooshroom::class)
        actual val MULE = Vanilla(EntityMule::class)
        actual val OCELOT = Vanilla(EntityOcelot::class)
        actual val PARROT = Vanilla(EntityParrot::class)
        actual val PIG = Vanilla(EntityPig::class)
        actual val RABBIT = Vanilla(EntityRabbit::class)
        actual val SALMON = Vanilla(EntitySalmon::class)
        actual val SHEEP = Vanilla(EntitySheep::class)
        actual val SKELETON_HORSE = Vanilla(EntitySkeletonHorse::class)
        actual val SNOW_GOLEM = Vanilla(EntitySnowGolem::class)
        actual val SQUID = Vanilla(EntitySquid::class)
        actual val STRIDER = Vanilla(EntityStrider::class)
        actual val TROPICAL_FISH = Vanilla(EntityTropicalFish::class)
        actual val TURTLE = Vanilla(EntityTurtle::class)
        actual val VILLAGER = Vanilla(EntityVillager::class)
        actual val VILLAGER_V1 = Vanilla(null)
        actual val WANDERING_TRADER = Vanilla(Entity::class)

        //-------- Neutral Mobs --------//
        actual val BEE = Vanilla(EntityBee::class)
        actual val CAVE_SPIDER = Vanilla(EntityCaveSpider::class)
        actual val DOLPHIN = Vanilla(EntityDolphin::class)
        actual val ENDERMAN = Vanilla(EntityEnderman::class)
        actual val IRON_GOLEM = Vanilla(EntityIronGolem::class)
        actual val LLAMA = Vanilla(EntityLama::class) 
        actual val PIGLIN = Vanilla(EntityPiglin::class)
        actual val PANDA = Vanilla(EntityPanda::class)
        actual val POLAR_BEAR = Vanilla(EntityPolarBear::class)
        actual val PUFFERFISH = Vanilla(EntityPufferfish::class)
        actual val SPIDER = Vanilla(EntitySpider::class)
        actual val WOLF = Vanilla(EntityWolf::class)
        actual val ZOMBIFED_PIGLIN = Vanilla(EntityZombiePiglin::class)

        //-------- Hostile Mobs --------//
        actual val BLAZE = Vanilla(EntityBlaze::class)
        actual val CREEPER = Vanilla(EntityCreeper::class)
        actual val DROWNED = Vanilla(EntityDrowned::class)
        actual val ELDER_GUARDIAN = Vanilla(EntityElderGuardian::class)
        actual val ENDERMITE = Vanilla(EntityEndermite::class)
        actual val EVOKER = Vanilla(EntityEvoker::class)
        actual val GHAST = Vanilla(EntityGhast::class)
        actual val GUARDIAN = Vanilla(EntityGuardian::class)
        actual val HOGLIN = Vanilla(EntityHoglin::class)
        actual val HUSK = Vanilla(EntityHusk::class)
        actual val MAGMA_CUBE = Vanilla(EntityMagmaCube::class)
        actual val PHANTOM = Vanilla(EntityPhantom::class)
        actual val PIGLIN_BRUTE = Vanilla(EntityPiglinBrute::class)
        actual val PILLAGER = Vanilla(EntityPillager::class)
        actual val RAVEGER = Vanilla(EntityRavager::class)
        actual val SHULKER = Vanilla(EntityShulker::class)
        actual val SILVERFISH = Vanilla(EntitySilverfish::class)
        actual val SKELETON = Vanilla(EntitySkeleton::class)
        actual val SLIME = Vanilla(EntitySlime::class)
        actual val STRAY = Vanilla(EntityStray::class)
        actual val VEX = Vanilla(EntityVex::class)
        actual val VINDICATOR = Vanilla(EntityVindicator::class)
        actual val WITCH = Vanilla(EntityWitch::class)
        actual val WITHER_SKELETON = Vanilla(EntityWitherSkeleton::class)
        actual val ZOGLIN = Vanilla(EntityZoglin::class)
        actual val ZOMBIE = Vanilla(EntityZombie::class)
        actual val ZOMBIE_VILLAGER = Vanilla(EntityZombieVillager::class)
        actual val ZOMBIE_VILLAGER_V1 = Vanilla(null)

        //-------- Boss Mobs --------//
        actual val ENDER_DRAGON = Vanilla(EntityEnderDragon::class)
        actual val WITHER = Vanilla(EntityWither::class)
    }
}
