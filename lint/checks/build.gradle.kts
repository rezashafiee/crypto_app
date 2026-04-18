plugins {
    id("com.tilda.kotlin.jvm")
}

dependencies {
    compileOnly(libs.lint.api)

    testImplementation(libs.lint.api)
    testImplementation(libs.test.junit4)
    testImplementation(libs.test.truth)
    testImplementation(libs.lint.tests)
}
