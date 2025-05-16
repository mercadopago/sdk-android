import org.gradle.api.JavaVersion

/**
 * MercadoPagoSDKConfigs object
 * @property minSdk minimum sdk version
 * @property compileSdk compile sdk version
 * @property targetSdk target sdk version
 * @property jvmTarget jvm target version
 * @property sourceCompatibility source compatibility
 * @property targetCompatibility target compatibility
 * @property DOKKA_IGNORE_PACKAGES packages to ignore in dokka documentation
 * @property DOKKA_HTML html tag for dokka
 * @property DOKKA_DIR dokka directory
 * @property ARTIFACT_ID artifact id
 * @property versionName sdk version name
 */
object MercadoPagoSDKConfig {
    const val minSdk = 23
    const val compileSdk = 35
    const val targetSdk = 35
    const val jvmTarget = "11"
    val sourceCompatibility = JavaVersion.VERSION_11
    val targetCompatibility = JavaVersion.VERSION_11

    // Dokka
    const val DOKKA_IGNORE_PACKAGES = ".*\\.internal.*|.*\\.di.*|.*\\.analytics.*"
    const val DOKKA_HTML = "dokkaHtml"
    const val DOKKA_DIR = "dokka"

    // Maven
    const val ARTIFACT_ID = "sdk-android"

    // SDK Android
    const val versionName = "0.0.1"
}
