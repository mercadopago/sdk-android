package com.mercadopago.sdk.android.threeds.domain.model.params

import com.mercadopago.sdk.android.threeds.data.model.ThreeDSAuthRequestParameters
import org.junit.Test
import kotlin.test.assertEquals

internal class ThreeDSAuthRequestParametersTest {

    @Test
    fun `when ThreeDSAuthRequestParameters is created with all parameters Then all properties should be correctly assigned`() {
        // Given
        val expectedSdkAppId = "test_sdk_app_id"
        val expectedDeviceData = "test_device_data"
        val expectedSdkEphemeralPublicKey = "test_ephemeral_public_key"
        val expectedSdkReferenceNumber = "test_reference_number"
        val expectedSdkTransactionId = "test_transaction_id"

        // When
        val authRequestParameters = ThreeDSAuthRequestParameters(
            sdkAppId = expectedSdkAppId,
            deviceData = expectedDeviceData,
            sdkEphemeralPublicKey = expectedSdkEphemeralPublicKey,
            sdkReferenceNumber = expectedSdkReferenceNumber,
            sdkTransactionId = expectedSdkTransactionId
        )

        // Then
        assertEquals(expectedSdkAppId, authRequestParameters.sdkAppId)
        assertEquals(expectedDeviceData, authRequestParameters.deviceData)
        assertEquals(expectedSdkEphemeralPublicKey, authRequestParameters.sdkEphemeralPublicKey)
        assertEquals(expectedSdkReferenceNumber, authRequestParameters.sdkReferenceNumber)
        assertEquals(expectedSdkTransactionId, authRequestParameters.sdkTransactionId)
    }

    @Test
    fun `when two ThreeDSAuthRequestParameters with same values are compared Then they should be equal`() {
        // Given
        val sdkAppId = "test_sdk_app_id"
        val deviceData = "test_device_data"
        val sdkEphemeralPublicKey = "test_ephemeral_public_key"
        val sdkReferenceNumber = "test_reference_number"
        val sdkTransactionId = "test_transaction_id"

        val authRequestParameters1 = ThreeDSAuthRequestParameters(
            sdkAppId = sdkAppId,
            deviceData = deviceData,
            sdkEphemeralPublicKey = sdkEphemeralPublicKey,
            sdkReferenceNumber = sdkReferenceNumber,
            sdkTransactionId = sdkTransactionId
        )

        val authRequestParameters2 = ThreeDSAuthRequestParameters(
            sdkAppId = sdkAppId,
            deviceData = deviceData,
            sdkEphemeralPublicKey = sdkEphemeralPublicKey,
            sdkReferenceNumber = sdkReferenceNumber,
            sdkTransactionId = sdkTransactionId
        )

        // When & Then
        assertEquals(authRequestParameters1, authRequestParameters2)
        assertEquals(authRequestParameters1.hashCode(), authRequestParameters2.hashCode())
    }

    @Test
    fun `when ThreeDSAuthRequestParameters toString is called Then should contain all property values`() {
        // Given
        val authRequestParameters = ThreeDSAuthRequestParameters(
            sdkAppId = "test_sdk_app_id",
            deviceData = "test_device_data",
            sdkEphemeralPublicKey = "test_ephemeral_public_key",
            sdkReferenceNumber = "test_reference_number",
            sdkTransactionId = "test_transaction_id"
        )

        // When
        val stringRepresentation = authRequestParameters.toString()

        // Then
        assert(stringRepresentation.contains("test_sdk_app_id"))
        assert(stringRepresentation.contains("test_device_data"))
        assert(stringRepresentation.contains("test_ephemeral_public_key"))
        assert(stringRepresentation.contains("test_reference_number"))
        assert(stringRepresentation.contains("test_transaction_id"))
    }

    @Test
    fun `when ThreeDSAuthRequestParameters is created with empty strings Then properties should be empty`() {
        // Given
        val emptyString = ""

        // When
        val authRequestParameters = ThreeDSAuthRequestParameters(
            sdkAppId = emptyString,
            deviceData = emptyString,
            sdkEphemeralPublicKey = emptyString,
            sdkReferenceNumber = emptyString,
            sdkTransactionId = emptyString
        )

        // Then
        assertEquals(emptyString, authRequestParameters.sdkAppId)
        assertEquals(emptyString, authRequestParameters.deviceData)
        assertEquals(emptyString, authRequestParameters.sdkEphemeralPublicKey)
        assertEquals(emptyString, authRequestParameters.sdkReferenceNumber)
        assertEquals(emptyString, authRequestParameters.sdkTransactionId)
    }

    @Test
    fun `when ThreeDSAuthRequestParameters copy is called with changes Then only changed properties should be different`() {
        // Given
        val originalAuthRequestParameters = ThreeDSAuthRequestParameters(
            sdkAppId = "original_sdk_app_id",
            deviceData = "original_device_data",
            sdkEphemeralPublicKey = "original_ephemeral_public_key",
            sdkReferenceNumber = "original_reference_number",
            sdkTransactionId = "original_transaction_id"
        )

        val newSdkAppId = "new_sdk_app_id"

        // When
        val copiedAuthRequestParameters = originalAuthRequestParameters.copy(
            sdkAppId = newSdkAppId
        )

        // Then
        assertEquals(newSdkAppId, copiedAuthRequestParameters.sdkAppId)
        assertEquals(originalAuthRequestParameters.deviceData, copiedAuthRequestParameters.deviceData)
        assertEquals(
            originalAuthRequestParameters.sdkEphemeralPublicKey,
            copiedAuthRequestParameters.sdkEphemeralPublicKey
        )
        assertEquals(originalAuthRequestParameters.sdkReferenceNumber, copiedAuthRequestParameters.sdkReferenceNumber)
        assertEquals(originalAuthRequestParameters.sdkTransactionId, copiedAuthRequestParameters.sdkTransactionId)
    }
}
