package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.FooterSummaryInterest
import com.mercadopago.sdk.android.checkout.domain.model.FooterSummaryRow
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooter
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooterSummary
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmHeader
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmItem
import com.mercadopago.sdk.android.checkout.presentation.model.FooterSummaryInterestUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.FooterSummaryRowUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterSummaryUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmHeaderUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmItemUiModel

internal fun ReviewConfirmHeader.toUiModel() =
    ReviewConfirmHeaderUiModel(
        title = title,
        sellerName = sellerName,
        sellerIconUrl = sellerIconUrl,
    )

internal fun ReviewConfirmItem.toUiModel() =
    ReviewConfirmItemUiModel(
        type = type,
        label = label,
        value = value,
        changeLabel = changeLabel,
    )

internal fun ReviewConfirmFooterSummary.toUiModel() =
    ReviewConfirmFooterSummaryUiModel(
        products = products?.map { it.toUiModel() },
        coupon = coupon?.toUiModel(),
        interest = interest?.toUiModel(),
    )

internal fun FooterSummaryRow.toUiModel() =
    FooterSummaryRowUiModel(
        label = label,
        amount = amount,
    )

internal fun FooterSummaryInterest.toUiModel() =
    FooterSummaryInterestUiModel(
        title = title,
        tooltipMessage = tooltipMessage,
        amount = amount,
    )

internal fun ReviewConfirmFooter.toUiModel() =
    ReviewConfirmFooterUiModel(
        buttonLabel = button.label,
        totalAmount = totalAmount,
        description = description,
        interestLabel = interestLabel,
    )
