package com.mercadopago.sdk.android.coremethods.domain.model

import com.mercadopago.sdk.android.coremethods.domain.model.params.BuyerIdentificationParam
import org.junit.Assert.assertEquals
import org.junit.Test

internal class BuyerIdentificationParamTest {
    @Test
    fun `test BuyerIdentification creation`() {
        val name = "John Doe"
        val number = "123456789"
        val type = "CPF"
        val buyerIdentification = BuyerIdentificationParam(name = name, number = number, type = type)

        assertEquals(name, buyerIdentification.name)
        assertEquals(number, buyerIdentification.number)
        assertEquals(type, buyerIdentification.type)
    }

    @Test
    fun `test BuyerIdentification equality`() {
        val name = "Jane Doe"
        val number = "987654321"
        val type = "CNPJ"

        val buyerIdentification1 = BuyerIdentificationParam(name = name, number = number, type = type)
        val buyerIdentification2 = BuyerIdentificationParam(name = name, number = number, type = type)

        assertEquals(buyerIdentification1, buyerIdentification2)
    }

    @Test
    fun `test BuyerIdentification hashCode`() {
        val name = "John Smith"
        val number = "111222333"
        val type = "CPF"

        val buyerIdentification = BuyerIdentificationParam(name = name, number = number, type = type)

        val expectedHashCode = name.hashCode() * 31 * 31 + number.hashCode() * 31 + type.hashCode()
        assertEquals(expectedHashCode, buyerIdentification.hashCode())
    }
}
