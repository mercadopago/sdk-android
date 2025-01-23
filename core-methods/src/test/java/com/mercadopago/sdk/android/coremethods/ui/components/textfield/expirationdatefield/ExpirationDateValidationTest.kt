package com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdatefield

import com.mercadopago.sdk.android.coremethods.domain.usecase.IsExpirationDateValidUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class ExpirationDateValidationTest {

    @Test
    fun `when pass a expiration date and a length Then return is valid`() {
        val expirationDate = "1025"
        val length = 4

        val expirationDateValidUseCase = IsExpirationDateValidUseCase()

        val isValid = expirationDateValidUseCase.invoke(expirationDate, length)
        assertEquals(true, isValid)
    }

    @Test
    fun `when pass a expiration date and a length Then return is not valid`() {
        val expirationDate = "1020"
        val length = 4

        val expirationDateValidUseCase = IsExpirationDateValidUseCase()

        val isValid = expirationDateValidUseCase.invoke(expirationDate, length)
        assertEquals(false, isValid)
    }
}
