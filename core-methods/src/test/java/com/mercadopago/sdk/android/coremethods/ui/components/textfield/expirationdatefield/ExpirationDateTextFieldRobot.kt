package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdatefield

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.onNodeWithTag
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.PCIFieldRobot
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextField
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCITextFieldTestTags
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.rememberPCIFieldState
import junit.framework.TestCase.assertEquals

internal class ExpirationDateTextFieldRobot(
    composeRule: ComposeContentTestRule,
) : PCIFieldRobot(composeRule) {
    fun createExpirationDateField(
        enabled: Boolean = true,
        readOnly: Boolean = false,
        onEvent: (ExpirationDateFieldEvent) -> Unit = {},
        decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
            @Composable { innerTextField -> innerTextField() },
        stateRestorationTester: StateRestorationTester? = null,
    ) {
        testTag = PCITextFieldTestTags.ExpirationDate.tag
        setContent(stateRestorationTester) {
            fieldState = rememberPCIFieldState()
            MaterialTheme {
                ExpirationDateTextField(
                    state = fieldState,
                    onEvent = onEvent,
                    enabled = enabled,
                    readOnly = readOnly,
                    decorationBox = decorationBox,
                )
            }
        }
    }

    fun assetTextVisualTransformation(
        unmaskedText: String,
        maskedText: String,
    ) {
        composeRule.onNodeWithTag(testTag)
            .assertExists()
            .assert(hasText(maskedText))
        assertEquals(unmaskedText, fieldState.input)
    }
}

internal fun expirationDateRobot(
    composeTestRule: ComposeContentTestRule,
    block: ExpirationDateTextFieldRobot.() -> Unit,
) = ExpirationDateTextFieldRobot(composeTestRule).apply(block)
