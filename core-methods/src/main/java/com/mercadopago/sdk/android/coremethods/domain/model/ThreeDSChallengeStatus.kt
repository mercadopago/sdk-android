package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents the possible status values for a 3DS challenge after user interaction.
 *
 * This enum is used to update the backend with the result of the 3DS challenge flow.
 *
 * @property value The string representation of the status sent to the API.
 */
enum class ThreeDSChallengeStatus(val value: String) {
    /**
     * Challenge completed successfully by the user.
     */
    COMPLETED("COMPLETED"),

    /**
     * Challenge cancelled by the user.
     */
    CANCELLED("CANCELLED"),

    /**
     * Error occurred during the challenge execution.
     */
    ERROR("ERROR"),

    /**
     * Challenge expired due to timeout.
     */
    TIMEOUT("TIMEOUT"),
}
