package com.mercadopago.sdk.android.threeds

import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSSeverity
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test class to verify the public models work correctly.
 */
internal class PublicModelsTest {

    @Test
    fun `when MPThreeDSWarning is created Then should set all fields correctly`() {
        // Given
        val id = "test_id"
        val message = "Test warning message"
        val severity = MPThreeDSSeverity.HIGH

        // When
        val warning = MPThreeDSWarning(id, message, severity)

        // Then
        assertEquals(id, warning.id)
        assertEquals(message, warning.message)
        assertEquals(severity, warning.severity)
    }

    @Test
    fun `when MPThreeDSAuthRequestParameters is created Then should set all fields correctly`() {
        // Given
        val sdkAppId = "test_app_id"
        val deviceData = "test_device_data"
        val sdkEphemeralPublicKey = "test_public_key"
        val sdkReferenceNumber = "test_ref_number"
        val sdkTransactionId = "test_transaction_id"

        // When
        val params = MPThreeDSRequestParams(
            sdkAppId = sdkAppId,
            deviceData = deviceData,
            sdkEphemeralPublicKey = sdkEphemeralPublicKey,
            sdkReferenceNumber = sdkReferenceNumber,
            sdkTransactionId = sdkTransactionId
        )

        // Then
        assertEquals(sdkAppId, params.sdkAppId)
        assertEquals(deviceData, params.deviceData)
        assertEquals(sdkEphemeralPublicKey, params.sdkEphemeralPublicKey)
        assertEquals(sdkReferenceNumber, params.sdkReferenceNumber)
        assertEquals(sdkTransactionId, params.sdkTransactionId)
    }

    @Test
    fun `when MPThreeDSAuthenticationResponse is created Then should set all fields correctly`() {
        // Given
        val response = "CHALLENGE"
        val threeDSServerTransID = "server_trans_id"
        val acsReferenceNumber = "acs_ref_number"
        val dsTransID = "ds_trans_id"
        val acsTransID = "acs_trans_id"
        val acsSignedContent = "acs_signed_content"

        // When
        val authResponse = MPThreeDSAuthenticationModel(
            response = response,
            threeDSServerTransID = threeDSServerTransID,
            acsReferenceNumber = acsReferenceNumber,
            dsTransID = dsTransID,
            acsTransID = acsTransID,
            acsSignedContent = acsSignedContent
        )

        // Then
        assertEquals(response, authResponse.response)
        assertEquals(threeDSServerTransID, authResponse.threeDSServerTransID)
        assertEquals(acsReferenceNumber, authResponse.acsReferenceNumber)
        assertEquals(dsTransID, authResponse.dsTransID)
        assertEquals(acsTransID, authResponse.acsTransID)
        assertEquals(acsSignedContent, authResponse.acsSignedContent)
    }

    @Test
    fun `when MPThreeDSChallengeError is created Then should extend Exception`() {
        // Given
        val code = "TEST_ERROR"
        val message = "Test error message"
        val details = "Additional details"
        val cause = RuntimeException("Cause")

        // When
        val error = MPThreeDSChallengeError(code, message, details, cause)

        // Then
        assertEquals(code, error.code)
        assertEquals(message, error.message)
        assertEquals(details, error.details)
        assertEquals(cause, error.cause)

        // Verify it's an Exception
        assertNotNull(error as Exception)
    }

    @Test
    fun `when MPThreeDSAuthenticated is created Then should set all fields correctly`() {
        // Given
        val challengeResponse = mockThreeDSChallengeResponse()
        val challengeCompleted = true

        // When
        val authenticated = MPThreeDSAuthenticated(challengeResponse, challengeCompleted)

        // Then
        assertEquals(challengeResponse, authenticated.challengeResponse)
        assertEquals(challengeCompleted, authenticated.challengeCompleted)
    }

    @Test
    fun `when MPThreeDSChallengeResult OnSuccess is created Then should contain result`() {
        // Given
        val authenticated = mockThreeDSAuthenticated()

        // When
        val result = MPThreeDSChallengeResult.OnSuccess(authenticated)

        // Then
        assertEquals(authenticated, result.result)
    }

    @Test
    fun `when MPThreeDSChallengeResult OnError is created Then should contain error`() {
        // Given
        val error = mockThreeDSChallengeError()

        // When
        val result = MPThreeDSChallengeResult.OnError(error)

        // Then
        assertEquals(error, result.error)
    }
}
