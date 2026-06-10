package com.mercadopago.sdk.android.checkout.domain.model

import java.math.BigDecimal

/**
 * Sealed class representing the payment data resulting from a successful checkout.
 * The subtype corresponds to the [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType]
 * configured in the builder.
 */
sealed class MPPaymentData {
    /**
     * Payment data for a [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType.CardSave] checkout.
     *
     * @property token Payment token generated for the transaction.
     * @property paymentMethodId Identifier of the selected payment method.
     * @property paymentTypeId Identifier of the selected payment type.
     * @property payer Payer information associated with the payment.
     * @property issuerId Optional identifier of the card issuer.
     */
    data class CardSave(
        val token: String,
        val paymentMethodId: String,
        val paymentTypeId: String,
        val payer: Payer?,
        val issuerId: String?,
    ) : MPPaymentData()

    /**
     * Payment data for a [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType.CardTransaction] checkout.
     *
     * @property transactionAmount Total amount of the transaction.
     * @property paymentMethodId Identifier of the selected payment method.
     * @property paymentTypeId Identifier of the selected payment type.
     * @property payer Payer information associated with the payment.
     * @property installment Number of installments selected.
     * @property issuerId Optional identifier of the card issuer.
     */
    data class CardTransaction(
        val transactionAmount: BigDecimal?,
        val paymentMethodId: String,
        val paymentTypeId: String,
        val payer: Payer?,
        val installment: Int?,
        val issuerId: String?,
    ) : MPPaymentData()

    /**
     * Payment data for a [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType.CardTransaction] checkout.
     *
     * @property transactionAmount Total amount of the transaction.
     * @property paymentMethodId Identifier of the selected payment method.
     * @property paymentTypeId Identifier of the selected payment type.
     * @property payer Payer information associated with the payment.
     * @property installment Number of installments selected.
     * @property issuerId Optional identifier of the card issuer.
     */
    data class Payment(
        val transactionAmount: BigDecimal?,
        val paymentMethodId: String,
        val paymentTypeId: String,
        val payer: Payer?,
        val installment: Int?,
        val issuerId: String?,
    ) : MPPaymentData()

    /**
     * Payment data for a [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType.CardTransaction] checkout.
     *
     * @property transactionAmount Total amount of the transaction.
     * @property paymentMethodId Identifier of the selected payment method.
     * @property paymentTypeId Identifier of the selected payment type.
     * @property payer Payer information associated with the payment.
     * @property installment Number of installments selected.
     * @property issuerId Optional identifier of the card issuer.
     */
    data class Payment(
        val transactionAmount: BigDecimal?,
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
