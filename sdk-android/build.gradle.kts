plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.mercadopago.sdk.android"
    compileSdk = MercadoPagoSDKConfigs.compileSdk

    defaultConfig {
        minSdk = MercadoPagoSDKConfigs.minSdk
        version = MercadoPagoSDKConfigs.versionName
        buildConfigField("String", "SdkVersion", "\"${MercadoPagoSDKConfigs.versionName}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    implementation(projects.core)
    implementation(projects.analytics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.okhttp.mockWebServer)
    implementation(libs.androidx.datastore)
    implementation(libs.device.sdk)

    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.kotlin.mockk)
    testImplementation(libs.cashapp.turbine)
    testImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
