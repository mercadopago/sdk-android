package com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
 * PCI field that holds the logic to be used across multiple fields.
 * @param value The current value of the field. It should use the value from the PCIFieldState.
 * @param onValueChange The callback that is triggered when the value of the field changes.
 * This should update the value of the PCIFieldState.
 * @param onFocusChanged The callback that is triggered when the focus of the field changes.
 * @param modifier The modifier to be applied to the field.
 * @param decorationBox The decoration box to be applied to the field. This will be passed by the integrators.
 * @param textStyle The text style to be applied to the field.
 * @param enabled Whether the field is enabled or not.
 * @param readOnly Whether the field is read only or not.
 * @param keyboardOptions The keyboard options to be applied to the field.
 * @param cursorBrush The cursor brush to be applied to the field.
 * @param visualTransformation The visual transformation to be applied to the field.
 *
 * @sample com.mercadopago.sdk.android.coremethods.ui.components.samples.PCITextFieldBasicSample
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
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        decorationBox = decorationBox,
        enabled = enabled,
        cursorBrush = cursorBrush,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        modifier = modifier
            .testTag(PCITextFieldTestTags.Field.tag)
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.isFocused)
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
