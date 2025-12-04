package com.mercadopago.sdk.android.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateFormat
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformationDefaults
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme

/**
 * Composable function that displays a card number text field with MercadoPago styling.
 *
 * This component wraps the CardNumberTextField with consistent styling, validation, and accessibility features.
 * It provides automatic formatting for card numbers and handles focus states, errors, and labels.
 *
 * @param modifier The modifier to be applied to the component.
 * @param state The PCIFieldState that manages the secure field state.
 * @param isFocused Whether the field is currently focused. Used to display focus-specific styling.
 * @param showPlaceHolder Whether to show a placeholder text when the field is empty.
 * @param error Whether the field is in an error state. Displays error styling when true.
 * @param enabled Whether the field is enabled for user interaction.
 * @param label Optional label text displayed above the field.
 * @param helper Optional helper text displayed below the field.
 * @param placeHolder Field place holder.
 * @param visualTransformation The visual transformation to apply to the input (e.g., masking).
 * Defaults to card number masking format.
 * @param onEvent Callback invoked when card number events occur (e.g., value changes, validation).
 */
@Composable
fun MPCardNumberTextField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    isFocused: Boolean = false,
    showPlaceHolder: Boolean = false,
    error: Boolean = false,
    enabled: Boolean = true,
    label: String? = null,
    helper: String? = null,
    placeHolder: String = MP_EMPTY_STRING,
    visualTransformation: VisualTransformation = MaskVisualTransformationDefaults.CardNumber,
    onEvent: (CardNumberTextFieldEvent) -> Unit,
) {
    MPInputBody(
        error = error,
        enabled = enabled,
        label = label,
        helper = helper,
    ) {
        CardNumberTextField(
            state = state,
            modifier = modifier.fillMaxWidth(),
            onEvent = onEvent,
            textStyle = MercadoPagoTheme.typography.body.mediumRegular,
            enabled = enabled,
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error,
                ) {
                    Box {
                        if (showPlaceHolder) {
                            MPText(
                                text = placeHolder,
                                textStyle = MPTextStyle.BodyMediumRegular,
                                modifier = Modifier.align(Alignment.CenterStart),
                            )
                        }
                        innerTextField()
                    }
                }
            },
        )
    }
}

/**
 * Composable function that displays an expiration date text field with MercadoPago styling.
 *
 * This component wraps the ExpirationDateTextField with consistent styling and validation for card expiration dates.
 * It automatically formats the date input based on the specified format and handles focus states, errors, and labels.
 *
 * @param modifier The modifier to be applied to the component.
 * @param state The PCIFieldState that manages the secure field state.
 * @param dateFormat The format for the expiration date. Defaults to ShortFormat (MM/YY).
 * @param isFocused Whether the field is currently focused. Used to display focus-specific styling.
 * @param showPlaceHolder Whether to show a placeholder text when the field is empty.
 * @param error Whether the field is in an error state. Displays error styling when true.
 * @param enabled Whether the field is enabled for user interaction.
 * @param label Optional label text displayed above the field.
 * @param helper Optional helper text displayed below the field.
 * @param placeHolder Field place holder.
 * @param onEvent Callback invoked when expiration date events occur (e.g., value changes, validation).
 */
@Composable
fun MPExpirationDateTextField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    dateFormat: ExpirationDateFormat = ExpirationDateFormat.ShortFormat,
    isFocused: Boolean = false,
    showPlaceHolder: Boolean = false,
    error: Boolean = false,
    enabled: Boolean = true,
    label: String? = null,
    helper: String? = null,
    placeHolder: String = MP_EMPTY_STRING,
    onEvent: (ExpirationDateTextFieldEvent) -> Unit,
) {
    MPInputBody(
        error = error,
        enabled = enabled,
        label = label,
        helper = helper,
    ) {
        ExpirationDateTextField(
            state = state,
            modifier = modifier.fillMaxWidth(),
            enabled = enabled,
            dateFormat = dateFormat,
            onEvent = onEvent,
            textStyle = MercadoPagoTheme.typography.body.mediumRegular,
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error,
                ) {
                    Box {
                        if (showPlaceHolder) {
                            MPText(
                                text = placeHolder,
                                textStyle = MPTextStyle.BodyMediumRegular,
                                modifier = Modifier.align(Alignment.CenterStart),
                            )
                        }
                        innerTextField()
                    }
                }
            },
        )
    }
}

/**
 * Composable function that displays a security code (CVV/CVC) text field with MercadoPago styling.
 *
 * This component wraps the SecurityCodeTextField with consistent styling and validation for card security codes.
 * It handles variable length security codes (typically 3 or 4 digits), provides secure input masking,
 * and displays an icon indicator. The component manages focus states, errors, and labels consistently
 * with other payment input fields.
 *
 * @param modifier The modifier to be applied to the component.
 * @param state The PCIFieldState that manages the secure field state.
 * @param securityCodeSize The expected length of the security code. Defaults to 3 for most cards,
 *                         but can be 4 for cards like American Express.
 * @param isFocused Whether the field is currently focused. Used to display focus-specific styling.
 * @param showPlaceHolder Whether to show a placeholder text when the field is empty.
 * @param error Whether the field is in an error state. Displays error styling when true.
 * @param enabled Whether the field is enabled for user interaction.
 * @param label Optional label text displayed above the field.
 * @param helper Optional helper text displayed below the field.
 * @param placeHolder Field place holder.
 * @param onEvent Callback invoked when security code events occur (e.g., value changes, validation).
 */
@Composable
fun MPSecurityCodeTextField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    securityCodeSize: Int = 3,
    isFocused: Boolean = false,
    showPlaceHolder: Boolean = false,
    error: Boolean = false,
    enabled: Boolean = true,
    label: String? = null,
    helper: String? = null,
    placeHolder: String = MP_EMPTY_STRING,
    onEvent: (SecurityCodeTextFieldEvent) -> Unit,
) {
    MPInputBody(
        error = error,
        enabled = enabled,
        label = label,
        helper = helper,
    ) {
        SecurityCodeTextField(
            state = state,
            modifier = modifier.fillMaxWidth(),
            onEvent = onEvent,
            enabled = enabled,
            textStyle = MercadoPagoTheme.typography.body.mediumRegular,
            securityCodeSize = securityCodeSize,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (showPlaceHolder) {
                            MPText(
                                text = placeHolder,
                                textStyle = MPTextStyle.BodyMediumRegular,
                                modifier = Modifier.align(Alignment.CenterStart),
                            )
                        }
                        innerTextField()
                    }
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(34.dp),
                    )
                }
            },
        )
    }
}

/**
 * Internal composable that provides a standardized decoration box for input fields.
 *
 * This component wraps the inner text field with consistent styling including borders,
 * height, and padding. It's used internally by all MP input field components to maintain
 * visual consistency.
 *
 * @param isFocused Whether the field is currently focused. Affects border styling.
 * @param error Whether the field is in an error state. Affects border color.
 * @param content The content to display inside the decoration box (typically innerTextField and icons).
 */
@Composable
internal fun MPInputDecorationBox(
    isFocused: Boolean,
    error: Boolean,
    content: @Composable (RowScope.() -> Unit),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .addBorder(
                isFocused = isFocused,
                error = error,
            )
            .height(OutlinedTextFieldDefaults.MinHeight)
            .padding(horizontal = 16.dp),
    ) {
        content()
    }
}

@Composable
internal fun MPInputBody(
    error: Boolean = false,
    enabled: Boolean = true,
    label: String? = null,
    helper: String? = null,
    showHelperIcon: Boolean = false,
    icon: ImageVector? = null,
    content: @Composable () -> Unit,
) {
    val state =
        if (error) {
            LabelState.Error
        } else if (!enabled) {
            LabelState.Disabled
        } else {
            LabelState.Idle
        }
    Column {
        label?.let {
            MPLabel(
                it,
                modifier = Modifier.padding(start = MercadoPagoTheme.spacing.xxs),
                textStyle = MPTextStyle.BodySmallRegular,
                labelState = state,
            )
        }
        content()
        helper?.let {
            MPHelper(
                text = it,
                modifier = Modifier.padding(start = MercadoPagoTheme.spacing.xxs),
                labelState = state,
                showIcon = showHelperIcon,
                icon = icon,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MPDropList(
    text: String,
    modifier: Modifier = Modifier,
    dropList: List<String>,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
        ) {
            MPText(
                text = text,
                modifier = modifier.widthIn(min = 32.dp),
            )
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            dropList.forEach { option ->
                DropdownMenuItem(
                    text = {
                        MPText(text = option)
                    },
                    onClick = {
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
internal fun MPHelper(
    text: String,
    modifier: Modifier = Modifier,
    showIcon: Boolean = false,
    labelState: LabelState = LabelState.Idle,
    icon: ImageVector? = null,
) {
    Row {
        if (showIcon) {
            icon?.let {
                Icon(it, MP_EMPTY_STRING)
                Spacer(modifier = Modifier.padding(start = MercadoPagoTheme.spacing.xxs))
            }
        }
        MPLabel(
            text,
            modifier = modifier,
            labelState = labelState,
            textStyle = MPTextStyle.BodyExtraSmallSemiBold,
        )
    }
}

@Composable
internal fun MPLabel(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: MPTextStyle = MPTextStyle.Title,
    labelState: LabelState = LabelState.Idle,
) {
    val colorType = when (labelState) {
        LabelState.Idle -> MPTextColorType.Primary
        LabelState.Disabled -> MPTextColorType.Inverted
        LabelState.Error -> MPTextColorType.Negative
    }

    MPText(
        text,
        modifier = modifier,
        textStyle = textStyle,
        colorType = colorType,
    )
}

@Composable
internal fun Modifier.addBorder(
    isFocused: Boolean,
    error: Boolean = false,
): Modifier {
    return border(
        width = if (isFocused) 2.dp else 1.dp,
        color = if (error) {
            MercadoPagoTheme.color.accentNegative
        } else if (isFocused) {
            MercadoPagoTheme.color.secondary
        } else {
            MercadoPagoTheme.color.secondarySecondVariant
        },
        shape = MercadoPagoTheme.shape.xs,
    )
}

@Preview(showBackground = true)
@Composable
private fun MPSecurityCodeTextFieldPreview() {
    MercadoPagoTheme {
        val securityCodeState = rememberPCIFieldState()
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            MPSecurityCodeTextField(
                state = securityCodeState,
            ) {
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MPCardNumberTextFieldPreview() {
    MercadoPagoTheme {
        val cardNumberState = rememberPCIFieldState()
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            MPCardNumberTextField(
                state = cardNumberState,
            ) {
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MPExpirationDateTextFieldPreview() {
    MercadoPagoTheme {
        val expirationDateState = rememberPCIFieldState()
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            MPExpirationDateTextField(
                state = expirationDateState,
            ) {
            }
        }
    }
}

internal enum class LabelState {
    Idle,
    Disabled,
    Error,
}
