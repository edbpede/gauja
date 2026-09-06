// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later

import com.ncorti.ktfmt.gradle.tasks.KtfmtCheckTask
import com.ncorti.ktfmt.gradle.tasks.KtfmtFormatTask
import groovy.json.JsonOutput
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktfmt)
    alias(libs.plugins.dependency.analysis)
}

ktfmt { kotlinLangStyle() }

subprojects {
    apply(plugin = "com.autonomousapps.dependency-analysis")
    dependencyLocking { lockAllConfigurations() }
}

tasks.register("exportModuleGraph") {
    val output = layout.buildDirectory.file("reports/module-graph.json")
    outputs.file(output)
    // Edges come from evaluated project configurations, not declared file inputs.
    outputs.upToDateWhen { false }
    doLast {
        val projects =
            allprojects
                .filter {
                    it.plugins.hasPlugin("org.jetbrains.kotlin.jvm") ||
                        it.plugins.hasPlugin("com.android.application") ||
                        it.plugins.hasPlugin("com.android.library")
                }
                .map { module ->
                    mapOf(
                        "name" to module.path,
                        "edges" to
                            module.configurations.flatMap { configuration ->
                                configuration.dependencies.withType<ProjectDependency>().map {
                                    dependency ->
                                    mapOf(
                                        "target" to dependency.path,
                                        "scope" to configuration.name,
                                    )
                                }
                            },
                    )
                }
        val destination = output.get().asFile
        destination.parentFile.mkdirs()
        destination.writeText(JsonOutput.prettyPrint(JsonOutput.toJson(projects)))
    }
}

allprojects { apply(from = rootProject.file("gradle/resolved-dependencies.gradle.kts")) }

tasks.named("exportResolvedDependencies") {
    dependsOn("exportModuleGraph")
    dependsOn(gradle.includedBuild("build-logic").task(":exportResolvedDependencies"))
}

allprojects {
    if (path != ":core:api") {
        apply(plugin = "io.gitlab.arturbosch.detekt")
        extensions.configure<DetektExtension> {
            config.setFrom(rootProject.file("config/detekt.yml"))
            buildUponDefaultConfig = true
            source.setFrom(file("src"))
        }
        dependencies.add("detektPlugins", rootProject.libs.compose.rules)
        tasks.withType<Detekt>().configureEach { exclude("**/generated/**", "**/build/**") }
        val advisory =
            tasks.register<Detekt>("detektAdvisory") {
                config.setFrom(rootProject.file("config/detekt-advisory.yml"))
                setSource(file("src"))
                exclude("**/generated/**", "**/build/**")
                ignoreFailures = true
            }
        tasks.named("detekt") { finalizedBy(advisory) }
    }
}

// Explicit sources also cover AGP's built-in Kotlin and the included convention build.
configurations.named("ktfmt") {
    resolutionStrategy.force("com.facebook:ktfmt:${libs.versions.ktfmt.get()}")
}

val handwrittenKotlin =
    fileTree(projectDir) {
        include("**/*.kt", "**/*.kts")
        exclude("core/api/**", "**/generated/**", "**/build/**", "**/.gradle/**")
    }
val formatSources =
    tasks.register<KtfmtFormatTask>("ktfmtFormatSources") { setSource(handwrittenKotlin) }
val checkSources =
    tasks.register<KtfmtCheckTask>("ktfmtCheckSources") { setSource(handwrittenKotlin) }

tasks.named("ktfmtFormat") { dependsOn(formatSources) }

tasks.named("ktfmtCheck") { dependsOn(checkSources) }
