package com.mercadopago.sdk.android.checkout.domain.model

import java.math.BigDecimal

/**
 * Contains the payment data resulting from a successful checkout.
 *
 * Implementations:
 * - [CardToken] Checkout completed with a card token (card-form only scenario).
 * - [Order] Checkout completed with an order (payment scenario).
 */
sealed class MPPaymentData {
    /**
     * Payment data for the card-form only scenario.
     *
     * @property token Payment token generated for the transaction.
     * @property transactionAmount Total amount of the transaction.
     * @property paymentMethodId Identifier of the selected payment method.
     * @property paymentTypeId Identifier of the selected payment type.
     * @property payer Payer information associated with the payment.
     * @property installment Number of installments selected.
     * @property issuerId Identifier of the card issuer.
     */
    data class CardToken(
        val token: String,
        val transactionAmount: BigDecimal?,
        val paymentMethodId: String,
        val paymentTypeId: String,
        val payer: Payer?,
        val installment: Int?,
        val issuerId: String?,
    ) : MPPaymentData()

    /**
     * Payment data for the payment with order scenario.
     *
     * @property id Order identifier.
     * @property status Order status.
     * @property paymentMethodId Identifier of the selected payment method.
     * @property paymentTypeId Identifier of the selected payment type.
     * @property payer Payer information associated with the payment.
     * @property installment Number of installments selected.
     * @property issuerId Identifier of the card issuer.
     */
    data class Order(
        val id: String,
        val status: String,
        val paymentMethodId: String,
        val paymentTypeId: String,
        val payer: Payer?,
        val installment: Int?,
        val issuerId: String?,
    ) : MPPaymentData()
}

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
