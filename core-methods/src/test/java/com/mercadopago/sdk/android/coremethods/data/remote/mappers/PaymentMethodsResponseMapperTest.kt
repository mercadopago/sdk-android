package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.CardResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.FinancialInstitutionResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.LengthResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.SecurityCodeResponse
import org.junit.Assert.assertEquals
import kotlin.test.Test

internal class PaymentMethodsResponseMapperTest {
    @Test
    fun `FinancialInstitutionResponse toModel should correctly map to FinancialInstitutionModel`() {
        val response = FinancialInstitutionResponse(id = "inst_001", description = "Institution 1")

        val model = response.toModel()

        assertEquals(response.id, model.id)
        assertEquals(response.description, model.description)
    }

    @Test
    fun `CardResponse toModel should correctly map to CardModel`() {
        // Arrange
        val lengthResponse = LengthResponse(min = 16, max = 16)
        val securityCodeResponse = SecurityCodeResponse(mode = "mandatory", location = "back", length = 3)
        val response = CardResponse(
            bin = 41111,
            length = lengthResponse,
            validation = "Luhn",
            securityCode = securityCodeResponse,
        )

        val model = response.toModel()

        assertEquals(response.bin, model.bin)
        assertEquals(response.length?.min, model.length?.min)
        assertEquals(response.length?.max, model.length?.max)
        assertEquals(response.validation, model.validation)
        assertEquals(response.securityCode?.mode, model.securityCode?.mode)
        assertEquals(response.securityCode?.location, model.securityCode?.location)
        assertEquals(response.securityCode?.length, model.securityCode?.length)
    }

    @Test
    fun `LengthResponse toModel should correctly map to LengthModel`() {
        val response = LengthResponse(min = 16, max = 16)

        val model = response.toModel()

        assertEquals(response.min, model.min)
        assertEquals(response.max, model.max)
    }

    @Test
    fun `SecurityCodeResponse toModel should correctly map to SecurityCodeModel`() {
        val response = SecurityCodeResponse(mode = "mandatory", location = "back", length = 3)

        val model = response.toModel()

        assertEquals(response.mode, model.mode)
        assertEquals(response.location, model.location)
        assertEquals(response.length, model.length)
    }
}
