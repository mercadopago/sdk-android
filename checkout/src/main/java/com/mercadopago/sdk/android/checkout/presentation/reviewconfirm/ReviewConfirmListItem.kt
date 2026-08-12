package com.mercadopago.sdk.android.checkout.presentation.reviewconfirm

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmItem
import com.mercadopago.sdk.android.components.MPListItem
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo
import com.mercadopago.sdk.android.components.model.MPListItemTrailing

@Composable
internal fun ReviewConfirmListItem(
    item: ReviewConfirmItem,
    onChangeClick: (String) -> Unit,
) {
    val hasChange = item.changeLabel != null
    MPListItem(
        contentInfo = MPListItemContentInfo(
            title = item.label,
            description = item.value,
        ),
        trailing = if (hasChange) {
            MPListItemTrailing(
                text = item.changeLabel,
                type = MPListItemTrailing.Type.Icon(
                    icon = Icons.AutoMirrored.Sharp.KeyboardArrowRight,
                ),
            )
        } else {
            null
        },
        onClick = if (hasChange) {
            { onChangeClick(item.type) }
        } else {
            {}
        },
        modifier = if (hasChange) {
            Modifier.clearAndSetSemantics {
                role = Role.Button
                contentDescription = "${item.label}: ${item.value}. ${item.changeLabel}"
            }
        } else {
            Modifier
        },
    )
}
