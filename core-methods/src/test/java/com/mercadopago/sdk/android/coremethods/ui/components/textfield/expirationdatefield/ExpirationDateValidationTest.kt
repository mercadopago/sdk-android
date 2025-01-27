package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdatefield

import com.mercadopago.sdk.android.coremethods.domain.usecase.IsExpirationDateValidUseCase
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Locale

class ExpirationDateValidationTest {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    @Test
    fun `when pass a expiration date and a length of four Then return is valid`() {
        val expirationDate = String.format(Locale.getDefault(), "%02d%02d", currentMonth, currentYear)
        val length = 4

        val expirationDateValidUseCase = IsExpirationDateValidUseCase()

        val isValid = expirationDateValidUseCase.invoke(expirationDate, length)
        assertEquals(true, isValid)
    }

    @Test
    fun `when pass a expiration date and a length of four Then return is not valid`() {
        val expirationDate = String.format(Locale.getDefault(), "%02d%02d", currentMonth, currentYear - 5)
        val length = 4

        val expirationDateValidUseCase = IsExpirationDateValidUseCase()

        val isValid = expirationDateValidUseCase.invoke(expirationDate, length)
        assertEquals(false, isValid)
    }

    @Test
    fun `when pass no expiration date and a length of four Then return is not valid`() {
        val expirationDate = ""
        val length = 4

        val expirationDateValidUseCase = IsExpirationDateValidUseCase()

        val isValid = expirationDateValidUseCase.invoke(expirationDate, length)
        assertEquals(false, isValid)
    }

    @Test
    fun `when pass a expiration date with wrong month Then return is not valid`() {
        val expirationDate = String.format(Locale.getDefault(), "%02d%02d", currentMonth + 2, currentYear)
        val length = 4

        val expirationDateValidUseCase = IsExpirationDateValidUseCase()

        val isValid = expirationDateValidUseCase.invoke(expirationDate, length)
        assertEquals(false, isValid)
    }
}
