package com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.coremethods.ui.components.PreviewGroup
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextFieldTestTags
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState

internal const val COMPONENT_NAME_SIMPLE_TEXT = "simpleText"

/**
 * A PCI-compliant simple text input component for handling generic text input.
 *
 * This component provides a secure and flexible input field that can be used for any type
 * of text input in PCI-compliant contexts. It wraps the base [PCITextField] with a simplified
 * event interface while maintaining all the security features.
 *
 * The component integrates with [PCIFieldState] for secure input management and provides events
 * through [SimpleTextFieldEvent] for handling focus changes and value changes.
 *
 * Features:
 * - PCI-compliant input handling
 * - Flexible for any text input use case
 * - Real-time input state feedback
 * - Customizable appearance and behavior
 *
 * Example usage:
 * ```kotlin
 * val state = rememberPCIFieldState()
 *
 * SimpleTextField(
 *     state = state,
 *     onEvent = { event ->
 *         when (event) {
 *             is SimpleTextFieldEvent.OnValueChanged -> {
 *                 // Handle value change
 *             }
 *             is SimpleTextFieldEvent.OnFocusChanged -> {
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
 * @param onEvent Callback triggered for field events (focus changes, value changes)
 * @param enabled Controls whether the field is interactive
 * @param readOnly Controls whether the field is editable
 * @param decorationBox Composable for adding decorative elements around the text field
 * @param textStyle Text style applied to the input content
 * @param keyboardOptions Configuration for the software keyboard
 * @param keyboardActions Callbacks for keyboard action events
 * @param cursorBrush Brush applied to customize the cursor appearance
 * @param visualTransformation Visual transformations applied to the input text
 *
 * @see PCIFieldState
 * @see SimpleTextFieldEvent
 * @see PCITextField
 */
@Composable
fun SimpleTextField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    onEvent: (SimpleTextFieldEvent) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    decorationBox: @Composable (
        innerTextField: @Composable () -> Unit,
    ) -> Unit = @Composable { innerTextField -> innerTextField() },
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    PCITextField(
        value = state.input,
        onValueChange = { value ->
            state.input = value
            onEvent(SimpleTextFieldEvent.OnValueChanged(value = value))
        },
        onFocusChanged = { isFocused ->
            onEvent(SimpleTextFieldEvent.OnFocusChanged(isFocused))
        },
        modifier = modifier.testTag(PCITextFieldTestTags.Field.tag),
        enabled = enabled,
        readOnly = readOnly,
        decorationBox = decorationBox,
        cursorBrush = cursorBrush,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
    )
}

@Preview(
    name = "Simple Text Field Empty",
    group = PreviewGroup.TEXT_FIELD,
    showBackground = true,
)
@Composable
internal fun SimpleTextFieldEmptyPreview() {
    val state: PCIFieldState = rememberPCIFieldState()
    SimpleTextField(
        state = state,
        onEvent = { },
    )
}

@Preview(
    name = "Simple Text Field Filled",
    group = PreviewGroup.TEXT_FIELD,
    showBackground = true,
)
@Composable
internal fun SimpleTextFieldFilledPreview() {
    val state: PCIFieldState = rememberPCIFieldState().apply {
        input = "Sample text"
    }
    SimpleTextField(
        state = state,
        onEvent = { },
    )
}
