package com.mercadopago.sdk.android.checkout.domain.usecase

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ValidateSecurityCodeUseCaseTest {
    private val useCase = ValidateSecurityCodeUseCase()

    @Test
    fun `given numeric code with matching length then returns true`() {
        assertTrue(useCase(value = "123", length = 3))
    }

    @Test
    fun `given numeric code with matching length of four then returns true`() {
        assertTrue(useCase(value = "1234", length = 4))
    }

    @Test
    fun `given empty code then returns false`() {
        assertFalse(useCase(value = "", length = 3))
    }

    @Test
    fun `given code shorter than expected length then returns false`() {
        assertFalse(useCase(value = "12", length = 3))
    }

    @Test
    fun `given code longer than expected length then returns false`() {
        assertFalse(useCase(value = "12345", length = 4))
    }

    @Test
    fun `given code with non numeric characters then returns false`() {
        assertFalse(useCase(value = "12a", length = 3))
    }

    @Test
    fun `given code with whitespace then returns false`() {
        assertFalse(useCase(value = "12 ", length = 3))
    }
}
