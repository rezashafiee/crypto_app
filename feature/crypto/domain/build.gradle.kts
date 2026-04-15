plugins {
    id("com.tilda.kotlin.jvm")
}

dependencies {

    api(project(":core:domain"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.common)

    testImplementation(libs.test.junit4)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.test.mockk)
}
