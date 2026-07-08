package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickInitializationResponse
import com.mercadopago.sdk.android.checkout.domain.mapper.toDomain
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput

internal fun PaymentBrickInitializationResponse.toDomain(): PaymentBrickInitializationOutput =
    PaymentBrickInitializationOutput(
        headerTitle = headerTitle,
        sections = sections.map { it.toDomain() },
        footer = footer.toDomain(),
    )
