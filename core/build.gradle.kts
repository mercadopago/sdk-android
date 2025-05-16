import com.mercadopago.sdk.android.CoreSDKConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id(MavenConfig.MAVEN_PUBLISH)
}

publishing {
    publications {
        register<MavenPublication>(MavenConfig.RELEASE) {
            groupId = MavenConfig.GROUP_ID
            artifactId = CoreSDKConfig.ARTIFACT_ID
            version = CoreSDKConfig.VERSION_NAME
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
    namespace = "com.mercadopago.sdk.android.core"
    compileSdk = MercadoPagoSDKConfig.compileSdk

    defaultConfig {
        minSdk = MercadoPagoSDKConfig.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "SdkVersion", "\"${MercadoPagoSDKConfig.versionName}\"")
        buildConfigField("String", "MERCADO_PAGO_API_URL", "\"https://api.mercadopago.com/\"")
        buildConfigField("String", "MERCADO_LIBRE_API_URL", "\"https://api.mercadolibre.com/\"")
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
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    api(platform(libs.koin.bom))
    api(libs.koin.core)
    api(libs.koin.android)
    api(libs.squareup.retrofit)
    api(libs.okhttp)
    api(libs.logging.interceptor)
    api(libs.converter.gson)

    testImplementation(libs.junit)
    testImplementation(libs.kotlin.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
