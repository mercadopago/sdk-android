package com.mercadopago.sdk.android.components.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.MP_EMPTY_STRING
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateFormat
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

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
    val defaults = getMPInputDefaults()
    MPInputBody(
        modifier = modifier,
        error = error,
        enabled = enabled,
        label = label,
        helper = helper,
        defaults = defaults,
    ) {
        ExpirationDateTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            dateFormat = dateFormat,
            onEvent = onEvent,
            textStyle = MercadoPagoAndesTheme.typography.heading.default.small,
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error,
                    defaults = defaults,
                ) {
                    Box {
                        if (showPlaceHolder && state.isEmpty) {
                            MPText(
                                text = placeHolder,
                                style = MercadoPagoAndesTheme.typography.body.default.medium,
                                color = defaults.colors.textPrimary,
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
private fun MPExpirationDateTextFieldPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes,
    ) {
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
