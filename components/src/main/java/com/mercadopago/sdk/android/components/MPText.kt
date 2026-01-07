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
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val TEXT_GROUP = "Text"

/**
 * Default values for MPText component.
 * Contains color and typography defaults.
 */
data class MPTextDefaults(
    val colors: MPTextColorDefaults,
    val typography: MPTextTypographyDefaults,
)

/**
 * Default color values for MPText component.
 * Contains colors for each MPTextColorType.
 */
data class MPTextColorDefaults(
    val primary: Color,
    val secondary: Color,
    val accent: Color,
    val negative: Color,
    val inverted: Color,
    val positive: Color,
    val disabled: Color,
)

/**
 * Default typography values for MPText component.
 * Contains TextStyle for each MPTextStyle.
 */
data class MPTextTypographyDefaults(
    val title: TextStyle,
    val bodyMediumSemiBold: TextStyle,
    val bodyMediumRegular: TextStyle,
    val bodySmallSemiBold: TextStyle,
    val bodySmallRegular: TextStyle,
    val bodyExtraSmallSemiBold: TextStyle,
)

/**
 * Provides default values for MPText component using MercadoPagoAndesTheme.
 * This function creates MPTextDefaults with colors and typography from the Andes theme.
 */
@Composable
fun getMPTextDefaults(): MPTextDefaults {
    val typography = MercadoPagoAndesTheme.typography
    return MPTextDefaults(
        colors = MPTextColorDefaults(
            primary = MercadoPagoAndesTheme.color.text.primary,
            secondary = MercadoPagoAndesTheme.color.text.secondary,
            accent = MercadoPagoAndesTheme.color.text.accent,
            negative = MercadoPagoAndesTheme.color.feedback.negative.textLoud,
            inverted = MercadoPagoAndesTheme.color.text.inverse,
            positive = MercadoPagoAndesTheme.color.feedback.positive.textLoud,
            disabled = MercadoPagoAndesTheme.color.text.disabled,
        ),
        typography = MPTextTypographyDefaults(
            title = typography.title.title,
            bodyMediumSemiBold = typography.body.bodyMediumSemiBold,
            bodyMediumRegular = typography.body.bodyMediumRegular,
            bodySmallSemiBold = typography.body.bodySmallSemiBold,
            bodySmallRegular = typography.body.bodySmallRegular,
            bodyExtraSmallSemiBold = typography.body.bodyExtraSmallSemiBold,
        ),
    )
}

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
    val defaults = getMPTextDefaults()
    val color = when (colorType) {
        MPTextColorType.Primary -> defaults.colors.primary
        MPTextColorType.Secondary -> defaults.colors.secondary
        MPTextColorType.Accent -> defaults.colors.accent
        MPTextColorType.Negative -> defaults.colors.negative
        MPTextColorType.Inverted -> defaults.colors.inverted
        MPTextColorType.Positive -> defaults.colors.positive
    }
    val style = when (textStyle) {
        MPTextStyle.Title -> defaults.typography.title
        MPTextStyle.BodyMediumSemiBold -> defaults.typography.bodyMediumSemiBold
        MPTextStyle.BodyMediumRegular -> defaults.typography.bodyMediumRegular
        MPTextStyle.BodySmallSemiBold -> defaults.typography.bodySmallSemiBold
        MPTextStyle.BodySmallRegular -> defaults.typography.bodySmallRegular
        MPTextStyle.BodyExtraSmallSemiBold -> defaults.typography.bodyExtraSmallSemiBold
    }
    Text(
        text = text,
        style = style,
        color = if (!enabled) defaults.colors.disabled else color,
        modifier = modifier,
    )
}

@Preview(name = "Text Tittle Text", group = TEXT_GROUP)
@Composable
internal fun TextTittlePreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes
    ) {
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
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes
    ) {
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
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes
    ) {
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
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes
    ) {
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
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes
    ) {
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
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes
    ) {
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
