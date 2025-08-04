package com.mercadopago.sdk.android.threeds.data.mappers

import com.mercadopago.sdk.android.threeds.data.datasource.mappers.toModel
import com.mercadopago.sdk.android.threeds.data.remote.response.MPThreeDSAuthenticationResponse
import org.junit.Test
import kotlin.test.assertEquals

internal class MPThreeDSAuthenticationResponseMapperTest {

    @Test
    fun `when MPThreeDSAuthenticationResponse is mapped to model Then should map all fields correctly`() {
        // Given
        val response = MPThreeDSAuthenticationResponse(
            response = "AUTHORIZED",
            threeDSServerTransID = "server_trans_id",
            acsReferenceNumber = "acs_ref_number",
            dsTransID = "ds_trans_id",
            acsTransID = "acs_trans_id",
            acsSignedContent = "acs_signed_content"
        )

        // When
        val model = response.toModel()

        // Then
        assertEquals(response.response, model.response)
        assertEquals(response.threeDSServerTransID, model.threeDSServerTransID)
        assertEquals(response.acsReferenceNumber, model.acsReferenceNumber)
        assertEquals(response.dsTransID, model.dsTransID)
        assertEquals(response.acsTransID, model.acsTransID)
        assertEquals(response.acsSignedContent, model.acsSignedContent)
    }

    @Test
    fun `when MPThreeDSAuthenticationResponse has CHALLENGE status Then should map correctly`() {
        // Given
        val response = MPThreeDSAuthenticationResponse(
            response = "CHALLENGE",
            threeDSServerTransID = "challenge_server_trans_id",
            acsReferenceNumber = "challenge_acs_ref_number",
            dsTransID = "challenge_ds_trans_id",
            acsTransID = "challenge_acs_trans_id",
            acsSignedContent = "challenge_acs_signed_content"
        )

        // When
        val model = response.toModel()

        // Then
        assertEquals("CHALLENGE", model.response)
        assertEquals("challenge_server_trans_id", model.threeDSServerTransID)
        assertEquals("challenge_acs_ref_number", model.acsReferenceNumber)
        assertEquals("challenge_ds_trans_id", model.dsTransID)
        assertEquals("challenge_acs_trans_id", model.acsTransID)
        assertEquals("challenge_acs_signed_content", model.acsSignedContent)
    }

    @Test
    fun `when MPThreeDSAuthenticationResponse has empty values Then should map empty values correctly`() {
        // Given
        val response = MPThreeDSAuthenticationResponse(
            response = "",
            threeDSServerTransID = "",
            acsReferenceNumber = "",
            dsTransID = "",
            acsTransID = "",
            acsSignedContent = ""
        )

        // When
        val model = response.toModel()

        // Then
        assertEquals("", model.response)
        assertEquals("", model.threeDSServerTransID)
        assertEquals("", model.acsReferenceNumber)
        assertEquals("", model.dsTransID)
        assertEquals("", model.acsTransID)
        assertEquals("", model.acsSignedContent)
    }
}
