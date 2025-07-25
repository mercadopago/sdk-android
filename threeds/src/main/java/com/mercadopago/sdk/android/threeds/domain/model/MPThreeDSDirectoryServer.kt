package com.mercadopago.sdk.android.threeds.domain.model

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
    AMEX("A000000025", "2.1.0"),
}
