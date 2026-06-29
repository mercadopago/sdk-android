@file:Suppress("TooManyFunctions") // Showkase preview functions and private composable helpers inflate the count

package com.mercadopago.sdk.android.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

private const val BUTTON_GROUP = "BUTTON"

internal data class MPButtonColorDefaults(
    val loudIdle: Color,
    val loudActive: Color,
    val quietIdle: Color,
    val quietActive: Color,
    val disabled: Color,
    val iconInverse: Color,
    val iconAccent: Color,
    val iconDisabled: Color,
    val borderAccent: Color,
    val fillPrimary: Color,
    val textInverse: Color,
    val textAccent: Color,
)

internal data class MPButtonSpacingDefaults(
    val horizontalLarge: Dp,
    val horizontalMedium: Dp,
    val heightLarge: Dp,
    val heightMedium: Dp,
    val iconSpacingLarge: Dp,
    val iconSpacingMedium: Dp,
    val focusPadding: Dp,
)

internal data class MPButtonShapeDefaults(
    val medium: Shape,
    val small: Shape,
)

internal data class MPButtonBorderWidthDefaults(
    val medium: Dp,
    val large: Dp,
    val xlarge: Dp,
)

internal data class MPButtonDefaults(
    val colors: MPButtonColorDefaults,
    val spacing: MPButtonSpacingDefaults,
    val shape: MPButtonShapeDefaults,
    val borderWidth: MPButtonBorderWidthDefaults,
)

@Composable
private fun getMPButtonDefaults(): MPButtonDefaults {
    return MPButtonDefaults(
        colors = MPButtonColorDefaults(
            loudIdle = MercadoPagoTheme.color.interactive.fillLoud.idle,
            loudActive = MercadoPagoTheme.color.interactive.fillLoud.active,
            quietIdle = MercadoPagoTheme.color.interactive.fillQuiet.idle,
            quietActive = MercadoPagoTheme.color.interactive.fillQuiet.active,
            disabled = MercadoPagoTheme.color.fill.disabled,
            iconInverse = MercadoPagoTheme.color.icon.inverse,
            iconAccent = MercadoPagoTheme.color.icon.accent,
            iconDisabled = MercadoPagoTheme.color.icon.disabled,
            borderAccent = MercadoPagoTheme.color.border.accent,
            fillPrimary = MercadoPagoTheme.color.fill.primary,
            textInverse = MercadoPagoTheme.color.text.inverse,
            textAccent = MercadoPagoTheme.color.text.accent,
        ),
        spacing = MPButtonSpacingDefaults(
            horizontalLarge = MercadoPagoTheme.spacing.paddings.xsmall,
            horizontalMedium = MercadoPagoTheme.spacing.paddings.micro,
            heightLarge = MercadoPagoTheme.spacing.paddings.large,
            heightMedium = MercadoPagoTheme.spacing.paddings.small,
            iconSpacingLarge = MercadoPagoTheme.spacing.paddings.xmicro,
            iconSpacingMedium = MercadoPagoTheme.spacing.paddings.xnano,
            focusPadding = MercadoPagoTheme.spacing.paddings.xnano,
        ),
        shape = MPButtonShapeDefaults(
            medium = MercadoPagoTheme.shape.medium,
            small = MercadoPagoTheme.shape.small,
        ),
        borderWidth = MPButtonBorderWidthDefaults(
            medium = MercadoPagoTheme.borderWidth.medium,
            large = MercadoPagoTheme.borderWidth.large,
            xlarge = MercadoPagoTheme.borderWidth.xlarge,
        ),
    )
}

/**
 * Button style enum class, used to determine the visual appearance of the button
 * This is used to change the button's background color and text styling
 */
enum class MPButtonStyle {
    /**
     * Loud: Primary button style with accent background color
     */
    Loud,

    /**
     * Quiet: Secondary button style with secondary background color
     */
    Quiet,

    /**
     * Transparent: Button style with transparent background
     */
    Transparent,
}

/**
 * Button icon type enum class, used to determine the icon placement within the button
 * This is used to control where the icon appears relative to the text
 */
enum class MPButtonIconType {
    /**
     * None: No icon displayed
     */
    None,

    /**
     * Left: Icon displayed to the left of the text
     */
    Left,

    /**
     * Right: Icon displayed to the right of the text
     */
    Right,
}

/**
 * Button size enum class, used to determine the button dimensions and spacing
 * This is used to control the button's height, padding, and text size
 */
enum class MPButtonSize {
    /**
     * Large: Larger button size with more padding and bigger text
     */
    Large,

    /**
     * Medium: Smaller button size with less padding and smaller text
     */
    Medium,
}

@Composable
private fun getButtonBackgroundColor(
    style: MPButtonStyle,
    enabled: Boolean,
    isPressed: Boolean,
    defaults: MPButtonDefaults,
): Color {
    return when (style) {
        MPButtonStyle.Loud -> if (enabled) {
            if (isPressed) {
                defaults.colors.loudActive
            } else {
                defaults.colors.loudIdle
            }
        } else {
            defaults.colors.disabled
        }

        MPButtonStyle.Quiet -> if (enabled) {
            if (isPressed) {
                defaults.colors.quietActive
            } else {
                defaults.colors.quietIdle
            }
        } else {
            defaults.colors.disabled
        }

        MPButtonStyle.Transparent -> Color.Transparent
    }
}

@Composable
private fun getTextColor(
    style: MPButtonStyle,
    defaults: MPButtonDefaults,
): Color {
    return when (style) {
        MPButtonStyle.Loud -> defaults.colors.textInverse
        MPButtonStyle.Quiet -> defaults.colors.textAccent
        MPButtonStyle.Transparent -> defaults.colors.textAccent
    }
}

@Composable
private fun getIconColor(
    style: MPButtonStyle,
    defaults: MPButtonDefaults = getMPButtonDefaults(),
): Color {
    return when (style) {
        MPButtonStyle.Loud -> defaults.colors.iconInverse
        MPButtonStyle.Quiet -> defaults.colors.iconAccent
        MPButtonStyle.Transparent -> defaults.colors.iconAccent
    }
}

@Composable
private fun getHorizontalPadding(
    size: MPButtonSize,
    defaults: MPButtonDefaults,
): Dp {
    return if (size == MPButtonSize.Large) {
        defaults.spacing.horizontalLarge
    } else {
        defaults.spacing.horizontalMedium
    }
}

@Composable
private fun getButtonHeight(
    size: MPButtonSize,
    defaults: MPButtonDefaults,
): Dp {
    return if (size == MPButtonSize.Large) {
        defaults.spacing.heightLarge
    } else {
        defaults.spacing.heightMedium
    }
}

@Composable
private fun Modifier.getFocusedModifier(
    defaults: MPButtonDefaults,
): Modifier {
    return this
        .border(
            width = defaults.borderWidth.medium,
            color = defaults.colors.quietActive,
            shape = defaults.shape.small,
        )
        .border(
            width = defaults.borderWidth.large,
            color = defaults.colors.borderAccent,
            shape = defaults.shape.small,
        )
        .border(
            width = defaults.borderWidth.xlarge,
            color = defaults.colors.fillPrimary,
            shape = defaults.shape.small,
        )
        .padding(
            horizontal = defaults.spacing.focusPadding,
            vertical = defaults.spacing.focusPadding,
        )
}

@Composable
private fun LeftIcon(
    icon: ImageVector,
    size: MPButtonSize,
    enabled: Boolean,
    iconColor: Color,
    defaults: MPButtonDefaults,
) {
    Icon(
        icon,
        "",
        modifier = Modifier
            .size(if (size == MPButtonSize.Large) 20.dp else 13.dp),
        tint = if (enabled) iconColor else defaults.colors.iconDisabled,
    )
    Spacer(
        Modifier.size(
            if (size == MPButtonSize.Large) {
                defaults.spacing.iconSpacingLarge
            } else {
                defaults.spacing.iconSpacingMedium
            },
        ),
    )
}

@Composable
private fun RightIcon(
    icon: ImageVector,
    size: MPButtonSize,
    enabled: Boolean,
    iconColor: Color,
    defaults: MPButtonDefaults,
) {
    Spacer(
        Modifier.size(
            if (size == MPButtonSize.Large) {
                defaults.spacing.iconSpacingLarge
            } else {
                defaults.spacing.iconSpacingMedium
            },
        ),
    )
    Icon(
        icon,
        "",
        modifier = Modifier
            .size(if (size == MPButtonSize.Large) 20.dp else 13.dp),
        tint = if (enabled) iconColor else defaults.colors.iconDisabled,
    )
}

/**
 * Button component - Handles button implementation with different styles and configurations
 * This component is used to build interactive buttons throughout the application
 * handling different visual styles, sizes, and icon placements
 *
 * @param text: button text label
 * @param modifier: button modifier
 * @param icon: optional icon to display in the button
 * @param style: button style, must be one of MPButtonStyle values (Loud, Quiet, Transparent)
 * @param iconType: type of icon placement (None, Left, Right)
 * @param size: button size (Large, Medium)
 * @param enabled: Boolean indicates if the component is enabled
 * @param isLoading: when true, shows a loading fill animation behind the text and disables interaction
 * @param onClick: callback function executed when button is clicked
 */
@Composable
fun MPButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    style: MPButtonStyle = MPButtonStyle.Loud,
    iconType: MPButtonIconType = MPButtonIconType.None,
    size: MPButtonSize = MPButtonSize.Large,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    val defaults = getMPButtonDefaults()
    val drawIcon = icon != null && iconType != MPButtonIconType.None
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val isClickable = enabled && !isLoading
    val backgroundColor = getButtonBackgroundColor(style, enabled, isPressed, defaults)
    val textColor = getTextColor(style, defaults)
    val iconColor = getIconColor(style, defaults)
    val contentPaddingHorizontal = getHorizontalPadding(size, defaults)
    val buttonHeight = getButtonHeight(size, defaults)

    val borderModifier = if (isFocused) {
        modifier.getFocusedModifier(defaults)
    } else {
        modifier.padding(0.dp)
    }

    Box(
        modifier = borderModifier
            .height(buttonHeight)
            .clip(defaults.shape.medium)
            .background(backgroundColor)
            .semantics { if (isLoading) stateDescription = "Carregando" }
            .clickable(
                enabled = isClickable,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
    ) {
        if (isLoading) {
            ButtonLoadingFill(style = style, defaults = defaults)
        }
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .height(20.dp)
                .padding(horizontal = contentPaddingHorizontal),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (drawIcon && iconType == MPButtonIconType.Left) {
                LeftIcon(icon!!, size, isClickable, iconColor, defaults)
            }
            MPText(
                text = text,
                style = if (size == MPButtonSize.Large) {
                    MercadoPagoTheme.typography.body.emphasis.medium
                } else {
                    MercadoPagoTheme.typography.body.emphasis.small
                },
                color = textColor,
            )
            if (drawIcon && iconType == MPButtonIconType.Right) {
                RightIcon(icon!!, size, isClickable, iconColor, defaults)
            }
        }
    }
}

@Composable
private fun ButtonLoadingFill(
    style: MPButtonStyle,
    defaults: MPButtonDefaults,
) {
    val fillColor = when (style) {
        MPButtonStyle.Loud -> defaults.colors.loudActive
        MPButtonStyle.Quiet, MPButtonStyle.Transparent -> defaults.colors.quietActive
    }
    val infiniteTransition = rememberInfiniteTransition(label = "button_loading")
    val fillFraction by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "loading_fill",
    )
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(fraction = fillFraction)
            .background(fillColor),
    )
}

@Preview(name = "Button Styles Large", group = BUTTON_GROUP)
@Composable
private fun MPButtonStylesLargePreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp),
        ) {
            MPButton(text = "Label", style = MPButtonStyle.Loud) {}
            Spacer(Modifier.size(10.dp))
            MPButton(text = "Label", style = MPButtonStyle.Quiet) {}
            Spacer(Modifier.size(10.dp))
            MPButton(text = "Label", style = MPButtonStyle.Transparent) {}
            Spacer(Modifier.size(10.dp))
            MPButton(text = "Label", style = MPButtonStyle.Loud, enabled = false) {}
        }
    }
}

@Preview(name = "Button Styles Medium", group = BUTTON_GROUP)
@Composable
private fun MPButtonStylesMediumPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp),
        ) {
            MPButton(text = "Label", style = MPButtonStyle.Loud, size = MPButtonSize.Medium) {}
            Spacer(Modifier.size(10.dp))
            MPButton(text = "Label", style = MPButtonStyle.Quiet, size = MPButtonSize.Medium) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Transparent,
                size = MPButtonSize.Medium,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Loud,
                enabled = false,
                size = MPButtonSize.Medium,
            ) {}
        }
    }
}

@Preview(name = "Button Icon Left Large", group = BUTTON_GROUP)
@Composable
private fun MPButtonIconLeftLargePreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp),
        ) {
            MPButton(
                text = "Label",
                style = MPButtonStyle.Loud,
                iconType = MPButtonIconType.Left,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Quiet,
                iconType = MPButtonIconType.Left,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Transparent,
                iconType = MPButtonIconType.Left,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Loud,
                enabled = false,
                iconType = MPButtonIconType.Left,
                icon = Icons.Filled.Favorite,
            ) {}
        }
    }
}

@Preview(name = "Button Icon Left Medium", group = BUTTON_GROUP)
@Composable
private fun MPButtonIconLeftMediumPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp),
        ) {
            MPButton(
                text = "Label",
                style = MPButtonStyle.Loud,
                size = MPButtonSize.Medium,
                iconType = MPButtonIconType.Left,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Quiet,
                size = MPButtonSize.Medium,
                iconType = MPButtonIconType.Left,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Transparent,
                size = MPButtonSize.Medium,
                iconType = MPButtonIconType.Left,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Loud,
                enabled = false,
                size = MPButtonSize.Medium,
                iconType = MPButtonIconType.Left,
                icon = Icons.Filled.Favorite,
            ) {}
        }
    }
}

@Preview(name = "Button Icon Right Large", group = BUTTON_GROUP)
@Composable
private fun MPButtonIconRightLargePreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp),
        ) {
            MPButton(
                text = "Label",
                style = MPButtonStyle.Loud,
                iconType = MPButtonIconType.Right,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Quiet,
                iconType = MPButtonIconType.Right,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Transparent,
                iconType = MPButtonIconType.Right,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Loud,
                enabled = false,
                iconType = MPButtonIconType.Right,
                icon = Icons.Filled.Favorite,
            ) {}
        }
    }
}

@Preview(name = "Button Icon Right Medium", group = BUTTON_GROUP)
@Composable
private fun MPButtonIconRightMediumPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp),
        ) {
            MPButton(
                text = "Label",
                style = MPButtonStyle.Loud,
                size = MPButtonSize.Medium,
                iconType = MPButtonIconType.Right,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Quiet,
                size = MPButtonSize.Medium,
                iconType = MPButtonIconType.Right,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Transparent,
                size = MPButtonSize.Medium,
                iconType = MPButtonIconType.Right,
                icon = Icons.Filled.Favorite,
            ) {}
            Spacer(Modifier.size(10.dp))
            MPButton(
                text = "Label",
                style = MPButtonStyle.Loud,
                enabled = false,
                size = MPButtonSize.Medium,
                iconType = MPButtonIconType.Right,
                icon = Icons.Filled.Favorite,
            ) {}
        }
    }
}
