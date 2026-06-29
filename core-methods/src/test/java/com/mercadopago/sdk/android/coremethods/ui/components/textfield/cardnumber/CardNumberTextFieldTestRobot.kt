package com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.text.input.VisualTransformation
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.PCIFieldRobot
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextFieldTestTags
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformationDefaults

internal class CardNumberTextFieldTestRobot(
    composeRule: ComposeContentTestRule,
) : PCIFieldRobot(composeRule) {
    fun createTextField(
        visualTransformation: VisualTransformation = MaskVisualTransformationDefaults.CardNumber,
        enabled: Boolean = true,
        readOnly: Boolean = false,
        onEvent: (CardNumberTextFieldEvent) -> Unit = {},
        decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
            @Composable { innerTextField -> innerTextField() },
        maxLength: Int = DEFAULT_CARD_NUMBER_MAX_LENGTH,
        stateRestorationTester: StateRestorationTester? = null,
    ) {
        testTag = PCITextFieldTestTags.CardNumber.tag
        setContent(stateRestorationTester) {
            fieldState = rememberPCIFieldState()
            MaterialTheme {
                CardNumberTextField(
                    state = fieldState,
                    onEvent = onEvent,
                    visualTransformation = visualTransformation,
                    enabled = enabled,
                    readOnly = readOnly,
                    maxLength = maxLength,
                    decorationBox = decorationBox,
                )
            }
        }
    }
}

internal fun cardNumberTextFieldRobot(
    composeTestRule: ComposeContentTestRule,
    block: CardNumberTextFieldTestRobot.() -> Unit,
) = CardNumberTextFieldTestRobot(composeTestRule).apply(block)
