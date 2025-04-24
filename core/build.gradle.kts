plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.mercadopago.sdk.android.core"
    compileSdk = MercadoPagoSDKConfigs.compileSdk

    defaultConfig {
        minSdk = MercadoPagoSDKConfigs.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "SdkVersion", "\"${MercadoPagoSDKConfigs.versionName}\"")
        buildConfigField("String", "MERCADO_PAGO_API_URL", "\"https://api.mercadopago.com/\"")
        buildConfigField("String", "MERCADO_LIBRE_API_URL", "\"https://api.mercadolibre.com/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = MercadoPagoSDKConfigs.sourceCompatibility
        targetCompatibility = MercadoPagoSDKConfigs.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = MercadoPagoSDKConfigs.jvmTarget
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
