package com.mercadopago.sdk.android.googlepay.domain.model

/**
 * Single-use Mercado Pago card token generated from Google Pay.
 *
 * @property tokenId card token id, to be sent to the integrator backend.
 * @property bin card bin.
 * @property paymentMethodId "visa", "master" or "amex".
 * @property paymentTypeId payment type of the card (credit, debit or prepaid).
 * @property cardDescription Google text, ex. "Visa •••• 1111", for the post-purchase screen.
 */
data class MPGooglePayToken(
    val tokenId: String,
    val bin: String,
    val paymentMethodId: String,
    val paymentTypeId: MPGooglePayPaymentType,
    val cardDescription: String?,
) {
    override fun toString() = "MPGooglePayToken(paymentMethodId=$paymentMethodId, paymentTypeId=$paymentTypeId)"
}
