package com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import junit.framework.TestCase.assertEquals

internal class PCITextFieldRobot(
    private val composeRule: ComposeContentTestRule
) {

    private lateinit var fieldState: PCIFieldState

    fun createTextField(
        enabled: Boolean = true,
        readOnly: Boolean = false,
        onFocusChanged: (isFocused: Boolean) -> Unit = { },
        decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
            @Composable { innerTextField -> innerTextField() },
        stateRestorationTester: StateRestorationTester? = null,
    ) {
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

    private fun setContent(stateRestorationTester: StateRestorationTester?, content: @Composable () -> Unit) {
        if (stateRestorationTester != null) {
            stateRestorationTester.setContent(content)
        } else {
            composeRule.setContent(content)
        }
    }

    fun performTapOnInput() {
        composeRule.onNodeWithTag(PCITextFieldTestTags.Field.tag)
            .assertExists()
            .performClick()
    }

    fun performTextInput(text: String) {
        composeRule.onNodeWithTag(PCITextFieldTestTags.Field.tag)
            .assertExists()
            .performTextInput(text)
    }

    fun assertTextInput(text: String) {
        composeRule.onNodeWithTag(PCITextFieldTestTags.Field.tag)
            .assertExists()
            .assert(hasText(text))
        assertEquals(text, fieldState.input)
    }

    fun assertTextIsDisplayed(text: String) {
        composeRule.onNodeWithText(text)
            .assertExists()
            .assert(hasText(text))
    }
}

internal fun pciTextFieldRobot(
    composeTestRule: ComposeContentTestRule,
    block: PCITextFieldRobot.() -> Unit
) = PCITextFieldRobot(composeTestRule).apply(block)
