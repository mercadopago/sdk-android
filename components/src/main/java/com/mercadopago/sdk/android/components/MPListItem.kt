package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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

private const val LIST_GROUP = "LIST_ITEM"

/**
 * Trailing Type enum class, used to determine the trailing showing type
 * This its used to change the component showed in trailing
 */
enum class MPTrailingType {
    /**
     *  Text: Trailing type that`s shows a text
     */
    Text,

    /**
     *  Pill: Trailing type that`s shows a pill
     */
    Pill,
}

/**
 * List Item component
 * @param text component text to be showed
 * @param modifier component modifier
 * @param selected component is selected
 * @param trailingText component is trailing text
 * @param trailingType component is trailing type [MPTrailingType]
 */
@Composable
fun MPListItem(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    trailingText: String? = null,
    trailingType: MPTrailingType = MPTrailingType.Text,
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MercadoPagoTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MPRadioButton(selected)
            Spacer(Modifier.size(MercadoPagoTheme.spacing.s))
            MPText(
                text = text,
                textStyle = MPTextStyle.BodyMediumRegular,
                modifier = Modifier.weight(1f),
            )

            trailingText?.let {
                when (trailingType) {
                    MPTrailingType.Text -> MPText(
                        trailingText,
                        textStyle = MPTextStyle.BodySmallRegular,
                    )

                    MPTrailingType.Pill -> Pill(trailingText)
                }
            }
        }

        Spacer(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MercadoPagoTheme.color.outline.secondary,)
        )
    }
}

@Preview(name = "List Item", group = LIST_GROUP, showBackground = true)
@Composable
private fun MPListItemPreview() {
    MercadoPagoTheme {
        Box(
            modifier = Modifier.padding(10.dp),
        ) {
            MPListItem(
                text = "List Item",
                trailingText = "trailing",
                trailingType = MPTrailingType.Pill,
                selected = true,
            )
        }
    }
}
