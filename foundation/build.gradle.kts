import com.mercadopago.sdk.android.FoundationSDKConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.klint)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
    id(MavenConfig.MAVEN_PUBLISH)
}

publishing {
    publications {
        register<MavenPublication>(MavenConfig.RELEASE) {
            groupId = MavenConfig.GROUP_ID
            artifactId = FoundationSDKConfig.ARTIFACT_ID
            version = FoundationSDKConfig.VERSION_NAME
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
    namespace = "com.mercadopago.sdk.android.foundation"
    compileSdk = MercadoPagoSDKConfig.COMPILE_SDK

    defaultConfig {
        minSdk = MercadoPagoSDKConfig.MIN_SDK
        version = FoundationSDKConfig.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = MercadoPagoSDKConfig.JVM_TARGET
        allWarningsAsErrors = false
        freeCompilerArgs += listOf(
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
        )
    }
    apply(plugin = "org.jetbrains.dokka")
}

tasks.named(MercadoPagoSDKConfig.DOKKA_HTML, org.jetbrains.dokka.gradle.DokkaTask::class).configure {
    outputDirectory.set(layout.buildDirectory.dir(MercadoPagoSDKConfig.DOKKA_DIR))
    dokkaSourceSets {
        configureEach {
            perPackageOption {
                matchingRegex.set(MercadoPagoSDKConfig.DOKKA_IGNORE_PACKAGES)
                suppress.set(true)
            }
        }
    }
}

kover {
    disable()
}

ksp {
    arg("skipPrivatePreviews", "true")
}

dependencies {

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

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
