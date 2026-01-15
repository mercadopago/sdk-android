package com.mercadopago.sdk.android.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme

internal data class MPHeaderDefaults(
    val colors: MPHeaderColorDefaults,
    val spacing: MPHeaderSpacingDefaults,
    val shape: MPHeaderShapeDefaults,
    val typography: MPHeaderTypographyDefaults,
)

internal data class MPHeaderColorDefaults(
    val backgroundPrimary: Color,
    val backButtonBackground: Color,
    val backButtonIcon: Color,
    val titleText: Color,
)

internal data class MPHeaderSpacingDefaults(
    val backButtonCornerRadius: Dp,
    val fadeOverlayExtraHeight: Dp,
)

internal data class MPHeaderShapeDefaults(
    val backButtonShape: Shape,
)

internal data class MPHeaderTypographyDefaults(
    val titleExpanded: TextStyle,
    val titleCollapsed: TextStyle,
    val subtitleExpanded: TextStyle,
)

@Composable
internal fun getMPHeaderDefaults(): MPHeaderDefaults {
    return MPHeaderDefaults(
        colors = MPHeaderColorDefaults(
            backgroundPrimary = MercadoPagoAndesTheme.color.background.primary,
            backButtonBackground = MercadoPagoAndesTheme.color.surface.primaryActive,
            backButtonIcon = MercadoPagoAndesTheme.color.icon.accent,
            titleText = MercadoPagoAndesTheme.color.text.primary,
        ),
        spacing = MPHeaderSpacingDefaults(
            backButtonCornerRadius = MercadoPagoAndesTheme.radius.xsmall,
            fadeOverlayExtraHeight = MercadoPagoAndesTheme.spacing.paddings.xtiny,
        ),
        shape = MPHeaderShapeDefaults(
            backButtonShape = MercadoPagoAndesTheme.shape.xsmall,
        ),
        typography = MPHeaderTypographyDefaults(
            titleExpanded = MercadoPagoAndesTheme.typography.heading.default.huge,
            titleCollapsed = MercadoPagoAndesTheme.typography.heading.default.medium,
            subtitleExpanded = MercadoPagoAndesTheme.typography.body.default.medium,
        ),
    )
}
