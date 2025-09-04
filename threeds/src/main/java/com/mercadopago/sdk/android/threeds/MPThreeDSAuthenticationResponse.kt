package com.mercadopago.sdk.android.threeds

/**
 * Authentication response from the backend after processing 3DS authentication.
 * This should be created from your backend response and passed to the challenge flow.
 *
 * @param response The authentication response status (e.g., "CHALLENGE", "AUTHORIZED")
 * @param threeDSServerTransID 3DS server transaction ID
 * @param acsReferenceNumber ACS reference number
 * @param dsTransID Directory server transaction ID
 * @param acsTransID ACS transaction ID
 * @param acsSignedContent ACS signed content
 */
data class MPThreeDSAuthenticationResponse(
    val response: String,
    val threeDSServerTransID: String,
    val acsReferenceNumber: String,
    val dsTransID: String,
    val acsTransID: String,
    val acsSignedContent: String,
)
