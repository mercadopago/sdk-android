package com.mercadopago.sdk.android.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

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
            backgroundPrimary = MercadoPagoTheme.color.background.primary,
            backButtonBackground = MercadoPagoTheme.color.interactive.fillQuiet.idle,
            backButtonIcon = MercadoPagoTheme.color.icon.accent,
            titleText = MercadoPagoTheme.color.text.primary,
        ),
        spacing = MPHeaderSpacingDefaults(
            backButtonCornerRadius = MercadoPagoTheme.radius.medium,
            fadeOverlayExtraHeight = MercadoPagoTheme.spacing.paddings.xtiny,
        ),
        shape = MPHeaderShapeDefaults(
            backButtonShape = MercadoPagoTheme.shape.medium,
        ),
        typography = MPHeaderTypographyDefaults(
            titleExpanded = MercadoPagoTheme.typography.heading.default.huge,
            titleCollapsed = MercadoPagoTheme.typography.heading.default.medium,
            subtitleExpanded = MercadoPagoTheme.typography.body.default.medium,
        ),
    )
}
