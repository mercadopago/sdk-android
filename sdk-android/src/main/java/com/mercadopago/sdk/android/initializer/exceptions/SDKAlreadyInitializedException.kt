package com.mercadopago.sdk.android.initializer.exceptions

class SDKAlreadyInitializedException(
    override val message: String = "The Mercado pago SDK is already initialized. " +
        "Please call `MercadoPagoSDK.initialize` only once.",
) : RuntimeException(message)
