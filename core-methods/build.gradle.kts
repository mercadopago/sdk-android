import java.io.FileInputStream
import java.util.Properties

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
            artifactId = CoreMethodsSDKConfig.ARTIFACT_ID
            version = CoreMethodsSDKConfig.VERSION_NAME
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
    namespace = "com.mercadopago.sdk.android.coremethods"
    compileSdk = MercadoPagoSDKConfig.COMPILE_SDK

    defaultConfig {
        minSdk = MercadoPagoSDKConfig.MIN_SDK
        version = CoreMethodsSDKConfig.VERSION_NAME

        val secretPropertiesFile = rootProject.file("secrets.properties")
        val secretProperties = Properties()
        runCatching {
            secretProperties.load(FileInputStream(secretPropertiesFile))
        }

        buildConfigField(
            "String",
            "CORE_METHODS_PRODUCT_ID",
            secretProperties.getProperty("coreMethods.productId", "\"\""),
        )

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
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    kotlinOptions {
        jvmTarget = MercadoPagoSDKConfig.JVM_TARGET
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
    implementation(libs.device.sdk)

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
