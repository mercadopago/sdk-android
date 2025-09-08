package com.mercadopago.sdk.android.threeds.domain.mappers

import com.mercadopago.sdk.android.threeds.data.mappers.toChallengeModel
import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSAuthenticationParams
import org.junit.Test
import kotlin.test.assertEquals

internal class MPThreeDSAuthenticationParamsMapperTest {

    @Test
    fun `when MPThreeDSAuthenticationModel is mapped to challenge model Then should map all fields correctly`() {
        // Given
        val authModel = MPThreeDSAuthenticationParams(
            response = "CHALLENGE",
            threeDSServerTransID = "server_trans_id",
            acsReferenceNumber = "acs_ref_number",
            dsTransID = "ds_trans_id",
            acsTransID = "acs_trans_id",
            acsSignedContent = "acs_signed_content"
        )

        // When
        val challengeModel = authModel.toChallengeModel()

        // Then
        assertEquals(authModel.threeDSServerTransID, challengeModel.threeDSServerTransID)
        assertEquals(authModel.acsReferenceNumber, challengeModel.acsReferenceNumber)
        assertEquals(authModel.dsTransID, challengeModel.dsTransID)
        assertEquals(authModel.acsTransID, challengeModel.acsTransID)
        assertEquals(authModel.acsSignedContent, challengeModel.acsSignedContent)
    }

    @Test
    fun `when MPThreeDSAuthenticationModel with AUTHORIZED status is mapped Then should create challenge model`() {
        // Given
        val authModel = MPThreeDSAuthenticationParams(
            response = "AUTHORIZED",
            threeDSServerTransID = "authorized_server_trans_id",
            acsReferenceNumber = "authorized_acs_ref_number",
            dsTransID = "authorized_ds_trans_id",
            acsTransID = "authorized_acs_trans_id",
            acsSignedContent = "authorized_acs_signed_content"
        )

        // When
        val challengeModel = authModel.toChallengeModel()

        // Then
        assertEquals("authorized_server_trans_id", challengeModel.threeDSServerTransID)
        assertEquals("authorized_acs_ref_number", challengeModel.acsReferenceNumber)
        assertEquals("authorized_ds_trans_id", challengeModel.dsTransID)
        assertEquals("authorized_acs_trans_id", challengeModel.acsTransID)
        assertEquals("authorized_acs_signed_content", challengeModel.acsSignedContent)
    }

    @Test
    fun `when MPThreeDSAuthenticationModel has empty values Then should map empty values correctly`() {
        // Given
        val authModel = MPThreeDSAuthenticationParams(
            response = "",
            threeDSServerTransID = "",
            acsReferenceNumber = "",
            dsTransID = "",
            acsTransID = "",
            acsSignedContent = ""
        )

        // When
        val challengeModel = authModel.toChallengeModel()

        // Then
        assertEquals("", challengeModel.threeDSServerTransID)
        assertEquals("", challengeModel.acsReferenceNumber)
        assertEquals("", challengeModel.dsTransID)
        assertEquals("", challengeModel.acsTransID)
        assertEquals("", challengeModel.acsSignedContent)
    }
}
