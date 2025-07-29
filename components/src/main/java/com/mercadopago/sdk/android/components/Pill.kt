package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

@Composable
fun Pill(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .width(48.dp) // TODO: Precisa adicionar um token
            .height(MercadoPagoTheme.spacing.m)
            .background(
                color = MercadoPagoTheme.color.feedback.positiveSecondary,
                shape = RoundedCornerShape(MercadoPagoTheme.spacing.m)
            ),
    ) {
        // TODO: Precisa adicionar um token de cor positive para texto no figma
        MPText(
            text,
            textStyle = MPTextStyle.BodySmallSemiBold,
            colorType = MPTextColorType.Positive
        )
    }
}

@Preview(name = "Pill")
@Composable
fun PillPreview() {
    MercadoPagoTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp)
                .background(Color.White)
        ) {
            Pill("Label")
        }
    }
}
