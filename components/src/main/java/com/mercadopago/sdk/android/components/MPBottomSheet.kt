package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val MP_BOTTOM_SHEET_GROUP = "MPBottomSheet"

/**
 * Bottom sheet for selecting the cardholder's document type.
 *
 * Renders a drag indicator, a header with [title] and dismiss button, and a list of
 * selectable [items]. The selected item is highlighted with a quiet-fill background
 * and a loud-fill vertical pipe on its left edge.
 *
 * Intended to be hosted inside a system ModalBottomSheet or equivalent.
 *
 * Note: The design specifies a top-directed shadow (0 -2dp 4dp rgba(40,40,52,0.1)).
 * No foundation token covers this effect — elevation should be handled by the host container.
 *
 * @param title Header title text
 * @param items Ordered list of document type options
 * @param selectedLabel [MPBottomSheetListItem.label] of the currently selected item, or null
 * @param onItemSelected Callback invoked when the user selects an item
 * @param onDismiss Callback invoked when the user taps the dismiss (X) button
 * @param modifier Component modifier
 */
@Composable
fun MPBottomSheet(
    title: String,
    items: List<MPBottomSheetListItem>,
    selectedLabel: String?,
    onItemSelected: (MPBottomSheetListItem) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val topShape = RoundedCornerShape(
        topStart = MercadoPagoTheme.radius.xlarge,
        topEnd = MercadoPagoTheme.radius.xlarge,
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(topShape)
            .background(MercadoPagoTheme.color.surface.primaryIdle),
    ) {
        MPBottomSheetDragIndicator()
        MPBottomSheetHeader(title = title, onDismiss = onDismiss)
        MPBottomSheetList(
            items = items,
            selectedLabel = selectedLabel,
            onItemSelected = onItemSelected,
        )
    }
}

/**
 * A document type option for [MPBottomSheet].
 *
 * @param label Display text shown in the list row; drives selection state
 */
data class MPBottomSheetListItem(
    val label: String,
)

@Composable
private fun MPBottomSheetDragIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MercadoPagoTheme.spacing.paddings.tiny),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(MercadoPagoTheme.spacing.paddings.small)
                .height(MercadoPagoTheme.spacing.paddings.xnano)
                .clip(MercadoPagoTheme.shape.full)
                .background(MercadoPagoTheme.color.interactive.icon.idle),
        )
    }
}

@Composable
private fun MPBottomSheetHeader(
    title: String,
    onDismiss: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MercadoPagoTheme.color.surface.primaryIdle)
            .padding(horizontal = MercadoPagoTheme.spacing.gap.xmicro),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MPText(
            text = title,
            style = MercadoPagoTheme.typography.heading.default.medium,
            color = MercadoPagoTheme.color.text.primary,
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = MercadoPagoTheme.spacing.paddings.xmicro,
                    top = MercadoPagoTheme.spacing.paddings.xmicro,
                    bottom = MercadoPagoTheme.spacing.paddings.xmicro,
                ),
        )
        Box(
            modifier = Modifier
                .padding(
                    start = MercadoPagoTheme.spacing.paddings.xnano,
                    top = MercadoPagoTheme.spacing.paddings.xnano,
                    bottom = MercadoPagoTheme.spacing.paddings.xmicro,
                )
                .size(MercadoPagoTheme.spacing.gap.xsmall)
                .clip(MercadoPagoTheme.shape.small)
                .background(MercadoPagoTheme.color.interactive.fillMute.idle)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.mp_icon_close_x),
                contentDescription = null,
                tint = MercadoPagoTheme.color.interactive.icon.idle,
            )
        }
    }
}

@Composable
private fun MPBottomSheetList(
    items: List<MPBottomSheetListItem>,
    selectedLabel: String?,
    onItemSelected: (MPBottomSheetListItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MercadoPagoTheme.spacing.paddings.xnano),
        verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.gap.xmicro),
    ) {
        items.forEach { item ->
            MPBottomSheetRow(
                item = item,
                selected = item.label == selectedLabel,
                onItemSelected = onItemSelected,
            )
        }
    }
}

@Composable
private fun MPBottomSheetRow(
    item: MPBottomSheetListItem,
    selected: Boolean,
    onItemSelected: (MPBottomSheetListItem) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MercadoPagoTheme.shape.small)
            .background(
                if (selected) {
                    MercadoPagoTheme.color.interactive.fillQuiet.idle
                } else {
                    MercadoPagoTheme.color.interactive.fillMute.idle
                },
            )
            .clickable { onItemSelected(item) },
    ) {
        MPText(
            text = item.label,
            style = MercadoPagoTheme.typography.body.default.large,
            color = MercadoPagoTheme.color.text.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MercadoPagoTheme.spacing.paddings.micro,
                    top = MercadoPagoTheme.spacing.paddings.xtiny,
                    bottom = MercadoPagoTheme.spacing.paddings.xtiny,
                ),
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(
                        start = MercadoPagoTheme.spacing.paddings.xnano,
                        top = MercadoPagoTheme.spacing.paddings.nano,
                        bottom = MercadoPagoTheme.spacing.paddings.nano,
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .width(MercadoPagoTheme.borderWidth.medium)
                        .fillMaxHeight()
                        .clip(MercadoPagoTheme.shape.full)
                        .background(MercadoPagoTheme.color.interactive.fillLoud.idle),
                )
            }
        }
    }
}

@Preview(name = "MPBottomSheet - CPF Selected", group = MP_BOTTOM_SHEET_GROUP)
@Composable
internal fun MPBottomSheetCpfSelectedPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPBottomSheet(
                title = "Documento do titular",
                items = listOf(
                    MPBottomSheetListItem(label = "CPF"),
                    MPBottomSheetListItem(label = "CNPJ"),
                    MPBottomSheetListItem(label = "Otro"),
                ),
                selectedLabel = "CPF",
                onItemSelected = {},
                onDismiss = {},
            )
        }
    }
}

@Preview(name = "MPBottomSheet - No Selection", group = MP_BOTTOM_SHEET_GROUP)
@Composable
internal fun MPBottomSheetNoSelectionPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPBottomSheet(
                title = "Documento do titular",
                items = listOf(
                    MPBottomSheetListItem(label = "CPF"),
                    MPBottomSheetListItem(label = "CNPJ"),
                    MPBottomSheetListItem(label = "Otro"),
                ),
                selectedLabel = null,
                onItemSelected = {},
                onDismiss = {},
            )
        }
    }
}
