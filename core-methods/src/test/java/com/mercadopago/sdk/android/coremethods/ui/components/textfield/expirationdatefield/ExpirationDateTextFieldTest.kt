package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdatefield

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ExpirationDateTextFieldTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    data class ExpirationDateState(
        var isFocused: Boolean = false,
        var filled: Boolean = false,
        var length: Int = 0,
    )

    @Before
    fun start() {
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.getInstance() } returns mockk<MPAnalytics>(relaxed = true)
    }

    @Test
    fun `when field is empty Then input should be empty`() {
        // Given
        val input = ""

        // When
        expirationDateRobot(composeTestRule) {
            createExpirationDateField()

            // Then
            assertTextInput(input)
        }
    }

    @Test
    fun `when user types input Then input should be updated`() {
        // Given
        val input = "123"
        val maskedInput = "12/3"

        // When
        expirationDateRobot(composeTestRule) {
            createExpirationDateField()
            performTapOnInput()
            performTextInput(input)

            // Then
            assetTextVisualTransformation(input, maskedInput)
        }
    }

    @Test
    fun `when user taps the input Then focus should be changed`() {
        // Given
        var expirationDateState = ExpirationDateState()

        // When
        expirationDateRobot(composeTestRule) {
            createExpirationDateField(
                onEvent = { expirationDateFieldEvent ->
                    when (expirationDateFieldEvent) {
                        is ExpirationDateTextFieldEvent.OnFocusChanged -> {
                            expirationDateState = expirationDateState.copy(
                                isFocused = expirationDateFieldEvent.isFocused,
                            )
                        }
                    }
                },
            )
            performTapOnInput()

            // Then
            assertTrue(expirationDateState.isFocused)
        }
    }

    @Test
    fun `when field is disabled and user taps the input Then focus does not change`() {
        // Given
        var expirationDateState = ExpirationDateState()

        // When
        expirationDateRobot(composeTestRule) {
            createExpirationDateField(
                onEvent = { expirationDateFieldEvent ->
                    when (expirationDateFieldEvent) {
                        is ExpirationDateTextFieldEvent.OnFocusChanged -> {
                            expirationDateState = expirationDateState.copy(
                                isFocused = expirationDateFieldEvent.isFocused,
                            )
                        }
                    }
                },
                enabled = false,
            )
            performTapOnInput()

            // Then
            assertFalse(expirationDateState.isFocused)
        }
    }

    @Test
    fun `when field has decoration box Then text should be visible`() {
        // Given
        val input = "123"
        val maskedInput = "12/3"
        val title = "Expiration Date"

        // When
        expirationDateRobot(composeTestRule) {
            createExpirationDateField(
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
            assetTextVisualTransformation(input, maskedInput)
            assertTextIsDisplayed(title)
        }
    }

    @Test
    fun `when user types input and configuration is changed Then input should be restored`() {
        // Given
        val input = "123"
        val maskedInput = "12/3"
        val stateRestorationTester = StateRestorationTester(composeTestRule)

        // When
        expirationDateRobot(composeTestRule) {
            createExpirationDateField(stateRestorationTester = stateRestorationTester)
            performTextInput(input)

            // Perform configuration change
            stateRestorationTester.emulateSavedInstanceStateRestore()

            // Then
            assetTextVisualTransformation(input, maskedInput)
        }
    }

    @Test
    fun `when user types text input and configuration is changed Then input should be restored`() {
        // Given
        val input = "123"
        val maskedInput = "12/3"
        val stateRestorationTester = StateRestorationTester(composeTestRule)

        // When
        expirationDateRobot(composeTestRule) {
            createExpirationDateField(stateRestorationTester = stateRestorationTester)
            performTextInput(input)

            // Perform configuration change
            stateRestorationTester.emulateSavedInstanceStateRestore()

            // Then
            assetTextVisualTransformation(input, maskedInput)
        }
    }
}
