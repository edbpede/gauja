// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
package app.gauja.feature.servers.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import app.gauja.core.common.ProbeError
import app.gauja.core.designsystem.GaujaTheme
import app.gauja.core.designsystem.generated.GaujaSpacing
import app.gauja.core.model.Compatibility
import app.gauja.core.model.ServerAddress
import app.gauja.feature.servers.R

@Composable
internal fun ServerCheckScreen(
    state: ServerCheckState,
    onEdit: (String) -> Unit,
    onCheck: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val wide =
        currentWindowAdaptiveInfo()
            .windowSizeClass
            .isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
    Surface(modifier = modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Column(
                modifier =
                    Modifier.widthIn(max = if (wide) 600.dp else 480.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(GaujaSpacing.xl),
                verticalArrangement = Arrangement.spacedBy(GaujaSpacing.lg),
            ) {
                Text(
                    stringResource(R.string.server_title),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(stringResource(R.string.server_intro))
                OutlinedTextField(
                    value = state.address,
                    onValueChange = onEdit,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.server_address)) },
                    singleLine = true,
                    isError = state.error == ProbeError.ADDRESS,
                )
                if (ServerAddress.parse(state.address)?.isPlainHttp == true) {
                    Text(
                        stringResource(R.string.server_http),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                if (state.checking) {
                    CircularProgressIndicator()
                    TextButton(onClick = onCancel) { Text(stringResource(R.string.server_cancel)) }
                } else {
                    Button(onClick = onCheck, enabled = state.address.isNotBlank()) {
                        Text(stringResource(R.string.server_check))
                    }
                }
                state.error?.let { error ->
                    Text(
                        stringResource(error.message()),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    )
                }
                state.snapshot?.let { snapshot ->
                    Text(
                        stringResource(R.string.server_connected),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    )
                    Text(snapshot.title ?: stringResource(R.string.server_unknown))
                    Text(
                        stringResource(
                            R.string.server_version,
                            snapshot.version ?: stringResource(R.string.server_unknown),
                        )
                    )
                    Text(
                        stringResource(
                            when (snapshot.compatibility) {
                                Compatibility.TESTED -> R.string.server_tested
                                Compatibility.TOO_OLD -> R.string.server_old
                                Compatibility.UNTESTED -> R.string.server_untested
                                Compatibility.UNKNOWN -> R.string.server_unknown_version
                            }
                        )
                    )
                    Text(
                        stringResource(
                            if (snapshot.initialized == true) R.string.server_initialized
                            else R.string.server_not_initialized
                        )
                    )
                    if (snapshot.restartRequired == true)
                        Text(stringResource(R.string.server_restart))
                    Text(stringResource(R.string.server_local, flag(snapshot.localLogin)))
                    Text(
                        stringResource(R.string.server_media_login, flag(snapshot.mediaServerLogin))
                    )
                    Text(stringResource(R.string.server_no_session))
                }
            }
        }
    }
}

@Composable
private fun flag(value: Boolean?): String =
    stringResource(
        when (value) {
            true -> R.string.server_enabled
            false -> R.string.server_disabled
            null -> R.string.server_unknown
        }
    )

private fun ProbeError.message(): Int =
    when (this) {
        ProbeError.ADDRESS -> R.string.server_invalid
        ProbeError.OFFLINE -> R.string.server_offline
        ProbeError.TLS -> R.string.server_tls
        ProbeError.DENIED -> R.string.server_denied
        ProbeError.REDIRECT -> R.string.server_redirect
        ProbeError.RESPONSE -> R.string.server_response
        ProbeError.SERVER -> R.string.server_failure
        ProbeError.NETWORK -> R.string.server_network
    }

@Preview
@PreviewScreenSizes
@Composable
private fun ServerCheckPreview() {
    GaujaTheme { ServerCheckScreen(ServerCheckState(), {}, {}, {}) }
}
