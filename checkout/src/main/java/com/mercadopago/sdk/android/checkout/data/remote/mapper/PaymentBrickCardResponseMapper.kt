package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickCardResponse
import com.mercadopago.sdk.android.checkout.domain.mapper.toDomain
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickCardOutput

internal fun PaymentBrickCardResponse.toDomain(): PaymentBrickCardOutput =
    PaymentBrickCardOutput(
        translations = translations.toDomain(),
        installment = installment?.toDomain(),
        paymentMethods = paymentMethods.map { it.toDomain() },
    )
