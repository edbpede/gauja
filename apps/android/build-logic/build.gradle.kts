// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
plugins { `kotlin-dsl` }

kotlin { jvmToolchain(17) }

dependencies {
    implementation(libs.agp)
    implementation(libs.kotlin.gradle)
    implementation(libs.kotlin.compose.gradle)
    implementation(libs.hilt.gradle)
    implementation(libs.ksp.gradle)
}

dependencyLocking { lockAllConfigurations() }

apply(from = "../gradle/resolved-dependencies.gradle.kts")
