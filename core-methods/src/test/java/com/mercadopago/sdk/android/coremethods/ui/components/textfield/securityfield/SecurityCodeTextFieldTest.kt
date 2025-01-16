package com.mercadopago.sdk.android.coremethods.ui.components.textfield.securityfield

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeFieldEvent
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SecurityCodeTextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private data class SecurityCodeState(
        var isFocused: Boolean = false,
        var filled: Boolean = false,
        var length: Int = 0,
    )

    @Test
    fun `when field is empty Then input should be empty`() {
        // Given
        val input = ""

        // When
        securityFieldRobot(composeTestRule) {
            createSecurityField()

            // Then
            assertTextInput(input)
        }
    }

    @Test
    fun `when user types input Then input should be updated`() {
        // Given
        val input = "123"

        // When
        securityFieldRobot(composeTestRule) {
            createSecurityField()
            performTapOnInput()
            performTextInput(input)

            // Then
            assertTextInput(input)
        }
    }

    @Test
    fun `when user taps the input Then focus should be changed`() {
        // Given
        var secureCodeState = SecurityCodeState()

        // When
        securityFieldRobot(composeTestRule) {
            createSecurityField(
                onEvent = { securityCodeFieldEvent ->
                    when (securityCodeFieldEvent) {
                        is SecurityCodeFieldEvent.FocusChanged -> {
                            secureCodeState = secureCodeState.copy(
                                isFocused = securityCodeFieldEvent.isFocused
                            )
                        }
                    }
                }
            )
            performTapOnInput()

            // Then
            assertTrue(secureCodeState.isFocused)
        }
    }

    @Test
    fun `when field is disabled and user taps the input Then focus does not change`() {
        // Given
        var secureCodeState = SecurityCodeState()

        // When
        securityFieldRobot(composeTestRule) {
            createSecurityField(
                onEvent = { securityCodeFieldEvent ->
                    when (securityCodeFieldEvent) {
                        is SecurityCodeFieldEvent.FocusChanged -> {
                            secureCodeState = secureCodeState.copy(
                                isFocused = securityCodeFieldEvent.isFocused
                            )
                        }
                    }
                },
                enabled = false,
            )
            performTapOnInput()

            // Then
            assertFalse(secureCodeState.isFocused)
        }
    }

    @Test
    fun `when field has decoration box Then text should be visible`() {
        // Given
        var input = "123"
        val title = "Card Number Field"

        // When
        securityFieldRobot(composeTestRule) {
            createSecurityField(
                decorationBox = { innerTextField ->
                    Column {
                        Text(text = title)
                        innerTextField()
                    }
                }
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
        var input = "123"
        val stateRestorationTester = StateRestorationTester(composeTestRule)

        // When
        securityFieldRobot(composeTestRule) {
            createSecurityField(stateRestorationTester = stateRestorationTester)
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
        var input = "123"
        val stateRestorationTester = StateRestorationTester(composeTestRule)

        // When
        securityFieldRobot(composeTestRule) {
            createSecurityField(stateRestorationTester = stateRestorationTester)
            performTextInput(input)

            // Perform configuration change
            stateRestorationTester.emulateSavedInstanceStateRestore()

            // Then
            assertTextInput(input)
        }
    }
}
