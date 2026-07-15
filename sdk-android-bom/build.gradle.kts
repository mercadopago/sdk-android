import com.mercadopago.sdk.android.BomConfig

plugins {
    id("java-platform")
    id(MavenConfig.MAVEN_PUBLISH)
}

dependencies {
    constraints {
        api(projects.core)
        api(projects.analytics)
        api(projects.sdkAndroid)
        api(projects.coreMethods)
        api(projects.mpExtended)
    }
}

publishing {
    publications {
        register<MavenPublication>(MavenConfig.RELEASE) {
            groupId = MavenConfig.GROUP_ID
            artifactId = BomConfig.ARTIFACT_ID
            version = BomConfig.VERSION_NAME
            afterEvaluate {
                from(components["javaPlatform"])
                dependencies {
                    api(projects.sdkAndroid)
                }
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

version = BomConfig.VERSION_NAME
