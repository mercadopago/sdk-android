package com.mercadopago.sdk.android.checkout.domain.model.params

internal sealed class PaymentMethodType {
    internal data class Card(
        val paymentMethodId: String,
        val paymentMethodType: String,
        val token: String,
        val installments: Int,
        val source: PaymentMethodSource? = null,
    ) : PaymentMethodType()

    internal data class Ticket(
        val paymentMethodId: String,
    ) : PaymentMethodType()

    internal companion object {
        internal const val TICKET_TYPE = "ticket"
    }
}

internal fun ProcessOrderParams.toPaymentMethodType(): PaymentMethodType =
    if (paymentMethodType == PaymentMethodType.TICKET_TYPE) {
        PaymentMethodType.Ticket(paymentMethodId = paymentMethodId)
    } else {
        PaymentMethodType.Card(
            paymentMethodId = paymentMethodId,
            paymentMethodType = paymentMethodType,
            token = token,
            installments = installments,
            source = paymentMethodSource,
        )
    }
