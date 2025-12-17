package com.mercadopago.sdk.android.threeds.data.mappers

import com.mercadopago.sdk.android.threeds.data.model.ThreeDSAuthRequestParameters
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ThreeDSParametersMapperTest {
    @Test
    fun `toModel should map ThreeDSAuthRequestParameters correctly`() {
        // Arrange
        val inputAuthRequestParams = ThreeDSAuthRequestParameters(
            sdkAppId = "app-id-123",
            deviceData = "device-data-456",
            sdkEphemeralPublicKey = "public-key-789",
            sdkReferenceNumber = "ref-number-101",
            sdkTransactionId = "transaction-id-202",
        )

        // Act
        val actualRequestParams = inputAuthRequestParams.toModel()

        // Assert
        assertEquals("app-id-123", actualRequestParams.sdkAppId)
        assertEquals("device-data-456", actualRequestParams.deviceData)
        assertEquals("public-key-789", actualRequestParams.sdkEphemeralPublicKey)
        assertEquals("ref-number-101", actualRequestParams.sdkReferenceNumber)
        assertEquals("transaction-id-202", actualRequestParams.sdkTransactionId)
    }

    @Test
    fun `toModel should handle empty strings in ThreeDSAuthRequestParameters`() {
        // Arrange
        val inputAuthRequestParams = ThreeDSAuthRequestParameters(
            sdkAppId = "",
            deviceData = "",
            sdkEphemeralPublicKey = "",
            sdkReferenceNumber = "",
            sdkTransactionId = "",
        )

        // Act
        val actualRequestParams = inputAuthRequestParams.toModel()

        // Assert
        assertEquals("", actualRequestParams.sdkAppId)
        assertEquals("", actualRequestParams.deviceData)
        assertEquals("", actualRequestParams.sdkEphemeralPublicKey)
        assertEquals("", actualRequestParams.sdkReferenceNumber)
        assertEquals("", actualRequestParams.sdkTransactionId)
    }

    @Test
    fun `toParams should map MPThreeDSAuthenticationModel correctly`() {
        // Arrange
        val inputAuthModel = MPThreeDSAuthenticationModel(
            response = "CHALLENGE",
            threeDSServerTransID = "server-trans-123",
            acsReferenceNumber = "acs-ref-456",
            dsTransID = "ds-trans-789",
            acsTransID = "acs-trans-101",
            acsSignedContent = "signed-content-abc",
        )

        // Act
        val actualAuthParams = inputAuthModel.toParams()

        // Assert
        assertEquals("CHALLENGE", actualAuthParams.response)
        assertEquals("server-trans-123", actualAuthParams.threeDSServerTransID)
        assertEquals("acs-ref-456", actualAuthParams.acsReferenceNumber)
        assertEquals("ds-trans-789", actualAuthParams.dsTransID)
        assertEquals("acs-trans-101", actualAuthParams.acsTransID)
        assertEquals("signed-content-abc", actualAuthParams.acsSignedContent)
    }

    @Test
    fun `toParams should handle empty strings in MPThreeDSAuthenticationModel`() {
        // Arrange
        val inputAuthModel = MPThreeDSAuthenticationModel(
            response = "",
            threeDSServerTransID = "",
            acsReferenceNumber = "",
            dsTransID = "",
            acsTransID = "",
            acsSignedContent = "",
        )

        // Act
        val actualAuthParams = inputAuthModel.toParams()

        // Assert
        assertEquals("", actualAuthParams.response)
        assertEquals("", actualAuthParams.threeDSServerTransID)
        assertEquals("", actualAuthParams.acsReferenceNumber)
        assertEquals("", actualAuthParams.dsTransID)
        assertEquals("", actualAuthParams.acsTransID)
        assertEquals("", actualAuthParams.acsSignedContent)
    }

    @Test
    fun `toParams should handle different response types`() {
        // Arrange - Test different response types
        val challengeModel = MPThreeDSAuthenticationModel(
            response = "CHALLENGE",
            threeDSServerTransID = "trans-123",
            acsReferenceNumber = "ref-456",
            dsTransID = "ds-789",
            acsTransID = "acs-101",
            acsSignedContent = "content-abc",
        )

        val authorizedModel = challengeModel.copy(response = "AUTHORIZED")
        val failedModel = challengeModel.copy(response = "FAILED")

        // Act
        val challengeParams = challengeModel.toParams()
        val authorizedParams = authorizedModel.toParams()
        val failedParams = failedModel.toParams()

        // Assert
        assertEquals("CHALLENGE", challengeParams.response)
        assertEquals("AUTHORIZED", authorizedParams.response)
        assertEquals("FAILED", failedParams.response)

        // Verify other fields remain the same
        assertEquals("trans-123", challengeParams.threeDSServerTransID)
        assertEquals("trans-123", authorizedParams.threeDSServerTransID)
        assertEquals("trans-123", failedParams.threeDSServerTransID)
    }
}
