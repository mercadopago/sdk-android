package com.mercadopago.sdk.android.threeds.data.model

internal data class MPThreeDSAuthenticationParams(
    val response: String,
    val threeDSServerTransID: String,
    val acsReferenceNumber: String,
    val dsTransID: String,
    val acsTransID: String,
    val acsSignedContent: String,
)
