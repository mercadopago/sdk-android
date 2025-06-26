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
 * Label Type enum class, used to determine the label type
 * This its used to change the token color
 */
enum class LabelColorType {
    /**
     *  Primary: Label of the Primary type
     */
    Primary,

    /**
     *  Secondary: Label of the Secondary type
     */
    Secondary,

    /**
     *  Accent: Label of the Accent type
     */
    Accent,

    /**
     *  Negative: Label of the Negative type
     */
    Negative,

    /**
     *  Inverted: Label of the Inverted type
     */
    Inverted
}

/**
 * Label Text type enum class, used to determine the label type
 * This its used to change the text style
 */
enum class LabelTextType {
    /**
     * Tittle: Label of the Tittle type
     */
    Tittle,

    /**
     * BodyMediumSemiBold: Label of the BodyMediumSemiBold type
     */
    BodyMediumSemiBold,

    /**
     * BodyMediumRegular: Label of the BodyMediumRegular type
     */
    BodyMediumRegular,

    /**
     * BodySmallSemiBold: Label of the BodySmallSemiBold type
     */
    BodySmallSemiBold,

    /**
     * BodySmallRegular: Label of the BodySmallRegular type
     */
    BodySmallRegular,

    /**
     * BodyExtraSmallSemiBold: Label of the BodyExtraSmallSemiBold type
     */
    BodyExtraSmallSemiBold,
}

/**
 * Label component- This handle the text of the components
 * This component is used to build others components
 * handling the text implementation
 *
 * @param modifier: label modifier
 * @param text: label text
 * @param labelTextStyle: text style, must be using
 * @param labelColorType: type of label color
 * @param enabled: Boolean indicates if the component its enabled
 */
@Composable
fun Label(
    modifier: Modifier = Modifier,
    text: String = "",
    labelTextStyle: LabelTextType = LabelTextType.Tittle,
    labelColorType: LabelColorType = LabelColorType.Primary,
    enabled: Boolean = true,
) {
    LabelComposable(
        modifier = modifier,
        text = text,
        labelStyle = labelTextStyle,
        labelColorType = labelColorType,
        enabled = enabled,
    )
}

@Composable
internal fun LabelComposable(
    modifier: Modifier,
    text: String,
    labelStyle: LabelTextType,
    labelColorType: LabelColorType,
    enabled: Boolean,
) {
    val color = when (labelColorType) {
        LabelColorType.Primary -> {
            MercadoPagoTheme.color.text.primary
        }

        LabelColorType.Secondary -> {
            MercadoPagoTheme.color.text.secondary
        }

        LabelColorType.Accent -> {
            MercadoPagoTheme.color.text.accent
        }

        LabelColorType.Negative -> {
            MercadoPagoTheme.color.text.negative
        }

        LabelColorType.Inverted -> {
            MercadoPagoTheme.color.text.inverted
        }
    }

    val style = when (labelStyle) {
        LabelTextType.Tittle -> {
            MercadoPagoTheme.typography.title.smallSemibold
        }

        LabelTextType.BodyMediumSemiBold -> {
            MercadoPagoTheme.typography.body.mediumSemibold
        }

        LabelTextType.BodyMediumRegular -> {
            MercadoPagoTheme.typography.body.mediumRegular
        }

        LabelTextType.BodySmallSemiBold -> {
            MercadoPagoTheme.typography.body.smallSemibold
        }

        LabelTextType.BodySmallRegular -> {
            MercadoPagoTheme.typography.body.smallRegular
        }

        LabelTextType.BodyExtraSmallSemiBold -> {
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

@Preview(name = "Label Tittle Text", group = TEXT_GROUP)
@Composable
internal fun LabelTittlePreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            Label(
                text = "My Tittle Primary Label",
                labelTextStyle = LabelTextType.Tittle,
                labelColorType = LabelColorType.Primary
            )
            Label(
                text = "My Tittle Secondary Label",
                labelTextStyle = LabelTextType.Tittle,
                labelColorType = LabelColorType.Secondary
            )
            Label(
                text = "My Tittle Accent Label",
                labelTextStyle = LabelTextType.Tittle,
                labelColorType = LabelColorType.Accent
            )
            Label(
                text = "My Tittle Inverted Label",
                labelTextStyle = LabelTextType.Tittle,
                labelColorType = LabelColorType.Inverted
            )
            Label(
                text = "My Tittle Negative Label",
                labelTextStyle = LabelTextType.Tittle,
                labelColorType = LabelColorType.Negative
            )
            Label(
                text = "My Tittle Disabled Label",
                labelTextStyle = LabelTextType.Tittle,
                labelColorType = LabelColorType.Primary,
                enabled = false
            )
        }
    }
}

@Preview(name = "Label BodyMediumSemiBold Text", group = TEXT_GROUP)
@Composable
internal fun LabelBodyMediumSemiBoldPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            Label(
                text = "My BodyMediumSemiBold Primary Label",
                labelTextStyle = LabelTextType.BodyMediumSemiBold,
                labelColorType = LabelColorType.Primary
            )
            Label(
                text = "My BodyMediumSemiBold Secondary Label",
                labelTextStyle = LabelTextType.BodyMediumSemiBold,
                labelColorType = LabelColorType.Secondary
            )
            Label(
                text = "My BodyMediumSemiBold Accent Label",
                labelTextStyle = LabelTextType.BodyMediumSemiBold,
                labelColorType = LabelColorType.Accent
            )
            Label(
                text = "My BodyMediumSemiBold Inverted Label",
                labelTextStyle = LabelTextType.BodyMediumSemiBold,
                labelColorType = LabelColorType.Inverted
            )
            Label(
                text = "My BodyMediumSemiBold Negative Label",
                labelTextStyle = LabelTextType.BodyMediumSemiBold,
                labelColorType = LabelColorType.Negative
            )
            Label(
                text = "My BodyMediumSemiBold Disabled Label",
                labelTextStyle = LabelTextType.BodyMediumSemiBold,
                labelColorType = LabelColorType.Primary,
                enabled = false
            )
        }
    }
}

@Preview(name = "Label BodyMediumRegular Text", group = TEXT_GROUP)
@Composable
internal fun LabelBodyMediumRegularPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            Label(
                text = "My BodyMediumRegular Primary Label",
                labelTextStyle = LabelTextType.BodyMediumRegular,
                labelColorType = LabelColorType.Primary
            )
            Label(
                text = "My BodyMediumRegular Secondary Label",
                labelTextStyle = LabelTextType.BodyMediumRegular,
                labelColorType = LabelColorType.Secondary
            )
            Label(
                text = "My BodyMediumRegular Accent Label",
                labelTextStyle = LabelTextType.BodyMediumRegular,
                labelColorType = LabelColorType.Accent
            )
            Label(
                text = "My BodyMediumRegular Inverted Label",
                labelTextStyle = LabelTextType.BodyMediumRegular,
                labelColorType = LabelColorType.Inverted
            )
            Label(
                text = "My BodyMediumRegular Negative Label",
                labelTextStyle = LabelTextType.BodyMediumRegular,
                labelColorType = LabelColorType.Negative
            )
            Label(
                text = "My BodyMediumRegular Disabled Label",
                labelTextStyle = LabelTextType.BodyMediumRegular,
                labelColorType = LabelColorType.Primary,
                enabled = false
            )
        }
    }
}

@Preview(name = "Label BodySmallSemiBold Text", group = TEXT_GROUP)
@Composable
internal fun LabelBodySmallSemiBoldPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            Label(
                text = "My BodySmallSemiBold Primary Label",
                labelTextStyle = LabelTextType.BodySmallSemiBold,
                labelColorType = LabelColorType.Primary
            )
            Label(
                text = "My BodySmallSemiBold Secondary Label",
                labelTextStyle = LabelTextType.BodySmallSemiBold,
                labelColorType = LabelColorType.Secondary
            )
            Label(
                text = "My BodySmallSemiBold Accent Label",
                labelTextStyle = LabelTextType.BodySmallSemiBold,
                labelColorType = LabelColorType.Accent
            )
            Label(
                text = "My BodySmallSemiBold Inverted Label",
                labelTextStyle = LabelTextType.BodySmallSemiBold,
                labelColorType = LabelColorType.Inverted
            )
            Label(
                text = "My BodySmallSemiBold Negative Label",
                labelTextStyle = LabelTextType.BodySmallSemiBold,
                labelColorType = LabelColorType.Negative
            )
            Label(
                text = "My BodySmallSemiBold Disabled Label",
                labelTextStyle = LabelTextType.BodySmallSemiBold,
                labelColorType = LabelColorType.Primary,
                enabled = false
            )
        }
    }
}

@Preview(name = "Label BodySmallRegular Text", group = TEXT_GROUP)
@Composable
internal fun LabelBodySmallRegularPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            Label(
                text = "My BodySmallRegular Primary Label",
                labelTextStyle = LabelTextType.BodySmallRegular,
                labelColorType = LabelColorType.Primary
            )
            Label(
                text = "My BodySmallRegular Secondary Label",
                labelTextStyle = LabelTextType.BodySmallRegular,
                labelColorType = LabelColorType.Secondary
            )
            Label(
                text = "My BodySmallRegular Accent Label",
                labelTextStyle = LabelTextType.BodySmallRegular,
                labelColorType = LabelColorType.Accent
            )
            Label(
                text = "My BodySmallRegular Inverted Label",
                labelTextStyle = LabelTextType.BodySmallRegular,
                labelColorType = LabelColorType.Inverted
            )
            Label(
                text = "My BodySmallRegular Negative Label",
                labelTextStyle = LabelTextType.BodySmallRegular,
                labelColorType = LabelColorType.Negative
            )
            Label(
                text = "My BodySmallRegular Disabled Label",
                labelTextStyle = LabelTextType.BodySmallRegular,
                labelColorType = LabelColorType.Primary,
                enabled = false
            )
        }
    }
}

@Preview(name = "Label BodyExtraSmallSemiBold Text", group = TEXT_GROUP)
@Composable
internal fun LabelBodyExtraSmallSemiBoldPreview() {
    MercadoPagoTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
        ) {
            Label(
                text = "My BodyExtraSmallSemiBold Primary Label",
                labelTextStyle = LabelTextType.BodyExtraSmallSemiBold,
                labelColorType = LabelColorType.Primary
            )
            Label(
                text = "My BodyExtraSmallSemiBold Secondary Label",
                labelTextStyle = LabelTextType.BodyExtraSmallSemiBold,
                labelColorType = LabelColorType.Secondary
            )
            Label(
                text = "My BodyExtraSmallSemiBold Accent Label",
                labelTextStyle = LabelTextType.BodyExtraSmallSemiBold,
                labelColorType = LabelColorType.Accent
            )
            Label(
                text = "My BodyExtraSmallSemiBold Inverted Label",
                labelTextStyle = LabelTextType.BodyExtraSmallSemiBold,
                labelColorType = LabelColorType.Inverted
            )
            Label(
                text = "My BodyExtraSmallSemiBold Negative Label",
                labelTextStyle = LabelTextType.BodyExtraSmallSemiBold,
                labelColorType = LabelColorType.Negative
            )
            Label(
                text = "My BodyExtraSmallSemiBold Disabled Label",
                labelTextStyle = LabelTextType.BodyExtraSmallSemiBold,
                labelColorType = LabelColorType.Primary,
                enabled = false
            )
        }
    }
}
