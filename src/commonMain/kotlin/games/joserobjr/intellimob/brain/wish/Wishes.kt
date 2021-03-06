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

import com.github.michaelbull.logging.InlineLogger
import games.joserobjr.intellimob.brain.Brain
import games.joserobjr.intellimob.control.api.EntityControls
import games.joserobjr.intellimob.control.api.PhysicalControl
import games.joserobjr.intellimob.coroutines.AI
import games.joserobjr.intellimob.coroutines.RestartableJob
import games.joserobjr.intellimob.entity.RegularEntity
import games.joserobjr.intellimob.math.generic.IDoubleVectorXYZ
import games.joserobjr.intellimob.math.motion.IHorizontalVelocity
import games.joserobjr.intellimob.math.position.entity.EntityPos
import games.joserobjr.intellimob.trait.WithEntityPos
import games.joserobjr.intellimobjvm.collection.asMap
import games.joserobjr.intellimobjvm.collection.toEnumMap
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource
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

    fun CoroutineScope.moveTo(pos: EntityPos, timeLimit: Duration? = null, sprinting: Boolean = false, speedMultiplier: IHorizontalVelocity? = null): Job {
        return launchWish(PhysicalControl.MOVE, WishMoveToPos(pos, sprinting, speedMultiplier), timeLimit)
    }

    fun CoroutineScope.moveTo(entity: WithEntityPos, timeLimit: Duration? = null, sprinting: Boolean = false, speedMultiplier: IHorizontalVelocity? = null): Job {
        return launchWish(PhysicalControl.MOVE, WishMoveToEntity(entity, sprinting, speedMultiplier), timeLimit)
    }

    fun CoroutineScope.stayStill(timeLimit: Duration): Job {
        return launchWish(PhysicalControl.MOVE, WishStayStill, timeLimit)
    }

    fun CoroutineScope.lookAt(pos: WithEntityPos, timeLimit: Duration? = null, quickly: Boolean = false): Job {
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
                                        with(wish) {
                                            start()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: CancellationException) {
                if ("The entity despawned" != e.message) {
                    log.warn(e) { "An unexpected cancellation happened while executing the wishes" }
                }
                throw e
            } catch (e: Throwable) {
                log.error(e) { "An uncaught exception happened while executing the wishes" }
                throw e
            } finally {
                channels.values.forEach { it.cancel() }
                coroutineContext.cancelChildren()
                log.info { "Wish execution stopped" }
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

    private suspend fun replaceAndJoin(control: PhysicalControl, wish: Wish) {
        val newWish = ActiveWish(wish, coroutineContext.job)
        replaceWish(control, newWish)
        val name = "${wish::class.simpleName}"
        val start = TimeSource.Monotonic.markNow()
        try {
            log.info { "> Wish $name started" }
            newWish.job.first().join()
        } finally {
            val took = start.elapsedNow()
            log.info { "< Wish $name completed. Took $took" }
        }
    }

    private fun CoroutineScope.launchWish(control: PhysicalControl, wish: Wish, timeLimit: Duration?): Job {
        return launch(Dispatchers.AI) {
            try {
                if (timeLimit != null) {
                    try {
                        withTimeout(timeLimit.toLongMilliseconds()) {
                            replaceAndJoin(control, wish)
                        }
                    } catch (e: TimeoutCancellationException) {
                        coroutineContext.cancelChildren(e)
                    }
                } else {
                    replaceAndJoin(control, wish)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                log.warn(e) { "An exception occurred while executing the wish ${wish::class}" }
                cancel("An exception has occurred", e)
                throw e
            }
        }
    }

    private inner class ActiveWish(
        val wish: Wish,
        val parentJob: Job,
    ) {
        private val _job = MutableStateFlow<Job?>(null)
        val job = _job.filterNotNull().take(1) 
        suspend fun stop() {
            job.first().cancelAndJoin()
        }

        fun CoroutineScope.start() {
            val job = launch(parentJob + Dispatchers.AI) {
                val wishJob = with(brain.owner.controls) {
                    with(wish) {
                        start()
                    }
                }
                wishJob?.join()
            }
            repeat(100000) {
                if (_job.tryEmit(job)) {
                    return
                }
            }
            throw IllegalStateException("Could not emit the job to the state flow")
        }
    }
    
    companion object {
        private val log = InlineLogger()
    }
}

