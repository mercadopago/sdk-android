package com.mercadopago.sdk.android.components.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.MP_EMPTY_STRING
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextFieldEvent
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

/**
 * Composable function that displays a simple text field with MercadoPago styling.
 *
 * This component wraps the SimpleTextField with consistent styling for generic text input.
 * It provides a PCI-compliant input field that can be used for any type of text input,
 * managing focus states, errors, and labels consistently with other payment input fields.
 *
 * @param modifier The modifier to be applied to the component.
 * @param state The PCIFieldState that manages the secure field state.
 * @param isFocused Whether the field is currently focused. Used to display focus-specific styling.
 * @param showPlaceHolder Whether to show a placeholder text when the field is empty.
 * @param enabled Whether the field is enabled for user interaction.
 * @param error Whether the field is in an error state. Displays error styling when true.
 * @param label Optional label text displayed above the field.
 * @param helper Optional helper text displayed below the field.
 * @param placeHolder Field place holder.
 * @param onEvent Callback invoked when text field events occur (e.g., value changes, focus changes).
 */
@Composable
fun MPSimpleTextField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    isFocused: Boolean = false,
    showPlaceHolder: Boolean = false,
    enabled: Boolean = true,
    error: String = "",
    label: String = "",
    helper: String = "",
    placeHolder: String = MP_EMPTY_STRING,
    onEvent: (SimpleTextFieldEvent) -> Unit,
) {
    val defaults = getMPInputDefaults()
    MPInputBody(
        modifier = modifier,
        label = label,
        helper = helper,
        error = error,
        defaults = defaults,
    ) {
        SimpleTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            onEvent = onEvent,
            enabled = enabled,
            textStyle = MercadoPagoTheme.typography.body.default.medium,
            cursorBrush = SolidColor(defaults.colors.cursor),
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error.isNotBlank(),
                    enabled = enabled,
                    defaults = defaults,
                ) {
                    Box {
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

/**
 * Composable function that displays a simple text field with MercadoPago styling.
 *
 * Plain-string variant for non-PCI fields (e.g. email). Manages its own [BasicTextField]
 * internally and notifies focus changes via [onFocusChange].
 *
 * @param modifier The modifier to be applied to the component.
 * @param value The current text value of the field.
 * @param isFocused Whether the field is currently focused. Used to display focus-specific styling.
 * @param showPlaceHolder Whether to show a placeholder text when the field is empty.
 * @param enabled Whether the field is enabled for user interaction.
 * @param error Error message displayed below the field. Empty string means no error.
 * @param label Optional label text displayed above the field.
 * @param helper Optional helper text displayed below the field.
 * @param placeHolder Placeholder text shown when the field is empty and [showPlaceHolder] is true.
 * @param keyboardOptions Keyboard options such as keyboard type and IME action.
 * @param onValueChange Callback invoked when the text value changes.
 * @param onFocusChange Callback invoked when the focus state changes.
 */
@Composable
fun MPSimpleTextField(
    modifier: Modifier = Modifier,
    value: String,
    isFocused: Boolean = false,
    showPlaceHolder: Boolean = false,
    enabled: Boolean = true,
    error: String = "",
    label: String = "",
    helper: String = "",
    placeHolder: String = MP_EMPTY_STRING,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit = {},
) {
    val defaults = getMPInputDefaults()
    MPInputBody(
        modifier = modifier,
        label = label,
        helper = helper,
        error = error,
        defaults = defaults,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { onFocusChange(it.isFocused) },
            enabled = enabled,
            textStyle = MercadoPagoTheme.typography.body.default.medium,
            cursorBrush = SolidColor(defaults.colors.cursor),
            keyboardOptions = keyboardOptions,
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error.isNotBlank(),
                    enabled = enabled,
                    defaults = defaults,
                ) {
                    Box {
                        if (showPlaceHolder && value.isEmpty()) {
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
private fun MPSimpleTextFieldPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Default,
    ) {
        val simpleTextState = rememberPCIFieldState()
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            MPSimpleTextField(
                state = simpleTextState,
                label = "Name",
                placeHolder = "Enter your name",
                helper = "text helper",
                showPlaceHolder = true,
            ) {
            }
        }
    }
}
