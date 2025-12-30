package com.mercadopago.sdk.android.coremethods.domain.provider.models

/**
 * Authentication response from the backend after processing 3DS authentication.
 * This should be created from your backend response and passed to the challenge flow.
 *
 * @property threeDSServerTransID 3DS server transaction ID
 * @property acsReferenceNumber ACS reference number
 * @property dsTransID Directory server transaction ID
 * @property acsTransID ACS transaction ID
 * @property acsSignedContent ACS signed content
 * @property callbackUrl callback url
 */
data class ThreeDSAuthenticationModel(
    val threeDSServerTransID: String,
    val acsReferenceNumber: String,
    val dsTransID: String,
    val acsTransID: String,
    val acsSignedContent: String,
    val callbackUrl: String,
)
