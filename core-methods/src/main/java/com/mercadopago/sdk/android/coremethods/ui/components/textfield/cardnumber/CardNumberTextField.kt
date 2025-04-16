package com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber

import androidx.annotation.IntRange
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
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
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformation
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformationDefaults

internal const val COMPONENT_NAME_CARD_NUMBER = "cardNumber"
internal const val BIN_LENGTH = 8
internal const val DEFAULT_CARD_NUMBER_MAX_LENGTH = 19
internal const val LAST_DIGITS_LENGTH = 4
internal const val MIN_CARD_LENGTH = 8L
internal const val MAX_CARD_LENGTH = 19L

/**
 * Card Number input component. This PCI handles user input of card numbers.
 * You can add configurations to this field like:
 * @param state The [PCIFieldState] of the component. It makes the field PCI and holds the card number value.
 * @param onEvent Callback for the [CardNumberTextFieldEvent]. It contains relevant callbacks
 * for the card number input type.
 * @param modifier optional [Modifier] for this text field.
 * @param maxLength This can be updated after calling paymentMethods with the card bin. It will ensure the users have
 * the right amount of numbers after the bin is completed. The value needs to range from 8 to 19 digits.
 * @param enabled controls the enabled state of the [BasicTextField]. When `false`, the text
 * field will be neither editable nor focusable, the input of the text field will not be selectable
 * @param readOnly controls the editable state of the [BasicTextField]. When `true`, the text
 * field can not be modified, however, a user can focus it and copy text from it. Read-only text
 * fields are usually used to display pre-filled forms that user can not edit
 * @param decorationBox Composable lambda that allows to add decorations around text field, such
 * as card issuer icon, placeholder, helper messages or similar, and automatically increase the hit target area
 * of the text field. To allow you to control the placement of the inner text field relative to your
 * decorations, the text field implementation will pass in a framework-controlled composable
 * parameter "innerTextField" to the decorationBox lambda you provide. You must call
 * innerTextField exactly once.
 * @param textStyle Style configuration that applies at character level such as color, font etc.
 * @param keyboardOptions software keyboard options that contains configuration such as
 * [ImeAction]. If you change the [KeyboardType], it will always be updated
 * to [KeyboardType.Number] to ensure users only type numbers inside this field.
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction].
 * @param cursorBrush [Brush] to paint cursor with. If [SolidColor] with [Color.Unspecified]
 * provided, there will be no cursor drawn
 * @param visualTransformation The visual transformation filter for changing the visual
 * representation of the card number. By default, a mask for 16 digits card number is used.
 * You can updated the mask after calling paymentMethods with the bin and setting this value
 * to a new mask using the [MaskVisualTransformation].
 *
 * Samples
 *
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
