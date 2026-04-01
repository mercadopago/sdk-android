package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val PILL_GROUP = "Pill"

/**
 * Pill Component
 * @param text component text to be showed
 * @param modifier component modifier
 */
@Composable
fun MPPill(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(MercadoPagoAndesTheme.spacing.paddings.xtiny)
            .background(
                color = MercadoPagoAndesTheme.color.feedback.positive.fillQuiet,
                shape = RoundedCornerShape(MercadoPagoAndesTheme.spacing.paddings.xtiny),
            )
            .padding(horizontal = MercadoPagoAndesTheme.spacing.paddings.xmicro),
    ) {
        MPText(
            text = text,
            style = MercadoPagoAndesTheme.typography.body.emphasis.small,
            color = MercadoPagoAndesTheme.color.feedback.positive.textLoud,
        )
    }
}

@Preview(name = "Pill - Green", group = PILL_GROUP)
@Composable
internal fun PillPreview() {
    MercadoPagoTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White)
                .padding(20.dp),
        ) {
            MPPill("Label")
        }
    }
}
