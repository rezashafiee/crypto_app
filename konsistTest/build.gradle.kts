plugins {
    id("com.tilda.android.library")
}

android {
    namespace = "com.tilda.konsisttest"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    testImplementation(libs.test.junit4)
    testImplementation(libs.test.konsist)
    testImplementation(kotlin("test"))
    testImplementation(libs.androidx.lifecycle.viewmodel.android)
}