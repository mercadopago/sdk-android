package com.mercadopago.sdk.android.coremethods.exceptions

class InitializationException(
    message: String = "SDK is not initialized. " +
        "Please start the SDK inside your Application class calling CoreMethods.initialize()",
) : Exception(message)
