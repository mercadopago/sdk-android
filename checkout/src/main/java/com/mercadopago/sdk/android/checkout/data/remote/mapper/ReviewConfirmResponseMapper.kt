package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmResponse
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData

internal fun ReviewConfirmResponse.toViewData(): ReviewConfirmViewData =
    ReviewConfirmViewData(
        header = header,
        items = items,
        footerSummary = footerSummary,
        footer = footer,
    )
