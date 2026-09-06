// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
import Common
import Data
import Dependencies
import Model
import Observation

struct ServerCheckState {
    var address = ""
    var checking = false
    var snapshot: ServerSnapshot?
    var error: ProbeError?
}

@Observable
final class ServerCheckModel {
    private(set) var state = ServerCheckState()
    @ObservationIgnored @Dependency(\.serverProbe) private var probe
    @ObservationIgnored private var request: Task<Void, Never>?
    @ObservationIgnored private var generation = 0

    func edit(_ address: String) {
        cancel()
        state = ServerCheckState(address: address)
    }

    func check() {
        guard !state.checking else { return }
        guard let address = ServerAddress(state.address) else {
            state.error = .address
            return
        }
        generation += 1
        let identity = generation
        state.checking = true
        state.error = nil
        request = Task {
            do {
                let snapshot = try await probe.check(address)
                guard identity == generation, !Task.isCancelled else { return }
                state.snapshot = snapshot
                state.checking = false
            } catch {
                guard identity == generation, !Task.isCancelled else { return }
                state.error = error as? ProbeError ?? .response
                state.checking = false
            }
        }
    }

    func foreground() { if state.snapshot != nil { check() } }

    func cancel() {
        generation += 1
        request?.cancel()
        request = nil
        state.checking = false
    }
}
