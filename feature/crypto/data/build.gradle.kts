import com.tilda.build.Configs
import com.tilda.build.Dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("external.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    namespace = "com.tilda.feature.crypto.data"
    compileSdk = Configs.compileSdk

    defaultConfig {
        minSdk = Configs.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "API_KEY",
            "\"${localProperties.getProperty("API_KEY")}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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

    implementation(project(":core:data"))
    implementation(project(":feature:crypto:domain"))

    implementation(Dependencies.AndroidX.workRuntimeKtx)
    implementation(Dependencies.DI.koinAndroid)
    implementation(Dependencies.Ktor.json)
    implementation(Dependencies.AndroidX.roomRuntime)

    implementation(Dependencies.AndroidX.pagingRuntime)

    testImplementation(Dependencies.Test.junit4)
    testImplementation(Dependencies.Test.truth)
    testImplementation(Dependencies.Kotlin.coroutinesTest)
    testImplementation(Dependencies.Test.mockk)
    androidTestImplementation(Dependencies.AndroidX.testExtJunit)
    androidTestImplementation(Dependencies.AndroidX.espressoCore)
    androidTestImplementation(Dependencies.Test.truth)
    androidTestImplementation(Dependencies.Kotlin.coroutinesTest)
}