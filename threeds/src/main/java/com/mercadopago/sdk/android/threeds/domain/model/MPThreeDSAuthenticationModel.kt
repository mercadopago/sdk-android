package com.mercadopago.sdk.android.threeds.domain.model

internal data class MPThreeDSAuthenticationModel(
    val response: String,
    val threeDSServerTransID: String,
    val acsReferenceNumber: String,
    val dsTransID: String,
    val acsTransID: String,
    val acsSignedContent: String,
)
