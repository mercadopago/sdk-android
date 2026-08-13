package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.runtime.Composable
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterSummaryUiModel
import com.mercadopago.sdk.android.components.MPListItem
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo

@Composable
internal fun ReviewConfirmFooterSummarySection(
    summary: ReviewConfirmFooterSummaryUiModel,
) {
    summary.products?.forEach { product ->
        MPListItem(
            contentInfo = MPListItemContentInfo(
                title = product.label,
                description = product.amount,
            ),
        )
    }

    summary.coupon?.let { coupon ->
        MPListItem(
            contentInfo = MPListItemContentInfo(
                title = coupon.label,
                description = coupon.amount,
            ),
        )
    }

    summary.interest?.let { interest ->
        MPListItem(
            contentInfo = MPListItemContentInfo(
                title = interest.title,
                description = interest.amount,
            ),
        )
    }
}
