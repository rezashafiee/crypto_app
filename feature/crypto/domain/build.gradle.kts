import com.tilda.build.Configs
import com.tilda.build.Dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(Configs.jvmTarget)
    }
}

dependencies {

    api(project(":core:domain"))

    implementation(Dependencies.Kotlin.coroutinesCore)
    implementation(Dependencies.AndroidX.pagingCommon)

    testImplementation(Dependencies.Test.junit4)
    testImplementation(Dependencies.AndroidX.pagingTesting)
    testImplementation(Dependencies.Kotlin.coroutinesTest)
    testImplementation(Dependencies.Test.mockk)
}
