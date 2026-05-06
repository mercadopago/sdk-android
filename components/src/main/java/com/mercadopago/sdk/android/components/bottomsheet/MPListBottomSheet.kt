package com.mercadopago.sdk.android.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.model.MPBottomSheetListItem
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SELECTION_DISMISS_DELAY_MS = 300L

/**
 * Modal bottom sheet for selecting from a list of items.
 *
 * Manages its own [ModalBottomSheet] lifecycle, including:
 * - Instant visual selection on tap (no wait for state propagation)
 * - 300ms delay after selection before animating close
 * - Slide-down animation on dismiss via X button or programmatic close
 *
 * @param title Header title text
 * @param items Ordered list of options
 * @param selectedLabel [MPBottomSheetListItem.label] of the currently selected item, or null
 * @param onItemSelected Callback invoked immediately when the user taps an item
 * @param onDismiss Callback invoked after the sheet finishes closing
 * @param modifier Applied to the inner sheet content
 */
@Composable
fun MPListBottomSheet(
    title: String,
    items: List<MPBottomSheetListItem>,
    selectedLabel: String?,
    onItemSelected: (MPBottomSheetListItem) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var pendingSelectedLabel by remember { mutableStateOf<String?>(null) }

    fun hideWithAnimation() {
        scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.Transparent,
        dragHandle = null,
        contentWindowInsets = { WindowInsets(0) },
    ) {
        MPListBottomSheetContent(
            title = title,
            items = items,
            selectedLabel = pendingSelectedLabel ?: selectedLabel,
            onItemSelected = { item ->
                pendingSelectedLabel = item.label
                onItemSelected(item)
                scope.launch {
                    delay(SELECTION_DISMISS_DELAY_MS)
                    sheetState.hide()
                }.invokeOnCompletion {
                    onDismiss()
                    pendingSelectedLabel = null
                }
            },
            onDismiss = ::hideWithAnimation,
            modifier = modifier,
        )
    }
}

@Composable
private fun MPListBottomSheetContent(
    title: String,
    items: List<MPBottomSheetListItem>,
    selectedLabel: String?,
    onItemSelected: (MPBottomSheetListItem) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MPBottomSheet(title = title, onDismiss = onDismiss, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
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
            .semantics {
                role = Role.RadioButton
                this.selected = selected
            }
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ) { onItemSelected(item) },
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
            MPListBottomSheetContent(
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
            MPListBottomSheetContent(
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
