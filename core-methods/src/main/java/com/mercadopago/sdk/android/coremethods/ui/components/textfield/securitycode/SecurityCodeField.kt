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
 *  The SecurityCode input
 *
 * @param modifier The modifier to be applied to the field.
 * @param state [PCIFieldState] This class holds the input data of secure fields
 * @param onEvent The callback that is triggered when the focus of the field changes.
 * @param securityCodeSize The security code length limit
 * @param enabled controls the enabled state of the [SecurityCode].
 * @param readOnly controls the editable state of the [SecurityCode].
 * @param decorationBox The decoration box to be applied to the field.
 * @param textStyle The text style to be applied to the field.
 * @param keyboardOptions The keyboard options to be applied to the field.
 * @param cursorBrush The cursor brush to be applied to the field.
 * @param visualTransformation The visual transformation to be applied to the field.
 * @sample com.mercadopago.sdk.android.coremethods.ui.components.samples.SecurityCodeFieldBasicSample
 * @sample com.mercadopago.sdk.android.coremethods.ui.components.samples.SecurityCodeFieldDecorationBoxSample
 */
@Composable
fun SecurityCode(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    onEvent: (SecurityCodeFieldEvent) -> Unit,
    securityCodeSize: Int = 3, // TODO- change to use a constant
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
