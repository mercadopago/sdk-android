package com.mercadopago.sdk.android.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.extensions.isNotNull
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo
import com.mercadopago.sdk.android.components.model.MPListItemTrailing
import com.mercadopago.sdk.android.components.model.MPListItemType
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val LIST_GROUP = "LIST_ITEM"

/**
 * List Item component
 * @param modifier component modifier
 * @param contentInfo component content information (title, header, description)
 * @param trailing component trailing content (text, icon, color)
 * @param leftImage component left image
 * @param type component type (RadioButton, etc.)
 * @param onClick component click action
 */
@Composable
fun MPListItem(
    modifier: Modifier = Modifier,
    contentInfo: MPListItemContentInfo,
    trailing: MPListItemTrailing? = null,
    leftImage: ImageVector? = null,
    type: MPListItemType? = null,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(MercadoPagoAndesTheme.spacing.paddings.xmicro),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MercadoPagoAndesTheme.spacing.paddings.xmicro),
    ) {
        if (type is MPListItemType.RadioButton) {
            MPRadioButton(
                selected = type.selected,
                modifier = Modifier.size(20.dp),
            )
        }

        if (leftImage.isNotNull()) {
            Icon(
                imageVector = leftImage,
                contentDescription = null,
                tint = MercadoPagoAndesTheme.color.icon.accent,
                modifier = Modifier.size(40.dp),
            )
        }

        Column(
            modifier = Modifier.weight(1f),
        ) {
            MPListItemContentInfo(contentInfo = contentInfo)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(MercadoPagoAndesTheme.spacing.paddings.xnano),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MPListItemTrailing(trailing = trailing)
        }
    }
}

@Composable
private fun MPListItemContentInfo(
    contentInfo: MPListItemContentInfo,
) {
    if (contentInfo.header.isNotNull()) {
        MPText(
            text = contentInfo.header,
            style = MercadoPagoAndesTheme.typography.body.default.medium,
            color = MercadoPagoAndesTheme.color.text.primary,
        )
    }
    MPText(
        text = contentInfo.title.orEmpty(),
        style = MercadoPagoAndesTheme.typography.body.default.medium,
        color = MercadoPagoAndesTheme.color.text.primary,
        fontWeight = FontWeight.Bold,
    )
    if (contentInfo.description.isNotNull()) {
        MPText(
            text = contentInfo.description,
            style = MercadoPagoAndesTheme.typography.body.default.small,
            color = MercadoPagoAndesTheme.color.text.secondary,
        )
    }
}

@Composable
private fun MPListItemTrailing(
    trailing: MPListItemTrailing?,
) {
    trailing?.let {
        if (it.text.isNotNull()) {
            MPText(
                text = it.text,
                style = MercadoPagoAndesTheme.typography.body.default.medium,
                color = it.textColor ?: MercadoPagoAndesTheme.color.text.secondary,
            )
        }

        when (it.type) {
            is MPListItemTrailing.Type.Icon ->
                Icon(
                    imageVector = it.type.icon,
                    contentDescription = null,
                    tint = MercadoPagoAndesTheme.color.icon.secondary,
                    modifier = Modifier.size(20.dp),
                )
            is MPListItemTrailing.Type.None, null -> Unit
        }
    }
}

@Preview(name = "List Item", group = LIST_GROUP, showBackground = true)
@Composable
private fun MPListItemPreview() {
    MercadoPagoTheme {
        Box(
            modifier = Modifier.padding(10.dp),
        ) {
            MPListItem(
                contentInfo = MPListItemContentInfo(
                    title = "Title",
                    description = "Description",
                ),
                trailing = MPListItemTrailing(
                    type = MPListItemTrailing.Type.Icon(Icons.AutoMirrored.Sharp.KeyboardArrowRight),
                    text = "$ 1.000",
                    textColor = MercadoPagoAndesTheme.color.fill.accentLoud,
                ),
            )
        }
    }
}
