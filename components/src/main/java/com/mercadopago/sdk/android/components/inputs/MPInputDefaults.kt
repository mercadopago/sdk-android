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
    val andesTheme = MercadoPagoAndesTheme
    return MPInputDefaults(
        colors = MPInputColorDefaults(
            borderIdle = andesTheme.color.interactive.border.idle,
            borderActive = andesTheme.color.interactive.border.active,
            borderError = andesTheme.color.feedback.negative.borderLoud,
            cursor = andesTheme.color.interactive.border.active,
            textPrimary = andesTheme.color.text.primary,
            textSecondary = andesTheme.color.text.secondary,
            textDisabled = andesTheme.color.text.disabled,
            textError = andesTheme.color.feedback.negative.textLoud,
        ),
        spacing = MPInputSpacingDefaults(
            labelPadding = andesTheme.spacing.paddings.xnano,
            helperPadding = andesTheme.spacing.paddings.xnano,
            horizontalPadding = 16.dp,
        ),
        border = MPInputBorderDefaults(
            widthIdle = andesTheme.borderWidth.small,
            widthFocused = andesTheme.borderWidth.medium,
            shape = andesTheme.shape.xsmall,
        ),
    )
}
