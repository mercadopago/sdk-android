package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val TOOLTIP_GROUP = "Tooltip"

/**
 * Tooltip component - displays short contextual text near a target element on interaction.
 *
 * Width behavior follows Compose modifier conventions:
 * - Hug (default): tooltip wraps content width — pass no width modifier
 * - Fixed: tooltip fills available width — pass e.g. Modifier.fillMaxWidth()
 *
 * Dismiss-on-scroll is the caller's responsibility: listen to the parent scroll state and
 * hide the tooltip accordingly. The Flowbook spec (PR #45) defines this behavior for CVV tooltips.
 *
 * @param text the contextual label to display
 * @param modifier component modifier
 * @param maxWidth maximum width constraint; defaults to 296.dp per Flowbook CVV tooltip spec (PR #45)
 */
@Composable
fun MPTooltip(
    text: String,
    modifier: Modifier = Modifier,
    maxWidth: Dp = 296.dp, // sem token: 296dp fora da escala de spacing (Flowbook PR #45)
) {
    val shape = MercadoPagoTheme.shape.tiny
    MPText(
        text = text,
        style = MercadoPagoTheme.typography.body.default.small,
        color = MercadoPagoTheme.color.text.inverse,
        modifier = Modifier
            .widthIn(max = maxWidth)
            .then(modifier)
            .shadow(elevation = 2.dp, shape = shape) // sem token: foundation não possui token de elevation
            .background(
                color = MercadoPagoTheme.color.fill.inverse,
                shape = shape,
            )
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xmicro,
                vertical = MercadoPagoTheme.spacing.paddings.pico,
            ),
    )
}

@Preview(name = "Tooltip - Hug", group = TOOLTIP_GROUP)
@Composable
internal fun TooltipHugPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPTooltip(text = "Label")
        }
    }
}

@Preview(name = "Tooltip - Fixed", group = TOOLTIP_GROUP)
@Composable
internal fun TooltipFixedPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPTooltip(
                text = "Label",
                modifier = Modifier.padding(horizontal = MercadoPagoTheme.spacing.paddings.small),
            )
        }
    }
}

@Preview(name = "Tooltip - CVV", group = TOOLTIP_GROUP)
@Composable
internal fun TooltipCvvPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Default) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
        ) {
            MPTooltip(
                text = "Son 3 números que puedes encontrar en el dorso de la tarjeta o en la app de tu banco.",
                maxWidth = 296.dp,
            )
        }
    }
}
