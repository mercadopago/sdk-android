package com.mercadopago.sdk.android.components.inputs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme

/**
 * Default configuration for MPInput components using Andes tokens.
 * This data class centralizes all token references used by input components.
 */
internal data class MPInputDefaults(
    val colors: MPInputColorDefaults,
    val spacing: MPInputSpacingDefaults,
    val border: MPInputBorderDefaults,
)

/**
 * Typography defaults for input components.
 */
internal data class MPInputTypographyDefaults(
    val fontFamily: FontFamily,
    val fontSize: TextUnit,
    val lineHeight: TextUnit,
    val fontWeight: FontWeight,
    val letterSpacing: TextUnit,
)

/**
 * Color defaults for input components.
 */
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

/**
 * Spacing defaults for input components.
 */
internal data class MPInputSpacingDefaults(
    val labelPadding: Dp,
    val helperPadding: Dp,
    val horizontalPadding: Dp,
)

/**
 * Border defaults for input components.
 */
internal data class MPInputBorderDefaults(
    val widthIdle: Dp,
    val widthFocused: Dp,
    val shape: Shape,
)

/**
 * Gets the default configuration for MPInput components using Andes tokens.
 *
 * @return MPInputDefaults with all token values from MercadoPagoAndesTheme
 */
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
