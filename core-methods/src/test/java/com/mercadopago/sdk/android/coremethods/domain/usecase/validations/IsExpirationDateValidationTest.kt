package com.mercadopago.sdk.android.coremethods.domain.usecase.validations

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Locale
import kotlin.test.assertFalse

internal class IsExpirationDateValidationTest {
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    private val useCase = IsExpirationDateValidUseCase()

    @Test
    fun `when pass a expiration date and a length of four Then return is valid`() {
        val expirationDate = String.format(Locale.getDefault(), "%02d%02d", currentMonth, currentYear)
        val length = 4

        val isValid = useCase(expirationDate, length)
        assertEquals(true, isValid)
    }

    @Test
    fun `when pass a expiration date and a length of four Then return is not valid`() {
        val expirationDate = String.format(Locale.getDefault(), "%02d%02d", currentMonth, currentYear - 5)
        val length = 4

        val isValid = useCase(expirationDate, length)
        assertEquals(false, isValid)
    }

    @Test
    fun `when pass no expiration date and a length of four Then return is not valid`() {
        val expirationDate = ""
        val length = 4

        val isValid = useCase(expirationDate, length)
        assertEquals(false, isValid)
    }

    @Test
    fun `when pass a expiration date with wrong month Then return is not valid`() {
        val expirationDate = String.format(Locale.getDefault(), "%02d%02d", currentMonth - 2, currentYear)
        val length = 4

        val isValid = useCase(expirationDate, length)
        assertEquals(false, isValid)
    }

    @Test
    fun `when pass a expiration date with month bigger than 12 Then return is not valid`() {
        val expirationDate = String.format(Locale.getDefault(), "%02d%02d", 13, currentYear)
        val length = 4
        val isValid = useCase(expirationDate, length)

        assertEquals(false, isValid)
    }

    @Test
    fun `when pass a expiration date with month as 0 Then return is not valid`() {
        val expirationDate = String.format(Locale.getDefault(), "%02d%02d", 0, currentYear)
        val length = 4
        val isValid = useCase(expirationDate, length)

        assertEquals(false, isValid)
    }

    @Test
    fun `when pass a expiration date with year bigger than 99 Then return is not valid`() {
        val expirationDate = "12/100"
        val length = 4
        val isValid = useCase(expirationDate, length)

        assertEquals(false, isValid)
    }

    @Test
    fun `when expiration date month is 99 and year 99 Then return false`() {
        // Given
        val expirationDate = "99/99"
        val length = 4

        // When
        val isValid = useCase(expirationDate, length)

        // Then
        assertFalse(isValid)
    }

    @Test
    fun `when expiration date month is 12 and year 99 Then return false`() {
        // Given
        val expirationDate = "12/99"
        val length = 4

        // When
        val isValid = useCase(expirationDate, length)

        // Then
        assertFalse(isValid)
    }
}
