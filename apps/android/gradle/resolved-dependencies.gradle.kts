// SPDX-FileCopyrightText: 2026 Gauja contributors
// SPDX-License-Identifier: AGPL-3.0-or-later

import groovy.json.JsonOutput
import org.gradle.api.artifacts.component.ModuleComponentIdentifier

tasks.register("exportResolvedDependencies") {
    doLast {
        val dependencies = linkedSetOf<Map<String, String>>()
        val selected =
            configurations.filter {
                it.isCanBeResolved &&
                    (it.name.endsWith("classpath", ignoreCase = true) ||
                        it.name.startsWith("ksp") ||
                        it.name.startsWith("detekt") ||
                        it.name == "ktfmt")
            } + buildscript.configurations.filter { it.isCanBeResolved && it.name == "classpath" }
        selected.forEach { configuration ->
            configuration.incoming.resolutionResult.allComponents.forEach { component ->
                val id = component.id
                if (id is ModuleComponentIdentifier) {
                    val runtime =
                        rootProject.name != "build-logic" &&
                            configuration.name.contains("runtimeClasspath", ignoreCase = true) &&
                            !configuration.name.contains("test", ignoreCase = true) &&
                            !configuration.name.contains("lint", ignoreCase = true)
                    dependencies.add(
                        mapOf(
                            "group" to id.group,
                            "name" to id.module,
                            "version" to id.version,
                            "scope" to if (runtime) "runtime" else "build",
                        )
                    )
                }
            }
        }
        val output = layout.buildDirectory.file("reports/resolved-dependencies.json").get().asFile
        output.parentFile.mkdirs()
        output.writeText(JsonOutput.prettyPrint(JsonOutput.toJson(dependencies.toList())))
    }
}
