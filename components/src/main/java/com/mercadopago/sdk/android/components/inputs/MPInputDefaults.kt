package com.mercadopago.sdk.android.components.inputs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme

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
            borderIdle = MercadoPagoAndesTheme.color.interactive.border.idle,
            borderActive = MercadoPagoAndesTheme.color.interactive.border.active,
            borderError = MercadoPagoAndesTheme.color.feedback.negative.borderLoud,
            cursor = MercadoPagoAndesTheme.color.interactive.border.active,
            textPrimary = MercadoPagoAndesTheme.color.text.primary,
            textSecondary = MercadoPagoAndesTheme.color.text.secondary,
            textDisabled = MercadoPagoAndesTheme.color.text.disabled,
            textError = MercadoPagoAndesTheme.color.feedback.negative.textLoud,
        ),
        spacing = MPInputSpacingDefaults(
            labelPadding = MercadoPagoAndesTheme.spacing.paddings.pico,
            helperPadding = MercadoPagoAndesTheme.spacing.paddings.xnano,
            horizontalPadding = 16.dp,
        ),
        border = MPInputBorderDefaults(
            widthIdle = MercadoPagoAndesTheme.borderWidth.small,
            widthFocused = MercadoPagoAndesTheme.borderWidth.medium,
            shape = MercadoPagoAndesTheme.shape.medium,
        ),
    )
}
