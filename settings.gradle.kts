val secretsFile = file("secrets.properties")
val secrets = java.util.Properties().apply {
    if (secretsFile.exists()) {
        secretsFile.inputStream().use { load(it) }
    }
}

private fun readSecret(name: String): String? {
    val fromProps: String? = secrets.getProperty(name)
    val fromSysProp: String? = System.getProperty(name)
    val fromEnv: String? = System.getenv(name)
    return (fromProps ?: fromSysProp ?: fromEnv)
        ?.trim()
        ?.removeSurrounding("\"")
        ?.removeSurrounding("'")
}

val furyMavenRepoUrl: String? = readSecret("FURY_MAVEN_REPO_URL")

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
        if (furyMavenRepoUrl != null) {
            maven { url = uri(furyMavenRepoUrl) }
        }
        maven { url = uri("https://artifacts.mercadolibre.com/repository/android-releases") }
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
include(":foundation")
include(":components")
include(":checkout")
include(":mp-extended")
