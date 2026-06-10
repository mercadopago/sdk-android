package com.mercadopago.sdk.android.checkout.domain.model

internal data class EmailInitializationOutput(
    val title: String,
    val buttonLabel: String,
    val fieldLabel: String,
    val fieldPlaceholder: String,
    val errorFieldEmpty: String,
    val errorEmailInvalid: String,
    val prefilledEmail: String? = null,
)
