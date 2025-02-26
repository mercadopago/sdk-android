package com.mercadopago.sdk.android.coremethods.domain.model

data class IdentificationTypes(
    val id: String,
    val name: String,
    val type: String,
    val minLength: Int,
    val maxLength: Int
)
