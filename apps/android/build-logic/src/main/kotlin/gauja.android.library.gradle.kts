// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later
plugins { id("com.android.library") }

android {
    namespace = "app.gauja." + project.path.removePrefix(":").replace(':', '.')
    compileSdk = 37
    defaultConfig { minSdk = 30 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint { lintConfig = rootProject.file("config/lint.xml") }
    testOptions { unitTests.isIncludeAndroidResources = true }
}

androidComponents.beforeVariants { it.enableAndroidTest = false }
