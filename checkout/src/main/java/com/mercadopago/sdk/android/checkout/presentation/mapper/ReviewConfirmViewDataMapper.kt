package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmScreenState

internal fun ReviewConfirmViewData.toScreenState(): ReviewConfirmScreenState.Success =
    ReviewConfirmScreenState.Success(
        header = header.toUiModel(),
        items = items.map { it.toUiModel() },
        footerSummary = footerSummary?.toUiModel(),
        footer = footer.toUiModel(),
    )
