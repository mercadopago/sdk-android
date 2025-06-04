package com.mercadopago.sdk.android.coremethods.domain.model

/**
 * Represents a card issuer entity in the payment processing system.
 * This class contains information about the financial institution that issued
 * the payment card, including its identification, processing details, and visual
 * representation.
 *
 * @param id The unique identifier of the card issuer
 * @param merchantAccountId The identifier of the merchant's account with this issuer
 * @param processingMode The processing mode used by this issuer (e.g., "aggregator", "gateway")
 * @param status The current status of the issuer in the system
 * @param thumbnail URL to the issuer's logo image (48x48 pixels)
 */
data class CardIssuer(
    val id: String? = null,
    val merchantAccountId: String? = null,
    val processingMode: String? = null,
    val status: String? = null,
    val thumbnail: String? = null,
)
