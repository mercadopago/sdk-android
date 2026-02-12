package com.mercadopago.sdk.android.checkout.domain.model

/**
 * Represents an error produced by the card form brick during checkout.
 *
 * @property serviceError Optional error code or identifier from the payment service.
 * @property message Optional human-readable error message to display to the user.
 */
data class CardFormBrickError(
    val serviceError: String? = null,
    val message: String? = null,
)
