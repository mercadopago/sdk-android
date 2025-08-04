package com.mercadopago.sdk.android.threeds.domain.model

import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthRequestParameters
import com.mercadopago.sdk.android.threeds.domain.model.params.ThreeDSAuthenticationParams
import org.junit.Test
import kotlin.test.assertEquals

internal class MPThreeDSModelsTest {

    @Test
    fun `when MPThreeDSAuthenticated is created Then should set all fields correctly`() {
        // Given
        val challengeModel = MPThreeDSChallengeModel(
            threeDSServerTransID = "server_trans_id",
            acsReferenceNumber = "acs_ref_number",
            dsTransID = "ds_trans_id",
            acsTransID = "acs_trans_id",
            acsSignedContent = "acs_signed_content"
        )
        val challengeCompleted = true

        // When
        val authenticated = MPThreeDSAuthenticated(challengeModel, challengeCompleted)

        // Then
        assertEquals(challengeModel, authenticated.challengeResponse)
        assertEquals(challengeCompleted, authenticated.challengeCompleted)
    }

    @Test
    fun `when MPThreeDSAuthenticated is created with default challenge completed Then should be false`() {
        // Given
        val challengeModel = MPThreeDSChallengeModel(
            threeDSServerTransID = "server_trans_id",
            acsReferenceNumber = "acs_ref_number",
            dsTransID = "ds_trans_id",
            acsTransID = "acs_trans_id",
            acsSignedContent = "acs_signed_content"
        )

        // When
        val authenticated = MPThreeDSAuthenticated(challengeModel)

        // Then
        assertEquals(challengeModel, authenticated.challengeResponse)
        assertEquals(false, authenticated.challengeCompleted)
    }

    @Test
    fun `when MPThreeDSChallengeModel is created Then should set all fields correctly`() {
        // Given
        val threeDSServerTransID = "test_server_trans_id"
        val acsReferenceNumber = "test_acs_ref_number"
        val dsTransID = "test_ds_trans_id"
        val acsTransID = "test_acs_trans_id"
        val acsSignedContent = "test_acs_signed_content"

        // When
        val challengeModel = MPThreeDSChallengeModel(
            threeDSServerTransID,
            acsReferenceNumber,
            dsTransID,
            acsTransID,
            acsSignedContent
        )

        // Then
        assertEquals(threeDSServerTransID, challengeModel.threeDSServerTransID)
        assertEquals(acsReferenceNumber, challengeModel.acsReferenceNumber)
        assertEquals(dsTransID, challengeModel.dsTransID)
        assertEquals(acsTransID, challengeModel.acsTransID)
        assertEquals(acsSignedContent, challengeModel.acsSignedContent)
    }

    @Test
    fun `when MPThreeDSAuthenticationModel is created Then should set all fields correctly`() {
        // Given
        val response = "AUTHORIZED"
        val threeDSServerTransID = "server_trans_id"
        val acsReferenceNumber = "acs_ref_number"
        val dsTransID = "ds_trans_id"
        val acsTransID = "acs_trans_id"
        val acsSignedContent = "acs_signed_content"

        // When
        val authModel = MPThreeDSAuthenticationModel(
            response,
            threeDSServerTransID,
            acsReferenceNumber,
            dsTransID,
            acsTransID,
            acsSignedContent
        )

        // Then
        assertEquals(response, authModel.response)
        assertEquals(threeDSServerTransID, authModel.threeDSServerTransID)
        assertEquals(acsReferenceNumber, authModel.acsReferenceNumber)
        assertEquals(dsTransID, authModel.dsTransID)
        assertEquals(acsTransID, authModel.acsTransID)
        assertEquals(acsSignedContent, authModel.acsSignedContent)
    }

    @Test
    fun `when ThreeDSAuthRequestParameters is created Then should set all fields correctly`() {
        // Given
        val sdkAppId = "test_app_id"
        val deviceData = "test_device_data"
        val sdkEphemeralPublicKey = "test_public_key"
        val sdkReferenceNumber = "test_ref_number"
        val sdkTransactionId = "test_transaction_id"

        // When
        val authParams = ThreeDSAuthRequestParameters(
            sdkAppId,
            deviceData,
            sdkEphemeralPublicKey,
            sdkReferenceNumber,
            sdkTransactionId
        )

        // Then
        assertEquals(sdkAppId, authParams.sdkAppId)
        assertEquals(deviceData, authParams.deviceData)
        assertEquals(sdkEphemeralPublicKey, authParams.sdkEphemeralPublicKey)
        assertEquals(sdkReferenceNumber, authParams.sdkReferenceNumber)
        assertEquals(sdkTransactionId, authParams.sdkTransactionId)
    }

    @Test
    fun `when ThreeDSAuthenticationParams is created Then should set all fields correctly`() {
        // Given
        val token = "test_token"
        val sdkAppId = "test_app_id"
        val sdkEncData = "test_enc_data"
        val sdkEphemPubKey = "test_ephemeral_pub_key"
        val sdkMaxTimeout = "10"
        val sdkReferenceNumber = "test_ref_number"
        val sdkTransId = "test_trans_id"

        // When
        val authParams = ThreeDSAuthenticationParams(
            token,
            sdkAppId,
            sdkEncData,
            sdkEphemPubKey,
            sdkMaxTimeout,
            sdkReferenceNumber,
            sdkTransId
        )

        // Then
        assertEquals(token, authParams.token)
        assertEquals(sdkAppId, authParams.sdkAppId)
        assertEquals(sdkEncData, authParams.sdkEncData)
        assertEquals(sdkEphemPubKey, authParams.sdkEphemPubKey)
        assertEquals(sdkMaxTimeout, authParams.sdkMaxTimeout)
        assertEquals(sdkReferenceNumber, authParams.sdkReferenceNumber)
        assertEquals(sdkTransId, authParams.sdkTransId)
    }

    @Test
    fun `when data classes are compared for equality Then should work correctly`() {
        // Given
        val challengeModel1 = MPThreeDSChallengeModel(
            "id1", "ref1", "ds1", "acs1", "content1"
        )
        val challengeModel2 = MPThreeDSChallengeModel(
            "id1", "ref1", "ds1", "acs1", "content1"
        )
        val challengeModel3 = MPThreeDSChallengeModel(
            "id2", "ref2", "ds2", "acs2", "content2"
        )

        // When & Then
        assertEquals(challengeModel1, challengeModel2)
        assertEquals(challengeModel1.hashCode(), challengeModel2.hashCode())
        assertEquals(challengeModel1.toString(), challengeModel2.toString())

        // Different objects should not be equal
        assert(challengeModel1 != challengeModel3)
    }

    @Test
    fun `when MPThreeDSAuthenticated objects are compared Then should work correctly`() {
        // Given
        val challengeModel = MPThreeDSChallengeModel(
            "id1", "ref1", "ds1", "acs1", "content1"
        )
        val authenticated1 = MPThreeDSAuthenticated(challengeModel, true)
        val authenticated2 = MPThreeDSAuthenticated(challengeModel, true)
        val authenticated3 = MPThreeDSAuthenticated(challengeModel, false)

        // When & Then
        assertEquals(authenticated1, authenticated2)
        assertEquals(authenticated1.hashCode(), authenticated2.hashCode())

        // Different challenge completed values should not be equal
        assert(authenticated1 != authenticated3)
    }
}
