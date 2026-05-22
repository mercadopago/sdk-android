package com.mercadopago.sdk.android.checkout.presentation.state

internal data class EmailScreenState(
    val translate: Translate,
    val value: String = "",
    val isError: Boolean = false,
    val isButtonEnabled: Boolean = false,
) {
    internal data class Translate(
        val title: String,
        val fieldLabel: String,
        val fieldPlaceholder: String,
        val buttonLabel: String,
        val errorFieldEmpty: String,
        val errorEmailInvalid: String,
        val errorFieldRequired: String,
    )
}
