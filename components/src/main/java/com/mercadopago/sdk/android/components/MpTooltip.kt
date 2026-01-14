package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val TOOLTIP_MAX_WIDTH_FRACTION: Float = 0.9f

/**
 * Tooltip component with two visual styles: Dark and Blue.
 *
 * - Renders a bubble with rounded corners and a pointer tail on the bottom-right.
 * - Displays a title, a description and a dismiss action ("×").
 */
@Composable
fun MpTooltip(
    title: String,
    description: String,
    style: MpTooltipStyle,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
) {
    val colors = MpTooltipColors(
        container = tooltipBackgroundColorPalette(style),
        close = MercadoPagoTheme.color.text.inverted,
    )

    val bubbleShape: Shape = TooltipBubbleShape(
        cornerRadius = MercadoPagoTheme.spacing.s,
        pointerWidth = MercadoPagoTheme.spacing.l,
        pointerHeight = MercadoPagoTheme.spacing.s,
        pointerEndOffset = MercadoPagoTheme.spacing.l,
    )

    Surface(
        modifier = modifier
            .defaultMinSize(minWidth = 240.dp, minHeight = 86.dp)
            .fillMaxWidth(TOOLTIP_MAX_WIDTH_FRACTION)
            .padding(MercadoPagoTheme.spacing.m),
        color = colors.container,
        shape = bubbleShape,
    ) {
        Column(
            modifier = Modifier
                .background(colors.container)
                .padding(
                    horizontal = MercadoPagoTheme.spacing.m,
                    vertical = MercadoPagoTheme.spacing.m,
                ),
            verticalArrangement = Arrangement.spacedBy(MercadoPagoTheme.spacing.s),
        ) {
            TooltipHeader(title = title, onDismiss = onDismiss, closeTint = colors.close)
            TooltipDescription(text = description)
        }
    }
}

@Composable
private fun TooltipHeader(
    title: String,
    onDismiss: () -> Unit,
    closeTint: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MPText(
            text = title,
            style = MercadoPagoAndesTheme.typography.body.emphasis.medium,
            color = MercadoPagoAndesTheme.color.text.inverse,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .size(MercadoPagoTheme.spacing.m)
                .padding(MercadoPagoTheme.spacing.xxs)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painterResource(R.drawable.mp_icon_close_x),
                "",
                tint = closeTint,
            )
        }
    }
}

@Composable
private fun TooltipDescription(
    text: String,
) {
    MPText(
        text = text,
        style = MercadoPagoAndesTheme.typography.body.default.small,
        color = MercadoPagoAndesTheme.color.text.inverse,
    )
}

/**
 * Defines the available visual styles for `MpTooltip`.
 *
 * - `Dark`: Uses an inverted background for high contrast.
 * - `Blue`: Uses the accent color background.
 */
enum class MpTooltipStyle {
    /** Dark style using inverted background. */
    Dark,

    /** Blue style using the accent color. */
    Blue,
}

private data class MpTooltipColors(
    val container: Color,
    val close: Color,
)

@Composable
private fun tooltipBackgroundColorPalette(
    style: MpTooltipStyle,
): Color {
    return when (style) {
        MpTooltipStyle.Dark -> MercadoPagoTheme.color.background.inverted
        MpTooltipStyle.Blue -> MercadoPagoTheme.color.accent
    }
}

/**
 * Bubble shape with rounded corners and a pointer tail at the bottom right.
 */
private class TooltipBubbleShape(
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
private fun PreviewMpTooltipDark() {
    MercadoPagoTheme {
        MpTooltip(
            title = "Title",
            description = "Text description.",
            style = MpTooltipStyle.Dark,
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMpTooltipBlue() {
    MercadoPagoTheme {
        MpTooltip(
            title = "Title can be this big",
            description = "Text description. this description can be much bigger",
            style = MpTooltipStyle.Blue,
        ) {}
    }
}
