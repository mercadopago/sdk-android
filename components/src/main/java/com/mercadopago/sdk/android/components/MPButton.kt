package com.mercadopago.sdk.android.components

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

private const val BUTTON_GROUP = "BUTTON"

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
    onClick: () -> Unit,
) {
    val drawIcon = icon != null && iconType != MPButtonIconType.None
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val backgroundColor = getButtonBackgroundColor(style, enabled, isPressed)
    val textColor = getTextColorType(style)
    val iconColor = getIconColor(style)
    val contentPaddingHorizontal = getHorizontalPadding(size, iconType)
    val contentPaddingVertical = getVerticalPadding(size)

    val borderModifier = if (isFocused) {
        modifier.getFocusedModifier()
    } else {
        modifier.padding(0.dp)
    }

    Box(
        modifier = borderModifier
            .clip(MercadoPagoTheme.shape.xs)
            .background(backgroundColor)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = contentPaddingHorizontal, vertical = contentPaddingVertical),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.height(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (drawIcon && iconType == MPButtonIconType.Left) {
                LeftIcon(icon!!, size, enabled, iconColor)
            }

            MPText(
                text,
                textStyle = if (size == MPButtonSize.Large) {
                    MPTextStyle.BodyMediumSemiBold
                } else {
                    MPTextStyle.BodySmallSemiBold
                },
                colorType = textColor,
                enabled = enabled,
            )

            if (drawIcon && iconType == MPButtonIconType.Right) {
                RightIcon(icon!!, size, enabled, iconColor)
            }
        }
    }
}

@Composable
private fun getButtonBackgroundColor(
    style: MPButtonStyle,
    enabled: Boolean,
    isPressed: Boolean,
): Color {
    return when (style) {
        MPButtonStyle.Loud -> if (enabled) {
            if (isPressed) {
                MercadoPagoTheme.color.accentSecondVariant
            } else {
                MercadoPagoTheme.color.accent
            }
        } else {
            MercadoPagoTheme.color.background.tertiary
        }

        MPButtonStyle.Quiet -> if (enabled) {
            if (isPressed) {
                MercadoPagoTheme.color.secondarySecondVariant
            } else {
                MercadoPagoTheme.color.secondary
            }
        } else {
            MercadoPagoTheme.color.background.tertiary
        }

        MPButtonStyle.Transparent -> Color.Transparent
    }
}

private fun getTextColorType(style: MPButtonStyle): MPTextColorType {
    return when (style) {
        MPButtonStyle.Loud -> MPTextColorType.Inverted
        MPButtonStyle.Quiet -> MPTextColorType.Accent
        MPButtonStyle.Transparent -> MPTextColorType.Accent
    }
}

@Composable
private fun getIconColor(style: MPButtonStyle): Color {
    return when (style) {
        MPButtonStyle.Loud -> MercadoPagoTheme.color.text.inverted
        MPButtonStyle.Quiet -> MercadoPagoTheme.color.text.accent
        MPButtonStyle.Transparent -> MercadoPagoTheme.color.text.accent
    }
}

@Composable
private fun getHorizontalPadding(
    size: MPButtonSize,
    iconType: MPButtonIconType,
): androidx.compose.ui.unit.Dp {
    return if (size == MPButtonSize.Large) {
        if (iconType != MPButtonIconType.None) {
            MercadoPagoTheme.spacing.m
        } else {
            MercadoPagoTheme.spacing.xl
        }
    } else {
        MercadoPagoTheme.spacing.s
    }
}

@Composable
private fun getVerticalPadding(size: MPButtonSize): androidx.compose.ui.unit.Dp {
    return if (size == MPButtonSize.Large) {
        MercadoPagoTheme.spacing.s
    } else {
        MercadoPagoTheme.spacing.xxs
    }
}

@Composable
private fun Modifier.getFocusedModifier(): Modifier {
    return this
        .border(
            width = 2.dp,
            color = MercadoPagoTheme.color.secondarySecondVariant,
            shape = MaterialTheme.shapes.small,
        )
        .border(
            width = 3.dp,
            color = MercadoPagoTheme.color.accent,
            shape = MaterialTheme.shapes.small,
        )
        .border(
            width = 5.dp,
            color = Color.White,
            shape = MaterialTheme.shapes.small,
        )
        .padding(horizontal = 4.dp, vertical = 5.dp)
}

@Composable
private fun LeftIcon(
    icon: ImageVector,
    size: MPButtonSize,
    enabled: Boolean,
    iconColor: Color,
) {
    Icon(
        icon,
        "",
        modifier = Modifier
            .size(if (size == MPButtonSize.Large) 20.dp else 13.dp),
        tint = if (enabled) iconColor else MercadoPagoTheme.color.text.disabled,
    )
    Spacer(
        Modifier.size(
            if (size == MPButtonSize.Large) {
                MercadoPagoTheme.spacing.s
            } else {
                MercadoPagoTheme.spacing.xxs
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
) {
    Spacer(
        Modifier.size(
            if (size == MPButtonSize.Large) {
                MercadoPagoTheme.spacing.s
            } else {
                MercadoPagoTheme.spacing.xxs
            },
        ),
    )
    Icon(
        icon,
        "",
        modifier = Modifier
            .size(if (size == MPButtonSize.Large) 20.dp else 13.dp),
        tint = if (enabled) iconColor else MercadoPagoTheme.color.text.disabled,
    )
}

@Preview(name = "Button Styles Large", group = BUTTON_GROUP)
@Composable
private fun MPButtonStylesLargePreview() {
    MercadoPagoTheme {
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
    MercadoPagoTheme {
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
    MercadoPagoTheme {
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
    MercadoPagoTheme {
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
    MercadoPagoTheme {
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
    MercadoPagoTheme {
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
