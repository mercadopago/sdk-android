package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.usecase.GenerateTokenWithCardIdUseCase
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeViewEvent
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeState as SecurityCodeConfigState

@OptIn(ExperimentalCoroutinesApi::class)
internal class SecurityCodeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fieldError = "Completá los 3 dígitos"
    private val generateTokenUseCase = mockk<GenerateTokenWithCardIdUseCase>(relaxed = true)
    private val securityCodePCIState = mockk<PCIFieldState>(relaxed = true)

    private fun makeConfig(
        maxLength: Int = MAX_LENGTH,
    ) = SecurityCodeScreenConfig(
        title = "Ingresá el código de seguridad",
        securityCodeState = SecurityCodeConfigState(
            label = "Código de seguridad",
            placeholder = "Ej: 123",
            helper = "Últimos 3 dígitos del reverso",
            error = fieldError,
            length = maxLength,
            maxLength = maxLength,
        ),
        footerState = FooterState(isVisible = true, buttonLabel = "Continuar"),
        cardId = "card-123",
        cardTitle = "Mastercard •••• 6351",
        cardDescription = "Mastercard Crédito",
        cardImageUrl = "https://example.com/card.png",
    )

    @Before
    fun setUp() {
        // Default stub so non-runTest tests that pass validation don't leave uncaught coroutine exceptions.
        coEvery { generateTokenUseCase(any(), any()) } returns Result.Success("default-token")
    }

    private fun makeViewModel(
        config: SecurityCodeScreenConfig = makeConfig(),
    ) = SecurityCodeViewModel(config = config, generateTokenUseCase = generateTokenUseCase)

    @Test
    fun `initial state maps config`() {
        val state = makeViewModel().viewState.value

        assertEquals("Ingresá el código de seguridad", state.title)
        assertEquals("Código de seguridad", state.securityCodeState.label)
        assertEquals("Ej: 123", state.securityCodeState.placeHolder)
        assertEquals(MAX_LENGTH, state.securityCodeState.maxLength)
        assertEquals(fieldError, state.securityCodeState.validation.errorEmpty)
        assertEquals(fieldError, state.securityCodeState.validation.errorIncomplete)
        assertNull(state.fieldError)
        assertEquals("Mastercard •••• 6351", state.cardTitle)
        assertEquals("Mastercard Crédito", state.cardDescription)
        assertEquals("https://example.com/card.png", state.cardImageUrl)
    }

    @Test
    fun `onContinue with empty field publishes field error`() {
        val viewModel = makeViewModel()

        viewModel.onContinue(securityCodePCIState)

        assertEquals(fieldError, viewModel.viewState.value.fieldError)
        assertNull(viewModel.viewEvent.value)
    }

    @Test
    fun `onContinue with incomplete field publishes field error`() {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = 1))
        viewModel.onContinue(securityCodePCIState)

        assertEquals(fieldError, viewModel.viewState.value.fieldError)
    }

    @Test
    fun `onContinue with complete field clears error`() {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = MAX_LENGTH))
        viewModel.onContinue(securityCodePCIState)

        assertNull(viewModel.viewState.value.fieldError)
    }

    @Test
    fun `length change clears previous field error`() {
        val viewModel = makeViewModel()
        viewModel.onContinue(securityCodePCIState)
        assertEquals(fieldError, viewModel.viewState.value.fieldError)

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = 1))

        assertNull(viewModel.viewState.value.fieldError)
    }

    @Test
    fun `focus lost while empty publishes field error`() {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnFocusChanged(isFocused = true))
        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnFocusChanged(isFocused = false))

        assertEquals(fieldError, viewModel.viewState.value.fieldError)
        assertTrue(!viewModel.viewState.value.securityCodeState.isFocused)
    }

    @Test
    fun `input filled toggles footer button enabled`() {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnInputFilled(isFilled = true))
        assertTrue(viewModel.viewState.value.footerState.buttonState?.enabled == true)

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnInputFilled(isFilled = false))
        assertTrue(viewModel.viewState.value.footerState.buttonState?.enabled == false)
    }

    @Test
    fun `isValid event updates field state`() {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.IsValid(isValid = true))

        assertTrue(viewModel.viewState.value.securityCodeState.isValid)
    }

    @Test
    fun `onUserCancelled emits event with security code screen`() {
        val viewModel = makeViewModel()

        viewModel.onUserCancelled()

        val event = viewModel.viewEvent.value
        assertTrue(event is SecurityCodeViewEvent.OnUserCancelled)
        assertEquals(
            MPUserCancelledContext.Payment(screens = listOf(Screen.SECURITY_CODE)),
            event.context,
        )
    }

    @Test
    fun `onViewEventConsumed clears event`() {
        val viewModel = makeViewModel()
        viewModel.onUserCancelled()

        viewModel.onViewEventConsumed()

        assertNull(viewModel.viewEvent.value)
    }

    @Test
    fun `onContinue with valid CVV calls tokenization and emits OnTokenSuccess`() = runTest {
        val expectedToken = "token-success-xyz"
        coEvery {
            generateTokenUseCase("card-123", securityCodePCIState)
        } returns Result.Success(expectedToken)
        val viewModel = makeViewModel()
        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = MAX_LENGTH))

        viewModel.onContinue(securityCodePCIState)

        coVerify { generateTokenUseCase("card-123", securityCodePCIState) }
        val event = viewModel.viewEvent.value
        assertIs<SecurityCodeViewEvent.OnTokenSuccess>(event)
        assertEquals("card-123", event.cardId)
        assertEquals(expectedToken, event.token)
    }

    @Test
    fun `onContinue with valid CVV on token error emits OnTokenError`() = runTest {
        val checkoutError = mockk<MercadoPagoCheckoutError>(relaxed = true)
        coEvery {
            generateTokenUseCase("card-123", securityCodePCIState)
        } returns Result.Error(checkoutError)
        val viewModel = makeViewModel()
        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = MAX_LENGTH))

        viewModel.onContinue(securityCodePCIState)

        val event = viewModel.viewEvent.value
        assertIs<SecurityCodeViewEvent.OnTokenError>(event)
        assertEquals(checkoutError, event.error)
    }

    @Test
    fun `onContinue with invalid CVV does not call tokenization`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onContinue(securityCodePCIState)

        coVerify(exactly = 0) { generateTokenUseCase(any(), any()) }
        assertNull(viewModel.viewEvent.value)
        assertEquals(fieldError, viewModel.viewState.value.fieldError)
    }

    @Test
    fun `onContinue sets button loading before tokenization and resets after`() = runTest {
        coEvery {
            generateTokenUseCase("card-123", securityCodePCIState)
        } returns Result.Success("token-abc")
        val viewModel = makeViewModel()
        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = MAX_LENGTH))

        viewModel.onContinue(securityCodePCIState)

        assertTrue(viewModel.viewState.value.footerState.buttonState?.isLoading == false)
    }

    private companion object {
        const val MAX_LENGTH = 3
    }
}
