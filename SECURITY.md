<!--
SPDX-FileCopyrightText: 2026 Gauja contributors
SPDX-License-Identifier: AGPL-3.0-or-later
-->

# Security policy

## Reporting a vulnerability

Report vulnerabilities privately through GitHub's private vulnerability reporting: open the **Security** tab of this repository and choose **Report a vulnerability**. Do not open a public issue or pull request for a security problem.

You will get an acknowledgement within seven days. Fixes ship in the next release of the affected app, and the advisory is published once a fixed version is available.

## Supported versions

Only the latest released version of each app (Android and iOS are versioned independently) receives security fixes. Older versions are not patched; update to the latest release.

## What Gauja promises about your data

Gauja talks to your own Seerr server and nothing else (plus `plex.tv` during Plex sign-in and the image host your server is configured to use). It ships no telemetry, no analytics, no crash reporting, no advertising identifiers and no device fingerprinting.

Secrets (session cookies, API keys, basic-auth passwords, Plex tokens) live only in Android Keystore-backed encrypted storage or the iOS Keychain. They never appear in logs, recorded fixtures, crash output or exported diagnostics; a CI guard rejects code that formats a secret into a log call, and recorded fixtures are scanned for credentials.

TLS uses system trust by default. A self-signed server certificate is accepted only after you confirm its fingerprint for that server profile. Plain HTTP is allowed for LAN use and is flagged persistently in the app.

Deleting a server profile wipes its cookie jar, keys and caches.
