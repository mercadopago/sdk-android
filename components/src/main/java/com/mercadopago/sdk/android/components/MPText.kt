package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val TEXT_GROUP = "Text"

/**
 * Text Type enum class, used to determine the label type
 * This its used to change the token color
 */
enum class MPTextColorType {
    /**
     *  Primary: Text of the Primary type
     */
    Primary,

    /**
     *  Secondary: Text of the Secondary type
     */
    Secondary,

    /**
     *  Accent: Text of the Accent type
     */
    Accent,

    /**
     *  Negative: Text of the Negative type
     */
    Negative,

    /**
     *  Inverted: Text of the Inverted type
     */
    Inverted,

    /**
     *  Positive: Text of the Positive type
     */
    Positive,
}

/**
 * Text Text type enum class, used to determine the label type
 * This its used to change the text style
 */
enum class MPTextStyle {
    /**
     * Tittle: Text of the Tittle type
     */
    Title,

    /**
     * BodyMediumSemiBold: Text of the BodyMediumSemiBold type
     */
    BodyMediumSemiBold,

    /**
     * BodyMediumRegular: Text of the BodyMediumRegular type
     */
    BodyMediumRegular,

    /**
     * BodySmallSemiBold: Text of the BodySmallSemiBold type
     */
    BodySmallSemiBold,

    /**
     * BodySmallRegular: Text of the BodySmallRegular type
     */
    BodySmallRegular,

    /**
     * BodyExtraSmallSemiBold: Text of the BodyExtraSmallSemiBold type
     */
    BodyExtraSmallSemiBold,
}

/**
 * Text component- This handle the text of the components
 * This component is used to build others components
 * handling the text implementation
 *
 * @param text: label text
 * @param modifier: label modifier
 * @param textStyle: text style, must be using
 * @param colorType: type of label color
 * @param enabled: Boolean indicates if the component its enabled
 */
@Composable
fun MPText(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: MPTextStyle = MPTextStyle.Title,
    colorType: MPTextColorType = MPTextColorType.Primary,
    enabled: Boolean = true,
) {
    val color = when (colorType) {
        MPTextColorType.Primary -> {
            MercadoPagoTheme.newColor.text.primary
        }

        MPTextColorType.Secondary -> {
            MercadoPagoTheme.newColor.text.secondary
        }

        MPTextColorType.Accent -> {
            MercadoPagoTheme.newColor.text.accent
        }

        MPTextColorType.Negative -> {
            MercadoPagoTheme.newColor.feedback.negative.textLoud
        }

        MPTextColorType.Inverted -> {
            MercadoPagoTheme.newColor.text.inverse
        }

        MPTextColorType.Positive -> {
            MercadoPagoTheme.newColor.feedback.positive.textLoud
        }
    }

    val typography = MercadoPagoTheme.newTypography.heading
    val style = when (textStyle) {
        MPTextStyle.Title -> {
            TextStyle(
                fontFamily = typography.familyDefault,
                fontWeight = typography.weight.semibold,
                fontSize = typography.size.size20,
                lineHeight = typography.lineHeight.lineHeight24,
                letterSpacing = typography.letterSpacing.spacing0,
            )
        }

        MPTextStyle.BodyMediumSemiBold -> {
            TextStyle(
                fontFamily = typography.familyDefault,
                fontWeight = typography.weight.semibold,
                fontSize = typography.size.size16,
                lineHeight = typography.lineHeight.lineHeight24,
                letterSpacing = typography.letterSpacing.spacing0,
            )
        }

        MPTextStyle.BodyMediumRegular -> {
            TextStyle(
                fontFamily = typography.familyDefault,
                fontWeight = typography.weight.regular,
                fontSize = typography.size.size16,
                lineHeight = typography.lineHeight.lineHeight20,
                letterSpacing = typography.letterSpacing.spacing0,
            )
        }

        MPTextStyle.BodySmallSemiBold -> {
            TextStyle(
                fontFamily = typography.familyDefault,
                fontWeight = typography.weight.semibold,
                fontSize = typography.size.size14,
                lineHeight = typography.lineHeight.lineHeight20,
                letterSpacing = typography.letterSpacing.spacing0,
            )
        }

        MPTextStyle.BodySmallRegular -> {
            TextStyle(
                fontFamily = typography.familyDefault,
                fontWeight = typography.weight.regular,
                fontSize = typography.size.size14,
                lineHeight = typography.lineHeight.lineHeight20,
                letterSpacing = typography.letterSpacing.spacing0,
            )
        }

        MPTextStyle.BodyExtraSmallSemiBold -> {
            TextStyle(
                fontFamily = typography.familyDefault,
                fontWeight = typography.weight.semibold,
                fontSize = typography.size.size12,
                lineHeight = typography.lineHeight.lineHeight16,
                letterSpacing = typography.letterSpacing.spacing0,
            )
        }
    }

    Text(
        text = text,
        style = style,
        color = if (!enabled) MercadoPagoTheme.newColor.text.disabled else color,
        modifier = modifier,
    )
}

@Preview(name = "Text Tittle Text", group = TEXT_GROUP)
@Composable
internal fun TextTittlePreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPText(
                text = "My Tittle Primary Text",
                textStyle = MPTextStyle.Title,
                colorType = MPTextColorType.Primary,
            )
            MPText(
                text = "My Tittle Secondary Text",
                textStyle = MPTextStyle.Title,
                colorType = MPTextColorType.Secondary,
            )
            MPText(
                text = "My Tittle Accent Text",
                textStyle = MPTextStyle.Title,
                colorType = MPTextColorType.Accent,
            )
            MPText(
                text = "My Tittle Inverted Text",
                textStyle = MPTextStyle.Title,
                colorType = MPTextColorType.Inverted,
            )
            MPText(
                text = "My Tittle Negative Text",
                textStyle = MPTextStyle.Title,
                colorType = MPTextColorType.Negative,
            )
            MPText(
                text = "My Tittle Disabled Text",
                textStyle = MPTextStyle.Title,
                colorType = MPTextColorType.Primary,
                enabled = false,
            )
            MPText(
                text = "My Tittle Positive Text",
                textStyle = MPTextStyle.Title,
                colorType = MPTextColorType.Positive,
            )
        }
    }
}

@Preview(name = "Text BodyMediumSemiBold Text", group = TEXT_GROUP)
@Composable
internal fun TextBodyMediumSemiBoldPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPText(
                text = "My BodyMediumSemiBold Primary Text",
                textStyle = MPTextStyle.BodyMediumSemiBold,
                colorType = MPTextColorType.Primary,
            )
            MPText(
                text = "My BodyMediumSemiBold Secondary Text",
                textStyle = MPTextStyle.BodyMediumSemiBold,
                colorType = MPTextColorType.Secondary,
            )
            MPText(
                text = "My BodyMediumSemiBold Accent Text",
                textStyle = MPTextStyle.BodyMediumSemiBold,
                colorType = MPTextColorType.Accent,
            )
            MPText(
                text = "My BodyMediumSemiBold Inverted Text",
                textStyle = MPTextStyle.BodyMediumSemiBold,
                colorType = MPTextColorType.Inverted,
            )
            MPText(
                text = "My BodyMediumSemiBold Negative Text",
                textStyle = MPTextStyle.BodyMediumSemiBold,
                colorType = MPTextColorType.Negative,
            )
            MPText(
                text = "My BodyMediumSemiBold Disabled Text",
                textStyle = MPTextStyle.BodyMediumSemiBold,
                colorType = MPTextColorType.Primary,
                enabled = false,
            )
            MPText(
                text = "My BodyMediumSemiBold Positive Text",
                textStyle = MPTextStyle.BodyMediumSemiBold,
                colorType = MPTextColorType.Positive,
            )
        }
    }
}

@Preview(name = "Text BodyMediumRegular Text", group = TEXT_GROUP)
@Composable
internal fun TextBodyMediumRegularPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPText(
                text = "My BodyMediumRegular Primary Text",
                textStyle = MPTextStyle.BodyMediumRegular,
                colorType = MPTextColorType.Primary,
            )
            MPText(
                text = "My BodyMediumRegular Secondary Text",
                textStyle = MPTextStyle.BodyMediumRegular,
                colorType = MPTextColorType.Secondary,
            )
            MPText(
                text = "My BodyMediumRegular Accent Text",
                textStyle = MPTextStyle.BodyMediumRegular,
                colorType = MPTextColorType.Accent,
            )
            MPText(
                text = "My BodyMediumRegular Inverted Text",
                textStyle = MPTextStyle.BodyMediumRegular,
                colorType = MPTextColorType.Inverted,
            )
            MPText(
                text = "My BodyMediumRegular Negative Text",
                textStyle = MPTextStyle.BodyMediumRegular,
                colorType = MPTextColorType.Negative,
            )
            MPText(
                text = "My BodyMediumRegular Disabled Text",
                textStyle = MPTextStyle.BodyMediumRegular,
                colorType = MPTextColorType.Primary,
                enabled = false,
            )
            MPText(
                text = "My BodyMediumRegular Positive Text",
                textStyle = MPTextStyle.BodyMediumRegular,
                colorType = MPTextColorType.Positive,
            )
        }
    }
}

@Preview(name = "Text BodySmallSemiBold Text", group = TEXT_GROUP)
@Composable
internal fun TextBodySmallSemiBoldPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPText(
                text = "My BodySmallSemiBold Primary Text",
                textStyle = MPTextStyle.BodySmallSemiBold,
                colorType = MPTextColorType.Primary,
            )
            MPText(
                text = "My BodySmallSemiBold Secondary Text",
                textStyle = MPTextStyle.BodySmallSemiBold,
                colorType = MPTextColorType.Secondary,
            )
            MPText(
                text = "My BodySmallSemiBold Accent Text",
                textStyle = MPTextStyle.BodySmallSemiBold,
                colorType = MPTextColorType.Accent,
            )
            MPText(
                text = "My BodySmallSemiBold Inverted Text",
                textStyle = MPTextStyle.BodySmallSemiBold,
                colorType = MPTextColorType.Inverted,
            )
            MPText(
                text = "My BodySmallSemiBold Negative Text",
                textStyle = MPTextStyle.BodySmallSemiBold,
                colorType = MPTextColorType.Negative,
            )
            MPText(
                text = "My BodySmallSemiBold Disabled Text",
                textStyle = MPTextStyle.BodySmallSemiBold,
                colorType = MPTextColorType.Primary,
                enabled = false,
            )
            MPText(
                text = "My BodySmallSemiBold Positive Text",
                textStyle = MPTextStyle.BodySmallSemiBold,
                colorType = MPTextColorType.Positive,
            )
        }
    }
}

@Preview(name = "Text BodySmallRegular Text", group = TEXT_GROUP)
@Composable
internal fun TextBodySmallRegularPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPText(
                text = "My BodySmallRegular Primary Text",
                textStyle = MPTextStyle.BodySmallRegular,
                colorType = MPTextColorType.Primary,
            )
            MPText(
                text = "My BodySmallRegular Secondary Text",
                textStyle = MPTextStyle.BodySmallRegular,
                colorType = MPTextColorType.Secondary,
            )
            MPText(
                text = "My BodySmallRegular Accent Text",
                textStyle = MPTextStyle.BodySmallRegular,
                colorType = MPTextColorType.Accent,
            )
            MPText(
                text = "My BodySmallRegular Inverted Text",
                textStyle = MPTextStyle.BodySmallRegular,
                colorType = MPTextColorType.Inverted,
            )
            MPText(
                text = "My BodySmallRegular Negative Text",
                textStyle = MPTextStyle.BodySmallRegular,
                colorType = MPTextColorType.Negative,
            )
            MPText(
                text = "My BodySmallRegular Disabled Text",
                textStyle = MPTextStyle.BodySmallRegular,
                colorType = MPTextColorType.Primary,
                enabled = false,
            )
            MPText(
                text = "My BodySmallRegular Positive Text",
                textStyle = MPTextStyle.BodySmallRegular,
                colorType = MPTextColorType.Positive,
            )
        }
    }
}

@Preview(name = "Text BodyExtraSmallSemiBold Text", group = TEXT_GROUP)
@Composable
internal fun TextBodyExtraSmallSemiBoldPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White),
        ) {
            MPText(
                text = "My BodyExtraSmallSemiBold Primary Text",
                textStyle = MPTextStyle.BodyExtraSmallSemiBold,
                colorType = MPTextColorType.Primary,
            )
            MPText(
                text = "My BodyExtraSmallSemiBold Secondary Text",
                textStyle = MPTextStyle.BodyExtraSmallSemiBold,
                colorType = MPTextColorType.Secondary,
            )
            MPText(
                text = "My BodyExtraSmallSemiBold Accent Text",
                textStyle = MPTextStyle.BodyExtraSmallSemiBold,
                colorType = MPTextColorType.Accent,
            )
            MPText(
                text = "My BodyExtraSmallSemiBold Inverted Text",
                textStyle = MPTextStyle.BodyExtraSmallSemiBold,
                colorType = MPTextColorType.Inverted,
            )
            MPText(
                text = "My BodyExtraSmallSemiBold Negative Text",
                textStyle = MPTextStyle.BodyExtraSmallSemiBold,
                colorType = MPTextColorType.Negative,
            )
            MPText(
                text = "My BodyExtraSmallSemiBold Disabled Text",
                textStyle = MPTextStyle.BodyExtraSmallSemiBold,
                colorType = MPTextColorType.Primary,
                enabled = false,
            )
            MPText(
                text = "My BodyExtraSmallSemiBold Positive Text",
                textStyle = MPTextStyle.BodyExtraSmallSemiBold,
                colorType = MPTextColorType.Positive,
            )
        }
    }
}
