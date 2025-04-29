package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.coremethods.analytics.metricPCIFieldFocus
import com.mercadopago.sdk.android.coremethods.analytics.metricPCIFieldInitialization
import com.mercadopago.sdk.android.coremethods.domain.usecase.validations.IsExpirationDateValidUseCase
import com.mercadopago.sdk.android.coremethods.ui.components.PreviewGroup
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextFieldTestTags
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformation

internal const val COMPONENT_NAME_EXPIRATION_DATE = "expirationDate"

/**
 * Expiration date input component.
 *
 * This component allows users to enter a card expiration date.
 * It integrates the [PCIFieldState] that manages the entry and provides information of state of the field.
 *
 * @param modifier Modifier to customize the style and behavior of the field.
 * @param state A [PCIFieldState] object that contains and manages the input data for the expiration date field.
 * @param onEvent A callback triggered in response to field events, such as focus changes or value changes.
 * @param dateFormat This changes the max length that the input handle, using the [ExpirationDateFormat] enum class
 * this have to be align to the visual transformation mask
 * @param enabled Controls the enabled state of the [ExpirationDateTextField], allowing or preventing user interaction.
 * @param readOnly Controls whether the field is editable or read-only.
 * @param decorationBox A composable that allows the addition of decorative elements around the text field.
 * @param textStyle Text style to be applied to the field's content.
 * @param keyboardOptions The keyboard options to be applied to the field.
 * @param cursorBrush Brush applied to the text field's cursor, allowing customization of the cursor's appearance.
 *
 * @sample com.mercadopago.sdk.android.coremethods.ui.components.samples.ExpirationDateFieldWithCustomMask
 */
@Composable
fun ExpirationDateTextField(
    modifier: Modifier = Modifier,
    state: PCIFieldState,
    onEvent: (ExpirationDateTextFieldEvent) -> Unit,
    dateFormat: ExpirationDateFormat = ExpirationDateFormat.ShortFormat,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    decorationBox: @Composable (
        innerTextField: @Composable () -> Unit,
    ) -> Unit = @Composable { innerTextField -> innerTextField() },
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
) {
    val isCardNumberValidUseCase = remember { IsExpirationDateValidUseCase() }

    LaunchedEffect(key1 = true) {
        MPAnalytics.getInstance().trackMetric(
            metricPCIFieldInitialization(
                field = COMPONENT_NAME_EXPIRATION_DATE,
            ),
        )
    }

    PCITextField(
        value = state.input,
        onFocusChanged = { isFocused ->
            onEvent(ExpirationDateTextFieldEvent.OnFocusChanged(isFocused))
            if (isFocused) {
                MPAnalytics.getInstance().trackMetric(
                    metricPCIFieldFocus(
                        field = COMPONENT_NAME_EXPIRATION_DATE,
                    ),
                )
            }
        },
        onValueChange = { value ->
            val updatedValue = value.take(dateFormat.digits)
            val inputDigits = updatedValue.filter { it.isDigit() }
            val isFilled = updatedValue.length == dateFormat.digits

            if (isFilled) {
                onEvent(
                    ExpirationDateTextFieldEvent.IsValid(
                        isCardNumberValidUseCase(
                            inputDigits,
                            dateFormat.digits,
                        ),
                    ),
                )
            }

            onEvent(ExpirationDateTextFieldEvent.OnLengthChanged(length = updatedValue.length))
            onEvent(ExpirationDateTextFieldEvent.OnInputFilled(isFilled = isFilled))

            state.input = updatedValue
        },
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        modifier = modifier.testTag(PCITextFieldTestTags.ExpirationDate.tag),
        decorationBox = decorationBox,
        textStyle = textStyle,
        enabled = enabled,
        readOnly = readOnly,
        visualTransformation = MaskVisualTransformation(dateFormat.mask),
        cursorBrush = cursorBrush,
    )
}

@Preview(
    name = "Expiration Date Field Empty",
    group = PreviewGroup.TEXT_FIELD,
    showBackground = true,
)
@Composable
internal fun ExpirationDateEmptyPreview() {
    val state: PCIFieldState = rememberPCIFieldState()
    ExpirationDateTextField(
        state = state,
        onEvent = { _ ->
        },
    )
}

@Preview(
    name = "Expiration Date Field Filled",
    group = PreviewGroup.TEXT_FIELD,
    showBackground = true,
)
@Composable
internal fun ExpirationDateFilledPreview() {
    val state: PCIFieldState = rememberPCIFieldState().apply {
        input = "1225"
    }
    ExpirationDateTextField(
        state = state,
        onEvent = { _ ->
        },
    )
}
