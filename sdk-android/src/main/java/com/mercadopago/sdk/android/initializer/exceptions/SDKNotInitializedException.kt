package com.mercadopago.sdk.android.initializer.exceptions

/**
 * Exception thrown when MercadoPago SDK is not initialized.
 */
class SDKNotInitializedException(
    override val message: String = "MercadoPago SDK not initialized. Call MercadoPagoSDK.initialize() first."
) : RuntimeException()
