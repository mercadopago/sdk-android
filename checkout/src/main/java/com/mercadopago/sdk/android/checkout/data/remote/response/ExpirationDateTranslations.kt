package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class ExpirationDateTranslations(
    val label: String,
    val placeholder: String,
    val helper: String? = null,
    val tooltip: String? = null,
    val errorEmptyField: String,
    val errorIncompleteField: String,
    val errorInvalidField: String,
)
