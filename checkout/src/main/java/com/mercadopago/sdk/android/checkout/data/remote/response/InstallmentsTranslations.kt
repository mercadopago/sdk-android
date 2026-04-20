package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class InstallmentsTranslations(
    val header: InstallmentsHeaderTranslations,
    val interestFreeLabel: String,
    val totalLabel: String,
)
