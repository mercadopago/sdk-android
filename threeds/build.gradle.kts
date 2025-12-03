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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
        allWarningsAsErrors = false
    }
}

ksp {
    arg("skipPrivatePreviews", "true")
}

dependencies {
    testImplementation(libs.usdk)

    compileOnly(files("../libs/mc-3ds-sdk-android-6.6.71.aar"))
    // 3DS SDK dependencies
    implementation(libs.gson)
    implementation(libs.play.services.location)
    implementation(libs.play.services.ads.identifier)
    implementation(libs.play.services.auth.api.phone)
    implementation(libs.support.v4)

    implementation(projects.core)
    implementation(projects.sdkAndroid)
    implementation(projects.analytics)
    // CompileOnly dependency to access CoreMethods interfaces without creating runtime dependency
    compileOnly(projects.coreMethods)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.okhttp.mockWebServer)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.annotation)
    implementation(libs.device.sdk)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Test implementation of core-methods to access interfaces and models
    testImplementation(projects.coreMethods)

    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.mockk)
    testImplementation(libs.cashapp.turbine)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
