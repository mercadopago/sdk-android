# Mercado Pago SDK Android

[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

A mobile SDK whose main objective is to facilitate the integration of Mercado Pago payment solutions on your app, allowing a secure flow within the security standards for sensitive data transfer.

## Requirements
 - MinSDK 23+
 - Jetpack Compose Bom 2024.12.01+
 - Kotlin 2.0.0+

## Documentation
https://mercadopago.github.io/sdk-android/

## Install

Using toml:
When using the toml file, create a new definition for the libraries you'll be using

```
// Main SDK
mercadopago-sdk = { group = "com.mercadopago.android.sdk", name = "sdk-android", version.ref = "mercadoPagoSdkVersion" }
// Core Methods SDK
mercadopago-sdk-coreMethods = { group = "com.mercadopago.android.sdk", name = "core-methods", version.ref = "mercadoPagoSdkCoreMethodsVersion" }
```

Call inside the build.gradle file for the module you need
```kts
implementation(libs.mercadopago.sdk)
implementation(libs.mercadopago.sdk.coreMethods)
```

## Usage
1. Initialize the SDK
To initialize the SDK, you'll need a public key for your Mercado Pago developer account. This can be obtained from the developer panel. Use the main application from your app 
```kotlin
 MercadoPagoSDK.initialize(
    context = this,
    publicKey = "... write the public key here. This should be stored in a non-VCS file.",
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


## License

[Apache License Version 2.0](https://github.com/mercadopago/sdk-android/blob/main/LICENSE.md)

