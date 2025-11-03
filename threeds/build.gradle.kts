import com.mercadopago.sdk.android.ThreeDSSDKConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.klint)
    alias(libs.plugins.google.ksp)
    id(MavenConfig.MAVEN_PUBLISH)
}

publishing {
    publications {
        register<MavenPublication>(MavenConfig.RELEASE) {
            groupId = MavenConfig.GROUP_ID
            artifactId = ThreeDSSDKConfig.ARTIFACT_ID
            version = ThreeDSSDKConfig.VERSION_NAME
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
    namespace = "com.mercadopago.sdk.android.threeds"
    compileSdk = MercadoPagoSDKConfig.COMPILE_SDK

    defaultConfig {
        minSdk = MercadoPagoSDKConfig.MIN_SDK
        version = ThreeDSSDKConfig.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = false // Keep false for library modules
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            isJniDebuggable = false
            isRenderscriptDebuggable = false
            isPseudoLocalesEnabled = false
        }
        debug {
            isMinifyEnabled = false
            // Keep ProGuard rules for debug builds to catch issues early
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = MercadoPagoSDKConfig.sourceCompatibility
        targetCompatibility = MercadoPagoSDKConfig.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = MercadoPagoSDKConfig.JVM_TARGET
        allWarningsAsErrors = false
    }
}

ksp {
    arg("skipPrivatePreviews", "true")
}

dependencies {

    implementation("com.usdk.android:usdk:6.6.81")

    // 3DS SDK dependencies
    implementation("com.google.code.gson:gson:2.8.1")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-ads-identifier:18.2.0")
    implementation("com.google.android.gms:play-services-auth-api-phone:18.2.0")
    implementation("com.android.support:support-v4:28.0.0")

    api(projects.core)
    implementation(projects.sdkAndroid)
    api(projects.analytics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.okhttp.mockWebServer)
    api(libs.androidx.datastore)
    implementation(libs.androidx.annotation)
    api(libs.device.sdk)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.mockk)
    testImplementation(libs.cashapp.turbine)
    testImplementation(libs.kotlin.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
