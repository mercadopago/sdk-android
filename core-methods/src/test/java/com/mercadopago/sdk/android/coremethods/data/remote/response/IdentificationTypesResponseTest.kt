package com.mercadopago.sdk.android.coremethods.data.remote.response

import org.junit.Assert.assertEquals
import kotlin.test.Test

internal class IdentificationTypesResponseTest {

    @Test
    fun `should create IdentificationTypesResponse with correct properties`() {
        val id = "000"
        val name = "Document"
        val type = "CPF"
        val minLength = 6
        val maxLength = 15

        val response = IdentificationTypesResponse(
            id = id,
            name = name,
            type = type,
            minLength = minLength,
            maxLength = maxLength
        )

        // Assert
        assertEquals(id, response.id)
        assertEquals(name, response.name)
        assertEquals(type, response.type)
        assertEquals(minLength, response.minLength)
        assertEquals(maxLength, response.maxLength)
    }

    @Test
    fun `should create IdentificationTypesResponse with default values when not initialized`() {
        val response = IdentificationTypesResponse(
            id = "default_id",
            name = "default_name",
            type = "default_type",
            minLength = 0,
            maxLength = 0
        )

        assertEquals("default_id", response.id)
        assertEquals("default_name", response.name)
        assertEquals("default_type", response.type)
        assertEquals(0, response.minLength)
        assertEquals(0, response.maxLength)
    }

    @Test
    fun `should compare two IdentificationTypesResponse objects for equality`() {
        val response1 = IdentificationTypesResponse("id_001", "document", "RG", 6, 15)
        val response2 = IdentificationTypesResponse("id_001", "document", "RG", 6, 15)

        assertEquals(response1, response2)
    }

    @Test
    fun `should not be equal to another object type`() {
        val response = IdentificationTypesResponse("id_001", "document", "RG", 6, 15)
        val notAResponse = Any()

        assert(response != notAResponse)
    }
}
