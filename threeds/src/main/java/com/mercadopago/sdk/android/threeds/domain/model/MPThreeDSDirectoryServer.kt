package com.mercadopago.sdk.android.threeds.domain.model

/**
 * Enumeration of supported 3DS Directory Servers.
 * Each directory server has its own identifier and version.
 */
enum class MPThreeDSDirectoryServer(val directoryServerID: String, val messageVersion: String) {
    VISA("A000000003", "2.1.0"),
    MASTERCARD("A000000004", "2.1.0"),
    AMEX("A000000025", "2.1.0");
}
