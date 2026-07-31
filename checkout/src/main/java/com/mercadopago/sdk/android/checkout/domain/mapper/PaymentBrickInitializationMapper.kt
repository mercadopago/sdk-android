package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.MethodSelectionOptionResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.MethodSelectionScreenFooterResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.MethodSelectionScreenResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickFooter
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentMethod
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentSection
import com.mercadopago.sdk.android.checkout.data.remote.response.TicketOption
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionLayoutType
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenButton
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenFooter
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
        screen = screen?.toDomain(),
    )

internal fun MethodSelectionScreenResponse.toDomain(): MethodSelectionScreenData =
    MethodSelectionScreenData(
        headerTitle = headerTitle,
        selectionType = when (selectionType) {
            "chevron" -> MethodSelectionLayoutType.CHEVRON
            else -> MethodSelectionLayoutType.RADIO_BUTTON
        },
        footer = footer.toDomain(),
        options = options.map { it.toDomain() },
    )

internal fun MethodSelectionScreenFooterResponse.toDomain(): MethodSelectionScreenFooter =
    MethodSelectionScreenFooter(
        totalLabel = totalLabel,
        totalAmount = totalAmount,
        button = button?.let { MethodSelectionScreenButton(label = it.label) },
    )

internal fun MethodSelectionOptionResponse.toDomain(): MethodSelectionOption =
    MethodSelectionOption(id = id, name = name, subtitle = subtitle, iconUrl = iconUrl)

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
