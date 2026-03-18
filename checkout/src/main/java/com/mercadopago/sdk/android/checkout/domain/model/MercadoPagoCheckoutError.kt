package com.mercadopago.sdk.android.checkout.domain.model

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode

/**
 * SDK error following the 5-audience pattern:
 * 1. End users - localized messages
 * 2. Runtime code - pattern matching via sealed class
 * 3. Developers - debug descriptions
 * 4. Monitoring - stable error codes
 * 5. SDK consumers - clean API without internal visibility
 *
 * @property errorCode Stable error code for monitoring and analytics.
 * @property errorMessage Human-readable error message.
 * @property errorLocalized The area/feature where the error occurred.
 * @property errorCause Optional cause of the error, if any.
 */
sealed class MercadoPagoCheckoutError(
    val errorCode: ErrorCode,
    val errorMessage: String,
    val errorLocalized: String,
    val errorCause: String? = null,
) : Throwable(message = errorMessage) {
    /**
     * Network-related errors such as connection failures, timeouts, or unavailability.
     *
     * @property code The specific network error code.
     * @property messageError Localized error message for end users.
     * @property localized The area/feature where the error occurred.
     * @property throwable The original exception that caused this error, if any.
     */
    data class NetworkError(
        val code: ErrorCode,
        val messageError: String,
        val localized: String,
        val throwable: Throwable?,
    ) : MercadoPagoCheckoutError(
            errorCode = code,
            errorMessage = messageError,
            errorLocalized = localized,
            errorCause = throwable?.message,
        )

    /**
     * SDK configuration or initialization errors.
     *
     * @property code The specific configuration error code.
     * @property messageError Localized error message for developers.
     * @property localized The area/feature where the error occurred.
     */
    data class ConfigurationError(
        val code: ErrorCode,
        val messageError: String,
        val localized: String,
    ) : MercadoPagoCheckoutError(
            errorCode = code,
            errorMessage = messageError,
            errorLocalized = localized,
        )

    /**
     * Unknown or unexpected errors.
     *
     * @property code The specific error code.
     * @property messageError Localized error message.
     * @property localized The area/feature where the error occurred.
     * @property throwable The original exception that caused this error, if any.
     */
    data class UnknownError(
        val code: ErrorCode,
        val messageError: String,
        val localized: String,
        val throwable: Throwable?,
    ) : MercadoPagoCheckoutError(
            errorCode = code,
            errorMessage = messageError,
            errorLocalized = localized,
            errorCause = throwable?.message,
        )

    /**
     * Service-related errors including API failures, validation errors, and server-side issues.
     *
     * @property code The specific service error code.
     * @property messageError Localized error message describing the service issue.
     * @property localized The area/feature where the error occurred.
     * @property throwable The original exception that caused this error, if any.
     */
    data class ServiceError(
        val code: ErrorCode,
        val messageError: String,
        val localized: String,
        val throwable: Throwable? = null,
    ) : MercadoPagoCheckoutError(
            errorCode = code,
            errorMessage = messageError,
            errorLocalized = localized,
            errorCause = throwable?.message,
        )
}
