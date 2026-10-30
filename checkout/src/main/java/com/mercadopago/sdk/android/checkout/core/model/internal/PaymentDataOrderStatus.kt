package com.mercadopago.sdk.android.checkout.core.model.internal

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData

internal fun MPPaymentData.orderStatusOrNull(): String? =
    when (this) {
        is MPPaymentData.Payment -> orderStatus
        is MPPaymentData.CardTransaction -> orderStatus
        is MPPaymentData.CardSave -> null
    }
