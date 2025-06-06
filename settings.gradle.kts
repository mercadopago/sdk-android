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
        maven {
            url = uri("https://artifacts.mercadolibre.com/repository/android-releases")
        }
        gradlePluginPortal()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://artifacts.mercadolibre.com/repository/android-releases")
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "mercado-pago-sdk-android"
include(":example")
include(":core-methods")
include(":showkase")
include(":analytics")
include(":core")
include(":sdk-android")
include(":sdk-android-bom")
include(":components")
include(":checkout")
