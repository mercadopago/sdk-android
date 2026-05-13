package com.mercadopago.sdk.android.checkout.data.remote.response

internal data class DocumentTranslations(
    val label: String,
    val errorEmptyField: String,
    val errorIncompleteField: String,
    val errorInvalidField: String,
)
