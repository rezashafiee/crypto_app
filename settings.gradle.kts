pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Crypto"
include(":app")
include(":core:data")
include(":core:presentation")
include(":core:domain")
include(":feature:crypto:presentation")
include(":feature:crypto:data")
include(":feature:crypto:domain")
include(":feature:news:presentation")
include(":feature:news:data")
include(":feature:news:domain")
include(":konsistTest")
