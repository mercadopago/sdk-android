package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class IdentificationType(
    val id: String,
    val name: String,
    val minLength: Int,
    val maxLength: Int,
    val placeholder: String?,
    val mask: String?,
    val type: String?,
    val sequence: String?,
)
