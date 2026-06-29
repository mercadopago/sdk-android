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
 * A PCI-compliant text field component for entering card expiration dates.
 * This component provides a secure input field that handles card expiration dates with
 * automatic formatting and validation.
 *
 * The component supports both short (MM/YY) and long (MM/YYYY) date formats, automatically
 * formats the input with the appropriate separator, and validates the date against
 * current date and format rules.
 *
 * @see ExpirationDateFormat
 * @see ExpirationDateTextFieldEvent
 *
 * @param modifier Optional modifier for customizing the field's appearance and behavior
 * @param state The [PCIFieldState] that manages the expiration date input value
 * @param onEvent Callback for handling [ExpirationDateTextFieldEvent]s triggered during input
 * @param dateFormat The format to use for the expiration date (short or long)
 * @param enabled Whether the field is enabled for input
 * @param readOnly Whether the field is read-only (can be focused but not edited)
 * @param decorationBox Composable for customizing the field's visual appearance
 * @param textStyle Style configuration for the field's typography
 * @param keyboardOptions Configuration for the software keyboard
 * @param cursorBrush Brush for painting the text cursor
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
        MPAnalytics.tryGetInstance()?.trackMetric(
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
