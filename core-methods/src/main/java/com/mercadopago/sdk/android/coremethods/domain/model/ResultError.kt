package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * This class represents the result error of a request.
 * @param message: The error message.
 * @param code: The error code.
 */
data class ResultError(
    val message: String = "",
    val code: String = "",
)
