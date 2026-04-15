import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

// Shared convention for pure Kotlin/JVM modules (non-Android).
plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    // Use one Java baseline across all JVM modules.
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

// Keep Kotlin bytecode target consistent with Java toolchain settings.
tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}
