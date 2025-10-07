import com.tilda.build.Configs
import com.tilda.build.Dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.tilda.feature.crypto.presentation"
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(Configs.jvmTarget)
    }
}

dependencies {

    implementation(project(":feature:crypto:domain"))
    implementation(project(":core:presentation"))

    implementation(Dependencies.DI.koinAndroid)

    implementation(Dependencies.AndroidX.activityCompose)
    implementation(platform(Dependencies.Compose.bom))
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiGraphics)
    implementation(Dependencies.Compose.uiToolingPreview)
    implementation(Dependencies.Compose.uiTextGoogleFonts)
    implementation(Dependencies.Compose.material3)

    implementation(Dependencies.Coil3.compose)
    implementation(Dependencies.Coil3.networkOkHttp)

    implementation(Dependencies.AndroidX.pagingCompose)

    implementation(Dependencies.Charts.vicoComposeM3)

    androidTestImplementation(platform(Dependencies.Compose.bom))
    androidTestImplementation(Dependencies.Compose.uiTestJunit4)
    debugImplementation(Dependencies.Compose.uiTooling)
    debugImplementation(Dependencies.Compose.uiTestManifest)
    testImplementation(Dependencies.Test.junit4)
    testImplementation(Dependencies.Test.truth)
    androidTestImplementation(Dependencies.AndroidX.testExtJunit)
    androidTestImplementation(Dependencies.AndroidX.espressoCore)
    androidTestImplementation(Dependencies.Test.truth)
}