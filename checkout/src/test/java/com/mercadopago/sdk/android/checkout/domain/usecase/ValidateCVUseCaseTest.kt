package com.mercadopago.sdk.android.checkout.domain.usecase

import kotlin.test.Test
import kotlin.test.assertIs

internal class ValidateCVUseCaseTest {
    private val useCase = ValidateCVUseCase()

    @Test
    fun `given matching length then returns Valid`() {
        assertIs<CVVValidationResult.Valid>(useCase(cvvLength = 3, expectedLength = 3))
    }

    @Test
    fun `given matching 4-digit length then returns Valid`() {
        assertIs<CVVValidationResult.Valid>(useCase(cvvLength = 4, expectedLength = 4))
    }

    @Test
    fun `given zero length then returns Invalid Empty`() {
        assertIs<CVVValidationResult.Invalid.Empty>(useCase(cvvLength = 0, expectedLength = 3))
    }

    @Test
    fun `given length too short then returns Invalid IncorrectLength`() {
        assertIs<CVVValidationResult.Invalid.IncorrectLength>(useCase(cvvLength = 2, expectedLength = 3))
    }

    @Test
    fun `given length too long then returns Invalid IncorrectLength`() {
        assertIs<CVVValidationResult.Invalid.IncorrectLength>(useCase(cvvLength = 4, expectedLength = 3))
    }
}
