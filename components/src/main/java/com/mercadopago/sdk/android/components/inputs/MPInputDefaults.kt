package com.mercadopago.sdk.android.components.inputs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

internal data class MPInputDefaults(
    val colors: MPInputColorDefaults,
    val spacing: MPInputSpacingDefaults,
    val border: MPInputBorderDefaults,
)

internal data class MPInputColorDefaults(
    val borderIdle: Color,
    val borderActive: Color,
    val borderError: Color,
    val cursor: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textDisabled: Color,
    val textError: Color,
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
            borderError = MercadoPagoTheme.color.feedback.negative.borderLoud,
            cursor = MercadoPagoTheme.color.interactive.border.active,
            textPrimary = MercadoPagoTheme.color.text.primary,
            textSecondary = MercadoPagoTheme.color.text.secondary,
            textDisabled = MercadoPagoTheme.color.text.disabled,
            textError = MercadoPagoTheme.color.feedback.negative.textLoud,
        ),
        spacing = MPInputSpacingDefaults(
            labelPadding = MercadoPagoTheme.spacing.paddings.pico,
            helperPadding = MercadoPagoTheme.spacing.paddings.xnano,
            horizontalPadding = 16.dp,
        ),
        border = MPInputBorderDefaults(
            widthIdle = MercadoPagoTheme.borderWidth.small,
            widthFocused = MercadoPagoTheme.borderWidth.medium,
            shape = MercadoPagoTheme.shape.medium,
        ),
    )
}
