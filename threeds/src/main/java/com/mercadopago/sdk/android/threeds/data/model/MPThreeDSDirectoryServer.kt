package com.mercadopago.sdk.android.threeds.data.model

/**
 * Enumeration of supported 3DS Directory Servers.
 * Each directory server has its own identifier and version.
 *
 * @param directoryServerID The unique identifier for the directory server
 * @param messageVersion The 3DS message version supported by the directory server
 */
internal enum class MPThreeDSDirectoryServer(
    /** The unique identifier for the directory server */
    val directoryServerID: String,
    /** The 3DS message version supported by the directory server */
    val messageVersion: String,
) {
    /** VISA directory server */
    VISA("A000000003", "2.1.0"),

    /** Mastercard directory server */
    MASTERCARD("A000000004", "2.1.0"),

    /** American Express directory server */
    AMEX("A000000025", "2.1.0"), ;

    companion object {
        /**
         * Maps a payment method ID to the corresponding directory server.
         * Returns MASTERCARD as default for unknown payment methods.
         *
         * @param paymentMethodId The payment method identifier (e.g., "visa", "mastercard", "amex")
         * @return The corresponding MPThreeDSDirectoryServer enum value
         */
        internal fun paymentMethodDirectoryServer(
            paymentMethodId: String,
        ): MPThreeDSDirectoryServer {
            return when (paymentMethodId) {
                "visa", "debvisa" -> VISA
                "mastercard", "master" -> MASTERCARD
                "amex", "american_express" -> AMEX
                else -> MASTERCARD
            }
        }
    }
}
