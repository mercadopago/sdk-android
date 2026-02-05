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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.model.MPTrailing
import com.mercadopago.sdk.android.components.model.MPTrailingType
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val LIST_GROUP = "LIST_ITEM"

/**
 * List Item component
 * @param text component text to be showed
 * @param modifier component modifier
 * @param selected component is selected
 * @param description component description
 * @param trailing component trailing
 */
@Composable
fun MPListItem(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    description: String? = null,
    trailing: MPTrailing? = null,
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
                style = MercadoPagoAndesTheme.typography.body.default.medium,
                color = MercadoPagoAndesTheme.color.text.primary,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
            )

            trailing?.let { trailing ->
                when (trailing.type) {
                    MPTrailingType.Text -> MPText(
                        text = trailing.text,
                        style = MercadoPagoAndesTheme.typography.body.default.small,
                        color = trailing.textColor ?: MercadoPagoAndesTheme.color.text.primary,
                    )

                    MPTrailingType.Pill -> MPPill(trailing.text)
                }

                if (trailing.icon != null) {
                    Spacer(Modifier.size(MercadoPagoTheme.spacing.s))
                    Icon(
                        imageVector = trailing.icon,
                        contentDescription = null,
                        tint = MercadoPagoAndesTheme.color.icon.accent,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }

        Row {
            Text(
                text = description.orEmpty(),
                style = MercadoPagoAndesTheme.typography.body.default.small,
                color = MercadoPagoAndesTheme.color.text.primary,
            )
        }

        Spacer(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MercadoPagoTheme.color.outline.secondary),
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
                trailing = MPTrailing(
                    type = MPTrailingType.Text,
                    icon = Icons.AutoMirrored.Sharp.KeyboardArrowRight,
                    text = "Sem Acréscimo",
                    textColor = MercadoPagoAndesTheme.color.feedback.positive.textLoud,
                ),
                selected = true,
                description = "Description test",
            )
        }
    }
}
