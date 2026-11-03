package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickFooterOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentSectionOutput
import com.mercadopago.sdk.android.checkout.presentation.extensions.parseFormattedAmount
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentOptionState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentSectionState

internal fun PaymentBrickInitializationOutput.toScreenState(): PaymentBrickScreenState =
    PaymentBrickScreenState(
        title = headerTitle,
        sections = sections.map { it.toSectionState() },
        footerState = footer.toFooterState(),
    )

private fun PaymentBrickFooterOutput.toFooterState(): FooterState {
    val amount = totalAmount.takeIf { it.isNotBlank() }?.parseFormattedAmount()
    return FooterState(
        title = totalLabel,
        currencySymbol = amount?.currencySymbol.orEmpty(),
        amountIntegerPart = amount?.integerPart.orEmpty(),
        amountDecimalPart = amount?.decimalPart.orEmpty(),
        isVisible = true,
    )
}

private fun PaymentSectionOutput.toSectionState(): PaymentSectionState =
    PaymentSectionState(
        title = title,
        options = methods.map { it.toOptionState() },
    )

private fun PaymentMethodOutput.toOptionState(): PaymentOptionState =
    PaymentOptionState(
        id = cardData?.id ?: type,
        title = title,
        thumbnailUrl = iconUrl,
        description = subtitle,
    )
