package com.mercadopago.sdk.android.checkout.statusscreen.domain.model

internal data class StatusScreenViewData(
    val statusType: String,
    val header: StatusScreenHeader,
    val body: List<StatusScreenBodyItem>,
    val footer: StatusScreenFooter,
    val canRetry: Boolean? = null,
)
