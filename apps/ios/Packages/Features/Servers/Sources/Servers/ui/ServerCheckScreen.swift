// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Common
import DesignSystem
import Model
import SwiftUI

struct ServerCheckScreen: View {
    let state: ServerCheckState
    let onEdit: (String) -> Void
    let onCheck: () -> Void
    let onCancel: () -> Void
    @Environment(\.horizontalSizeClass) private var sizeClass
    @Environment(\.colorScheme) private var colorScheme

    var body: some View {
        Form {
            Section {
                Text("Connect Gauja to your Seerr server.")
                TextField("Server address", text: Binding(get: { state.address }, set: onEdit))
                    .textContentType(.URL)
                    .textInputAutocapitalization(.never)
                    .autocorrectionDisabled()
                    .keyboardType(.URL)
                    .accessibilityIdentifier("server-address")
                if ServerAddress(state.address)?.isPlainHTTP == true {
                    Text("HTTP is unencrypted. Use it only on a trusted network.")
                        .foregroundStyle(Color.gaujaError(colorScheme))
                }
                if state.checking {
                    ProgressView("Checking server")
                    Button("Cancel", action: onCancel)
                } else {
                    Button("Check server", action: onCheck).disabled(state.address.isEmpty)
                }
                if let error = state.error {
                    Text(error.message).foregroundStyle(Color.gaujaError(colorScheme))
                        .accessibilityIdentifier("server-error")
                }
            }
            if let snapshot = state.snapshot {
                Section("Server reached") {
                    Text(snapshot.title ?? "Not reported")
                    LabeledContent("Version", value: snapshot.version ?? "Not reported")
                    Text(snapshot.compatibility.message)
                    Text(
                        snapshot.initialized == true
                            ? "Server setup is complete." : "Complete server setup in Seerr before signing in.")
                    if snapshot.restartRequired == true { Text("Seerr reports that a restart is required.") }
                    LabeledContent("Local sign-in", value: flag(snapshot.localLogin))
                    LabeledContent("Media-server sign-in", value: flag(snapshot.mediaServerLogin))
                    Text("This checks the connection. It does not save a profile or sign you in.")
                }
            }
        }
        .scrollContentBackground(.hidden)
        .background(Color.gaujaBackground(colorScheme))
        .frame(maxWidth: sizeClass == .regular ? 640 : .infinity)
        .navigationTitle("Add server")
    }

    private func flag(_ value: Bool?) -> String {
        switch value {
        case true: "Enabled"
        case false: "Disabled"
        case nil: "Not reported"
        }
    }
}

extension Compatibility {
    fileprivate var message: String {
        switch self {
        case .tested: "This Seerr release is the tested baseline."
        case .tooOld: "This server is older than the supported Seerr 3.4.1 baseline."
        case .untested: "This release has not yet been tested with Gauja."
        case .unknown: "The server version could not be verified."
        }
    }
}

extension ProbeError {
    fileprivate var message: String {
        switch self {
        case .address: "Enter a valid HTTP or HTTPS address without credentials, query or fragment."
        case .offline: "Cannot reach the server. Check your connection and address, then retry."
        case .tls: "The certificate is not trusted. Fingerprint approval will be available with profile setup."
        case .denied: "The server denied access. Basic-auth entry will be available with profile setup."
        case .redirect: "The server redirected this request. Enter its direct address instead."
        case .response: "The response is not a valid Seerr response. Check the server address."
        case .server: "Seerr could not complete the check. Retry when the server is ready."
        case .network: "The connection failed. Check the address and retry."
        }
    }
}

#Preview {
    GaujaTheme { NavigationStack { ServerCheckScreen(state: .init(), onEdit: { _ in }, onCheck: {}, onCancel: {}) } }
}
