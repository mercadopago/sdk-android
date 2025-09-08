package com.mercadopago.sdk.android.threeds.domain.model

/**
 * Represents a warning from the 3DS SDK.
 *
 * @param id The warning identifier
 * @param message The warning message
 * @param severity The severity level of the warning
 */
data class MPThreeDSWarning(
    val id: String,
    val message: String,
    val severity: MPThreeDSSeverity
)

/**
 * Severity levels for 3DS warnings.
 */
enum class MPThreeDSSeverity {
    /** Low severity warning */
    LOW,
    /** Medium severity warning */
    MEDIUM,
    /** High severity warning */
    HIGH,
    /** No severity or unknown */
    NONE
}
