import com.tilda.build.Configs
import com.tilda.build.Dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = Configs.applicationId
    compileSdk = Configs.compileSdk

    defaultConfig {
        applicationId = Configs.applicationId
        minSdk = Configs.minSdk
        targetSdk = Configs.targetSdk
        versionCode = Configs.versionCode
        versionName = Configs.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        compose = true
    }

    lint {
        // Fail the build on lint errors
        abortOnError = true

        // Treat all warnings as errors (optional but good for CI)
        warningsAsErrors = true

        // Write lint results to HTML & SARIF (for GitHub Actions integration)
        htmlReport = true
        sarifReport = true

        // Exclude generated sources (to avoid noise)
        checkGeneratedSources = false

        checkDependencies = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(Configs.jvmTarget)
    }
}

dependencies {

    implementation(project(":core:data"))
    implementation(project(":core:presentation"))
    implementation(project(":feature:crypto:domain"))
    implementation(project(":feature:crypto:presentation"))
    implementation(project(":feature:crypto:data"))

    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.lifecycleRuntimeKtx)
    implementation(Dependencies.AndroidX.activityCompose)

    implementation(platform(Dependencies.Compose.bom))
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiGraphics)
    implementation(Dependencies.Compose.uiToolingPreview)
    implementation(Dependencies.Compose.uiTextGoogleFonts)
    implementation(Dependencies.Compose.material3)
    implementation(Dependencies.Compose.material3Adaptive)
    implementation(Dependencies.Compose.material3AdaptiveLayout)
    implementation(Dependencies.Compose.material3AdaptiveNavigation)

    implementation(Dependencies.AndroidX.pagingCompose)

    implementation(Dependencies.DI.koinAndroid)
    implementation(Dependencies.DI.koinCompose)
    implementation(Dependencies.DI.koinWorkManager)

    testImplementation(Dependencies.Test.junit4)
    testImplementation(Dependencies.Test.truth)
    androidTestImplementation(Dependencies.AndroidX.testExtJunit)
    androidTestImplementation(Dependencies.AndroidX.espressoCore)
    androidTestImplementation(Dependencies.Test.truth)
}