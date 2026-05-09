plugins {
    id("com.tilda.kotlin.jvm")
}

dependencies {
    api(project(":core:domain"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.common)
}
