package com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadopago.sdk.android.coremethods.ui.components.PreviewGroup

/**
 * A PCI-compliant text field component for handling sensitive payment information.
 * This composable provides a secure input field that safeguards sensitive data against exposure
 * and helps maintain PCI compliance in payment forms.
 *
 * The component wraps Compose's BasicTextField with additional security measures and
 * PCI compliance features, while maintaining the flexibility to customize its appearance
 * and behavior through various parameters.
 *
 * Example:
 * ```kotlin
 * val state = rememberPCIFieldState()
 * PCITextField(
 *     value = state.input,
 *     onValueChange = { value ->
 *         state.input = value
 *     },
 *     onFocusChanged = { isFocused ->
 *         // Handle focus changes
 *     },
 *     decorationBox = { innerTextField ->
 *         // Custom decoration with border and padding
 *         Box(
 *             modifier = Modifier
 *                 .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
 *                 .padding(8.dp)
 *         ) {
 *             innerTextField()
 *         }
 *     }
 * )
 * ```
 *
 * @see PCIFieldState
 *
 * @param value The current value of the field, typically from PCIFieldState
 * @param onValueChange Callback triggered when the field's value changes
 * @param onFocusChanged Callback triggered when the field's focus state changes
 * @param modifier Optional modifier for customizing the field's appearance and behavior
 * @param decorationBox Composable for customizing the field's visual appearance
 * @param textStyle Style configuration for the field's typography
 * @param enabled Whether the field is enabled for input
 * @param readOnly Whether the field is read-only (can be focused but not edited)
 * @param keyboardOptions Configuration for the software keyboard
 * @param keyboardActions Callbacks for keyboard action events
 * @param cursorBrush Brush for painting the text cursor
 * @param visualTransformation Visual transformation for formatting the input display
 */
@Composable
internal fun PCITextField(
    value: String,
    onValueChange: (String) -> Unit,
    onFocusChanged: (isFocused: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
        @Composable { innerTextField -> innerTextField() },
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val previousFocusState = remember { mutableStateOf<Boolean?>(null) }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        decorationBox = decorationBox,
        enabled = enabled,
        cursorBrush = cursorBrush,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        modifier = modifier
            .testTag(PCITextFieldTestTags.Field.tag)
            .onFocusChanged { focusState ->
                val isFocused = focusState.isFocused
                val previousFocus = previousFocusState.value
                if (previousFocus != null && previousFocus != isFocused) {
                    onFocusChanged(isFocused)
                }
                previousFocusState.value = isFocused
            },
    )
}

@Preview(name = "PCI Empty Text Field", group = PreviewGroup.TEXT_FIELD)
@Composable
internal fun PCITextFieldEmptyPreview() {
    MaterialTheme {
        val state = rememberPCIFieldState()
        PCITextField(
            value = state.input,
            onValueChange = { value ->
                state.input = value
            },
            onFocusChanged = { },
        )
    }
}

@Preview(name = "PCI Filled Text Field", group = PreviewGroup.TEXT_FIELD)
@Composable
internal fun PCITextFieldFilledPreview() {
    MaterialTheme {
        val state = rememberPCIFieldState().apply {
            input = "12345678"
        }
        PCITextField(
            value = state.input,
            onValueChange = { value ->
                state.input = value
            },
            onFocusChanged = { },
        )
    }
}

@Preview(name = "PCI Filled Text Field With Decoration Box", group = PreviewGroup.TEXT_FIELD)
@Composable
internal fun PCITextFieldFilledWithDecorationBoxPreview() {
    MaterialTheme {
        val state = rememberPCIFieldState().apply {
            input = "12345678"
        }
        PCITextField(
            value = state.input,
            onValueChange = { value ->
                state.input = value
            },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(6.dp),
                        )
                        .padding(4.dp),
                ) {
                    innerTextField()
                }
            },
            onFocusChanged = { },
            modifier = Modifier.padding(4.dp),
        )
    }
}
