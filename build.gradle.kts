import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.detekt) apply true
    alias(libs.plugins.kotlin.kover) apply true
    alias(libs.plugins.cashapp.paparazzi) apply false
    id("org.jetbrains.dokka") version "2.0.0"
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
    exclude("**/build/**")
}

allprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    apply(plugin = "org.jetbrains.dokka")

    tasks.dokkaHtml {
        // outputDirectory.set(rootDir.resolve(".documentation"))
    }

    kover {
        reports {
            filters {
                excludes {
                    // exclusions for all report variants
                    annotatedBy(
                        "androidx.compose.ui.tooling.preview.Preview",
                        "*Generated",
                        "*Sampled",
                        "*KoverIgnore"
                    )
                    classes(
                        "*.BuildConfig",
                        "**/*$*",
                        "*Preview*Kt",
                        "*Ioelementandroid*PreviewKt$*",
                        "*Ioelementandroid*PreviewKt",
                        "*.ComposableSingletons*",
                        "*.*\$*Preview\$*",
                        "*.*\$*Sample\$*",
                        "*.*\$*KoverIgnore\$*",
                    )
                    packages(
                        "com.airbnb.android.showkase*",
                        "com.mercadopago.sdk.android.coremethods.ui.components.samples*",
                    )
                }
            }
            verify {
                rule {
                    bound {
                        minValue = 80
                    }
                }
            }
        }
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}
