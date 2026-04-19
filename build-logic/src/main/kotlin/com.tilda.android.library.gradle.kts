import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

// Shared convention for Android library modules in this project.
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    // Centralized SDK defaults keep all library modules aligned.
    compileSdk = 37

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Rules in this file are packaged and applied by apps consuming the library.
        consumerProguardFiles("consumer-rules.pro")
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
