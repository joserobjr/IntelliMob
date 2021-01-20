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

package games.joserobjr.intellimob.brain.wish

import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.EntityControls
import games.joserobjr.intellimob.control.PhysicalControl
import games.joserobjr.intellimob.coroutines.AI
import games.joserobjr.intellimob.coroutines.RestartableJob
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.EntityPos
import games.joserobjr.intellimob.math.IDoubleVectorXYZ
import games.joserobjr.intellimobjvm.collection.asMap
import games.joserobjr.intellimobjvm.collection.toEnumMap
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.selects.select
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

/**
 * @author joserobjr
 * @since 2021-01-19
 */
@ExperimentalTime
internal class Wishes(val brain: Brain) {
    private val wishByControl: Map<PhysicalControl, MutableStateFlow<ActiveWish?>> =
        PhysicalControl.values().map { it to MutableStateFlow<ActiveWish?>(null) }.toEnumMap().asMap()

    private val _wishesJob = RestartableJob()
    private val wishesJob by _wishesJob

    fun CoroutineScope.moveTo(pos: EntityPos, timeLimit: Duration? = null, sprinting: Boolean = false): Job {
        return launchWish(PhysicalControl.MOVE, WishMoveToPos(pos, sprinting), timeLimit)
    }

    fun CoroutineScope.moveTo(entity: RegularEntity, timeLimit: Duration? = null, sprinting: Boolean = false): Job {
        return launchWish(PhysicalControl.MOVE, WishMoveToEntity(entity, sprinting), timeLimit)
    }

    fun CoroutineScope.stayStill(timeLimit: Duration): Job {
        return launchWish(PhysicalControl.MOVE, WishStayStill, timeLimit)
    }

    fun CoroutineScope.lookAt(pos: EntityPos, timeLimit: Duration? = null, quickly: Boolean = false): Job {
        return launchWish(PhysicalControl.LOOK, WishLookAtPos(pos, quickly), timeLimit)
    }

    fun CoroutineScope.lookAt(entity: RegularEntity, timeLimit: Duration? = null, quickly: Boolean = false): Job {
        return launchWish(PhysicalControl.LOOK, WishLookAtEntity(entity, quickly), timeLimit)
    }

    fun CoroutineScope.lookAtDelta(delta: IDoubleVectorXYZ, timeLimit: Duration? = null, quickly: Boolean = false): Job {
        return launchWish(PhysicalControl.LOOK, WishLookAtDelta(delta, quickly), timeLimit)
    }

    fun CoroutineScope.jumpOnce(timeLimit: Duration = 3.seconds): Job {
        return launchWish(PhysicalControl.JUMP, WishJumpOnce, timeLimit)
    }

    fun CoroutineScope.jumpUntil(timeLimit: Duration = 3.seconds, endCondition: suspend EntityControls.(jumped: Boolean)->Boolean): Job {
        return launchWish(PhysicalControl.JUMP, WishJumpUntil(endCondition), timeLimit)
    }

    fun Brain.startExecuting(): Job? {
        val job = _wishesJob.startSupervisorJob(thinkingJob) ?: return null

        CoroutineScope(job + Dispatchers.AI + CoroutineName("Wishes")).launch {
            val channels: Map<PhysicalControl, ReceiveChannel<ActiveWish?>> = wishByControl.mapValues { (_, flow) ->
                val channel = Channel<ActiveWish?>()
                launch {
                    flow.collect {
                        channel.send(it)
                    }
                }
                channel
            }
            try {
                while (true) {
                    select<Unit> {
                        channels.values.forEach { channel ->
                            channel.onReceive { wish ->
                                if (wish != null) {
                                    launch(brain.owner.updateDispatcher) {
                                        wish.start()
                                    }
                                }
                            }
                        }
                    }
                }
            } finally {
                channels.values.forEach { it.cancel() }
            }
        }

        return job
    }

    private suspend fun replaceWish(control: PhysicalControl, newWish: ActiveWish) {
        val stateFlow = wishByControl[control]!!
        while (true) {
            val activeWish = stateFlow.value
            activeWish?.stop()
            if (stateFlow.compareAndSet(activeWish, newWish)) {
                break
            } else {
                yield()
            }
        }
    }

    private suspend fun replaceAndJoin(control: PhysicalControl, wish: Wish, parentJob: Job) {
        val newWish = ActiveWish(wish, parentJob)
        replaceWish(control, newWish)
        newWish.job.join()
    }

    private fun CoroutineScope.launchWish(control: PhysicalControl, wish: Wish, timeLimit: Duration?): Job {
        val parentJob = coroutineContext.job
        return launch(Dispatchers.AI) {
            if (timeLimit != null) {
                withTimeout(timeLimit.toLongMilliseconds()) {
                    replaceAndJoin(control, wish, parentJob)
                }
            } else {
                replaceAndJoin(control, wish, parentJob)
            }
        }
    }

    private inner class ActiveWish(
        val wish: Wish,
        parentJob: Job,
    ) {
        val job = SupervisorJob(parentJob)
        fun stop() {
            job.cancel()
        }

        fun start() {
            CoroutineScope(job + brain.owner.updateDispatcher).launch {
                val wishJob = with(brain.owner.controls) {
                    with(wish) {
                        start()
                    }
                }
                withContext(Dispatchers.AI) {
                    wishJob?.join()
                    job.complete()
                }
            }
        }
    }
}
