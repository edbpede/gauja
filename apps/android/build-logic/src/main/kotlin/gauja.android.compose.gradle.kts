// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later

import com.android.build.api.dsl.CommonExtension

plugins { id("org.jetbrains.kotlin.plugin.compose") }

extensions.configure<CommonExtension> { buildFeatures.compose = true }

val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies.add("implementation", dependencies.platform(catalog.findLibrary("compose-bom").get()))
