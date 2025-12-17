package com.mercadopago.sdk.android.threeds.domain.model

/**
 * Represents an error that occurred during the 3DS challenge process.
 *
 * @param code Error code from the 3DS SDK or backend
 * @param message Human-readable error message
 * @param details Additional error details if available
 * @param cause The underlying cause of the error
 */
data class MPThreeDSChallengeError(
    val code: String,
    override val message: String,
    val details: String? = null,
    /** The underlying cause of the error */
    override val cause: Throwable? = null,
) : Exception(message, cause) {
    /**
     * Companion object providing factory methods for creating error instances.
     */
    companion object {
        /**
         * Creates an error from a generic exception.
         *
         * @param exception The exception to convert
         * @return A MPThreeDSChallengeError instance
         */
        fun fromException(exception: Throwable): MPThreeDSChallengeError {
            return MPThreeDSChallengeError(
                code = "UNKNOWN_ERROR",
                message = exception.message ?: "Unknown error occurred",
                cause = exception,
            )
        }

        /**
         * Creates an authentication failed error.
         *
         * @param reason The reason for authentication failure
         * @return A MPThreeDSChallengeError instance
         */
        fun authenticationFailed(reason: String): MPThreeDSChallengeError {
            return MPThreeDSChallengeError(
                code = "AUTHENTICATION_FAILED",
                message = "3DS Authentication failed: $reason",
            )
        }

        /**
         * Creates a challenge failed error.
         *
         * @param reason The reason for challenge failure
         * @return A MPThreeDSChallengeError instance
         */
        fun challengeFailed(reason: String): MPThreeDSChallengeError {
            return MPThreeDSChallengeError(
                code = "CHALLENGE_FAILED",
                message = "3DS Challenge failed: $reason",
            )
        }
    }
}
