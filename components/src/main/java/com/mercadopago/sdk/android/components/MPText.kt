package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val TEXT_GROUP = "Text"

/**
 * Text component- This handle the text of the components
 * This component is used to build others components
 * handling the text implementation
 *
 * @param text: label text
 * @param modifier: label modifier
 * @param style: TextStyle to use
 * @param color: Color to use
 * @param fontWeight: FontWeight to use
 */
@Composable
fun MPText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MercadoPagoTheme.typography.body.default.small,
    color: Color = MercadoPagoTheme.color.text.primary,
    fontWeight: FontWeight? = null,
) {
    Text(
        text = text,
        style = style,
        color = color,
        modifier = modifier,
        fontWeight = fontWeight,
    )
}

@Preview(name = "Text Tittle Text", group = TEXT_GROUP)
@Composable
internal fun TextTittlePreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPText(
                text = "My Tittle Primary Text",
                style = MercadoPagoTheme.typography.body.default.medium,
                color = MercadoPagoTheme.color.text.primary,
            )
            MPText(
                text = "My Tittle Secondary Text",
                style = MercadoPagoTheme.typography.body.default.medium,
                color = MercadoPagoTheme.color.text.secondary,
            )
            MPText(
                text = "My Tittle Accent Text",
                style = MercadoPagoTheme.typography.body.default.medium,
                color = MercadoPagoTheme.color.text.accent,
            )
            MPText(
                text = "My Tittle Inverted Text",
                style = MercadoPagoTheme.typography.body.default.medium,
                color = MercadoPagoTheme.color.feedback.negative.textLoud,
            )
            MPText(
                text = "My Tittle Negative Text",
                style = MercadoPagoTheme.typography.body.default.medium,
                color = MercadoPagoTheme.color.text.inverse,
            )
            MPText(
                text = "My Tittle Disabled Text",
                style = MercadoPagoTheme.typography.body.default.medium,
                color = MercadoPagoTheme.color.text.disabled,
            )
            MPText(
                text = "My Tittle Positive Text",
                style = MercadoPagoTheme.typography.body.default.medium,
                color = MercadoPagoTheme.color.feedback.positive.textLoud,
            )
        }
    }
}
