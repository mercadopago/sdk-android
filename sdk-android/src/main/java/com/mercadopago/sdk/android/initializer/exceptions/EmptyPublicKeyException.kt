package com.mercadopago.sdk.android.initializer.exceptions

/**
 * Exception thrown when the public key is empty.
 */
class EmptyPublicKeyException(
    override val message: String = "The Public Key is empty. " +
        "Please check the public key being passed to the initialization"
) : RuntimeException(message)
