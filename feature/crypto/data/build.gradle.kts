import java.util.Properties

plugins {
    id("com.tilda.android.library")
    alias(libs.plugins.ksp)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("external.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    namespace = "com.tilda.feature.crypto.data"
    defaultConfig {
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
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(project(":core:data"))
    implementation(project(":feature:crypto:domain"))

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.koin.android)
    implementation(libs.retrofit.retrofit)
    implementation(libs.moshi.moshi)
    implementation(libs.androidx.room.runtime)
    ksp(libs.moshi.codegen)

    implementation(libs.androidx.paging.runtime)

    testImplementation(libs.test.junit4)
    testImplementation(libs.test.truth)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.test.mockk)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.test.truth)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}