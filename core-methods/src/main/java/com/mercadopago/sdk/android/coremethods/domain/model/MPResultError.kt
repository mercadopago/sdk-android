package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents different types of errors that can occur during SDK operations.
 * This sealed class provides a type-safe way to handle various error scenarios,
 * including request failures and validation errors.
 * It helps standardize error handling across the SDK.
 *
 * Example:
 * ```kotlin
 * // Handle errors
 * when (error) {
 *     is ResultError.Request -> {
 *         println("Request failed: ${error.message} (${error.code})")
 *     }
 *     is ResultError.Validation -> {
 *         println("Validation failed: ${error.message}")
 *     }
 * }
 * ```
 *
 * @see Request
 * @see Validation
 */
sealed class MPResultError {
    /**
     * Represents an error that occurred during a network request or API call.
     * This class contains information about request failures, including
     * error messages and error codes for proper error handling.
     *
     * @param message A human-readable description of the error
     * @param code A unique identifier for the type of error
     *
     * Example:
     * ```kotlin
     * ResultError.Request(
     *     message = "Failed to connect to server",
     *     code = "CONNECTION_ERROR"
     * )
     * ```
     */
    data class Request(
        val message: String,
        val code: String,
    ) : MPResultError()

    /**
     * Represents an error that occurred during data validation.
     * This class contains information about validation failures,
     * such as invalid input data or missing required fields.
     *
     * @param message A human-readable description of the validation error
     *
     * Example:
     * ```kotlin
     * ResultError.Validation(
     *     message = "Card number must be 16 digits"
     * )
     * ```
     */
    data class Validation(
        val message: String,
    ) : MPResultError()
}
