package com.tilda.build

object Dependencies {
    object Kotlin {
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    }

    object AndroidX {
        // Core & lifecycle
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val lifecycleRuntimeKtx =
            "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
        const val lifecycleViewModelAndroid =
            "androidx.lifecycle:lifecycle-viewmodel-android:${Versions.lifecycle}"

        // Activity/Window/Work
        const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
        const val window = "androidx.window:window:${Versions.window}"
        const val workRuntimeKtx = "androidx.work:work-runtime-ktx:${Versions.work}"

        // Paging
        const val pagingRuntime = "androidx.paging:paging-runtime:${Versions.paging}"
        const val pagingCommon = "androidx.paging:paging-common:${Versions.paging}"
        const val pagingCompose = "androidx.paging:paging-compose:${Versions.paging}"
        const val pagingTesting = "androidx.paging:paging-testing:${Versions.paging}"

        // Room
        const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
        const val roomPaging = "androidx.room:room-paging:${Versions.room}"
        const val roomTesting = "androidx.room:room-testing:${Versions.room}"

        // AndroidX test
        const val testExtJunit = "androidx.test.ext:junit:${Versions.androidXTestJunit}"
        const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    }

    object Compose {
        const val bom = "androidx.compose:compose-bom:${Versions.composeBom}"
        const val ui = "androidx.compose.ui:ui"
        const val uiGraphics = "androidx.compose.ui:ui-graphics"
        const val uiTooling = "androidx.compose.ui:ui-tooling"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
        const val uiTextGoogleFonts =
            "androidx.compose.ui:ui-text-google-fonts:${Versions.composeUiTextGoogleFonts}"
        const val material3 = "androidx.compose.material3:material3"
        const val material3Adaptive = "androidx.compose.material3.adaptive:adaptive"
        const val material3AdaptiveLayout = "androidx.compose.material3.adaptive:adaptive-layout"
        const val material3AdaptiveNavigation =
            "androidx.compose.material3.adaptive:adaptive-navigation"
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest"
        const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4"
    }

    object DI {
        const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
        const val koinCompose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
        const val koinWorkManager = "io.insert-koin:koin-androidx-workmanager:${Versions.koin}"
    }

    object Ktor {
        const val core = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val cio = "io.ktor:ktor-client-cio:${Versions.ktor}"
        const val logging = "io.ktor:ktor-client-logging:${Versions.ktor}"
        const val negotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
        const val json = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
    }

    object Coil3 {
        const val compose = "io.coil-kt.coil3:coil-compose:${Versions.coil3}"
        const val networkOkHttp = "io.coil-kt.coil3:coil-network-okhttp:${Versions.coil3}"
    }

    object Charts {
        const val vicoComposeM3 = "com.patrykandpatrick.vico:compose-m3:${Versions.vico}"
    }

    object Test {
        const val junit4 = "junit:junit:${Versions.junit4}"
        const val truth = "com.google.truth:truth:${Versions.truth}"
        const val mockk = "io.mockk:mockk:${Versions.mockk}"
        const val konsist = "com.lemonappdev:konsist:${Versions.konsist}"
    }
}
