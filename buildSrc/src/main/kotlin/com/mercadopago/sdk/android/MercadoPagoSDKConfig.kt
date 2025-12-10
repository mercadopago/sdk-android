import org.gradle.api.JavaVersion

/**
 * MercadoPagoSDKConfigs object
 * @property MIN_SDK minimum sdk version
 * @property COMPILE_SDK compile sdk version
 * @property TARGET_SDK target sdk version
 * @property JVM_TARGET jvm target version
 * @property sourceCompatibility source compatibility
 * @property targetCompatibility target compatibility
 * @property DOKKA_IGNORE_PACKAGES packages to ignore in dokka documentation
 * @property DOKKA_HTML html tag for dokka
 * @property DOKKA_DIR dokka directory
 * @property ARTIFACT_ID artifact id
 * @property VERSION_NAME sdk version name
 */
object MercadoPagoSDKConfig {
    const val MIN_SDK = 23
    const val COMPILE_SDK = 35
    const val TARGET_SDK = 35
    const val JVM_TARGET = "11"
    val sourceCompatibility = JavaVersion.VERSION_11
    val targetCompatibility = JavaVersion.VERSION_11

    // Dokka
    const val DOKKA_IGNORE_PACKAGES = ".*\\.internal.*|.*\\.di.*|.*\\.analytics.*"
    const val DOKKA_HTML = "dokkaHtml"
    const val DOKKA_DIR = "dokka"

    // Maven
    const val ARTIFACT_ID = "sdk-android"

    // SDK Android
    const val VERSION_NAME = "0.1.3"
}
