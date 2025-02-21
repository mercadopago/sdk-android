package com.mercadopago.sdk.android.initializer.exceptions

class SDKNotInitializedException(
    override val message: String = "MercadoPago SDK not initialized. Call MercadoPagoSDK.initialize() first."
) : RuntimeException()
