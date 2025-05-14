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
 * @property NEXUS_NAME nexus repository reference name
 * @property NEXUS_URL nexus repository url
 * @property RELEASE release
 * @property MAVEN_PUBLISH maven publish
 * @property GROUP_ID group id
 * @property ARTIFACT_ID artifact id
 */
object MercadoPagoSDKConfig {
    const val minSdk = 23
    const val compileSdk = 35
    const val targetSdk = 35
    const val jvmTarget = "11"
    val sourceCompatibility = JavaVersion.VERSION_11
    val targetCompatibility = JavaVersion.VERSION_11

    // SDK Android
    const val versionName = "0.0.1"
    const val NEXUS_NAME = "sdkAndroid"
    const val NEXUS_URL = "https://artifacts.mercadolibre.com/repository/android-releases/"
    const val RELEASE = "release"
    const val MAVEN_PUBLISH = "maven-publish"
    const val GROUP_ID = "com.mercadopago.android.sdk"
    const val ARTIFACT_ID = "sdk-android"
}
