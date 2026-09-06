// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.feature.servers.navigation

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.gauja.core.navigation.EntryProviderInstaller
import app.gauja.core.navigation.ServerRoute
import app.gauja.feature.servers.ui.ServerCheckScreen
import app.gauja.feature.servers.ui.ServerCheckViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object ServerEntries {
    @Provides
    @IntoSet
    fun entries(): EntryProviderInstaller = {
        entry<ServerRoute> {
            val model: ServerCheckViewModel = hiltViewModel()
            val state by model.state.collectAsStateWithLifecycle()
            val lifecycle = LocalLifecycleOwner.current.lifecycle
            DisposableEffect(lifecycle, model) {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_START -> model.foreground()
                        Lifecycle.Event.ON_STOP -> model.cancel()
                        else -> Unit
                    }
                }
                lifecycle.addObserver(observer)
                onDispose {
                    lifecycle.removeObserver(observer)
                    model.cancel()
                }
            }
            ServerCheckScreen(state, model::edit, model::check, model::cancel)
        }
    }
}
