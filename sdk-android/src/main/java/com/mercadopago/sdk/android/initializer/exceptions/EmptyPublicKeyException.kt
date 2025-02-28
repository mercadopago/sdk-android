package com.mercadopago.sdk.android.initializer.exceptions

class EmptyPublicKeyException(
    override val message: String = "The Public Key is empty. " +
        "Please check the public key being passed to the initialization"
) : RuntimeException(message)
