package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

import androidx.annotation.IntRange
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.coremethods.domain.usecase.IsExpirationDateValidUseCase
import com.mercadopago.sdk.android.coremethods.ui.components.PreviewGroup
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.DefaultExpirationDateMaxLength
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextFieldTestTags
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextField
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformationDefaults

/**
 * Expiration date input component.
 *
 * This component allows users to enter a card expiration date.
 * It integrates the [PCIFieldState] that manages the entry and provides information of state of the field.
 *
 * @param modifier Modifier to customize the style and behavior of the field.
 * @param state A [PCIFieldState] object that contains and manages the input data for the security field.
 * @param onEvent A callback triggered in response to field events, such as focus changes or value changes.
 * @param maxLength This changes the max length that the input handle,
 * this have to be align to the visual transformation mask
 * @param enabled Controls the enabled state of the [SecurityCodeTextField], allowing or preventing user interaction.
 * @param readOnly Controls whether the field is editable or read-only.
 * @param decorationBox A composable that allows the addition of decorative elements around the text field.
 * @param textStyle Text style to be applied to the field's content.
 * @param keyboardOptions The keyboard options to be applied to the field.
 * @param cursorBrush Brush applied to the text field's cursor, allowing customization of the cursor's appearance.
 * @param visualTransformation The visual transformation to be applied to the field.
 * @see MaskVisualTransformationDefaults
 *
 * @sample com.mercadopago.sdk.android.coremethods.ui.components.samples.ExpirationDateFieldWithCustomMask
 */
@Composable
fun ExpirationDateTextField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    onEvent: (ExpirationDateFieldEvent) -> Unit,
    @IntRange(from = 4, to = 6) maxLength: Int = DefaultExpirationDateMaxLength,
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
    val isCardNumberValidUseCase = remember { IsExpirationDateValidUseCase() }

    PCITextField(
        value = state.input,
        onFocusChanged = { isFocused ->
            onEvent(ExpirationDateFieldEvent.OnFocusChanged(isFocused))
        },
        onValueChange = { value ->
            val updatedValue = value.take(maxLength)
            val inputDigits = updatedValue.filter { it.isDigit() }
            val isValid = isCardNumberValidUseCase(inputDigits, maxLength)

            onEvent(ExpirationDateFieldEvent.OnLengthChanged(length = updatedValue.length))
            onEvent(ExpirationDateFieldEvent.OnInputFilled(isFilled = updatedValue.length == maxLength))
            onEvent(ExpirationDateFieldEvent.IsValid(isValid))
            state.input = updatedValue
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
    ExpirationDateTextField(
        state = state,
        onEvent = { expirationDateEvent ->
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
    ExpirationDateTextField(
        state = state,
        onEvent = { expirationDateEvent ->
        }
    )
}
