package com.mercadopago.sdk.android.threeds.domain.model

/**
 * Represents an error that occurred during the 3DS challenge process.
 *
 * @param code Error code from the 3DS SDK or backend
 * @param message Human-readable error message
 * @param details Additional error details if available
 */
data class MPThreeDSChallengeError(
    val code: String,
    val message: String,
    val details: String? = null,
    val cause: Throwable? = null,
) : Exception(message, cause) {

    companion object {
        fun fromException(exception: Throwable): MPThreeDSChallengeError {
            return MPThreeDSChallengeError(
                code = "UNKNOWN_ERROR",
                message = exception.message ?: "Unknown error occurred",
                cause = exception
            )
        }

        fun authenticationFailed(reason: String): MPThreeDSChallengeError {
            return MPThreeDSChallengeError(
                code = "AUTHENTICATION_FAILED",
                message = "3DS Authentication failed: $reason"
            )
        }

        fun challengeFailed(reason: String): MPThreeDSChallengeError {
            return MPThreeDSChallengeError(
                code = "CHALLENGE_FAILED",
                message = "3DS Challenge failed: $reason"
            )
        }
    }
}
