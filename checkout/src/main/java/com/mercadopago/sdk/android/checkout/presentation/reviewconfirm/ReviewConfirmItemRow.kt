package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmItem
import com.mercadopago.sdk.android.components.MPListItem
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo
import com.mercadopago.sdk.android.components.model.MPListItemTrailing

@Composable
internal fun ReviewConfirmItemRow(
    item: ReviewConfirmItem,
    onChangeClick: () -> Unit = {},
) {
    val clickHandler: () -> Unit = if (item.changeLabel != null) onChangeClick else ({})
    MPListItem(
        modifier = Modifier.fillMaxWidth(),
        contentInfo = MPListItemContentInfo(
            title = item.label,
            description = item.value,
        ),
        trailing = item.changeLabel?.let { MPListItemTrailing(text = it) },
        onClick = clickHandler,
    )
}
