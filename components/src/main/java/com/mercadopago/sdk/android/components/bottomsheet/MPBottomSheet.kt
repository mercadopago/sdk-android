package com.mercadopago.sdk.android.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.R
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

internal const val MP_BOTTOM_SHEET_GROUP = "MPBottomSheet"

/**
 * Generic bottom sheet container with a drag indicator and a header.
 *
 * Intended to be hosted inside a system ModalBottomSheet or equivalent.
 * Use [MPListBottomSheet] for the document-type selection use case.
 *
 * Note: The design specifies a top-directed shadow (0 -2dp 4dp rgba(40,40,52,0.1)).
 * No foundation token covers this effect — elevation should be handled by the host container.
 *
 * @param title Header title text
 * @param onDismiss Callback invoked when the user taps the dismiss (X) button
 * @param modifier Component modifier
 * @param content Slot rendered below the header
 */
@Composable
fun MPBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val topShape = RoundedCornerShape(
        topStart = MercadoPagoTheme.radius.xlarge,
        topEnd = MercadoPagoTheme.radius.xlarge,
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(topShape)
            .background(MercadoPagoTheme.color.surface.primaryIdle)
            .navigationBarsPadding(),
    ) {
        MPBottomSheetDragIndicator()
        MPBottomSheetHeader(title = title, onDismiss = onDismiss)
        content()
    }
}

@Composable
private fun MPBottomSheetDragIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(MercadoPagoTheme.spacing.paddings.tiny)
            .clearAndSetSemantics {},
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
                .semantics { role = Role.Button }
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.mp_icon_close_x),
                contentDescription = stringResource(R.string.mp_bottom_sheet_close_description),
                tint = MercadoPagoTheme.color.interactive.icon.idle,
            )
        }
    }
}

@Preview(name = "MPBottomSheet - Custom Content", group = MP_BOTTOM_SHEET_GROUP)
@Composable
internal fun MPBottomSheetPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPBottomSheet(
                title = "Título do bottom sheet",
                onDismiss = {},
            ) {
                MPText(
                    text = "Conteúdo customizado",
                    style = MercadoPagoTheme.typography.body.default.large,
                    color = MercadoPagoTheme.color.text.primary,
                    modifier = Modifier.padding(MercadoPagoTheme.spacing.paddings.xnano),
                )
            }
        }
    }
}
