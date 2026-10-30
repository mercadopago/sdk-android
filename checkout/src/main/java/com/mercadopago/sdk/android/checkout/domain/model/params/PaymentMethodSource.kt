package com.mercadopago.sdk.android.checkout.domain.model.params

internal enum class PaymentMethodSource(
    val wireValue: String,
) {
    SavedCard("saved_card"),
    NewCard("new_card"),
    Ticket("ticket"),
}
