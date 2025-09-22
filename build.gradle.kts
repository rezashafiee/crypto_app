// Top-level build file where you can add configuration options common to all sub-projects/modules.

import java.util.Locale

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.devtools.ksp") version "2.2.10-2.0.2" apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.ktlint)
}

tasks.register<Exec>("konsistCheck") {
    group = "verification"
    description = "Runs Konsist static code analysis"

    val gradlew =
        if (System.getProperty("os.name").lowercase(Locale.getDefault()).contains("windows")) {
            project.rootProject.file("gradlew.bat")
        } else {
            project.rootProject.file("gradlew")
        }

    commandLine(gradlew.absolutePath, "konsistTest:test", "--rerun-tasks")
    standardOutput = System.out
    errorOutput = System.err
}
