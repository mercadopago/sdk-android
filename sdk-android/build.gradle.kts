plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id(MercadoPagoSDKConfig.MAVEN_PUBLISH)
}

publishing {
    publications {
        register<MavenPublication>(MercadoPagoSDKConfig.RELEASE) {
            groupId = MercadoPagoSDKConfig.GROUP_ID
            artifactId = MercadoPagoSDKConfig.ARTIFACT_ID
            version = MercadoPagoSDKConfig.versionName
            afterEvaluate {
                from(components[MercadoPagoSDKConfig.RELEASE])
            }
        }
    }
}

android {
    namespace = "com.mercadopago.sdk.android"
    compileSdk = MercadoPagoSDKConfig.compileSdk

    defaultConfig {
        minSdk = MercadoPagoSDKConfig.minSdk
        version = MercadoPagoSDKConfig.versionName
        buildConfigField("String", "SdkVersion", "\"${MercadoPagoSDKConfig.versionName}\"")

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
    api(projects.core)
    api(projects.analytics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.okhttp.mockWebServer)
    api(libs.androidx.datastore)
    api(libs.device.sdk)

    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.kotlin.mockk)
    testImplementation(libs.cashapp.turbine)
    testImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
