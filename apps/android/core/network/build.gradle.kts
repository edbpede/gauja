// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
plugins { id("gauja.jvm") }

dependencies {
    implementation(libs.inject)
    implementation(libs.okhttp)
    implementation(project(":core:model"))
    testImplementation(libs.junit)
    testImplementation(libs.okhttp.mockwebserver)
}
