package com.mercadopago.sdk.android.googlepay.domain.model

internal enum class GooglePayCardNetwork(
    val paymentMethodId: String,
    val googleNetwork: String,
) {
    VISA("visa", "VISA"),
    MASTER("master", "MASTERCARD"),
    AMEX("amex", "AMEX"),
    ;

    companion object {
        fun fromPaymentMethodId(
            id: String?,
        ): GooglePayCardNetwork? = entries.firstOrNull { it.paymentMethodId == id }

        fun fromGoogleNetwork(
            name: String?,
        ): GooglePayCardNetwork? = entries.firstOrNull { it.googleNetwork == name }
    }
}
