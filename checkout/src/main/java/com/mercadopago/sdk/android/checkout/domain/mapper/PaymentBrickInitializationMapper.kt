package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickFooter
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentMethod
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentSection
import com.mercadopago.sdk.android.checkout.data.remote.response.TicketOption
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickFooterOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentSectionOutput
import com.mercadopago.sdk.android.checkout.domain.model.TicketOptionOutput

internal fun PaymentSection.toDomain(): PaymentSectionOutput =
    PaymentSectionOutput(
        title = title,
        methods = methods.map { it.toDomain() },
    )

internal fun PaymentMethod.toDomain(): PaymentMethodOutput =
    PaymentMethodOutput(
        type = type,
        title = title,
        subtitle = subtitle,
        iconUrl = iconUrl,
        cardData = cardData?.toDomain(),
        options = options?.map { it.toDomain() },
    )

internal fun TicketOption.toDomain(): TicketOptionOutput =
    TicketOptionOutput(
        id = id,
        name = name,
        iconUrl = iconUrl,
    )

internal fun PaymentBrickFooter.toDomain(): PaymentBrickFooterOutput =
    PaymentBrickFooterOutput(
        totalLabel = totalLabel,
        totalAmount = totalAmount,
    )
