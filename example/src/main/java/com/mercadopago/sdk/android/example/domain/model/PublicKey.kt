package com.mercadopago.sdk.android.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
internal data class PublicKey(
    val publicKey: String,
    val countryCode: String,
)
