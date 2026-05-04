package com.mercadopago.sdk.android.components.inputs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

internal data class MPInputDefaults(
    val colors: MPInputColorDefaults,
    val spacing: MPInputSpacingDefaults,
    val border: MPInputBorderDefaults,
)

internal data class MPInputColorDefaults(
    val borderIdle: Color,
    val borderActive: Color,
    val borderDisabled: Color,
    val borderError: Color,
    val cursor: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textDisabled: Color,
    val textError: Color,
    val iconSecondary: Color,
)

internal data class MPInputSpacingDefaults(
    val labelPadding: Dp,
    val helperPadding: Dp,
    val horizontalPadding: Dp,
)

internal data class MPInputBorderDefaults(
    val widthIdle: Dp,
    val widthFocused: Dp,
    val shape: Shape,
)

@Composable
internal fun getMPInputDefaults(): MPInputDefaults {
    return MPInputDefaults(
        colors = MPInputColorDefaults(
            borderIdle = MercadoPagoTheme.color.interactive.border.idle,
            borderActive = MercadoPagoTheme.color.interactive.border.active,
            borderDisabled = MercadoPagoTheme.color.border.disabled,
            borderError = MercadoPagoTheme.color.feedback.negative.borderLoud,
            cursor = MercadoPagoTheme.color.interactive.border.active,
            textPrimary = MercadoPagoTheme.color.text.primary,
            textSecondary = MercadoPagoTheme.color.text.secondary,
            textDisabled = MercadoPagoTheme.color.text.disabled,
            textError = MercadoPagoTheme.color.feedback.negative.textLoud,
            iconSecondary = MercadoPagoTheme.color.icon.secondary,
        ),
        spacing = MPInputSpacingDefaults(
            labelPadding = MercadoPagoTheme.spacing.paddings.pico,
            helperPadding = MercadoPagoTheme.spacing.paddings.xnano,
            horizontalPadding = MercadoPagoTheme.spacing.paddings.micro,
        ),
        border = MPInputBorderDefaults(
            widthIdle = MercadoPagoTheme.borderWidth.small,
            widthFocused = MercadoPagoTheme.borderWidth.medium,
            shape = MercadoPagoTheme.shape.medium,
        ),
    )
}
