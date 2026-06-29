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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.components.extensions.isGreaterThan
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val FIXED_FOOTER_GROUP = "FixedFooter"

/**
 * Data class representing the amount display configuration
 *
 * @property currencySymbol The currency symbol to display (e.g., "$")
 * @property integerPart The integer part of the amount (e.g., "1.000")
 * @property decimalPart The decimal part of the amount (e.g., "00")
 */
data class MPAmountData(
    val currencySymbol: String,
    val integerPart: String,
    val decimalPart: String,
)

/**
 * Data class representing the button configuration
 *
 * @property text The button label text
 * @property style The button style (default: Loud)
 * @property enabled Whether the button is enabled
 * @property isLoading When true, shows a loading animation inside the button and disables interaction
 * @property onClick Callback executed when button is clicked
 */
data class MPFixedFooterButtonData(
    val text: String,
    val style: MPButtonStyle = MPButtonStyle.Loud,
    val enabled: Boolean = true,
    val isLoading: Boolean = false,
    val onClick: () -> Unit,
)

/**
 * Fixed Footer component - Displays a footer with title, amount, subtitle and an optional button
 * This component is commonly used at the bottom of screens to show pricing information
 * and a call-to-action button
 *
 * @param title The title text displayed on the left side
 * @param modifier The modifier to apply to this component
 * @param amount The amount data containing currency symbol, integer and decimal parts
 * @param subtitle Optional subtitle text displayed below the amount
 * @param button Optional button configuration. When null, no button is displayed
 */
@Composable
fun MPFixedFooter(
    title: String,
    modifier: Modifier = Modifier,
    amount: MPAmountData? = null,
    subtitle: String? = null,
    button: MPFixedFooterButtonData? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MercadoPagoTheme.color.background.primary)
            .padding(
                horizontal = MercadoPagoTheme.spacing.paddings.xtiny,
                vertical = MercadoPagoTheme.spacing.paddings.xtiny,
            ),
    ) {
        if (amount?.integerPart?.isGreaterThan() == true) {
            HeaderSection(
                title = title,
                amount = amount,
                subtitle = subtitle,
            )
            Spacer(modifier = Modifier.height(MercadoPagoTheme.spacing.paddings.micro))
        }
        button?.let {
            MPButton(
                text = it.text,
                modifier = Modifier.fillMaxWidth(),
                style = it.style,
                enabled = it.enabled,
                isLoading = it.isLoading,
                onClick = it.onClick,
            )
        }
    }
}

@Composable
private fun HeaderSection(
    title: String,
    amount: MPAmountData,
    subtitle: String?,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            MPText(
                text = title,
                style = MercadoPagoTheme.typography.body.emphasis.large,
                color = MercadoPagoTheme.color.text.primary,
            )
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                AmountText(amount = amount)
                if (subtitle != null) {
                    MPText(
                        text = subtitle,
                        style = MercadoPagoTheme.typography.body.default.medium,
                        color = MercadoPagoTheme.color.text.secondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun AmountText(
    amount: MPAmountData,
) {
    Row {
        MPText(
            text = amount.currencySymbol,
            style = MercadoPagoTheme.typography.heading.default.medium,
            color = MercadoPagoTheme.color.text.primary,
        )
        MPText(
            text = amount.integerPart,
            style = MercadoPagoTheme.typography.heading.default.medium,
            color = MercadoPagoTheme.color.text.primary,
        )
        if (amount.decimalPart.isGreaterThan()) {
            Spacer(modifier = Modifier.size(MercadoPagoTheme.spacing.paddings.xnano))
            MPText(
                text = amount.decimalPart,
                style = MercadoPagoTheme.typography.body.emphasis.small,
                color = MercadoPagoTheme.color.text.primary,
            )
        }
    }
}

@Preview(name = "Fixed Footer with Button", group = FIXED_FOOTER_GROUP)
@Composable
private fun MPFixedFooterWithButtonPreview() {
    MercadoPagoTheme {
        MPFixedFooter(
            title = "Text",
            amount = MPAmountData(
                currencySymbol = "$",
                integerPart = "1.000",
                decimalPart = "00",
            ),
            subtitle = "Text",
            button = MPFixedFooterButtonData(
                text = "Label",
                onClick = {},
            ),
        )
    }
}

@Preview(name = "Fixed Footer without Button", group = FIXED_FOOTER_GROUP)
@Composable
private fun MPFixedFooterWithoutButtonPreview() {
    MercadoPagoTheme {
        MPFixedFooter(
            title = "Text",
            amount = MPAmountData(
                currencySymbol = "$",
                integerPart = "1.000",
                decimalPart = "00",
            ),
            subtitle = "Text",
            button = null,
        )
    }
}

@Preview(name = "Fixed Footer without Subtitle", group = FIXED_FOOTER_GROUP)
@Composable
private fun MPFixedFooterWithoutSubtitlePreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier.background(Color.LightGray),
        ) {
            MPFixedFooter(
                title = "Total",
                amount = MPAmountData(
                    currencySymbol = "$",
                    integerPart = "2.500",
                    decimalPart = "50",
                ),
                subtitle = null,
                button = MPFixedFooterButtonData(
                    text = "Pagar",
                    onClick = {},
                ),
            )
        }
    }
}

@Preview(name = "Fixed Footer Disabled Button", group = FIXED_FOOTER_GROUP)
@Composable
private fun MPFixedFooterDisabledButtonPreview() {
    MercadoPagoTheme {
        MPFixedFooter(
            title = "Total a pagar",
            amount = MPAmountData(
                currencySymbol = "R$",
                integerPart = "150",
                decimalPart = "99",
            ),
            subtitle = "em até 12x",
            button = MPFixedFooterButtonData(
                text = "Continuar",
                enabled = false,
                onClick = {},
            ),
        )
    }
}
