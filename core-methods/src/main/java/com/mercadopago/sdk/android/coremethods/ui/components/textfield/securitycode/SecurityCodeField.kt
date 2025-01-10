package com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.coremethods.ui.components.PreviewGroup
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState

/**
 *  This class
 *
 * @param state
 * @param onEvent
 * @param securityCodeSize The callback that is triggered when the focus of the field changes.
 * @param modifier The modifier to be applied to the field.
 * @param enabled controls the enabled state of the [PCITextField]. When `false`, the text
 * field will be neither editable nor focusable, the input of the text field will not be selectable
 * @param readOnly controls the editable state of the [PCITextField]. When `true`, the text
 * field can not be modified, however, a user can focus it and copy text from it. Read-only text
 * fields are usually used to display pre-filled forms that user can not edit
 * @param decorationBox The decoration box to be applied to the field. This will be passed by the integrators.
 * @param textStyle The text style to be applied to the field.
 * @param keyboardOptions The keyboard options to be applied to the field.
 * @param cursorBrush The cursor brush to be applied to the field.
 * @param textSelectionColor The selection text box color to be applied to the field.
 * @param visualTransformation The visual transformation to be applied to the field.
 * @sample com.mercadopago.sdk.android.coremethods.ui.components.samples.SecurityCodeFieldBasicSample
 * @sample com.mercadopago.sdk.android.coremethods.ui.components.samples.SecurityCodeFieldDecorationBoxSample
 */
@Composable
fun SecurityCode(
    state: PCIFieldState,
    onEvent: (SecurityCodeFieldEvent) -> Unit,
    securityCodeSize: Int = 3, // TODO- change to use a constant
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    decorationBox: @Composable (
        innerTextField: @Composable () -> Unit
    ) -> Unit = @Composable { innerTextField -> innerTextField() },
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    PCITextField(
        value = state.input,
        onValueChange = { value ->
            if (value.length <= securityCodeSize && value.none { !it.isDigit() }) {
                onEvent(SecurityCodeFieldEvent.Filled(isFilled = value.length == securityCodeSize))
                onEvent(SecurityCodeFieldEvent.Length(length = value.length))
                state.input = value
            }
        },
        onFocusChanged = { isFocused ->
            onEvent(SecurityCodeFieldEvent.FocusChanged(isFocused))
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        decorationBox = decorationBox,
        cursorBrush = cursorBrush,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        visualTransformation = visualTransformation
    )
}

@Preview(name = "Empty Security Field", group = PreviewGroup.SECURITY_FIELD, showBackground = true)
@Composable
fun SecurityCodePreview() {
    val state: PCIFieldState = rememberPCIFieldState()
    SecurityCode(
        state = state,
        onEvent = { securityCodeFieldEvent ->
        },
        securityCodeSize = 3
    )
}
