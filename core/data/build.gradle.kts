import com.tilda.build.Configs
import com.tilda.build.Dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.tilda.core.data"
    compileSdk = Configs.compileSdk

    defaultConfig {
        minSdk = Configs.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://data-api.coindesk.com/\""
            )
        }
        debug {
            buildConfigField(
                "String",
                "BASE_URL",
                "\"https://data-api.coindesk.com/\""
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(Configs.jvmTarget)
    }
}

dependencies {

    implementation(project(":core:domain"))

    api(Dependencies.Ktor.core)
    implementation(Dependencies.Ktor.cio)
    implementation(Dependencies.Ktor.logging)
    implementation(Dependencies.Ktor.negotiation)
    implementation(Dependencies.Ktor.json)

    implementation(Dependencies.AndroidX.roomRuntime)
    ksp(Dependencies.AndroidX.roomCompiler)
    implementation(Dependencies.AndroidX.roomKtx)
    implementation(Dependencies.AndroidX.roomPaging)

    implementation(Dependencies.DI.koinAndroid)

    testImplementation(Dependencies.Test.junit4)
    testImplementation(Dependencies.Test.truth)
    testImplementation(Dependencies.AndroidX.roomTesting)
    androidTestImplementation(Dependencies.AndroidX.testExtJunit)
    androidTestImplementation(Dependencies.AndroidX.espressoCore)
    androidTestImplementation(Dependencies.Test.truth)
}