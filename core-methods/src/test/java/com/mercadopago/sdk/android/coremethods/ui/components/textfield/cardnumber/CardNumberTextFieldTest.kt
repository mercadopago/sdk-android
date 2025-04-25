package com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createComposeRule
import app.cash.turbine.test
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.coremethods.ui.utils.MaskVisualTransformation
import com.mercadopago.sdk.android.coremethods.utils.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class CardNumberTextFieldTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun start() {
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.getInstance() } returns mockk<MPAnalytics>(relaxed = true)
    }

    @Test
    fun `when field is empty Then input should be empty`() =
        runTest {
            // Given
            val input = ""
            val events = Channel<CardNumberTextFieldEvent>(Channel.BUFFERED)

            // When
            cardNumberTextFieldRobot(composeTestRule) {
                createTextField(onEvent = events::trySend)

                performTextInput(input)

                // Then
                assertTextInput(input)
            }
            events.receiveAsFlow().test {
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = false), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = true), awaitItem())
            }
        }

    @Test
    fun `when card number has 15 digits and mask is default Then input should be formatted`() =
        runTest {
            // Given
            val input = "378282246310005"
            val maskedInput = "3782 8224 6310 005"
            val events = Channel<CardNumberTextFieldEvent>(Channel.BUFFERED)

            // When
            cardNumberTextFieldRobot(composeTestRule) {
                createTextField(onEvent = events::trySend)

                performTextInput(input)

                // Then
                assertTextInput(maskedInput, input)
            }
            events.receiveAsFlow().test {
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = false), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = true), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnLengthChanged(length = 15), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnBinChanged(cardBin = input.take(BIN_LENGTH)), awaitItem())
                assertEquals(
                    CardNumberTextFieldEvent.OnLastFourDigitsFilled(
                        lastFourDigits = input.takeLast(LAST_DIGITS_LENGTH),
                    ),
                    awaitItem(),
                )
                assertEquals(CardNumberTextFieldEvent.IsValid(isValid = true), awaitItem())
            }
        }

    @Test
    fun `when card number has 15 digits and mask is custom Then input should be formatted`() =
        runTest {
            // Given
            val input = "378282246310005"
            val maskedInput = "3782 822463 10005"
            val events = Channel<CardNumberTextFieldEvent>(Channel.BUFFERED)

            // When
            cardNumberTextFieldRobot(composeTestRule) {
                createTextField(
                    visualTransformation = MaskVisualTransformation("#### ###### #####"),
                    onEvent = events::trySend,
                )

                performTextInput(input)

                // Then
                assertTextInput(maskedInput, input)
            }
            events.receiveAsFlow().test {
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = false), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = true), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnLengthChanged(length = 15), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnBinChanged(cardBin = input.take(BIN_LENGTH)), awaitItem())
                assertEquals(
                    CardNumberTextFieldEvent.OnLastFourDigitsFilled(
                        lastFourDigits = input.takeLast(LAST_DIGITS_LENGTH),
                    ),
                    awaitItem(),
                )
                assertEquals(CardNumberTextFieldEvent.IsValid(isValid = true), awaitItem())
            }
        }

    @Test
    fun `when card number has 16 digits and mask is default Then input should be formatted`() =
        runTest {
            // Given
            val input = "5031433215406351"
            val maskedInput = "5031 4332 1540 6351"
            val events = Channel<CardNumberTextFieldEvent>(Channel.BUFFERED)

            // When
            cardNumberTextFieldRobot(composeTestRule) {
                createTextField(onEvent = events::trySend)

                performTextInput(input)

                // Then
                assertTextInput(maskedInput, input)
            }
            events.receiveAsFlow().test {
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = false), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = true), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnLengthChanged(length = 16), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnBinChanged(cardBin = input.take(BIN_LENGTH)), awaitItem())
                assertEquals(
                    CardNumberTextFieldEvent.OnLastFourDigitsFilled(
                        lastFourDigits = input.takeLast(LAST_DIGITS_LENGTH),
                    ),
                    awaitItem(),
                )
                assertEquals(CardNumberTextFieldEvent.IsValid(isValid = true), awaitItem())
            }
        }

    @Test
    fun `when card number has 19 digits and mask is default Then input should be formatted`() =
        runTest {
            // Given
            val input = "6205500000000000004"
            val maskedInput = "6205 5000 0000 0000004"
            val events = Channel<CardNumberTextFieldEvent>(Channel.BUFFERED)

            // When
            cardNumberTextFieldRobot(composeTestRule) {
                createTextField(onEvent = events::trySend)

                performTextInput(input)

                // Then
                assertTextInput(maskedInput, input)
            }
            events.receiveAsFlow().test {
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = false), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = true), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnLengthChanged(length = 19), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnBinChanged(cardBin = input.take(BIN_LENGTH)), awaitItem())
                assertEquals(
                    CardNumberTextFieldEvent.OnLastFourDigitsFilled(
                        lastFourDigits = input.takeLast(LAST_DIGITS_LENGTH),
                    ),
                    awaitItem(),
                )
                assertEquals(CardNumberTextFieldEvent.IsValid(isValid = true), awaitItem())
            }
        }

    @Test
    fun `when card number has 14 digits and mask is default Then input should be formatted`() =
        runTest {
            // Given
            val input = "36227206271667"
            val maskedInput = "3622 7206 2716 67"
            val events = Channel<CardNumberTextFieldEvent>(Channel.BUFFERED)

            // When
            cardNumberTextFieldRobot(composeTestRule) {
                createTextField(onEvent = events::trySend)

                performTextInput(input)

                // Then
                assertTextInput(maskedInput, input)
            }
            events.receiveAsFlow().test {
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = false), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = true), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnLengthChanged(length = 14), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnBinChanged(cardBin = input.take(BIN_LENGTH)), awaitItem())
                assertEquals(
                    CardNumberTextFieldEvent.OnLastFourDigitsFilled(
                        lastFourDigits = input.takeLast(LAST_DIGITS_LENGTH),
                    ),
                    awaitItem(),
                )
                assertEquals(CardNumberTextFieldEvent.IsValid(isValid = true), awaitItem())
            }
        }

    @Test
    fun `when card number more digits than the max length Then input should only have max length`() =
        runTest {
            // Given
            val input = "6205500000000000004123"
            val maskedInput = "6205 5000 0000 0000004"
            val maxLength = 19
            val inputWithMaxLength = input.take(maxLength)

            val events = Channel<CardNumberTextFieldEvent>(Channel.BUFFERED)

            // When
            cardNumberTextFieldRobot(composeTestRule) {
                createTextField(
                    onEvent = events::trySend,
                    maxLength = maxLength,
                )

                performTextInput(input)

                // Then
                assertTextInput(maskedInput, inputWithMaxLength)
            }
            events.receiveAsFlow().test {
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = false), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = true), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnLengthChanged(length = maxLength), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnBinChanged(cardBin = input.take(BIN_LENGTH)), awaitItem())
                assertEquals(
                    CardNumberTextFieldEvent.OnLastFourDigitsFilled(
                        lastFourDigits = inputWithMaxLength.takeLast(LAST_DIGITS_LENGTH),
                    ),
                    awaitItem(),
                )
                assertEquals(CardNumberTextFieldEvent.IsValid(isValid = true), awaitItem())
            }
        }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `when user types number and deletes bin Then event should trigger update`() =
        runTest {
            // Given
            val input = "36227206271667"
            val events = Channel<CardNumberTextFieldEvent>(Channel.BUFFERED)

            // When
            cardNumberTextFieldRobot(composeTestRule) {
                createTextField(onEvent = events::trySend)
                performTextInput(input)
                clearTextInput()
            }

            // Then
            events.receiveAsFlow().test {
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = false), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnFocusChanged(isFocused = true), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnLengthChanged(length = 14), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnBinChanged(cardBin = input.take(BIN_LENGTH)), awaitItem())
                assertEquals(
                    CardNumberTextFieldEvent.OnLastFourDigitsFilled(
                        lastFourDigits = input.takeLast(LAST_DIGITS_LENGTH),
                    ),
                    awaitItem(),
                )
                assertEquals(CardNumberTextFieldEvent.IsValid(isValid = true), awaitItem())

                // Cleared Field Validations
                assertEquals(CardNumberTextFieldEvent.OnLengthChanged(length = 0), awaitItem())
                assertEquals(CardNumberTextFieldEvent.OnBinChanged(cardBin = null), awaitItem())
                assertEquals(CardNumberTextFieldEvent.IsValid(isValid = false), awaitItem())
            }
        }
}
