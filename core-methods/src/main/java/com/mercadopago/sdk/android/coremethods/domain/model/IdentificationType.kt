package com.mercadopago.sdk.android.coremethods.domain.model

data class IdentificationType(
    val id: String,
    val name: String,
    val type: String,
    val minLength: Int,
    val maxLength: Int
)
