package com.mercadopago.sdk.android.checkout.statusscreen.domain.model

internal data class StatusScreenHeader(
    val title: String,
    val iconUrl: String? = null,
    val iconRes: Int? = null,
    val subtitle: String? = null,
)
