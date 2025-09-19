package com.mercadopago.sdk.android.threeds.data.model

/**
 * Internal authentication parameters used for the 3DS challenge flow.
 * These parameters are mapped from the domain model for internal processing.
 *
 * @param response The authentication response status
 * @param threeDSServerTransID 3DS server transaction ID
 * @param acsReferenceNumber ACS reference number
 * @param dsTransID Directory server transaction ID
 * @param acsTransID ACS transaction ID
 * @param acsSignedContent ACS signed content
 */
internal data class MPThreeDSAuthenticationParams(
    val response: String,
    val threeDSServerTransID: String,
    val acsReferenceNumber: String,
    val dsTransID: String,
    val acsTransID: String,
    val acsSignedContent: String,
)
