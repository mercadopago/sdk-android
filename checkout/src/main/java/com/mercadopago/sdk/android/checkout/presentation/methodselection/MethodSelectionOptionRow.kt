package com.mercadopago.sdk.android.checkout.presentation.methodselection

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.runtime.Composable
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionOption
import com.mercadopago.sdk.android.components.MPListItem
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo
import com.mercadopago.sdk.android.components.model.MPListItemLeading
import com.mercadopago.sdk.android.components.model.MPListItemTrailing
import com.mercadopago.sdk.android.components.model.MPListItemType

@Composable
internal fun MethodSelectionOptionRow(
    option: MethodSelectionOption,
    isArrowLayout: Boolean,
    isSelected: Boolean,
    onTap: (MethodSelectionOption) -> Unit,
) {
    MPListItem(
        contentInfo = MPListItemContentInfo(
            title = option.name,
            description = option.subtitle,
        ),
        leftImage = MPListItemLeading.Thumbnail(url = option.iconUrl.orEmpty()),
        trailing = if (isArrowLayout) {
            MPListItemTrailing(type = MPListItemTrailing.Type.Icon(Icons.AutoMirrored.Sharp.KeyboardArrowRight))
        } else {
            null
        },
        type = if (!isArrowLayout) MPListItemType.RadioButton(selected = isSelected) else null,
        onClick = { onTap(option) },
    )
}
