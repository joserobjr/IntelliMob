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

package games.joserobjr.intellimob

import com.google.inject.Inject
import games.joserobjr.intellimob.annotation.ExperimentalIntelliMobApi
import games.joserobjr.intellimob.entity.api.Fox
import games.joserobjr.intellimob.entity.impl.EntityFox
import org.cloudburstmc.server.event.Listener
import org.cloudburstmc.server.event.server.ServerInitializationEvent
import org.cloudburstmc.server.plugin.Plugin
import org.cloudburstmc.server.plugin.PluginContainer
import org.cloudburstmc.server.plugin.PluginDescription
import org.cloudburstmc.server.registry.EntityRegistry
import org.slf4j.Logger
import java.nio.file.Path

/**
 * @author joserobjr
 * @since 2021-01-11
 */
@Plugin(
    id = "intellimob",
    name = "IntelliMob",
    version = "0.1.0-SNAPSHOT",
    authors = ["joserobjr@powernukkit.org"]
)
@OptIn(ExperimentalIntelliMobApi::class)
internal class IntelliMobCloudburstPlugin @Inject constructor(
    private val description: PluginDescription,
    private val logger: Logger,
    private val dataDir: Path,
    internal val container: PluginContainer
) {
    @Listener
    internal fun onLoad(@Suppress("UNUSED_PARAMETER") event: ServerInitializationEvent) {
        with(EntityRegistry.get()) {
            register(container, Fox.TYPE, ::EntityFox, 2000, false)
        }
    }
    
    companion object {
        internal lateinit var instance: IntelliMobCloudburstPlugin
    }
}
