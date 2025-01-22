package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

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
import com.mercadopago.sdk.android.coremethods.ui.components.PreviewGroup
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.ExpirationDateLength
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.MaskVisualTransformationDefaults
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextFieldTestTags
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextField

/**
 * Expiration date input component.
 *
 * This component allows users to enter a card expiration date.
 * It integrates the [PCIFieldState] that manages the entry and provides information of state of the field.
 *
 * @param modifier Modifier to customize the style and behavior of the field.
 * @param state A [PCIFieldState] object that contains and manages the input data for the security field.
 * @param onEvent A callback triggered in response to field events, such as focus changes or value changes.
 * @param enabled Controls the enabled state of the [SecurityCodeTextField], allowing or preventing user interaction.
 * @param readOnly Controls whether the field is editable or read-only.
 * @param decorationBox A composable that allows the addition of decorative elements around the text field.
 * @param textStyle Text style to be applied to the field's content.
 * @param cursorBrush Brush applied to the text field's cursor, allowing customization of the cursor's appearance.
 */
@Composable
fun ExpirationDateField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    onEvent: (ExpirationDateFieldEvent) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    decorationBox: @Composable (
        innerTextField: @Composable () -> Unit
    ) -> Unit = @Composable { innerTextField -> innerTextField() },
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    visualTransformation: VisualTransformation = MaskVisualTransformationDefaults.ExpirationDate,
) {
    PCITextField(
        value = state.input,
        onFocusChanged = { isFocused ->
            onEvent(ExpirationDateFieldEvent.OnFocusChanged(isFocused))
        },
        onValueChange = { value ->
            if (value.length <= ExpirationDateLength && value.none { !it.isDigit() }) {
                onEvent(ExpirationDateFieldEvent.OnInputFilled(isFilled = value.length == ExpirationDateLength))
                onEvent(ExpirationDateFieldEvent.OnLengthChanged(length = value.length))
                state.input = value
            }
        },
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        modifier = modifier.testTag(PCITextFieldTestTags.ExpirationDate.tag),
        decorationBox = decorationBox,
        textStyle = textStyle,
        enabled = enabled,
        readOnly = readOnly,
        visualTransformation = visualTransformation,
        cursorBrush = cursorBrush,
    )
}

@Preview(
    name = "Expiration Date Field Empty",
    group = PreviewGroup.EXPIRATION_DATE_FIELD,
    showBackground = true
)
@Composable
fun ExpirationDateEmptyPreview() {
    val state: PCIFieldState = rememberPCIFieldState()
    ExpirationDateField(
        state = state,
        onEvent = { securityCodeFieldEvent ->
        }
    )
}

@Preview(
    name = "Expiration Date Field Filled",
    group = PreviewGroup.EXPIRATION_DATE_FIELD,
    showBackground = true
)
@Composable
fun ExpirationDateFilledPreview() {
    val state: PCIFieldState = rememberPCIFieldState().apply {
        input = "1225"
    }
    ExpirationDateField(
        state = state,
        onEvent = { securityCodeFieldEvent ->
        }
    )
}
