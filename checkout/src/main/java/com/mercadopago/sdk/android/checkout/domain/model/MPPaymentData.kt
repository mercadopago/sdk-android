package com.mercadopago.sdk.android.checkout.domain.model

import java.math.BigDecimal

/**
 * Contains the payment data resulting from a successful checkout.
 *
 * In the card-form only scenario, [token] is populated and [orderId]/[orderStatus] are null.
 * In the payment with order scenario, [orderId] and [orderStatus] are populated and [token] is null.
 *
 * @property token Payment token generated for the transaction. Present in the card-form only scenario.
 * @property orderId Order identifier. Present in the payment with order scenario.
 * @property orderStatus Order status. Present in the payment with order scenario.
 * @property transactionAmount Total amount of the transaction.
 * @property paymentMethodId Identifier of the selected payment method.
 * @property paymentTypeId Identifier of the selected payment type.
 * @property payer Payer information associated with the payment.
 * @property installment Number of installments selected.
 * @property issuerId Identifier of the card issuer.
 */
data class MPPaymentData(
    val token: String? = null,
    val orderId: String? = null,
    val orderStatus: String? = null,
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
