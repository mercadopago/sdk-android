package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

enum class MPTrailingType {
    Text,
    Pill

}

@Composable
fun MPListItem(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean = false,
    trailingText: String? = null,
    trailingType: MPTrailingType = MPTrailingType.Text,
) {
    Column (
        modifier = modifier
    ){
        Row (
            modifier = Modifier.fillMaxWidth().padding(MercadoPagoTheme.spacing.xs),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                MPRadioButton(selected)
                Spacer(Modifier.size(MercadoPagoTheme.spacing.s))
                MPText(
                    text = text,
                    modifier = Modifier.weight(1f, false)
                )
            }

            trailingText?.let {
                when (trailingType) {
                    MPTrailingType.Text -> MPText(trailingText, Modifier.weight(1f, false))
                    MPTrailingType.Pill -> Pill(trailingText, Modifier.weight(1f, false))
                }
            }
        }

        Spacer(Modifier.fillMaxWidth().height(1.dp).background(color = MercadoPagoTheme.color.secondarySecondVariant))
    }
}

@Preview
@Composable
fun MPListItemPreview() {
    MercadoPagoTheme {
        MPListItem(text = "List Item item te sdasdasdasdasdaim ", trailingText = "training")
    }
}
