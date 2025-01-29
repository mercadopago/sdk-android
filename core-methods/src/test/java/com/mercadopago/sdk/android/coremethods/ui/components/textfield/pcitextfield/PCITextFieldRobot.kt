package com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.PCIFieldRobot

internal open class PCITextFieldRobot(
    composeRule: ComposeContentTestRule,
) : PCIFieldRobot(composeRule) {
    fun createTextField(
        enabled: Boolean = true,
        readOnly: Boolean = false,
        onFocusChanged: (isFocused: Boolean) -> Unit = { },
        decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
            @Composable { innerTextField -> innerTextField() },
        stateRestorationTester: StateRestorationTester? = null,
    ) {
        testTag = PCITextFieldTestTags.Field.tag
        setContent(stateRestorationTester) {
            fieldState = rememberPCIFieldState()
            MaterialTheme {
                PCITextField(
                    value = fieldState.input,
                    onValueChange = {
                        fieldState.input = it
                    },
                    decorationBox = decorationBox,
                    onFocusChanged = onFocusChanged,
                    enabled = enabled,
                    readOnly = readOnly,
                )
            }
        }
    }
}

internal fun pciTextFieldRobot(
    composeTestRule: ComposeContentTestRule,
    block: PCITextFieldRobot.() -> Unit,
) = PCITextFieldRobot(composeTestRule).apply(block)
