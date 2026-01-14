package com.mercadopago.sdk.android.components.inputs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.components.MPText
import com.mercadopago.sdk.android.components.MP_EMPTY_STRING
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoAndesTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoThemes

/**
 * Composable function that displays an identification text field with MercadoPago styling.
 *
 * This component provides a PCI-compliant input field for identification documents (CPF, DNI, etc.)
 * with a dropdown selector for identification type. It combines a type selector dropdown with
 * a secure input field, managing focus states, errors, and labels consistently with other
 * payment input fields.
 *
 * @param modifier The modifier to be applied to the component.
 * @param state The PCIFieldState that manages the secure field state for the identification value.
 * @param identificationTypes List of available identification types for the dropdown.
 * @param selectedIdentificationType The currently selected identification type.
 * @param isFocused Whether the field is currently focused. Used to display focus-specific styling.
 * @param showPlaceHolder Whether to show a placeholder text when the field is empty.
 * @param error Whether the field is in an error state. Displays error styling when true.
 * @param enabled Whether the field is enabled for user interaction.
 * @param label Optional label text displayed above the field.
 * @param helper Optional helper text displayed below the field.
 * @param placeHolder Field place holder.
 * @param onEvent Callback invoked when identification field events occur (value changes, focus, type selection).
 */
@Composable
fun MPIdentificationTextField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    identificationTypes: List<IdentificationType>,
    selectedIdentificationType: IdentificationType?,
    isFocused: Boolean = false,
    showPlaceHolder: Boolean = false,
    error: Boolean = false,
    enabled: Boolean = true,
    label: String? = null,
    helper: String? = null,
    placeHolder: String = MP_EMPTY_STRING,
    onEvent: (IdentificationTextFieldEvent) -> Unit,
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
        IdentificationTextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            identificationType = selectedIdentificationType,
            onEvent = onEvent,
            enabled = enabled,
            textStyle = MercadoPagoAndesTheme.typography.heading.headingSmallDefault,
            cursorBrush = SolidColor(defaults.colors.cursor),
            decorationBox = { innerTextField ->
                MPInputDecorationBox(
                    isFocused = isFocused,
                    error = error,
                    defaults = defaults,
                ) {
                    MPIdentificationTypeSelector(
                        identificationTypes = identificationTypes,
                        selectedIdentificationType = selectedIdentificationType,
                        onTypeSelected = { identificationType ->
                            onEvent(IdentificationTextFieldEvent.OnTypeSelected(identificationType))
                        },
                        defaults = defaults,
                    )
                    VerticalDivider(modifier = Modifier.height(40.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (showPlaceHolder && state.isEmpty) {
                            MPText(
                                text = placeHolder,
                                style = MercadoPagoAndesTheme.typography.body.bodyMediumDefault,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MPIdentificationTypeSelector(
    identificationTypes: List<IdentificationType>,
    selectedIdentificationType: IdentificationType?,
    onTypeSelected: (IdentificationType) -> Unit,
    defaults: MPInputDefaults,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
        ) {
            MPText(
                text = selectedIdentificationType?.name.orEmpty(),
                style = MercadoPagoAndesTheme.typography.body.bodyMediumDefault,
                color = defaults.colors.textPrimary,
                modifier = Modifier.widthIn(min = 32.dp),
            )
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            identificationTypes.forEach { identificationType ->
                DropdownMenuItem(
                    text = {
                        identificationType.name?.let {
                            MPText(
                                text = it,
                                style = MercadoPagoAndesTheme.typography.body.bodyMediumDefault,
                                color = defaults.colors.textPrimary,
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        onTypeSelected(identificationType)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MPIdentificationTextFieldPreview() {
    MercadoPagoTheme(
        theme = MercadoPagoThemes.Andes,
    ) {
        val identificationState = rememberPCIFieldState()
        val identificationTypes = listOf(
            IdentificationType(id = "CPF", name = "CPF", type = "number", minLength = 11, maxLength = 11),
            IdentificationType(id = "CNPJ", name = "CNPJ", type = "number", minLength = 14, maxLength = 14),
        )
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            MPIdentificationTextField(
                state = identificationState,
                identificationTypes = identificationTypes,
                selectedIdentificationType = identificationTypes.first(),
                label = "Identification",
                placeHolder = "000.000.000-00",
                showPlaceHolder = true,
            ) {
            }
        }
    }
}
