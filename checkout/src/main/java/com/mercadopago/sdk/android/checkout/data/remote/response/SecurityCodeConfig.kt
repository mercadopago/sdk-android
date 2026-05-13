package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class SecurityCodeConfig(
    val type: String,
    val length: Int,
    val mode: String? = null,
    val cardLocation: String? = null,
    val tooltip: String? = null,
    val placeholder: String? = null,
    val optional: Boolean = false,
)
