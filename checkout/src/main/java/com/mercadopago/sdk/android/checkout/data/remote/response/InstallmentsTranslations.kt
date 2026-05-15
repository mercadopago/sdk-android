package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class InstallmentsTranslations(
    val header: InstallmentsHeaderTranslations,
    // tech-debt: campo será removido na PR 4/4 do split 3917 quando consumidores migrarem para payButtonLabel
    val interestFreeLabel: String,
    val totalLabel: String,
    val payButtonLabel: String = "",
)
