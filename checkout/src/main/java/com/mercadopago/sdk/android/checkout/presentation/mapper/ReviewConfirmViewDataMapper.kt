package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmScreenState

internal fun ReviewConfirmViewData.toScreenState(
    emailChangeEnabled: Boolean,
): ReviewConfirmScreenState.Success =
    ReviewConfirmScreenState.Success(
        header = header.toUiModel(),
        items = items.map { it.toUiModel(emailChangeEnabled = emailChangeEnabled) },
        footerSummary = footerSummary?.toUiModel(),
        footer = footer.toUiModel(),
    )
