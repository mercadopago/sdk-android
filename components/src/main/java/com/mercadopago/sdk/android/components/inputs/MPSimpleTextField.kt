package com.mercadopago.sdk.android.components.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.MPTextStyle
import com.mercadopago.sdk.android.components.MP_EMPTY_STRING
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextFieldEvent
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
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
 * @param error Whether the field is in an error state. Displays error styling when true.
 * @param enabled Whether the field is enabled for user interaction.
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
    error: Boolean = false,
    enabled: Boolean = true,
    label: String? = null,
    helper: String? = null,
    placeHolder: String = MP_EMPTY_STRING,
    onEvent: (SimpleTextFieldEvent) -> Unit,
) {
    MPInputBody(
        modifier = modifier,
        error = error,
        enabled = enabled,
        label = label,
        helper = helper,
    ) {
        SimpleTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            onEvent = onEvent,
            enabled = enabled,
            textStyle = TextStyle(
                fontFamily = MercadoPagoAndesTheme.typography.heading.familyDefault,
                fontSize = MercadoPagoAndesTheme.typography.heading.size.size16,
                lineHeight = MercadoPagoAndesTheme.typography.heading.lineHeight.lineHeight20,
                fontWeight = MercadoPagoAndesTheme.typography.heading.weight.regular,
                letterSpacing = MercadoPagoAndesTheme.typography.heading.letterSpacing.spacing0,
            ),
            cursorBrush = SolidColor(MercadoPagoAndesTheme.color.interactive.border.active),
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error,
                ) {
                    Box {
                        if (showPlaceHolder && state.isEmpty) {
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

@Preview(showBackground = true)
@Composable
private fun MPSimpleTextFieldPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes
    ) {
        val simpleTextState = rememberPCIFieldState()
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            MPSimpleTextField(
                state = simpleTextState,
                label = "Name",
                placeHolder = "Enter your name",
                showPlaceHolder = true,
            ) {
            }
        }
    }
}
