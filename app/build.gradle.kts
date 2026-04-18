plugins {
    id("com.tilda.android.application")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.tilda.crypto"

    defaultConfig {
        applicationId = "com.tilda.crypto"
        versionCode = 1
        versionName = "1.0"
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

dependencies {

    implementation(project(":core:data"))
    implementation(project(":core:presentation"))
    implementation(project(":feature:crypto:domain"))
    implementation(project(":feature:crypto:presentation"))
    implementation(project(":feature:crypto:data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui.text.google.fonts)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.adaptive)
    implementation(libs.compose.material3.adaptive.layout)
    implementation(libs.compose.material3.adaptive.navigation)

    implementation(libs.androidx.paging.compose)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.workmanager)
    implementation(libs.timber)

    debugImplementation(libs.performance.leakcanary)

    testImplementation(libs.test.junit4)
    testImplementation(libs.test.truth)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.test.truth)
}
