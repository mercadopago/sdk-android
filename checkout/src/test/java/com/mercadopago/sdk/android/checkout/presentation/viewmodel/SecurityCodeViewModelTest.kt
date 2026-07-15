package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeViewEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeState as SecurityCodeConfigState

internal class SecurityCodeViewModelTest {
    private val fieldError = "Completá los 3 dígitos"

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

    private fun makeViewModel(
        config: SecurityCodeScreenConfig = makeConfig(),
    ) = SecurityCodeViewModel(config = config)

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

        viewModel.onContinue()

        assertEquals(fieldError, viewModel.viewState.value.fieldError)
        assertNull(viewModel.viewEvent.value)
    }

    @Test
    fun `onContinue with incomplete field publishes field error`() {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = 1))
        viewModel.onContinue()

        assertEquals(fieldError, viewModel.viewState.value.fieldError)
    }

    @Test
    fun `onContinue with complete field clears error`() {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = MAX_LENGTH))
        viewModel.onContinue()

        assertNull(viewModel.viewState.value.fieldError)
    }

    @Test
    fun `length change clears previous field error`() {
        val viewModel = makeViewModel()
        viewModel.onContinue()
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

    private companion object {
        const val MAX_LENGTH = 3
    }
}
