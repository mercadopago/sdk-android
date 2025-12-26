package com.mercadopago.sdk.android.threeds.domain.model

/**
 * Authentication response from the backend after processing 3DS authentication.
 * This should be created from your backend response and passed to the challenge flow.
 *
 * @param threeDSServerTransID 3DS server transaction ID
 * @param acsReferenceNumber ACS reference number
 * @param dsTransID Directory server transaction ID
 * @param acsTransID ACS transaction ID
 * @param acsSignedContent ACS signed content
 */
data class MPThreeDSAuthenticationModel(
    val threeDSServerTransID: String,
    val acsReferenceNumber: String,
    val dsTransID: String,
    val acsTransID: String,
    val acsSignedContent: String,
)
