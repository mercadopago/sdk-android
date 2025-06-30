package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Inverted
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
 * @param labelTextStyle: text style, must be using
 * @param labelColorType: type of label color
 * @param enabled: Boolean indicates if the component its enabled
 */
@Composable
fun MPText(
    text: String,
    modifier: Modifier = Modifier,
    labelTextStyle: MPTextStyle = MPTextStyle.Title,
    labelColorType: MPTextColorType = MPTextColorType.Primary,
    enabled: Boolean = true,
) {
    val color = when (labelColorType) {
        MPTextColorType.Primary -> {
            MercadoPagoTheme.color.text.primary
        }

        MPTextColorType.Secondary -> {
            MercadoPagoTheme.color.text.secondary
        }

        MPTextColorType.Accent -> {
            MercadoPagoTheme.color.text.accent
        }

        MPTextColorType.Negative -> {
            MercadoPagoTheme.color.text.negative
        }

        MPTextColorType.Inverted -> {
            MercadoPagoTheme.color.text.inverted
        }
    }

    val style = when (labelTextStyle) {
        MPTextStyle.Title -> {
            MercadoPagoTheme.typography.title.smallSemibold
        }

        MPTextStyle.BodyMediumSemiBold -> {
            MercadoPagoTheme.typography.body.mediumSemibold
        }

        MPTextStyle.BodyMediumRegular -> {
            MercadoPagoTheme.typography.body.mediumRegular
        }

        MPTextStyle.BodySmallSemiBold -> {
            MercadoPagoTheme.typography.body.smallSemibold
        }

        MPTextStyle.BodySmallRegular -> {
            MercadoPagoTheme.typography.body.smallRegular
        }

        MPTextStyle.BodyExtraSmallSemiBold -> {
            MercadoPagoTheme.typography.body.extraSmallSemibold
        }
    }

    Text(
        text = text,
        style = style,
        color = if (!enabled) MercadoPagoTheme.color.text.disabled else color,
        modifier = modifier
    )
}

@Preview(name = "Text Tittle Text", group = TEXT_GROUP)
@Composable
internal fun LabelTittlePreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            MPText(
                text = "My Tittle Primary Text",
                labelTextStyle = MPTextStyle.Title,
                labelColorType = MPTextColorType.Primary
            )
            MPText(
                text = "My Tittle Secondary Text",
                labelTextStyle = MPTextStyle.Title,
                labelColorType = MPTextColorType.Secondary
            )
            MPText(
                text = "My Tittle Accent Text",
                labelTextStyle = MPTextStyle.Title,
                labelColorType = MPTextColorType.Accent
            )
            MPText(
                text = "My Tittle Inverted Text",
                labelTextStyle = MPTextStyle.Title,
                labelColorType = MPTextColorType.Inverted
            )
            MPText(
                text = "My Tittle Negative Text",
                labelTextStyle = MPTextStyle.Title,
                labelColorType = MPTextColorType.Negative
            )
            MPText(
                text = "My Tittle Disabled Text",
                labelTextStyle = MPTextStyle.Title,
                labelColorType = MPTextColorType.Primary,
                enabled = false
            )
        }
    }
}

@Preview(name = "Text BodyMediumSemiBold Text", group = TEXT_GROUP)
@Composable
internal fun LabelBodyMediumSemiBoldPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            MPText(
                text = "My BodyMediumSemiBold Primary Text",
                labelTextStyle = MPTextStyle.BodyMediumSemiBold,
                labelColorType = MPTextColorType.Primary
            )
            MPText(
                text = "My BodyMediumSemiBold Secondary Text",
                labelTextStyle = MPTextStyle.BodyMediumSemiBold,
                labelColorType = MPTextColorType.Secondary
            )
            MPText(
                text = "My BodyMediumSemiBold Accent Text",
                labelTextStyle = MPTextStyle.BodyMediumSemiBold,
                labelColorType = MPTextColorType.Accent
            )
            MPText(
                text = "My BodyMediumSemiBold Inverted Text",
                labelTextStyle = MPTextStyle.BodyMediumSemiBold,
                labelColorType = MPTextColorType.Inverted
            )
            MPText(
                text = "My BodyMediumSemiBold Negative Text",
                labelTextStyle = MPTextStyle.BodyMediumSemiBold,
                labelColorType = MPTextColorType.Negative
            )
            MPText(
                text = "My BodyMediumSemiBold Disabled Text",
                labelTextStyle = MPTextStyle.BodyMediumSemiBold,
                labelColorType = MPTextColorType.Primary,
                enabled = false
            )
        }
    }
}

@Preview(name = "Text BodyMediumRegular Text", group = TEXT_GROUP)
@Composable
internal fun LabelBodyMediumRegularPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            MPText(
                text = "My BodyMediumRegular Primary Text",
                labelTextStyle = MPTextStyle.BodyMediumRegular,
                labelColorType = MPTextColorType.Primary
            )
            MPText(
                text = "My BodyMediumRegular Secondary Text",
                labelTextStyle = MPTextStyle.BodyMediumRegular,
                labelColorType = MPTextColorType.Secondary
            )
            MPText(
                text = "My BodyMediumRegular Accent Text",
                labelTextStyle = MPTextStyle.BodyMediumRegular,
                labelColorType = MPTextColorType.Accent
            )
            MPText(
                text = "My BodyMediumRegular Inverted Text",
                labelTextStyle = MPTextStyle.BodyMediumRegular,
                labelColorType = MPTextColorType.Inverted
            )
            MPText(
                text = "My BodyMediumRegular Negative Text",
                labelTextStyle = MPTextStyle.BodyMediumRegular,
                labelColorType = MPTextColorType.Negative
            )
            MPText(
                text = "My BodyMediumRegular Disabled Text",
                labelTextStyle = MPTextStyle.BodyMediumRegular,
                labelColorType = MPTextColorType.Primary,
                enabled = false
            )
        }
    }
}

@Preview(name = "Text BodySmallSemiBold Text", group = TEXT_GROUP)
@Composable
internal fun LabelBodySmallSemiBoldPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            MPText(
                text = "My BodySmallSemiBold Primary Text",
                labelTextStyle = MPTextStyle.BodySmallSemiBold,
                labelColorType = MPTextColorType.Primary
            )
            MPText(
                text = "My BodySmallSemiBold Secondary Text",
                labelTextStyle = MPTextStyle.BodySmallSemiBold,
                labelColorType = MPTextColorType.Secondary
            )
            MPText(
                text = "My BodySmallSemiBold Accent Text",
                labelTextStyle = MPTextStyle.BodySmallSemiBold,
                labelColorType = MPTextColorType.Accent
            )
            MPText(
                text = "My BodySmallSemiBold Inverted Text",
                labelTextStyle = MPTextStyle.BodySmallSemiBold,
                labelColorType = MPTextColorType.Inverted
            )
            MPText(
                text = "My BodySmallSemiBold Negative Text",
                labelTextStyle = MPTextStyle.BodySmallSemiBold,
                labelColorType = MPTextColorType.Negative
            )
            MPText(
                text = "My BodySmallSemiBold Disabled Text",
                labelTextStyle = MPTextStyle.BodySmallSemiBold,
                labelColorType = MPTextColorType.Primary,
                enabled = false
            )
        }
    }
}

@Preview(name = "Text BodySmallRegular Text", group = TEXT_GROUP)
@Composable
internal fun LabelBodySmallRegularPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            MPText(
                text = "My BodySmallRegular Primary Text",
                labelTextStyle = MPTextStyle.BodySmallRegular,
                labelColorType = MPTextColorType.Primary
            )
            MPText(
                text = "My BodySmallRegular Secondary Text",
                labelTextStyle = MPTextStyle.BodySmallRegular,
                labelColorType = MPTextColorType.Secondary
            )
            MPText(
                text = "My BodySmallRegular Accent Text",
                labelTextStyle = MPTextStyle.BodySmallRegular,
                labelColorType = MPTextColorType.Accent
            )
            MPText(
                text = "My BodySmallRegular Inverted Text",
                labelTextStyle = MPTextStyle.BodySmallRegular,
                labelColorType = MPTextColorType.Inverted
            )
            MPText(
                text = "My BodySmallRegular Negative Text",
                labelTextStyle = MPTextStyle.BodySmallRegular,
                labelColorType = MPTextColorType.Negative
            )
            MPText(
                text = "My BodySmallRegular Disabled Text",
                labelTextStyle = MPTextStyle.BodySmallRegular,
                labelColorType = MPTextColorType.Primary,
                enabled = false
            )
        }
    }
}

@Preview(name = "Text BodyExtraSmallSemiBold Text", group = TEXT_GROUP)
@Composable
internal fun LabelBodyExtraSmallSemiBoldPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            MPText(
                text = "My BodyExtraSmallSemiBold Primary Text",
                labelTextStyle = MPTextStyle.BodyExtraSmallSemiBold,
                labelColorType = MPTextColorType.Primary
            )
            MPText(
                text = "My BodyExtraSmallSemiBold Secondary Text",
                labelTextStyle = MPTextStyle.BodyExtraSmallSemiBold,
                labelColorType = MPTextColorType.Secondary
            )
            MPText(
                text = "My BodyExtraSmallSemiBold Accent Text",
                labelTextStyle = MPTextStyle.BodyExtraSmallSemiBold,
                labelColorType = MPTextColorType.Accent
            )
            MPText(
                text = "My BodyExtraSmallSemiBold Inverted Text",
                labelTextStyle = MPTextStyle.BodyExtraSmallSemiBold,
                labelColorType = MPTextColorType.Inverted
            )
            MPText(
                text = "My BodyExtraSmallSemiBold Negative Text",
                labelTextStyle = MPTextStyle.BodyExtraSmallSemiBold,
                labelColorType = MPTextColorType.Negative
            )
            MPText(
                text = "My BodyExtraSmallSemiBold Disabled Text",
                labelTextStyle = MPTextStyle.BodyExtraSmallSemiBold,
                labelColorType = MPTextColorType.Primary,
                enabled = false
            )
        }
    }
}
