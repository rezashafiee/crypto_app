import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

// Shared convention for Android application modules in this project.
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    // Centralized SDK defaults keep all app modules aligned.
    compileSdk = 37

    defaultConfig {
        minSdk = 28
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// Keep Kotlin bytecode target consistent with Java toolchain settings.
tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    add("lintChecks", project(":lint:checks"))
}
