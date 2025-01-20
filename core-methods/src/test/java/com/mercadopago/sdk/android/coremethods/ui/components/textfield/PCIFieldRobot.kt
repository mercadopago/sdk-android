package com.mercadopago.sdk.android.coremethods.ui.components.textfield

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import junit.framework.TestCase.assertEquals

/**
 * Used to test field States
 * @param composeRule allows you to set content without the necessity to provide a host for the content.
 */
internal open class PCIFieldRobot(
    private val composeRule: ComposeContentTestRule
) {

    /** Used to test fields States
     */
    internal lateinit var fieldState: PCIFieldState

    internal lateinit var testTag: String

    internal fun setContent(
        stateRestorationTester: StateRestorationTester?,
        content: @Composable () -> Unit
    ) {
        if (stateRestorationTester != null) {
            stateRestorationTester.setContent(content)
        } else {
            composeRule.setContent(content)
        }
    }

    fun performTapOnInput() {
        composeRule.onNodeWithTag(testTag)
            .assertExists()
            .performClick()
    }

    fun performTextInput(text: String) {
        composeRule.onNodeWithTag(testTag)
            .assertExists()
            .performTextInput(text)
    }

    fun assertTextInput(text: String) {
        composeRule.onNodeWithTag(testTag)
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
