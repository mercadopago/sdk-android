package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val FIXED_FOOTER_GROUP = "FixedFooter"
private const val SUPERSCRIPT_ID = "superscript"

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
 * @property onClick Callback executed when button is clicked
 */
data class MPFixedFooterButtonData(
    val text: String,
    val style: MPButtonStyle = MPButtonStyle.Loud,
    val enabled: Boolean = true,
    val onClick: () -> Unit,
)

/**
 * Fixed Footer component - Displays a footer with title, amount, subtitle and an optional button
 * This component is commonly used at the bottom of screens to show pricing information
 * and a call-to-action button
 *
 * @param title The title text displayed on the left side
 * @param amount The amount data containing currency symbol, integer and decimal parts
 * @param modifier The modifier to apply to this component
 * @param subtitle Optional subtitle text displayed below the amount
 * @param buttonData Optional button configuration. When null, no button is displayed
 */
@Composable
fun MPFixedFooter(
    title: String,
    amount: MPAmountData,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    buttonData: MPFixedFooterButtonData? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MercadoPagoTheme.color.background.primary)
            .padding(
                horizontal = MercadoPagoTheme.spacing.m,
                vertical = MercadoPagoTheme.spacing.m,
            ),
    ) {
        HeaderSection(
            title = title,
            amount = amount,
            subtitle = subtitle,
        )
        if (buttonData != null) {
            Spacer(modifier = Modifier.height(MercadoPagoTheme.spacing.m))
            MPButton(
                text = buttonData.text,
                modifier = Modifier.fillMaxWidth(),
                style = buttonData.style,
                enabled = buttonData.enabled,
                onClick = buttonData.onClick,
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
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MPText(
                text = title,
                textStyle = MPTextStyle.BodyMediumRegular,
                colorType = MPTextColorType.Primary,
            )
            AmountText(amount = amount)
        }
        if (subtitle != null) {
            MPText(
                text = subtitle,
                textStyle = MPTextStyle.BodySmallRegular,
                colorType = MPTextColorType.Accent,
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}

@Composable
private fun AmountText(amount: MPAmountData) {
    val annotatedString = buildAnnotatedString {
        append("${amount.currencySymbol} ${amount.integerPart} ")
        appendInlineContent(SUPERSCRIPT_ID, amount.decimalPart)
    }

    val inlineContent = mapOf(
        SUPERSCRIPT_ID to InlineTextContent(
            placeholder = Placeholder(
                width = (amount.decimalPart.length * 10).sp,
                height = 14.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Top,
            ),
        ) {
            Text(
                text = amount.decimalPart,
                style = MercadoPagoTheme.typography.body.extraSmallSemibold,
                color = MercadoPagoTheme.color.text.primary,
            )
        },
    )

    Text(
        text = annotatedString,
        inlineContent = inlineContent,
        style = MercadoPagoTheme.typography.title.smallSemibold,
        color = MercadoPagoTheme.color.text.primary,
    )
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
            buttonData = MPFixedFooterButtonData(
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
            buttonData = null,
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
                buttonData = MPFixedFooterButtonData(
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
            buttonData = MPFixedFooterButtonData(
                text = "Continuar",
                enabled = false,
                onClick = {},
            ),
        )
    }
}
