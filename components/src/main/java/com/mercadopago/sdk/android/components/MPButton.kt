package com.mercadopago.sdk.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    val buttonColors = when (style) {
        MPButtonStyle.Loud -> ButtonDefaults.buttonColors().copy(
            containerColor = if (isPressed) MercadoPagoTheme.color.accentSecondVariant
            else MercadoPagoTheme.color.accent,
            disabledContainerColor = MercadoPagoTheme.color.background.tertiary
        )

        MPButtonStyle.Quiet -> ButtonDefaults.buttonColors().copy(
            containerColor = if (isPressed) MercadoPagoTheme.color.secondarySecondVariant
            else MercadoPagoTheme.color.secondary,
            disabledContainerColor = MercadoPagoTheme.color.background.tertiary
        )

        MPButtonStyle.Transparent -> ButtonDefaults.buttonColors().copy(
            containerColor = if (isPressed) Color.Transparent
            else Color.Transparent,
            disabledContainerColor = Color.Transparent
        )
    }

    val textColor = when (style) {
        MPButtonStyle.Loud -> MPTextColorType.Inverted
        MPButtonStyle.Quiet -> MPTextColorType.Accent
        MPButtonStyle.Transparent -> MPTextColorType.Accent
    }

    val iconColor: Color = when (style) {
        MPButtonStyle.Loud -> MercadoPagoTheme.color.text.inverted
        MPButtonStyle.Quiet -> MercadoPagoTheme.color.text.accent
        MPButtonStyle.Transparent -> MercadoPagoTheme.color.text.accent
    }

    Button(
        onClick = onClick,
        modifier = if (isFocused) {
            modifier
                .border(
                    width = 2.dp,
                    color = MercadoPagoTheme.color.secondarySecondVariant,
                    shape = MaterialTheme.shapes.small
                )
                .border(
                    width = 3.dp,
                    color = MercadoPagoTheme.color.accent,
                    shape = MaterialTheme.shapes.small
                )
                .border(
                    width = 5.dp,
                    color = Color.White,
                    shape = MaterialTheme.shapes.small
                )
                .padding(horizontal = 4.dp, vertical = 1.dp)
        } else {
            modifier
                .padding(0.dp)
                .defaultMinSize(1.dp, 1.dp)
        },
        enabled = enabled,
        colors = buttonColors,
        shape = MercadoPagoTheme.shape.xs,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(
            horizontal = if (size == MPButtonSize.Large) if (iconType != MPButtonIconType.None) MercadoPagoTheme.spacing.m else MercadoPagoTheme.spacing.xl
            else MercadoPagoTheme.spacing.s,
            vertical = if (size == MPButtonSize.Large) MercadoPagoTheme.spacing.s else MercadoPagoTheme.spacing.xs
        )
    ) {
        Row {
            if (drawIcon && iconType == MPButtonIconType.Left) {
                Icon(
                    icon!!,
                    "",
                    modifier = Modifier
                        .size(if (size == MPButtonSize.Large) 20.dp else 13.dp)
                        .align(Alignment.CenterVertically),
                    tint = if (enabled) iconColor else MercadoPagoTheme.color.text.disabled
                )
                Spacer(Modifier.size(if (size == MPButtonSize.Large) MercadoPagoTheme.spacing.s else MercadoPagoTheme.spacing.xxs))
            }

            MPText(
                text,
                textStyle = if (size == MPButtonSize.Large) MPTextStyle.BodyMediumSemiBold else MPTextStyle.BodySmallSemiBold,
                colorType = textColor,
                enabled = enabled
            )

            if (drawIcon && iconType == MPButtonIconType.Right) {
                Spacer(Modifier.size(if (size == MPButtonSize.Large) MercadoPagoTheme.spacing.s else MercadoPagoTheme.spacing.xxs))
                Icon(
                    icon!!,
                    "",
                    modifier = Modifier
                        .size(if (size == MPButtonSize.Large) 20.dp else 13.dp)
                        .align(Alignment.CenterVertically),
                    tint = if (enabled) iconColor else MercadoPagoTheme.color.text.disabled
                )
            }
        }
    }
}


@Preview(name = "Button", group = BUTTON_GROUP)
@Composable
private fun MpButtonPreview() {
    MercadoPagoTheme {
        Row {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(10.dp),

                ) {
                // Loud style
                MpButton(text = "Label", style = MPButtonStyle.Loud) {

                }
                Spacer(Modifier.size(10.dp))
                // Quiet style
                MpButton(text = "Label", style = MPButtonStyle.Quiet) {

                }
                Spacer(Modifier.size(10.dp))
                // Transparent style
                MpButton(text = "Label", style = MPButtonStyle.Transparent) {

                }
                Spacer(Modifier.size(10.dp))

                // Disabled state
                MpButton(text = "Label", style = MPButtonStyle.Loud, enabled = false) {

                }
                Spacer(Modifier.size(10.dp))
                Text("Icon Left")
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Loud,
                    iconType = MPButtonIconType.Left,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Quiet style
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Quiet,
                    iconType = MPButtonIconType.Left,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Transparent style
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Transparent,
                    iconType = MPButtonIconType.Left,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))

                // Disabled state
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Loud,
                    enabled = false,
                    iconType = MPButtonIconType.Left,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                Text("Icon Right")
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Loud,
                    iconType = MPButtonIconType.Right,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Quiet style
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Quiet,
                    iconType = MPButtonIconType.Right,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Transparent style
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Transparent,
                    iconType = MPButtonIconType.Right,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Disabled state
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Loud,
                    enabled = false,
                    iconType = MPButtonIconType.Right,
                    icon = Icons.Filled.Favorite
                ) {

                }
            }

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(10.dp),
            ) {
                // Loud style
                MpButton(text = "Label", style = MPButtonStyle.Loud, size = MPButtonSize.Medium) {

                }
                Spacer(Modifier.size(10.dp))
                // Quiet style
                MpButton(text = "Label", style = MPButtonStyle.Quiet, size = MPButtonSize.Medium) {

                }
                Spacer(Modifier.size(10.dp))
                // Transparent style
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Transparent,
                    size = MPButtonSize.Medium
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Disabled state
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Loud,
                    enabled = false,
                    size = MPButtonSize.Medium
                ) {

                }


                Spacer(Modifier.size(10.dp))
                Text("Icon Left")
                // Loud style
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Loud,
                    size = MPButtonSize.Medium,
                    iconType = MPButtonIconType.Left,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Quiet style
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Quiet,
                    size = MPButtonSize.Medium,
                    iconType = MPButtonIconType.Left,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Transparent style
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Transparent,
                    size = MPButtonSize.Medium,
                    iconType = MPButtonIconType.Left,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Disabled state
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Loud,
                    enabled = false,
                    size = MPButtonSize.Medium,
                    iconType = MPButtonIconType.Left,
                    icon = Icons.Filled.Favorite
                ) {

                }

                Spacer(Modifier.size(10.dp))
                Text("Icon Right")
                // Loud style
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Loud,
                    size = MPButtonSize.Medium,
                    iconType = MPButtonIconType.Right,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Quiet style
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Quiet,
                    size = MPButtonSize.Medium,
                    iconType = MPButtonIconType.Right,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Transparent style
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Transparent,
                    size = MPButtonSize.Medium,
                    iconType = MPButtonIconType.Right,
                    icon = Icons.Filled.Favorite
                ) {

                }
                Spacer(Modifier.size(10.dp))
                // Disabled state
                MpButton(
                    text = "Label",
                    style = MPButtonStyle.Loud,
                    enabled = false,
                    size = MPButtonSize.Medium,
                    iconType = MPButtonIconType.Right,
                    icon = Icons.Filled.Favorite
                ) {

                }
            }
        }
    }
}
