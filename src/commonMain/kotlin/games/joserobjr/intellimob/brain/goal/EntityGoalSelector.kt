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

package games.joserobjr.intellimob.brain.goal

import com.github.michaelbull.logging.InlineLogger
import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.api.PhysicalControl
import games.joserobjr.intellimob.coroutines.AI
import games.joserobjr.intellimob.coroutines.RestartableJob
import games.joserobjr.intellimob.math.ticks
import games.joserobjr.intellimobjvm.atomic.AtomicRef
import games.joserobjr.intellimobjvm.atomic.atomic
import games.joserobjr.intellimobjvm.atomic.getValue
import games.joserobjr.intellimobjvm.atomic.update
import games.joserobjr.intellimobjvm.collection.mutableSortedSetOf
import kotlinx.coroutines.*
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
import kotlin.time.measureTime
import kotlin.time.seconds

/**
 * @author joserobjr
 * @since 2021-01-18
 */
internal class EntityGoalSelector(
    private val brain: Brain, 
    val type: EntityGoalSelectorType = EntityGoalSelectorType.UNKNOWN
) {
    private val _goals = atomic(setOf<AddedGoal>())
    private val goals by _goals
    private val _selectingJob = RestartableJob()
    val selectingJob by _selectingJob
    
    @OptIn(ExperimentalTime::class)
    fun Brain.startSelecting(): Job? {
        val job = _selectingJob.startSupervisorJob(thinkingJob) ?: return null
        return CoroutineScope(job + Dispatchers.AI + CoroutineName("Goal Selector Type $type")).launch {
            val activeGoals = mutableSortedSetOf<AddedGoal>()
            var lastGoalsObj = goals
            while (true) {
                val updateTime = measureTime {
                    if (activeGoals.isEmpty() && goals.isEmpty()) {
                        delay(5.seconds)
                        return@measureTime
                    }
                    val goals = goals
                    if (goals !== lastGoalsObj) {
                        val removedGoals = activeGoals - goals
                        removedGoals.forEach { it.stop() }
                        activeGoals -= removedGoals
                        lastGoalsObj = goals
                    }
                    activeGoals.removeAll { it.activeJob?.isActive != true }

                    goals.forEach { goal ->
                        if (goal.canStart()) {
                            with(goal) {
                                start()
                                activeGoals += this
                            }
                        }
                    }
                }
                delay(ONE_TICK - updateTime)
            }
        }
    }

    fun add(goal: Goal): Boolean = add(goal, goal.defaultPriority)
    
    operator fun plusAssign(goal: Goal) {
        add(goal)
    }
    
    @OptIn(ExperimentalTime::class)
    fun add(goal: Goal, priority: Int): Boolean {
        val hasTarget = PhysicalControl.TARGET in goal.physicalControl
        val isAttack = type == EntityGoalSelectorType.ATTACK
        if (hasTarget != isAttack && type != EntityGoalSelectorType.UNKNOWN) {
            return false
        }
        if (!goal.canBeAddedTo(brain, this)) {
            return false
        }
        
        val added = AddedGoal(this, goal, goal.adjustPriority(brain, this, priority))
        _goals.update { it + added }
        goal.addedTo(brain, this)
        return true
    }

    @OptIn(ExperimentalTime::class)
    private class AddedGoal(
        val selector: EntityGoalSelector,
        val goal: Goal,
        val priority: Int
    ): Comparable<AddedGoal> {
        private val _activeJob = AtomicRef<Job?>(null)
        val activeJob by _activeJob
        val brain get() = selector.brain
        val memory = if (goal.needsMemory) GoalMemory() else null
        
        fun CoroutineScope.start() {
            _activeJob.update { old->
                launch {
                    if (old != null) {
                        log.info { "X Cancelling old goal" }
                        old.cancelAndJoin()
                    }
                    with(goal) {
                        val name = "${goal::class.simpleName}"
                        val time = TimeSource.Monotonic.markNow()
                        try {
                            log.info { "+ Starting $name" }
                            val job = start(brain, memory)
                            if (job == null) {
                                log.info { "@ Goal $name didn't create a job!" }
                            } else {
                                job.join()
                            }
                        } finally {
                            log.info { "- Completed $name in ${time.elapsedNow()}" }
                        }
                    }
                }
            }
        }
        
        suspend fun stop() {
            activeJob?.cancelAndJoin()
        }
        
        suspend fun canStart(): Boolean {
            if (activeJob?.isActive == true) {
                return false
            }
            return goal.canStart(brain, memory)
        }

        override fun compareTo(other: AddedGoal): Int {
            if (this == other) {
                return 0
            }
            // Lowest priority wins
            (other.priority - priority).takeUnless { it == 0 }?.let { return it }
            
            // Highest weight wins
            val weight = goal.physicalControl.sumBy { it.ordinal }
            val otherWeight = other.goal.physicalControl.sumBy { it.ordinal + 1 }
            (weight - otherWeight).takeUnless { it == 0 }?.let { return it }
            
            // More controls wins
            val controls = goal.physicalControl.size
            val otherControls = goal.physicalControl.size
            (controls - otherControls).takeUnless { it == 0 }?.let { return it }
            
            // The one with the most important goal wins
            val importance = goal.physicalControl.maxOf { it.ordinal }
            val otherImportance = goal.physicalControl.maxOf { it.ordinal }
            (importance - otherImportance).takeUnless { it == 0 }?.let { return it }

            // Group by brain
            selector.brain.hashCode().compareTo(other.selector.brain.hashCode()).takeUnless {  it == 0 }?.let { return it }
            
            // Group by selectors
            selector.hashCode().compareTo(other.selector.hashCode()).takeUnless {  it == 0 }?.let { return it }
            
            // Group by repeated goal
            goal.hashCode().compareTo(other.goal.hashCode()).takeUnless {  it == 0 }?.let { return it }

            // Nothing more to compare, give up and use the hash code with hope that they won't return be the same
            return hashCode().compareTo(other.hashCode())
        }
    }

    companion object {
        @ExperimentalTime
        private val ONE_TICK = 1.ticks
        private val log = InlineLogger()
    }
}
