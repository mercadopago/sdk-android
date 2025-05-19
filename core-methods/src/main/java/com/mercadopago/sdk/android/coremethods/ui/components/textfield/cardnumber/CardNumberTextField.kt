package com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber

import androidx.annotation.IntRange
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.coremethods.analytics.metricPCIFieldFocus
import com.mercadopago.sdk.android.coremethods.analytics.metricPCIFieldInitialization
import com.mercadopago.sdk.android.coremethods.domain.usecase.validations.IsCardNumberValidUseCase
import com.mercadopago.sdk.android.coremethods.ui.components.PreviewGroup
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextFieldTestTags
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformationDefaults

internal const val COMPONENT_NAME_CARD_NUMBER = "cardNumber"
internal const val BIN_LENGTH = 8
internal const val DEFAULT_CARD_NUMBER_MAX_LENGTH = 19
internal const val LAST_DIGITS_LENGTH = 4
internal const val MIN_CARD_LENGTH = 8L
internal const val MAX_CARD_LENGTH = 19L

/**
 * A PCI-compliant card number input component that handles user input of credit/debit card numbers.
 * This component provides real-time validation, formatting, and event notifications for card number input.
 * It supports various card number formats and automatically detects card types based on BIN.
 *
 * The component ensures PCI compliance by handling sensitive card data securely and provides
 * real-time feedback through events for validation, BIN changes, and input state changes.
 *
 * @see CardNumberTextFieldEvent
 * @see PCIFieldState
 *
 * @param state The [PCIFieldState] that manages the card number input value and PCI compliance
 * @param onEvent Callback for handling [CardNumberTextFieldEvent]s triggered during input
 * @param modifier Optional [Modifier] for customizing the text field's appearance and behavior
 * @param maxLength Maximum length of the card number (8-19 digits). Can be updated after BIN detection
 * @param enabled Whether the text field is enabled for input
 * @param readOnly Whether the text field is read-only (can be focused but not edited)
 * @param decorationBox Composable for customizing the text field's visual appearance
 * @param textStyle Style configuration for the text field's typography
 * @param keyboardOptions Configuration for the software keyboard
 * @param keyboardActions Callbacks for keyboard action events
 * @param cursorBrush Brush for painting the text cursor
 * @param visualTransformation Visual transformation for formatting the card number display
 *
 * Samples
 * @sample com.mercadopago.sdk.android.coremethods.ui.components.samples.CardNumberWithIssuerIconSample
 */
@Composable
fun CardNumberTextField(
    state: PCIFieldState,
    onEvent: (CardNumberTextFieldEvent) -> Unit,
    modifier: Modifier = Modifier,
    @IntRange(
        from = MIN_CARD_LENGTH,
        to = MAX_CARD_LENGTH,
    ) maxLength: Int = DEFAULT_CARD_NUMBER_MAX_LENGTH,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    decorationBox: @Composable (
        innerTextField: @Composable () -> Unit,
    ) -> Unit = @Composable { innerTextField -> innerTextField() },
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
    visualTransformation: VisualTransformation = MaskVisualTransformationDefaults.CardNumber,
) {
    val isCardNumberValidUseCase = remember { IsCardNumberValidUseCase() }

    LaunchedEffect(key1 = true) {
        MPAnalytics.getInstance().trackMetric(
            metricPCIFieldInitialization(
                field = COMPONENT_NAME_CARD_NUMBER,
            ),
        )
    }

    PCITextField(
        value = state.input,
        onValueChange = { value ->
            val inputDigits = value.filter { it.isDigit() }.take(maxLength)
            onEvent(CardNumberTextFieldEvent.OnLengthChanged(length = inputDigits.length))
            if (inputDigits.length >= BIN_LENGTH && state.input.length < BIN_LENGTH) {
                onEvent(CardNumberTextFieldEvent.OnBinChanged(cardBin = inputDigits.take(BIN_LENGTH)))
            }
            if (inputDigits.length < BIN_LENGTH && state.input.length >= BIN_LENGTH) {
                onEvent(CardNumberTextFieldEvent.OnBinChanged(cardBin = null))
            }
            val isValid = isCardNumberValidUseCase(inputDigits)
            if (isValid) {
                onEvent(
                    CardNumberTextFieldEvent.OnLastFourDigitsFilled(
                        lastFourDigits = inputDigits.takeLast(LAST_DIGITS_LENGTH),
                    ),
                )
            }
            onEvent(CardNumberTextFieldEvent.IsValid(isValid))
            state.input = inputDigits
        },
        onFocusChanged = { isFocused ->
            onEvent(CardNumberTextFieldEvent.OnFocusChanged(isFocused))
            if (isFocused) {
                MPAnalytics.getInstance().trackMetric(
                    metricPCIFieldFocus(
                        field = COMPONENT_NAME_CARD_NUMBER,
                    ),
                )
            }
        },
        enabled = enabled,
        readOnly = readOnly,
        decorationBox = decorationBox,
        cursorBrush = cursorBrush,
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        modifier = modifier.testTag(PCITextFieldTestTags.CardNumber.tag),
    )
}

@Preview(
    group = PreviewGroup.TEXT_FIELD,
    showBackground = true,
)
@Composable
internal fun CardNumberTextFieldEmptyPreview() {
    val state: PCIFieldState = rememberPCIFieldState()
    CardNumberTextField(
        state = state,
        onEvent = { _ -> },
    )
}

@Preview(
    group = PreviewGroup.TEXT_FIELD,
    showBackground = true,
)
@Composable
internal fun CardNumberText16DigitsFieldFilledPreview() {
    val state: PCIFieldState = rememberPCIFieldState().apply {
        input = "1234567890123456"
    }
    CardNumberTextField(
        state = state,
        onEvent = { _ -> },
    )
}

@Preview(
    group = PreviewGroup.TEXT_FIELD,
    showBackground = true,
)
@Composable
internal fun CardNumberText19DigitsFieldFilledPreview() {
    val state: PCIFieldState = rememberPCIFieldState().apply {
        input = "1234567890123456123"
    }
    CardNumberTextField(
        state = state,
        onEvent = { _ -> },
    )
}
