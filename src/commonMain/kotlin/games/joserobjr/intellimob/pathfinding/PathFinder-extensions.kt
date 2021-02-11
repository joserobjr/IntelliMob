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

package games.joserobjr.intellimob.pathfinding

import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.position.block.BlockPos
import games.joserobjr.intellimob.math.position.block.IBlockPos
import games.joserobjr.intellimob.math.position.entity.IEntityPos
import games.joserobjr.intellimob.world.WorldView

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal suspend fun PathFinder.findPath(world: WorldView, from: IEntityPos, to: IEntityPos): Path = findPath(world, from.toBlockPos(), to.toBlockPos())
internal suspend fun PathFinder.findPath(world: WorldView, from: IEntityPos, to: IBlockPos): Path = findPath(world, from.toBlockPos(), to)
internal suspend fun PathFinder.findPath(world: WorldView, from: IBlockPos, to: IEntityPos): Path = findPath(world, from, to.toBlockPos())

internal suspend inline fun PathFinder.findTarget(
    owner: RegularEntity,
    maxHorizontalDistance: Int,
    maxVerticalDistance: Int,
    configurer: TargetSearchSettings.() -> Unit = {}
): BlockPos? {
    val settings = TargetSearchSettings(owner, maxHorizontalDistance, maxVerticalDistance)
    settings.configurer()
    return findTargetWith(settings)
}
