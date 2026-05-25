package com.mercadopago.sdk.android.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.mercadopago.sdk.android.components.extensions.isNotNull
import com.mercadopago.sdk.android.components.model.MPListItemContentInfo
import com.mercadopago.sdk.android.components.model.MPListItemLeading
import com.mercadopago.sdk.android.components.model.MPListItemTrailing
import com.mercadopago.sdk.android.components.model.MPListItemType
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val LIST_GROUP = "LIST_ITEM"

/** Size type of the list item. */
enum class MPListSizeType {
    /** Large size variant. */
    Large,

    /** Medium size variant. */
    Medium,
}

/**
 * List Item component
 * @param modifier component modifier
 * @param sizeType component size type (Large or Medium)
 * @param contentInfo component content information (title, header, description)
 * @param trailing component trailing content (text, icon, color)
 * @param leftImage component left image
 * @param type component type (RadioButton, etc.)
 * @param onClick component click action
 */
@Composable
fun MPListItem(
    modifier: Modifier = Modifier,
    sizeType: MPListSizeType = MPListSizeType.Large,
    contentInfo: MPListItemContentInfo,
    trailing: MPListItemTrailing? = null,
    leftImage: MPListItemLeading? = null,
    type: MPListItemType? = null,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xmicro,
                vertical = MercadoPagoTheme.spacing.paddings.xmicro,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.paddings.xmicro),
    ) {
        if (type is MPListItemType.RadioButton) {
            MPRadioButton(
                selected = type.selected,
                modifier = Modifier.size(20.dp),
            )
        }

        when (leftImage) {
            is MPListItemLeading.Icon -> Icon(
                imageVector = leftImage.icon,
                contentDescription = null,
                tint = leftImage.tint,
                modifier = Modifier.size(40.dp),
            )

            is MPListItemLeading.Thumbnail -> AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(leftImage.url)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(44.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )

            null -> Unit
        }

        Column(
            modifier = Modifier.weight(1f),
        ) {
            MPListItemContentInfo(sizeType = sizeType, contentInfo = contentInfo)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.paddings.xnano),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MPListItemTrailing(trailing = trailing)
        }
    }
}

@Composable
private fun MPListItemContentInfo(
    sizeType: MPListSizeType,
    contentInfo: MPListItemContentInfo,
) {
    if (contentInfo.header.isNotNull()) {
        MPText(
            text = contentInfo.header,
            style = if (sizeType == MPListSizeType.Large) {
                MercadoPagoTheme.typography.heading.narrow.medium
            } else {
                MercadoPagoTheme.typography.heading.narrow.small
            },
            color = MercadoPagoTheme.color.text.primary,
        )
    }
    MPText(
        text = contentInfo.title.orEmpty(),
        style = if (sizeType == MPListSizeType.Large) {
            MercadoPagoTheme.typography.heading.narrow.medium
        } else {
            MercadoPagoTheme.typography.heading.narrow.small
        },
        color = MercadoPagoTheme.color.text.primary,
    )
    if (contentInfo.description.isNotNull()) {
        MPText(
            text = contentInfo.description,
            style = MercadoPagoTheme.typography.body.default.medium,
            color = MercadoPagoTheme.color.text.secondary,
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
                style = MercadoPagoTheme.typography.body.default.medium,
                color = it.textColor ?: MercadoPagoTheme.color.text.secondary,
            )
        }

        when (it.type) {
            is MPListItemTrailing.Type.Icon ->
                Icon(
                    imageVector = it.type.icon,
                    contentDescription = null,
                    tint = MercadoPagoTheme.color.icon.accent,
                    modifier = Modifier.size(MercadoPagoTheme.radius.xlarge),
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
                    textColor = MercadoPagoTheme.color.fill.accentLoud,
                ),
            )
        }
    }
}

@Preview(name = "List Item - Leading Icon", group = LIST_GROUP, showBackground = true)
@Composable
private fun MPListItemLeadingIconPreview() {
    MercadoPagoTheme {
        Box(modifier = Modifier.padding(10.dp)) {
            MPListItem(
                contentInfo = MPListItemContentInfo(
                    title = "Title",
                    description = "Description",
                ),
                leftImage = MPListItemLeading.Icon(Icons.AutoMirrored.Sharp.KeyboardArrowRight),
                trailing = MPListItemTrailing(
                    type = MPListItemTrailing.Type.Icon(Icons.AutoMirrored.Sharp.KeyboardArrowRight),
                ),
            )
        }
    }
}

@Preview(name = "List Item - Leading Thumbnail", group = LIST_GROUP, showBackground = true)
@Composable
private fun MPListItemLeadingThumbnailPreview() {
    MercadoPagoTheme {
        Box(modifier = Modifier.padding(10.dp)) {
            MPListItem(
                contentInfo = MPListItemContentInfo(
                    title = "Title",
                    description = "Description",
                ),
                leftImage = MPListItemLeading.Thumbnail(url = "https://http.cat/200"),
                trailing = MPListItemTrailing(
                    type = MPListItemTrailing.Type.Icon(Icons.AutoMirrored.Sharp.KeyboardArrowRight),
                ),
            )
        }
    }
}
