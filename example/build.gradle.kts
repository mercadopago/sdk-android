import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.mercadopago.sdk.android.example"
    compileSdk = MercadoPagoSDKConfig.compileSdk

    val secretPropertiesFile = rootProject.file("secrets.properties")
    val secretProperties = Properties()
    runCatching {
        secretProperties.load(FileInputStream(secretPropertiesFile))
    }

    defaultConfig {
        applicationId = "com.mercadopago.sdk.android.example"
        minSdk = MercadoPagoSDKConfig.minSdk
        targetSdk = MercadoPagoSDKConfig.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "PUBLIC_KEY",
            secretProperties.getProperty("publicKey.default", "\"\""),
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        compose = true
        buildConfig = true
    }
}

kover.reports.filters.excludes {
    // Disabled reports for sample app
    classes("*")
}

dependencies {
    implementation(projects.sdkAndroid)
    implementation(projects.coreMethods)
    api(platform(libs.koin.bom))
    api(libs.koin.core)
    api(libs.koin.android)
    api(libs.koin.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    debugImplementation(libs.showkase)
    implementation(libs.showkase.annotation)
    kspDebug(libs.showkase.processor)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
