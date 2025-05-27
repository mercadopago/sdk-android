import com.mercadopago.sdk.android.AnalyticsSDKConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id(MavenConfig.MAVEN_PUBLISH)
}

publishing {
    publications {
        register<MavenPublication>(MavenConfig.RELEASE) {
            groupId = MavenConfig.GROUP_ID
            artifactId = AnalyticsSDKConfig.ARTIFACT_ID
            version = AnalyticsSDKConfig.VERSION_NAME
            afterEvaluate {
                from(components[MavenConfig.RELEASE])
            }
        }
    }
    repositories {
        maven {
            name = MavenConfig.NEXUS_NAME
            credentials {
                username = System.getenv(MavenConfig.USERNAME)
                password = System.getenv(MavenConfig.PASSWORD)
            }
            url = uri(MavenConfig.NEXUS_URL)
        }
    }
}

android {
    namespace = "com.mercadopago.sdk.android.analytics"
    compileSdk = MercadoPagoSDKConfig.compileSdk

    defaultConfig {
        minSdk = MercadoPagoSDKConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = MercadoPagoSDKConfig.sourceCompatibility
        targetCompatibility = MercadoPagoSDKConfig.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = MercadoPagoSDKConfig.jvmTarget
    }
}

dependencies {

    implementation(projects.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.converter.gson)
    implementation(libs.converter.kotlinx.serialization)

    api(libs.androidx.datastore)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.mockk)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.cashapp.turbine)
    testImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
