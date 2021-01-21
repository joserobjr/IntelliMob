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

import games.joserobjr.intellimob.math.BlockPos
import games.joserobjr.intellimob.math.IBlockPos
import games.joserobjr.intellimob.world.WorldView
import kotlin.random.Random

/**
 * @author joserobjr
 * @since 2021-01-20
 */
internal class StraightLinePathFinder: PathFinder {
    override suspend fun findPath(world: WorldView, from: IBlockPos, to: IBlockPos): Path {
        return Path(PathNode(to))
    }

    override suspend fun findTargetWith(settings: TargetSearchSettings): BlockPos? {
        val dx = Random.nextInt(-settings.maxHorizontalDistance, settings.maxHorizontalDistance)
        val dz = Random.nextInt(-settings.maxHorizontalDistance, settings.maxHorizontalDistance)
        return settings.from.position.run { copy(x = x + dx, z = z + dz) }.toBlockPos()
    }
}
