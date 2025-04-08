import org.gradle.api.JavaVersion

/**
 * MercadoPagoSDKConfigs object
 * @property minSdk minimum sdk version
 * @property compileSdk compile sdk version
 * @property targetSdk target sdk version
 * @property jvmTarget jvm target version
 * @property sourceCompatibility source compatibility
 * @property targetCompatibility target compatibility
 * @property versionName sdk version name
 */
object MercadoPagoSDKConfigs {
    const val minSdk = 23
    const val compileSdk = 35
    const val targetSdk = 35
    const val jvmTarget = "11"
    val sourceCompatibility = JavaVersion.VERSION_11
    val targetCompatibility = JavaVersion.VERSION_11

    // SDK Android
    const val versionName = "0.0.1"
}
