package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * This class represents the result error of a request.
 * It can have multiple types to be mapped by the caller.
 */
sealed class ResultError : Exception() {

    /**
     * Represents an error of a request
     * @param message The error message.
     * @param code The error code.
     */
    data class Request(
        override val message: String,
        val code: Int,
    ) : ResultError()

    /**
     * Represents a validation error of a request
     * @param message The validation error message.
     */
    data class Validation(
        override val message: String,
    ) : ResultError()
}
