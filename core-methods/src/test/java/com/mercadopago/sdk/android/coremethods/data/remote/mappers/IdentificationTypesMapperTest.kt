package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.IdentificationTypesResponse
import org.junit.Assert.assertEquals
import kotlin.test.Test

class IdentificationTypesMapperTest {
    @Test
    fun `toModel should convert IdentificationTypesResponse to IdentificationType correctly`() {
        // Arrange
        val response = IdentificationTypesResponse(
            id = "id_001",
            name = "document",
            type = "RG",
            minLength = 6,
            maxLength = 15,
        )

        // Act
        val model = response.toModel()

        // Assert
        assertEquals(response.id, model.id)
        assertEquals(response.name, model.name)
        assertEquals(response.type, model.type)
        assertEquals(response.minLength, model.minLength)
        assertEquals(response.maxLength, model.maxLength)
    }

    @Test
    fun `toModel should create IdentificationType with correct values`() {
        // Arrange
        val response = IdentificationTypesResponse(
            id = "id_002",
            name = "document",
            type = "RG",
            minLength = 5,
            maxLength = 20,
        )

        // Act
        val model = response.toModel()

        // Assert
        assertEquals("id_002", model.id)
        assertEquals("document", model.name)
        assertEquals("RG", model.type)
        assertEquals(5, model.minLength)
        assertEquals(20, model.maxLength)
    }
}
