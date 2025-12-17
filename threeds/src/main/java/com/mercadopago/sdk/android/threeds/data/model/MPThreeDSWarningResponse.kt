package com.mercadopago.sdk.android.threeds.data.model

/**
 * Response model for 3DS warnings from the uSDK service.
 *
 * @param id The warning identifier
 * @param message The warning message
 * @param severity The severity level of the warning
 */
data class MPThreeDSWarningResponse(
    val id: String,
    val message: String,
    val severity: MPSeverityResponse,
)

/**
 * Severity levels for 3DS warnings from the uSDK service.
 */
enum class MPSeverityResponse {
    /** Low severity warning */
    LOW,

    /** Medium severity warning */
    MEDIUM,

    /** High severity warning */
    HIGH,

    /** No severity or unknown */
    NONE,

    ;

    /**
     * Severity levels for 3DS warnings from the uSDK service.
     */
    companion object {
        /**
         * Maps a numeric grade to the corresponding severity level.
         *
         * @param grade The numeric grade (0-3)
         * @return The corresponding MPSeverityResponse enum value
         */
        fun getWaningByGrade(grade: Int): MPSeverityResponse {
            return when (grade) {
                0 -> LOW
                1 -> MEDIUM
                2 -> HIGH
                else -> NONE
            }
        }
    }
}
