package com.mercadopago.sdk.android.initializer.exceptions

/**
 * Exception thrown when the MercadoPago SDK is already initialized.
 */
class SDKAlreadyInitializedException(
    override val message: String = "The Mercado pago SDK is already initialized. " +
        "Please call `MercadoPagoSDK.initialize` only once.",
) : RuntimeException(message)
