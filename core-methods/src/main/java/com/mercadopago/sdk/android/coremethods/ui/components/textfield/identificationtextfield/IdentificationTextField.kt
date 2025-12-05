package com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.ui.components.PreviewGroup
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextFieldTestTags
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState

internal const val COMPONENT_NAME_IDENTIFICATION = "identification"

/**
 * A PCI-compliant identification input component for handling identification document numbers.
 *
 * This component provides a secure input field for identification numbers (CPF, DNI, etc.),
 * with automatic validation based on the selected identification type. It wraps the base
 * [PCITextField] with identification-specific logic while maintaining all the security features.
 *
 * The component integrates with [PCIFieldState] for secure input management and provides events
 * through [IdentificationTextFieldEvent] for handling focus changes and value changes.
 *
 * Features:
 * - PCI-compliant input handling
 * - Automatic max length validation based on identification type
 * - Configurable keyboard type (numeric or alphanumeric) based on identification type
 * - Real-time input state feedback
 * - Customizable appearance and behavior
 *
 * Example usage:
 * ```kotlin
 * val state = rememberPCIFieldState()
 * val selectedType = IdentificationType(
 *     id = "CPF",
 *     name = "CPF",
 *     type = "number",
 *     minLength = 11,
 *     maxLength = 11
 * )
 *
 * IdentificationTextField(
 *     state = state,
 *     identificationType = selectedType,
 *     onEvent = { event ->
 *         when (event) {
 *             is IdentificationTextFieldEvent.OnValueChanged -> {
 *                 // Handle value change
 *             }
 *             is IdentificationTextFieldEvent.OnFocusChanged -> {
 *                 // Handle focus change
 *             }
 *         }
 *     },
 *     textStyle = MaterialTheme.typography.bodyLarge
 * )
 * ```
 *
 * @param modifier Modifier to customize the style and behavior of the field
 * @param state A [PCIFieldState] object that manages the secure input data
 * @param identificationType The selected [IdentificationType] that defines validation rules
 * @param onEvent Callback triggered for field events (focus changes, value changes)
 * @param enabled Controls whether the field is interactive
 * @param readOnly Controls whether the field is editable
 * @param decorationBox Composable for adding decorative elements around the text field
 * @param textStyle Text style applied to the input content
 * @param keyboardOptions Configuration for the software keyboard (defaults based on identification type)
 * @param keyboardActions Callbacks for keyboard action events
 * @param cursorBrush Brush applied to customize the cursor appearance
 * @param visualTransformation Visual transformations applied to the input text
 *
 * @see PCIFieldState
 * @see IdentificationTextFieldEvent
 * @see IdentificationType
 * @see PCITextField
 */
@Composable
fun IdentificationTextField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    identificationType: IdentificationType?,
    onEvent: (IdentificationTextFieldEvent) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    decorationBox: @Composable (
        innerTextField: @Composable () -> Unit,
    ) -> Unit = @Composable { innerTextField -> innerTextField() },
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val maxLength = identificationType?.maxLength ?: Int.MAX_VALUE
    val keyboardType = if (identificationType?.type == "number") {
        KeyboardType.Number
    } else {
        KeyboardType.Text
    }

    PCITextField(
        value = state.input,
        onValueChange = { value ->
            if (value.length <= maxLength) {
                state.input = value
                onEvent(IdentificationTextFieldEvent.OnValueChanged(value = value))
            }
        },
        onFocusChanged = { isFocused ->
            onEvent(IdentificationTextFieldEvent.OnFocusChanged(isFocused))
        },
        modifier = modifier.testTag(PCITextFieldTestTags.Field.tag),
        enabled = enabled,
        readOnly = readOnly,
        decorationBox = decorationBox,
        cursorBrush = cursorBrush,
        keyboardOptions = keyboardOptions ?: KeyboardOptions(keyboardType = keyboardType),
        keyboardActions = keyboardActions,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
    )
}

@Preview(
    name = "Identification Text Field Empty",
    group = PreviewGroup.TEXT_FIELD,
    showBackground = true,
)
@Composable
internal fun IdentificationTextFieldEmptyPreview() {
    val state: PCIFieldState = rememberPCIFieldState()
    IdentificationTextField(
        state = state,
        identificationType = IdentificationType(
            id = "CPF",
            name = "CPF",
            type = "number",
            minLength = 11,
            maxLength = 11,
        ),
        onEvent = { },
    )
}

@Preview(
    name = "Identification Text Field Filled",
    group = PreviewGroup.TEXT_FIELD,
    showBackground = true,
)
@Composable
internal fun IdentificationTextFieldFilledPreview() {
    val state: PCIFieldState = rememberPCIFieldState().apply {
        input = "12345678909"
    }
    IdentificationTextField(
        state = state,
        identificationType = IdentificationType(
            id = "CPF",
            name = "CPF",
            type = "number",
            minLength = 11,
            maxLength = 11,
        ),
        onEvent = { },
    )
}
