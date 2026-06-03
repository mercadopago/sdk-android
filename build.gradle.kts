import io.gitlab.arturbosch.detekt.Detekt

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

tasks.withType<Detekt>().configureEach {
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
    baseline = file("$rootDir/config/detekt/baseline.xml")
    setSource(files(projectDir))
    include("**/*.kt")
    include("**/*.kts")
    exclude("resources/")
    exclude("**/build/**")
    exclude("**/example/**")
    reports {
        xml.required.set(true)
        html.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}

allprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    kover {
        reports {
            filters {
                excludes {
                    // exclusions for all report variants
                    annotatedBy(
                        "androidx.compose.ui.tooling.preview.Preview",
                        "androidx.compose.runtime.Composable",
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
                        "*Exception*",
                        "*Activity*",
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
