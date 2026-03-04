# Mercado Pago SDK Android

![Sonatype Nexus (Repository)](https://img.shields.io/nexus/android-releases/com.mercadopago.android.sdk/sdk-android-bom?server=https%3A%2F%2Fartifacts.mercadolibre.com&logo=mercadopago&logoColor=%23000000&logoSize=auto&label=SDK%20BOM%20Version&labelColor=%23ffe600&color=%2302bcff&link=https%3A%2F%2Fartifacts.mercadolibre.com%2F%23browse%2Fbrowse%3Aandroid-releases%3Acom%252Fmercadopago%252Fandroid%252Fsdk%252Fsdk-android-bom)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

A mobile SDK whose main objective is to facilitate the integration of Mercado Pago payment solutions on your app, allowing a secure flow within the security standards for sensitive data transfer.

## Requirements
 - MinSDK 23+
 - Jetpack Compose Bom 2024.12.01+
 - Kotlin 2.0.0+

## Documentation
https://mercadopago.github.io/sdk-android/

## Install
TEst
You need to add this to your settings.build.gradle file.
```kotlin
pluginManagement {
    repositories {
        // Other dependencies...
        maven { url = uri("https://artifacts.mercadolibre.com/repository/android-releases") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // Other dependencies...
        maven {
            url = uri("https://artifacts.mercadolibre.com/repository/android-releases")
        }
    }
}
```

Using toml:
Add the following definitions to your `libs.versions.toml` file to declare the SDK dependencies. We recommend using our Bill of Materials (BOM) to ensure consistent versioning across all SDK modules and avoid dependency conflicts:

```
// Main SDK
[versions]
mercadoPagoSdkBomVersion = "latest-bom-version"

[libraries]
mercadopago-sdk-bom = { group = "com.mercadopago.android.sdk", name = "sdk-android-bom", version.ref = "mercadoPagoSdkBomVersion" }
// Core Methods SDK
mercadopago-sdk-coreMethods = { group = "com.mercadopago.android.sdk", name = "core-methods" }
```

Call inside the build.gradle file for the module you need
```kts
implementation(platform(libs.mercadopago.sdk.bom))
implementation(libs.mercadopago.sdk.coreMethods)
```

## Usage
1. Initialize the SDK
To initialize the SDK, you'll need a public key for your Mercado Pago developer account. This can be obtained from the developer panel. Use the main application from your app
```kotlin
 MercadoPagoSDK.initialize(
    context = this,
    publicKey = BuildConfig.PUBLIC_KEY, // Ensure that this key is securely managed and not hardcoded. It should not be stored in a Git file.
    countryCode = // use the country code for that public key,
)
```

2. Calling the SDK
You can call the SDK with this method. Here is an example with the Core Methods SDK:
```kotlin
 viewModelScope.launch {
    val mercadoPagoSDK = MercadoPagoSDK.getInstance() // Get the mercado pago sdk instance
    val coreMethods = mercadoPagoSDK.coreMethods // Get the core methods SDK instance
    val identificationTypesResult = coreMethods.getIdentificationTypes()
    when (identificationTypesResult) {
        is Result.Error -> {
            when (identificationTypesResult.error) {
                is ResultError.Request -> {
                    (identificationTypesResult.error as ResultError.Request).message
                    // Handle a request error
                }
                is ResultError.Validation -> {
                    (identificationTypesResult.error as ResultError.Validation).message
                    // Handle a validation error
                }
            }
        }
        is Result.Success -> {
            // Handle the success
            identificationTypesResult.data
        }
    }
}
```

## Sample App
You can also use our sample app to see how to use the SDK. Clone this repository and build the :example module. Don't forget to change the public key.


## License

[Apache License Version 2.0](https://github.com/mercadopago/sdk-android/blob/main/LICENSE.md)
