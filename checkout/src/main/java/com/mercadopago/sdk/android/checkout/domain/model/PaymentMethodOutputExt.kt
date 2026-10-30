package com.mercadopago.sdk.android.checkout.domain.model

internal val PaymentMethodOutput.isTicket: Boolean get() = type == "ticket"
