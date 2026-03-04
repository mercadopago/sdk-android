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

private const val DEFAULT_CARD_NUMBER_MAX_LENGTH = 19

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
 * @param enabled Whether the field is enabled for user interaction.
 * @param error Whether the field is in an error state. Displays error styling when true.
 * @param label Optional label text displayed above the field.
 * @param helper Optional helper text displayed below the field.
 * @param placeHolder Field place holder.
 * @param maxLength Field max length.
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
    enabled: Boolean = true,
    error: String = "",
    label: String = "",
    helper: String = "",
    placeHolder: String = MP_EMPTY_STRING,
    maxLength: Int? = null,
    visualTransformation: VisualTransformation = MaskVisualTransformationDefaults.CardNumber,
    onEvent: (CardNumberTextFieldEvent) -> Unit,
) {
    val defaults = getMPInputDefaults()
    MPInputBody(
        modifier = modifier,
        label = label,
        helper = helper,
        error = error,
        defaults = defaults,
    ) {
        CardNumberTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            onEvent = onEvent,
            textStyle = MercadoPagoAndesTheme.typography.body.default.medium,
            enabled = enabled,
            visualTransformation = visualTransformation,
            maxLength = maxLength ?: DEFAULT_CARD_NUMBER_MAX_LENGTH,
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error.isNotBlank(),
                    defaults = defaults,
                ) {
                    Box {
                        if (showPlaceHolder && state.isEmpty) {
                            MPText(
                                text = placeHolder,
                                style = MercadoPagoAndesTheme.typography.body.default.medium,
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
