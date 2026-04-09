package com.mercadopago.sdk.android.checkout.domain.model

import java.math.BigDecimal

/**
 * Contains the payment data resulting from a successful checkout.
 *
 * @property token Optional payment token generated for the transaction.
 * @property transactionAmount Total amount of the transaction.
 * @property paymentMethodId Optional identifier of the selected payment method.
 * @property paymentTypeId Optional identifier of the selected payment method.
 * @property payer Payer information associated with the payment.
 * @property installment Optional number of installments selected.
 * @property issuerId Optional identifier of the card issuer.
 */
data class MPPaymentData(
    val token: String,
    val transactionAmount: BigDecimal?,
    val paymentMethodId: String,
    val paymentTypeId: String,
    val payer: Payer?,
    val installment: Int?,
    val issuerId: String?,
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
