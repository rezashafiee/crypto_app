pluginManagement {
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
include(":konsistTest")
