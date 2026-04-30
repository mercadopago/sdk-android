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

private const val ISSUER_DOCUMENT_TYPE_SHEET_GROUP = "IssuerDocumentTypeSheet"

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
 * @param selectedItemId [MPDocumentTypeItem.id] of the currently selected item, or null
 * @param onItemSelected Callback invoked when the user selects an item
 * @param onDismiss Callback invoked when the user taps the dismiss (X) button
 * @param modifier Component modifier
 */
@Composable
fun MPIssuerDocumentTypeSheet(
    title: String,
    items: List<MPDocumentTypeItem>,
    selectedItemId: String?,
    onItemSelected: (MPDocumentTypeItem) -> Unit,
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
        IssuerDragIndicator()
        IssuerHeader(title = title, onDismiss = onDismiss)
        IssuerDocumentList(
            items = items,
            selectedItemId = selectedItemId,
            onItemSelected = onItemSelected,
        )
    }
}

/**
 * A document type option for [MPIssuerDocumentTypeSheet].
 *
 * @param id Unique identifier; drives selection state
 * @param label Display text shown in the list row
 */
data class MPDocumentTypeItem(
    val id: String,
    val label: String,
)

@Composable
private fun IssuerDragIndicator() {
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
private fun IssuerHeader(
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
private fun IssuerDocumentList(
    items: List<MPDocumentTypeItem>,
    selectedItemId: String?,
    onItemSelected: (MPDocumentTypeItem) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MercadoPagoTheme.spacing.paddings.xnano),
        verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.gap.xmicro),
    ) {
        items.forEach { item ->
            IssuerDocumentRow(
                item = item,
                selected = item.id == selectedItemId,
                onItemSelected = onItemSelected,
            )
        }
    }
}

@Composable
private fun IssuerDocumentRow(
    item: MPDocumentTypeItem,
    selected: Boolean,
    onItemSelected: (MPDocumentTypeItem) -> Unit,
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

@Preview(name = "IssuerDocumentTypeSheet - CPF Selected", group = ISSUER_DOCUMENT_TYPE_SHEET_GROUP)
@Composable
internal fun IssuerDocumentTypeSheetCpfSelectedPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPIssuerDocumentTypeSheet(
                title = "Documento do titular",
                items = listOf(
                    MPDocumentTypeItem(id = "CPF", label = "CPF"),
                    MPDocumentTypeItem(id = "CNPJ", label = "CNPJ"),
                    MPDocumentTypeItem(id = "OTRO", label = "Otro"),
                ),
                selectedItemId = "CPF",
                onItemSelected = {},
                onDismiss = {},
            )
        }
    }
}

@Preview(name = "IssuerDocumentTypeSheet - No Selection", group = ISSUER_DOCUMENT_TYPE_SHEET_GROUP)
@Composable
internal fun IssuerDocumentTypeSheetNoSelectionPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPIssuerDocumentTypeSheet(
                title = "Documento do titular",
                items = listOf(
                    MPDocumentTypeItem(id = "CPF", label = "CPF"),
                    MPDocumentTypeItem(id = "CNPJ", label = "CNPJ"),
                    MPDocumentTypeItem(id = "OTRO", label = "Otro"),
                ),
                selectedItemId = null,
                onItemSelected = {},
                onDismiss = {},
            )
        }
    }
}
