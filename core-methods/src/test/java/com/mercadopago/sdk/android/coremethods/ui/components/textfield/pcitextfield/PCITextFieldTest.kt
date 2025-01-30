package com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PCITextFieldTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `when field is empty Then input should be empty`() {
        // Given
        val input = ""

        // When
        pciTextFieldRobot(composeTestRule) {
            createTextField()

            // Then
            assertTextInput(input)
        }
    }

    @Test
    fun `when user types input Then input should be updated`() {
        // Given
        val input = "1234"

        // When
        pciTextFieldRobot(composeTestRule) {
            createTextField()
            performTapOnInput()
            performTextInput(input)

            // Then
            assertTextInput(input)
        }
    }

    @Test
    fun `when user taps the input Then focus should be changed`() {
        // Given
        var isFocused = false

        // When
        pciTextFieldRobot(composeTestRule) {
            createTextField(
                onFocusChanged = {
                    isFocused = it
                },
            )
            performTapOnInput()

            // Then
            assertTrue(isFocused)
        }
    }

    @Test
    fun `when field is disabled and user taps the input Then focus does not change`() {
        // Given
        var isFocused = false

        // When
        pciTextFieldRobot(composeTestRule) {
            createTextField(
                onFocusChanged = {
                    isFocused = it
                },
                enabled = false,
            )
            performTapOnInput()

            // Then
            assertFalse(isFocused)
        }
    }

    @Test
    fun `when field has decoration box Then text should be visible`() {
        // Given
        var input = "1234"
        val title = "Card Number Field"

        // When
        pciTextFieldRobot(composeTestRule) {
            createTextField(
                decorationBox = { innerTextField ->
                    Column {
                        Text(text = title)
                        innerTextField()
                    }
                },
            )
            performTapOnInput()
            performTextInput(input)

            // Then
            assertTextInput(input)
            assertTextIsDisplayed(title)
        }
    }

    @Test
    fun `when user types input and configuration is changed Then input should be restored`() {
        // Given
        var input = "1234"
        val stateRestorationTester = StateRestorationTester(composeTestRule)

        // When
        pciTextFieldRobot(composeTestRule) {
            createTextField(stateRestorationTester = stateRestorationTester)
            performTextInput(input)

            // Perform configuration change
            stateRestorationTester.emulateSavedInstanceStateRestore()

            // Then
            assertTextInput(input)
        }
    }

    @Test
    fun `when user types text input and configuration is changed Then input should be restored`() {
        // Given
        var input = "abcdefg"
        val stateRestorationTester = StateRestorationTester(composeTestRule)

        // When
        pciTextFieldRobot(composeTestRule) {
            createTextField(stateRestorationTester = stateRestorationTester)
            performTextInput(input)

            // Perform configuration change
            stateRestorationTester.emulateSavedInstanceStateRestore()

            // Then
            assertTextInput(input)
        }
    }
}
