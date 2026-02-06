import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
    id("org.jetbrains.kotlin.plugin.serialization") version libs.versions.kotlin
}

android {
    namespace = "com.mercadopago.sdk.android.example"
    compileSdk = MercadoPagoSDKConfig.COMPILE_SDK

    val secretPropertiesFile = rootProject.file("secrets.properties")
    val secretProperties = Properties()
    runCatching {
        secretProperties.load(FileInputStream(secretPropertiesFile))
    }
    val publicKeyListString = try {
        secretProperties.getProperty("publicKey.list", "")
            .replace("\"", "\\\"")
    } catch (_: Exception) {
        ""
    }

    defaultConfig {
        applicationId = "com.mercadopago.sdk.android.example"
        minSdk = MercadoPagoSDKConfig.MIN_SDK
        targetSdk = MercadoPagoSDKConfig.TARGET_SDK
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "PUBLIC_KEY",
            secretProperties.getProperty("publicKey.default", "\"\""),
        )
        buildConfigField(
            "String",
            "DEFAULT_PUBLIC_KEY_LIST",
            "\"$publicKeyListString\"",
        )
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
        jvmTarget = MercadoPagoSDKConfig.JVM_TARGET
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    lint {
        disable += "NullSafeMutableLiveData"
    }
}

kover.reports.filters.excludes {
    // Disabled reports for sample app
    classes("*")
}

dependencies {
    implementation(projects.sdkAndroid)
    implementation(projects.foundation)
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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
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
