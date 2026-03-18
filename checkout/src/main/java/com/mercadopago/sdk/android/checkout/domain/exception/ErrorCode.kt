package com.mercadopago.sdk.android.checkout.domain.exception

private const val CODE_NETWORK_CONNECTION_FAILED = -1009
private const val CODE_NETWORK_TIMEOUT = -1001
private const val CODE_SERVICE_ERROR = 2000
private const val CODE_INTEGRATION_ERROR = 3000
private const val CODE_UNKNOWN = 999

/**
 * Stable error codes for monitoring and tracking checkout errors.
 *
 * These codes provide consistent identifiers for different error types that can occur
 * during the checkout flow. They are designed to be stable across SDK versions to enable
 * reliable error monitoring, analytics, and debugging.
 *
 * @property value The numeric error code value used for logging and monitoring systems.
 */
enum class ErrorCode(val value: Int) {
    /**
     * Network connection failure.
     *
     * Indicates that the device could not establish a network connection.
     * Common causes include:
     * - No internet connectivity
     * - Airplane mode enabled
     * - Network unreachable
     * - DNS resolution failures
     */
    NETWORK_CONNECTION_FAILED(CODE_NETWORK_CONNECTION_FAILED),

    /**
     * Network request timeout.
     *
     * Indicates that a network request exceeded the allowed time limit.
     * This typically occurs when:
     * - Server is slow to respond
     * - Poor network quality
     * - Request took longer than configured timeout
     */
    NETWORK_TIMEOUT(CODE_NETWORK_TIMEOUT),

    /**
     * Service-side error.
     *
     * Indicates an error occurred on the server or during API communication.
     * This includes:
     * - API validation errors
     * - Server-side processing failures
     * - Invalid request parameters
     * - Business logic errors returned by the service
     */
    SERVICE_ERROR(CODE_SERVICE_ERROR),

    /**
     * SDK integration error.
     *
     * Indicates an error in how the SDK is configured or used by the integrator.
     * Common causes:
     * - Missing required configuration
     * - Invalid SDK initialization
     * - Incorrect API usage
     */
    INTEGRATION_ERROR(CODE_INTEGRATION_ERROR),

    /**
     * Unknown or unexpected error.
     *
     * Indicates an error that doesn't fit into any other category.
     * This is used as a fallback when the error type cannot be determined
     * or when an unexpected exception occurs.
     */
    UNKNOWN(CODE_UNKNOWN),
}
