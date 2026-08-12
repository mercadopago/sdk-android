package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class GetSecurityCodeScreenUseCaseTest {
    private val useCase = GetSecurityCodeScreenUseCase()

    private val field = SecurityCodeFieldOutput(
        label = "Código de segurança",
        placeholder = "ex.: 123",
        helper = "Fica no verso do cartão.",
        error = "Preencha este campo.",
    )

    private val screen = SecurityCodeScreenOutput(
        headerTitle = "Insira o código de segurança",
        field = field,
        buttonLabel = "Continuar",
    )

    @Test
    fun `given screen is absent then returns null`() {
        val securityCode = SecurityCodeOutput(length = 3, screen = null)

        assertNull(useCase(securityCode))
    }

    @Test
    fun `given screen is present then returns title from header`() {
        val securityCode = SecurityCodeOutput(length = 3, screen = screen)

        val (title, _) = requireNotNull(useCase(securityCode))

        assertEquals("Insira o código de segurança", title)
    }

    @Test
    fun `given screen is present then maps field data into SecurityCodeState`() {
        val securityCode = SecurityCodeOutput(length = 3, screen = screen)

        val (_, state) = requireNotNull(useCase(securityCode))

        assertEquals("Código de segurança", state.label)
        assertEquals("ex.: 123", state.placeholder)
        assertEquals("Fica no verso do cartão.", state.helper)
        assertEquals("Preencha este campo.", state.error)
    }

    @Test
    fun `given length is 4 then maps length and maxLength to security code length`() {
        val securityCode = SecurityCodeOutput(length = 4, screen = screen)

        val (_, state) = requireNotNull(useCase(securityCode))

        assertEquals(4, state.length)
        assertEquals(4, state.maxLength)
    }

    @Test
    fun `given field error is present then propagates message to state error`() {
        val securityCode = SecurityCodeOutput(length = 3, screen = screen)

        val (_, state) = requireNotNull(useCase(securityCode))

        assertEquals("Preencha este campo.", state.error)
    }

    @Test
    fun `given field error is null then maps error to empty string`() {
        val securityCode = SecurityCodeOutput(
            length = 3,
            screen = screen.copy(field = field.copy(error = null)),
        )

        val (_, state) = requireNotNull(useCase(securityCode))

        assertEquals("", state.error)
    }
}
