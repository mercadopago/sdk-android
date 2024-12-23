import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.detekt) apply true
}

tasks.withType(Detekt::class).configureEach {
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    baseline = file("$rootDir/config/detekt/baseline.xml")
    setSource(files(projectDir))
    include("**/*.kt")
    include("**/*.kts")
    exclude("resources/")
    exclude("build/")
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}
