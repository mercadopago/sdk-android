package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class SecurityCodeTranslations(
    val label: String,
    val placeholder: String,
    val helper: String? = null,
    val tooltip: String,
    val errorEmptyField: String,
    val errorIncompleteField: String,
    val errorInvalidField: String? = null,
)
