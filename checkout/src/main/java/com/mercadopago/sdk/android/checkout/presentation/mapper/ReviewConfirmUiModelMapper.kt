package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.FooterSummaryInterest
import com.mercadopago.sdk.android.checkout.domain.model.FooterSummaryRow
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooter
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmFooterSummary
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmHeader
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmInstallments
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmItem
import com.mercadopago.sdk.android.checkout.presentation.model.FooterSummaryInterestUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.FooterSummaryRowUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterSummaryUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmHeaderUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmInstallmentsUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmItemUiModel
import com.mercadopago.sdk.android.checkout.presentation.state.ITEM_TYPE_PAYER_EMAIL

internal fun ReviewConfirmHeader.toUiModel() =
    ReviewConfirmHeaderUiModel(
        title = title,
        sellerName = sellerName,
        sellerIconUrl = sellerIconUrl,
    )

internal fun ReviewConfirmItem.toUiModel(
    emailChangeEnabled: Boolean,
) = ReviewConfirmItemUiModel(
    type = type,
    label = label,
    value = value,
    buttonLabel = if (type == ITEM_TYPE_PAYER_EMAIL && !emailChangeEnabled) null else button?.label,
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
        currencySymbol = currencySymbol,
        totalAmount = totalAmount,
        totalLabel = totalLabel,
        installments = installments?.toUiModel(),
        description = description,
        interestLabel = interestLabel,
    )

internal fun ReviewConfirmInstallments.toUiModel() =
    ReviewConfirmInstallmentsUiModel(
        label = label,
        secondaryLabel = secondaryLabel,
        state = state,
    )
