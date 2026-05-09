plugins {
    id("com.tilda.android.library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.tilda.feature.news.data"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":feature:news:domain"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.retrofit.retrofit)
    implementation(libs.moshi.moshi)
    ksp(libs.moshi.codegen)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
}
