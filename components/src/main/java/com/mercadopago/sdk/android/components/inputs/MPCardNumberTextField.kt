package com.mercadopago.sdk.android.components.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.MP_EMPTY_STRING
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformationDefaults
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

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
        modifier = modifier,
        error = error,
        enabled = enabled,
        label = label,
        helper = helper,
    ) {
        CardNumberTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            onEvent = onEvent,
            textStyle = MercadoPagoAndesTheme.typography.heading.headingSmallDefault,
            enabled = enabled,
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error,
                ) {
                    Box {
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
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MPCardNumberTextFieldPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes,
    ) {
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
