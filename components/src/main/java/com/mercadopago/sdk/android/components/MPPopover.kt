package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

/**
 * Popover component with white background and dark text.
 *
 * - Renders a bubble with rounded corners and a pointer tail on the bottom-center.
 * - Displays a title, a description and a dismiss action ("×").
 */
@Composable
fun MPPopover(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
    onDismiss: () -> Unit = {},
) {
    val defaults = getPopoverDefaults()
    val bubbleShape: Shape = PopoverBubbleShape(
        cornerRadius = defaults.spacing.cornerRadius,
        pointerWidth = defaults.spacing.pointerWidth,
        pointerHeight = defaults.spacing.pointerHeight,
        pointerEndOffset = defaults.spacing.pointerEndOffset,
    )

    Surface(
        modifier = modifier
            .defaultMinSize(minWidth = 380.dp, minHeight = 86.dp)
            .padding(defaults.spacing.surfacePadding),
        color = defaults.colors.backgroundColor,
        shape = bubbleShape,
        shadowElevation = 5.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier
                    .background(defaults.colors.backgroundColor)
                    .weight(1f)
                    .padding(
                        horizontal = defaults.spacing.contentPaddingHorizontal,
                        vertical = defaults.spacing.contentPaddingVertical,
                    ),
                verticalArrangement = Arrangement.spacedBy(defaults.spacing.contentGap),
            ) {
                if (title.isNotBlank()) {
                    MPText(
                        text = title,
                        style = MercadoPagoTheme.typography.heading.default.medium,
                        color = MercadoPagoTheme.color.text.primary,
                    )
                }
                if (description.isNotBlank()) {
                    MPText(
                        text = description,
                        style = MercadoPagoTheme.typography.body.default.medium,
                        color = MercadoPagoTheme.color.text.primary,
                    )
                }
            }

            Icon(
                painterResource(R.drawable.mp_icon_close_x),
                "Close ",
                tint = defaults.colors.closeIconColor,
                modifier = Modifier
                    .size(23.dp)
                    .padding(top = 10.dp)
                    .clickable { onDismiss() },
            )
            Spacer(Modifier.size(10.dp))
        }
    }
}

internal data class PopoverDefaults(
    val colors: PopoverColorDefaults,
    val spacing: PopoverSpacingDefaults,
)

internal data class PopoverColorDefaults(
    val backgroundColor: Color,
    val titleTextColor: Color,
    val descriptionTextColor: Color,
    val closeIconColor: Color,
)

internal data class PopoverSpacingDefaults(
    val cornerRadius: Dp,
    val pointerWidth: Dp,
    val pointerHeight: Dp,
    val pointerEndOffset: Dp,
    val surfacePadding: Dp,
    val contentPaddingHorizontal: Dp,
    val contentPaddingVertical: Dp,
    val contentGap: Dp,
    val iconSize: Dp,
    val iconPadding: Dp,
)

@Composable
private fun getPopoverDefaults(): PopoverDefaults {
    return PopoverDefaults(
        colors = PopoverColorDefaults(
            backgroundColor = MercadoPagoTheme.color.surface.primaryIdle,
            titleTextColor = MercadoPagoTheme.color.text.primary,
            descriptionTextColor = MercadoPagoTheme.color.text.secondary,
            closeIconColor = MercadoPagoTheme.color.interactive.icon.idle,
        ),
        spacing = PopoverSpacingDefaults(
            cornerRadius = MercadoPagoTheme.radius.xlarge,
            pointerWidth = MercadoPagoTheme.spacing.paddings.xtiny,
            pointerHeight = MercadoPagoTheme.spacing.paddings.xnano,
            pointerEndOffset = MercadoPagoTheme.spacing.gap.micro,
            surfacePadding = MercadoPagoTheme.spacing.paddings.micro,
            contentPaddingHorizontal = MercadoPagoTheme.spacing.paddings.micro,
            contentPaddingVertical = MercadoPagoTheme.spacing.paddings.micro,
            contentGap = MercadoPagoTheme.spacing.gap.nano,
            iconSize = MercadoPagoTheme.spacing.paddings.micro,
            iconPadding = MercadoPagoTheme.spacing.paddings.pico,
        ),
    )
}

/**
 * Bubble shape with rounded corners and a pointer tail at the bottom center.
 */
private class PopoverBubbleShape(
    private val cornerRadius: Dp,
    private val pointerWidth: Dp,
    private val pointerHeight: Dp,
    private val pointerEndOffset: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        with(density) {
            val radiusPx: Float = cornerRadius.toPx()
            val pointerWidthPx: Float = pointerWidth.toPx()
            val pointerHeightPx: Float = pointerHeight.toPx()
            val endOffsetPx: Float = pointerEndOffset.toPx()

            val path = Path()

            val width: Float = size.width
            val height: Float = size.height

            val tailBaseRightX: Float = width - endOffsetPx
            val tailBaseLeftX: Float = tailBaseRightX - pointerWidthPx
            val tailTipX: Float = tailBaseLeftX + pointerWidthPx / 2f
            val tailTipY: Float = height + pointerHeightPx

            // Start from top-left corner
            path.moveTo(x = radiusPx, y = 0f)
            // Top edge and top-right corner
            path.lineTo(x = width - radiusPx, y = 0f)
            path.quadraticTo(x1 = width, y1 = 0f, x2 = width, y2 = radiusPx)
            // Right edge
            path.lineTo(x = width, y = height - radiusPx)
            path.quadraticTo(x1 = width, y1 = height, x2 = width - radiusPx, y2 = height)
            // Bottom edge until tail base right
            path.lineTo(x = tailBaseRightX, y = height)
            // Tail
            path.lineTo(x = tailTipX, y = tailTipY)
            path.lineTo(x = tailBaseLeftX, y = height)
            // Continue bottom edge to left corner
            path.lineTo(x = radiusPx, y = height)
            path.quadraticTo(x1 = 0f, y1 = height, x2 = 0f, y2 = height - radiusPx)
            // Left edge and close
            path.lineTo(x = 0f, y = radiusPx)
            path.quadraticTo(x1 = 0f, y1 = 0f, x2 = radiusPx, y2 = 0f)
            path.close()

            return Outline.Generic(path)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMPPopover() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        MPPopover(
            title = "Title",
            description = "This can be a single or multiline message",
            onDismiss = {},
        )
    }
}
