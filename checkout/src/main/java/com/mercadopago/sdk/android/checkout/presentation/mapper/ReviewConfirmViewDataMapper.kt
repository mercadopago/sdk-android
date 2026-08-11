package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmScreenState
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData

internal fun ReviewConfirmViewData.toScreenState(): ReviewConfirmScreenState.Success =
    ReviewConfirmScreenState.Success(
        header = header,
        items = items,
        footerSummary = footerSummary,
        footer = footer,
    )
