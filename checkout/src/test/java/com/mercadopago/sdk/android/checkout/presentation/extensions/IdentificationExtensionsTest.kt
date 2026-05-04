package com.mercadopago.sdk.android.checkout.presentation.extensions

import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class IdentificationExtensionsTest {
    @Test
    fun `given null IdentificationType then getPlaceholder returns null`() {
        val result = null.getPlaceholder()

        assertNull(result)
    }

    @Test
    fun `given IdentificationType with null id then getPlaceholder returns null`() {
        val result = IdentificationType(id = null).getPlaceholder()

        assertNull(result)
    }

    @Test
    fun `given IdentificationType CPF then getPlaceholder returns CPF mask`() {
        val result = IdentificationType(id = "CPF").getPlaceholder()

        assertEquals("999.999.999-99", result)
    }

    @Test
    fun `given IdentificationType CNPJ then getPlaceholder returns CNPJ mask`() {
        val result = IdentificationType(id = "CNPJ").getPlaceholder()

        assertEquals("99.999.999/9999-99", result)
    }

    @Test
    fun `given IdentificationType with unknown id then getPlaceholder returns null`() {
        val result = IdentificationType(id = "DNI").getPlaceholder()

        assertNull(result)
    }
}
