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
 * This class holds the input data of secure fields, safeguarding it against exposure
 * and helping maintain a PCI Compliant environment.
 * @param value The current value of the field. It should use the value from the PCIFieldState.
 * @param onValueChange The callback that is triggered when the value of the field changes.
 * This should update the value of the PCIFieldState.
 * @param onFocusChanged The callback that is triggered when the focus of the field changes.
 * @param modifier The modifier to be applied to the field.
 * @param decorationBox The decoration box to be applied to the field. This will be passed by the integrators.
 * @param textStyle The text style to be applied to the field.
 * @param enabled controls the enabled state of the [PCITextField]. When `false`, the text
 * field will be neither editable nor focusable, the input of the text field will not be selectable
 * @param readOnly controls the editable state of the [PCITextField]. When `true`, the text
 * field can not be modified, however, a user can focus it and copy text from it. Read-only text
 * fields are usually used to display pre-filled forms that user can not edit
 * @param keyboardOptions The keyboard options to be applied to the field.
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction].
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
    keyboardActions: KeyboardActions = KeyboardActions.Default,
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
        keyboardActions = keyboardActions,
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
