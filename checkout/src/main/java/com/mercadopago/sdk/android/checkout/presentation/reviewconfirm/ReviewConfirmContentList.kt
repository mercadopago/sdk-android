package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmFooterSummaryUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmHeaderUiModel
import com.mercadopago.sdk.android.checkout.presentation.model.ReviewConfirmItemUiModel
import com.mercadopago.sdk.android.components.MPListItem
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo

@Composable
internal fun ReviewConfirmContentList(
    header: ReviewConfirmHeaderUiModel,
    items: List<ReviewConfirmItemUiModel>,
    footerSummary: ReviewConfirmFooterSummaryUiModel?,
    footerHeightDp: Dp,
    onChangeClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .padding(bottom = footerHeightDp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        header.sellerName?.let { sellerName ->
            MPListItem(
                contentInfo = MPListItemContentInfo(
                    title = sellerName,
                ),
            )
        }

        items.forEach { item ->
            ReviewConfirmListItem(
                item = item,
                onChangeClick = onChangeClick,
            )
        }

        footerSummary?.let { summary ->
            ReviewConfirmFooterSummarySection(summary = summary)
        }
    }
}
