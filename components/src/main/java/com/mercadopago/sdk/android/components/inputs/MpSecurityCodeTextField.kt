package com.mercadopago.sdk.android.components.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.MP_EMPTY_STRING
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

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
        modifier = modifier,
        error = error,
        enabled = enabled,
        label = label,
        helper = helper,
    ) {
        SecurityCodeTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            onEvent = onEvent,
            enabled = enabled,
            textStyle = MercadoPagoAndesTheme.typography.heading.headingSmallDefault,
            securityCodeSize = securityCodeSize,
            cursorBrush = SolidColor(MercadoPagoAndesTheme.color.interactive.border.active),
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (showPlaceHolder && state.isEmpty) {
                            MPText(
                                text = placeHolder,
                                style = MercadoPagoAndesTheme.typography.body.bodyMediumDefault,
                                color = MercadoPagoAndesTheme.color.text.primary,
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

@Preview(showBackground = true)
@Composable
private fun MPSecurityCodeTextFieldPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes,
    ) {
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
