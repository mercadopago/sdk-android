plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id(MavenConfig.MAVEN_PUBLISH)
}

publishing {
    publications {
        register<MavenPublication>(MavenConfig.RELEASE) {
            groupId = MavenConfig.GROUP_ID
            artifactId = MPExtendedSDKConfig.ARTIFACT_ID
            version = MPExtendedSDKConfig.VERSION_NAME
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
    namespace = "com.mercadopago.sdk.android.coremethods.builder"
    compileSdk = MercadoPagoSDKConfig.COMPILE_SDK

    defaultConfig {
        minSdk = MercadoPagoSDKConfig.MIN_SDK
        version = MPExtendedSDKConfig.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
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
    }
}

dependencies {
    api(projects.coreMethods)
    implementation(projects.core)
    implementation(projects.sdkAndroid)
    implementation(projects.analytics)
    implementation(libs.device.sdk)
}
