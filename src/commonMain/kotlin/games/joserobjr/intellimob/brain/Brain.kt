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

package games.joserobjr.intellimob.brain

import games.joserobjr.intellimob.brain.goal.EntityGoalSelector
import games.joserobjr.intellimob.brain.goal.EntityGoalSelectorType
import games.joserobjr.intellimob.brain.wish.Wishes
import games.joserobjr.intellimob.coroutines.RestartableJob
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.trait.WithTimeSource
import kotlinx.coroutines.cancel
import kotlin.time.ExperimentalTime

/**
 * @author joserobjr
 * @since 2021-01-11
 */
internal class Brain(
    val owner: RegularEntity,
): WithTimeSource by owner {
    private val _thinkingJob = RestartableJob()
    val thinkingJob by _thinkingJob
    
    @ExperimentalTime
    val wishes: Wishes = Wishes(this)
    val normalGoals: EntityGoalSelector = EntityGoalSelector(this, EntityGoalSelectorType.NORMAL)
    val attackGoals: EntityGoalSelector = EntityGoalSelector(this, EntityGoalSelectorType.ATTACK)
    
    @OptIn(ExperimentalTime::class)
    fun startThinking() {
        val job = _thinkingJob.startSupervisorJob(owner.job) ?: return
        try {
            with(wishes) {
                checkNotNull(startExecuting()) { "Could not start the wish execution job" }
            }
            with(normalGoals) {
                checkNotNull(startSelecting()) { "Could not start the normal goals selector" }
            }
            with(attackGoals) {
                checkNotNull(startSelecting()) { "Could not start the attack goals selector" }
            }
        } catch (e: Throwable) {
            job.cancel("Some thinking jobs has failed to start", e)
            throw e
        }
    }
}
