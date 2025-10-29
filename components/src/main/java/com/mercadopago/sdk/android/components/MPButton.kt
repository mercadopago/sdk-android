package com.mercadopago.sdk.android.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val BUTTON_GROUP = "BUTTON"

enum class MPButtonStyle {
    Loud,
    Quiet,
    Transparent
}

enum class MPButtonIconType {
    None,
    Left,
    Right
}

enum class MPButtonSize {
    Large,
    Medium
}

@Composable
fun MpButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    style: MPButtonStyle = MPButtonStyle.Loud,
    iconType: MPButtonIconType = MPButtonIconType.None,
    size: MPButtonSize = MPButtonSize.Large,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val drawIcon = icon != null && iconType != MPButtonIconType.None

    val shapeColor = when (style) {
        MPButtonStyle.Loud -> ButtonDefaults.buttonColors().copy(
            containerColor = MercadoPagoTheme.color.accent,
            disabledContainerColor = MercadoPagoTheme.color.background.tertiary
        )

        MPButtonStyle.Quiet -> ButtonDefaults.buttonColors().copy(
            containerColor = MercadoPagoTheme.color.secondarySecondVariant,
            disabledContainerColor = MercadoPagoTheme.color.background.tertiary
        )

        MPButtonStyle.Transparent -> ButtonDefaults.buttonColors().copy(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        )
    }

    val textColor = when (style) {
        MPButtonStyle.Loud -> MPTextColorType.Inverted
        MPButtonStyle.Quiet -> MPTextColorType.Accent
        MPButtonStyle.Transparent -> MPTextColorType.Accent
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = shapeColor,
        shape = MercadoPagoTheme.shape.xs,
        contentPadding = PaddingValues(
            horizontal = if (size == MPButtonSize.Large) MercadoPagoTheme.spacing.xl
            else MercadoPagoTheme.spacing.s,
            vertical = MercadoPagoTheme.spacing.s
        )
    ) {
        Row {
            if (drawIcon && iconType == MPButtonIconType.Left) {
                Image(icon!!, "")
            }

            MPText(
                text,
                textStyle = MPTextStyle.BodyMediumSemiBold,
                colorType = textColor,
                enabled = enabled
            )

            if (drawIcon && iconType == MPButtonIconType.Right) {
                Image(icon!!, "")
            }
        }
    }
}

@Preview
@Composable
fun MpButtonPreview() {
    MercadoPagoTheme {
        Column {
            MpButton(text = "Label", style = MPButtonStyle.Loud) {

            }

            MpButton(text = "Label", style = MPButtonStyle.Quiet) {

            }

            MpButton(text = "Label", style = MPButtonStyle.Transparent) {

            }
        }
    }
}
