// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
plugins {
    id("gauja.jvm")
    alias(libs.plugins.kotlin.serialization)
}
dependencies {
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.serialization)
    testImplementation(libs.junit)
}
