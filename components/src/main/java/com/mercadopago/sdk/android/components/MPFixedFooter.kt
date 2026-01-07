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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val FIXED_FOOTER_GROUP = "FixedFooter"
private const val SUPERSCRIPT_ID = "superscript"

/**
 * Default tokens configuration for MPFixedFooter component
 */
data class FixedFooterDefaults(
    val colors: MPFixedFooterColorDefaults,
    val spacing: MPFixedFooterSpacingDefaults,
)

/**
 * Color tokens for MPFixedFooter component
 */
data class MPFixedFooterColorDefaults(
    val background: Color,
    val textPrimary: Color,
    val textAccent: Color,
)

/**
 * Spacing tokens for MPFixedFooter component
 */
data class MPFixedFooterSpacingDefaults(
    val paddingHorizontal: Dp,
    val paddingVertical: Dp,
    val spacingBetweenSections: Dp,
)

/**
 * Helper function to get default tokens for MPFixedFooter component
 */
@Composable
private fun getFixedFooterDefaults(): FixedFooterDefaults {
    val andesTheme = MercadoPagoAndesTheme
    return FixedFooterDefaults(
        colors = MPFixedFooterColorDefaults(
            background = andesTheme.color.background.primary,
            textPrimary = andesTheme.color.text.primary,
            textAccent = andesTheme.color.text.accent,
        ),
        spacing = MPFixedFooterSpacingDefaults(
            paddingHorizontal = andesTheme.spacing.paddings.xtiny,
            paddingVertical = andesTheme.spacing.paddings.xtiny,
            spacingBetweenSections = andesTheme.spacing.paddings.xtiny,
        ),
    )
}

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
    val defaults = getFixedFooterDefaults()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(defaults.colors.background)
            .padding(
                horizontal = defaults.spacing.paddingHorizontal,
                vertical = defaults.spacing.paddingVertical,
            ),
    ) {
        HeaderSection(
            title = title,
            amount = amount,
            subtitle = subtitle,
        )
        if (buttonData != null) {
            Spacer(modifier = Modifier.height(defaults.spacing.spacingBetweenSections))
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

/**
 * Header section containing title and amount on the same line, with subtitle below aligned to the right
 */
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

/**
 * Amount text with superscript decimal part
 */
@Composable
private fun AmountText(
    amount: MPAmountData,
) {
    val andesTheme = MercadoPagoAndesTheme
    val headingTypo = andesTheme.typography.heading
    val titleStyle = TextStyle(
        fontFamily = headingTypo.familyDefault,
        fontWeight = headingTypo.weight.semibold,
        fontSize = headingTypo.size.size20,
        lineHeight = headingTypo.lineHeight.lineHeight24,
        letterSpacing = headingTypo.letterSpacing.spacing0,
    )
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
            MPText(
                text = amount.decimalPart,
                textStyle = MPTextStyle.BodyExtraSmallSemiBold,
                colorType = MPTextColorType.Primary,
            )
        },
    )

    Text(
        text = annotatedString,
        inlineContent = inlineContent,
        style = titleStyle,
        color = andesTheme.color.text.primary,
    )
}

@Preview(name = "Fixed Footer with Button", group = FIXED_FOOTER_GROUP)
@Composable
private fun MPFixedFooterWithButtonPreview() {
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
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
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
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
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
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
    MercadoPagoTheme(theme = MercadoPagoThemes.Andes) {
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
