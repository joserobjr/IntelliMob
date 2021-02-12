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

package games.joserobjr.intellimob.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.job
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author joserobjr
 * @since 2021-01-19
 */
internal class RestartableJob: ReadOnlyProperty<Any?, Job> {
    private val _currentJob = MutableStateFlow(CompletedJob)
    val currentJob = _currentJob.asStateFlow()

    fun CoroutineScope.startSupervisorJob(): Job? {
        return startSupervisorJob(coroutineContext.job)
    }
    
    fun startSupervisorJob(parent: Job): Job? {
        val currentJob = currentJob.value
        if (!currentJob.isCompleted) {
            return null
        }
        val job = SupervisorJob(parent)
        if (!_currentJob.compareAndSet(currentJob, job)) {
            job.cancel()
            return null
        }
        return job
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): Job {
        return currentJob.value
    }
}
