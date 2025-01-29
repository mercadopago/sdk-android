package com.mercadopago.sdk.android.coremethods.domain.usecase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class IsCardNumberValidUseCaseTest {
    private val useCase = IsCardNumberValidUseCase()

    @Test
    fun `when card number has 15 digits and is valid Then return true`() {
        // Given
        val cardNumber = "5031433215406351"

        // When
        val result = useCase(cardNumber)

        // Then
        assertTrue(result)
    }

    @Test
    fun `when card number has 19 digits and is valid Then return true`() {
        // Given
        val cardNumber = "6205500000000000004"

        // When
        val result = useCase(cardNumber)

        // Then
        assertTrue(result)
    }

    @Test
    fun `when card number has 19 digits case 2 and is valid Then return true`() {
        // Given
        val cardNumber = "5993199916395529539"

        // When
        val result = useCase(cardNumber)

        // Then
        assertTrue(result)
    }

    @Test
    fun `when card number has 16 digits and is not valid Then return false`() {
        // Given
        val cardNumber = "5993199916395529"

        // When
        val result = useCase(cardNumber)

        // Then
        assertFalse(result)
    }

    @Test
    fun `when card number has 14 digits and is valid Then return true`() {
        // Given
        val cardNumber = "36227206271667"

        // When
        val result = useCase(cardNumber)

        // Then
        assertTrue(result)
    }

    @Test
    fun `when card number has 13 digits and is not valid Then return false`() {
        // Given
        val cardNumber = "1622720627166"

        // When
        val result = useCase(cardNumber)

        // Then
        assertFalse(result)
    }

    @Test
    fun `when card number has 8 digits and bin is valid Then return false`() {
        // Given
        val cardNumber = "36227206"

        // When
        val result = useCase(cardNumber)

        // Then
        assertFalse(result)
    }

    @Test
    fun `when card number is empty Then return false`() {
        // Given
        val cardNumber = ""

        // When
        val result = useCase(cardNumber)

        // Then
        assertFalse(result)
    }
}
