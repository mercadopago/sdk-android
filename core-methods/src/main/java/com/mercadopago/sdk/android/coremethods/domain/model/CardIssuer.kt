package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Card Issuer
 * @param id: issuer id
 * @param merchantAccountId: merchant account id
 * @param processingMode: processing mode
 * @param status: actual status
 * @param thumbnail: issuer thumbnail (48x48)
 */
data class CardIssuer(
    val id: String? = null,
    val merchantAccountId: String? = null,
    val processingMode: String? = null,
    val status: String? = null,
    val thumbnail: String? = null,
)
