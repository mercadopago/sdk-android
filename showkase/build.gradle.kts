plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.cashapp.paparazzi)
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.mercadopago.sdk.android.showkase"
    compileSdk = MercadoPagoSDKConfigs.compileSdk

    defaultConfig {
        applicationId = "com.mercadopago.sdk.android.showkase"
        minSdk = MercadoPagoSDKConfigs.minSdk
        targetSdk = MercadoPagoSDKConfigs.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = MercadoPagoSDKConfigs.jvmTarget
    }
    buildFeatures {
        compose = true
    }
}

kover.reports.filters.excludes {
    classes("*")
}

dependencies {
    implementation(projects.coreMethods)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.showkase)
    implementation(libs.showkase.annotation)
    ksp(libs.showkase.processor)

    androidTestImplementation(libs.androidx.compose.test)
    debugImplementation(libs.androidx.compose.test.manifest)
    kspTest(libs.showkase.processor)
    testImplementation(libs.test.parameterInjector)
    testImplementation(libs.junit)
    testImplementation(libs.showkase.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.test)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.compose.test.manifest)
}
