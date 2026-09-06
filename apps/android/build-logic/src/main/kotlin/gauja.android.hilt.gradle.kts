// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later

plugins {
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies.add("implementation", catalog.findLibrary("hilt").get())

dependencies.add("ksp", catalog.findLibrary("hilt-compiler").get())
