package com.mercadopago.sdk.android.threeds.domain.model.params

import org.junit.Test
import kotlin.test.assertEquals

internal class ThreeDSAuthenticationParamsTest {

    @Test
    fun `when ThreeDSAuthenticationParams is created with all parameters Then all properties should be correctly assigned`() {
        // Given
        val expectedToken = "test_token"
        val expectedSdkAppId = "test_sdk_app_id"
        val expectedSdkEncData = "test_enc_data"
        val expectedSdkEphemPubKey = "test_ephemeral_pub_key"
        val expectedSdkMaxTimeout = "10"
        val expectedSdkReferenceNumber = "test_reference_number"
        val expectedSdkTransId = "test_transaction_id"

        // When
        val authenticationParams = ThreeDSAuthenticationParams(
            token = expectedToken,
            sdkAppId = expectedSdkAppId,
            sdkEncData = expectedSdkEncData,
            sdkEphemPubKey = expectedSdkEphemPubKey,
            sdkMaxTimeout = expectedSdkMaxTimeout,
            sdkReferenceNumber = expectedSdkReferenceNumber,
            sdkTransId = expectedSdkTransId
        )

        // Then
        assertEquals(expectedToken, authenticationParams.token)
        assertEquals(expectedSdkAppId, authenticationParams.sdkAppId)
        assertEquals(expectedSdkEncData, authenticationParams.sdkEncData)
        assertEquals(expectedSdkEphemPubKey, authenticationParams.sdkEphemPubKey)
        assertEquals(expectedSdkMaxTimeout, authenticationParams.sdkMaxTimeout)
        assertEquals(expectedSdkReferenceNumber, authenticationParams.sdkReferenceNumber)
        assertEquals(expectedSdkTransId, authenticationParams.sdkTransId)
    }

    @Test
    fun `when two ThreeDSAuthenticationParams with same values are compared Then they should be equal`() {
        // Given
        val token = "test_token"
        val sdkAppId = "test_sdk_app_id"
        val sdkEncData = "test_enc_data"
        val sdkEphemPubKey = "test_ephemeral_pub_key"
        val sdkMaxTimeout = "15"
        val sdkReferenceNumber = "test_reference_number"
        val sdkTransId = "test_transaction_id"

        val authenticationParams1 = ThreeDSAuthenticationParams(
            token = token,
            sdkAppId = sdkAppId,
            sdkEncData = sdkEncData,
            sdkEphemPubKey = sdkEphemPubKey,
            sdkMaxTimeout = sdkMaxTimeout,
            sdkReferenceNumber = sdkReferenceNumber,
            sdkTransId = sdkTransId
        )

        val authenticationParams2 = ThreeDSAuthenticationParams(
            token = token,
            sdkAppId = sdkAppId,
            sdkEncData = sdkEncData,
            sdkEphemPubKey = sdkEphemPubKey,
            sdkMaxTimeout = sdkMaxTimeout,
            sdkReferenceNumber = sdkReferenceNumber,
            sdkTransId = sdkTransId
        )

        // When & Then
        assertEquals(authenticationParams1, authenticationParams2)
        assertEquals(authenticationParams1.hashCode(), authenticationParams2.hashCode())
    }

    @Test
    fun `when ThreeDSAuthenticationParams toString is called Then should contain all property values`() {
        // Given
        val authenticationParams = ThreeDSAuthenticationParams(
            token = "test_token",
            sdkAppId = "test_sdk_app_id",
            sdkEncData = "test_enc_data",
            sdkEphemPubKey = "test_ephemeral_pub_key",
            sdkMaxTimeout = "20",
            sdkReferenceNumber = "test_reference_number",
            sdkTransId = "test_transaction_id"
        )

        // When
        val stringRepresentation = authenticationParams.toString()

        // Then
        assert(stringRepresentation.contains("test_token"))
        assert(stringRepresentation.contains("test_sdk_app_id"))
        assert(stringRepresentation.contains("test_enc_data"))
        assert(stringRepresentation.contains("test_ephemeral_pub_key"))
        assert(stringRepresentation.contains("20"))
        assert(stringRepresentation.contains("test_reference_number"))
        assert(stringRepresentation.contains("test_transaction_id"))
    }

    @Test
    fun `when ThreeDSAuthenticationParams is created with empty strings Then properties should be empty`() {
        // Given
        val emptyString = ""

        // When
        val authenticationParams = ThreeDSAuthenticationParams(
            token = emptyString,
            sdkAppId = emptyString,
            sdkEncData = emptyString,
            sdkEphemPubKey = emptyString,
            sdkMaxTimeout = emptyString,
            sdkReferenceNumber = emptyString,
            sdkTransId = emptyString
        )

        // Then
        assertEquals(emptyString, authenticationParams.token)
        assertEquals(emptyString, authenticationParams.sdkAppId)
        assertEquals(emptyString, authenticationParams.sdkEncData)
        assertEquals(emptyString, authenticationParams.sdkEphemPubKey)
        assertEquals(emptyString, authenticationParams.sdkMaxTimeout)
        assertEquals(emptyString, authenticationParams.sdkReferenceNumber)
        assertEquals(emptyString, authenticationParams.sdkTransId)
    }

    @Test
    fun `when ThreeDSAuthenticationParams copy is called with changes Then only changed properties should be different`() {
        // Given
        val originalAuthenticationParams = ThreeDSAuthenticationParams(
            token = "original_token",
            sdkAppId = "original_sdk_app_id",
            sdkEncData = "original_enc_data",
            sdkEphemPubKey = "original_ephemeral_pub_key",
            sdkMaxTimeout = "10",
            sdkReferenceNumber = "original_reference_number",
            sdkTransId = "original_transaction_id"
        )

        val newToken = "new_token"
        val newTimeout = "30"

        // When
        val copiedAuthenticationParams = originalAuthenticationParams.copy(
            token = newToken,
            sdkMaxTimeout = newTimeout
        )

        // Then
        assertEquals(newToken, copiedAuthenticationParams.token)
        assertEquals(originalAuthenticationParams.sdkAppId, copiedAuthenticationParams.sdkAppId)
        assertEquals(originalAuthenticationParams.sdkEncData, copiedAuthenticationParams.sdkEncData)
        assertEquals(originalAuthenticationParams.sdkEphemPubKey, copiedAuthenticationParams.sdkEphemPubKey)
        assertEquals(newTimeout, copiedAuthenticationParams.sdkMaxTimeout)
        assertEquals(originalAuthenticationParams.sdkReferenceNumber, copiedAuthenticationParams.sdkReferenceNumber)
        assertEquals(originalAuthenticationParams.sdkTransId, copiedAuthenticationParams.sdkTransId)
    }

    @Test
    fun `when ThreeDSAuthenticationParams is created with numeric timeout Then timeout should be stored as string`() {
        // Given
        val numericTimeout = "15"

        // When
        val authenticationParams = ThreeDSAuthenticationParams(
            token = "test_token",
            sdkAppId = "test_sdk_app_id",
            sdkEncData = "test_enc_data",
            sdkEphemPubKey = "test_ephemeral_pub_key",
            sdkMaxTimeout = numericTimeout,
            sdkReferenceNumber = "test_reference_number",
            sdkTransId = "test_transaction_id"
        )

        // Then
        assertEquals(numericTimeout, authenticationParams.sdkMaxTimeout)
    }
}
