plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.klint)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.mercadopago.sdk.android.coremethods"
    compileSdk = MercadoPagoSDKConfigs.compileSdk

    defaultConfig {
        minSdk = MercadoPagoSDKConfigs.minSdk
        version = CoreMethodsSDKConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = MercadoPagoSDKConfigs.sourceCompatibility
        targetCompatibility = MercadoPagoSDKConfigs.targetCompatibility
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    buildFeatures {
        compose = true
    }
    kotlinOptions {
        jvmTarget = MercadoPagoSDKConfigs.jvmTarget
    }
}

kover.reports.filters.excludes {
    // Add exclusions from reports
}

ksp {
    arg("skipPrivatePreviews", "true")
}

dependencies {
    implementation(projects.core)
    implementation(projects.sdkAndroid)
    implementation(projects.analytics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.showkase)
    implementation(libs.showkase.annotation)
    kspDebug(libs.showkase.processor)
    implementation(libs.androidx.annotation)

    androidTestImplementation(libs.androidx.compose.test)
    debugImplementation(libs.androidx.compose.test.manifest)
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.compose.test)
    testImplementation(libs.cashapp.turbine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.koin.test)
    testImplementation(libs.kotlin.mockk)
}
