/**
 * MavenConfig object
 * @property GROUP_ID group id
 * @property NEXUS_NAME nexus repository reference name
 * @property NEXUS_URL nexus repository url
 * @property RELEASE release
 * @property MAVEN_PUBLISH maven publish
 * @property USERNAME maven username
 * @property PASSWORD maven password
 */
object MavenConfig {

    // Maven
    const val GROUP_ID = "com.mercadopago.android.sdk"
    const val RELEASE = "release"
    const val MAVEN_PUBLISH = "maven-publish"
    const val NEXUS_NAME = "sdkAndroid"
    const val NEXUS_URL = "https://artifacts.mercadolibre.com/repository/android-releases/"
    const val USERNAME = "MAVEN_USERNAME"
    const val PASSWORD = "MAVEN_PASSWORD"
}
