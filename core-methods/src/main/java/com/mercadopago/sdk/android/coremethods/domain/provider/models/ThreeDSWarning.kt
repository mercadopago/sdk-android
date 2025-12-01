package com.mercadopago.sdk.android.coremethods.domain.provider.models

/**
 * Represents a warning from the 3DS SDK.
 *
 * @property id The warning identifier
 * @property message The warning message
 * @property severity The severity level of the warning
 */
data class ThreeDSWarning(
    val id: String,
    val message: String,
    val severity: ThreeDSSeverity,
)

/**
 * Severity levels for 3DS warnings.
 */
enum class ThreeDSSeverity {
    /** Low severity warning */
    LOW,

    /** Medium severity warning */
    MEDIUM,

    /** High severity warning */
    HIGH,

    /** No severity or unknown */
    NONE,
}
