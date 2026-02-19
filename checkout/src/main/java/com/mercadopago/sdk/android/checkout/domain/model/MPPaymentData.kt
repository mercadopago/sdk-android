package com.mercadopago.sdk.android.checkout.domain.model

/**
 * Contains the payment data resulting from a successful checkout.
 *
 * @property transactionAmount Total amount of the transaction in the smallest currency unit.
 * @property token Optional payment token generated for the transaction.
 * @property installment Optional number of installments selected.
 * @property paymentMethodId Optional identifier of the selected payment method.
 * @property issuerId Optional identifier of the card issuer.
 * @property payer Payer information associated with the payment.
 */
data class MPPaymentData(
    val transactionAmount: Int,
    val token: String? = null,
    val installment: Int? = null,
    val paymentMethodId: String? = null,
    val issuerId: String? = null,
    val payer: Payer? = null,
)

/**
 * Represents the payer information for a payment.
 *
 * @property documentType Optional type of identification document (e.g. DNI, CPF).
 * @property documentNumber Optional identification document number.
 */
data class Payer(
    val documentType: String? = null,
    val documentNumber: String? = null,
)
