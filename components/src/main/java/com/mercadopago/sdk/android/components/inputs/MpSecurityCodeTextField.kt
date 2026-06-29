package com.mercadopago.sdk.android.components.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.MP_EMPTY_STRING
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
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
 * @param enabled Whether the field is enabled for user interaction.
 * @param error Whether the field is in an error state. Displays error styling when true.
 * @param label Optional label text displayed above the field.
 * @param helper Optional helper text displayed below the field.
 * @param onClickTooltip tooltip of secure code text field
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
    enabled: Boolean = true,
    error: String = "",
    label: String = "",
    helper: String = "",
    onClickTooltip: () -> Unit,
    placeHolder: String = MP_EMPTY_STRING,
    onEvent: (SecurityCodeTextFieldEvent) -> Unit,
) {
    val defaults = getMPInputDefaults()
    MPInputBody(
        modifier = modifier,
        label = label,
        helper = helper,
        error = error,
        onClickTooltip = onClickTooltip,
        showTooltipIcon = true,
        defaults = defaults,
    ) {
        SecurityCodeTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            onEvent = onEvent,
            enabled = enabled,
            textStyle = MercadoPagoTheme.typography.body.default.medium,
            securityCodeSize = securityCodeSize,
            cursorBrush = SolidColor(defaults.colors.cursor),
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error.isNotBlank(),
                    defaults = defaults,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (showPlaceHolder && state.isEmpty) {
                            MPText(
                                text = placeHolder,
                                style = MercadoPagoTheme.typography.body.default.medium,
                                color = defaults.colors.textSecondary,
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

@Preview(showBackground = true)
@Composable
private fun MPSecurityCodeTextFieldPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        val securityCodeState = rememberPCIFieldState()
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            MPSecurityCodeTextField(
                state = securityCodeState,
                onClickTooltip = {},
            ) {
            }
        }
    }
}
