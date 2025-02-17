import org.gradle.api.JavaVersion

object MercadoPagoSDKConfigs {
    const val minSdk = 21
    const val compileSdk = 35
    const val targetSdk = 35
    const val jvmTarget = "11"
    val sourceCompatibility = JavaVersion.VERSION_11
    val targetCompatibility = JavaVersion.VERSION_11

    // SDK Android
    const val versionName = "0.0.1"
}
