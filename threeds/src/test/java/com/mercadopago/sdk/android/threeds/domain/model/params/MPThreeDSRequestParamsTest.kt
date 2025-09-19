package com.mercadopago.sdk.android.threeds.domain.model.params

import org.junit.Test
import org.junit.Assert.*

class MPThreeDSRequestParamsTest {

    @Test
    fun `MPThreeDSRequestParams should create instance with all properties`() {
        // Arrange
        val sdkAppId = "app-id-123"
        val deviceData = "device-data-456"
        val sdkEphemeralPublicKey = "public-key-789"
        val sdkReferenceNumber = "ref-number-101"
        val sdkTransactionId = "transaction-id-202"

        // Act
        val requestParams = MPThreeDSRequestParams(
            sdkAppId = sdkAppId,
            deviceData = deviceData,
            sdkEphemeralPublicKey = sdkEphemeralPublicKey,
            sdkReferenceNumber = sdkReferenceNumber,
            sdkTransactionId = sdkTransactionId
        )

        // Assert
        assertEquals(sdkAppId, requestParams.sdkAppId)
        assertEquals(deviceData, requestParams.deviceData)
        assertEquals(sdkEphemeralPublicKey, requestParams.sdkEphemeralPublicKey)
        assertEquals(sdkReferenceNumber, requestParams.sdkReferenceNumber)
        assertEquals(sdkTransactionId, requestParams.sdkTransactionId)
    }

    @Test
    fun `MPThreeDSRequestParams should handle empty strings`() {
        // Arrange & Act
        val requestParams = MPThreeDSRequestParams(
            sdkAppId = "",
            deviceData = "",
            sdkEphemeralPublicKey = "",
            sdkReferenceNumber = "",
            sdkTransactionId = ""
        )

        // Assert
        assertEquals("", requestParams.sdkAppId)
        assertEquals("", requestParams.deviceData)
        assertEquals("", requestParams.sdkEphemeralPublicKey)
        assertEquals("", requestParams.sdkReferenceNumber)
        assertEquals("", requestParams.sdkTransactionId)
    }

    @Test
    fun `MPThreeDSRequestParams should handle long strings`() {
        // Arrange
        val longString = "a".repeat(1000)

        // Act
        val requestParams = MPThreeDSRequestParams(
            sdkAppId = longString,
            deviceData = longString,
            sdkEphemeralPublicKey = longString,
            sdkReferenceNumber = longString,
            sdkTransactionId = longString
        )

        // Assert
        assertEquals(longString, requestParams.sdkAppId)
        assertEquals(longString, requestParams.deviceData)
        assertEquals(longString, requestParams.sdkEphemeralPublicKey)
        assertEquals(longString, requestParams.sdkReferenceNumber)
        assertEquals(longString, requestParams.sdkTransactionId)
    }

    @Test
    fun `MPThreeDSRequestParams should handle special characters`() {
        // Arrange
        val specialChars = "!@#$%^&*()_+-=[]{}|;':\",./<>?"

        // Act
        val requestParams = MPThreeDSRequestParams(
            sdkAppId = specialChars,
            deviceData = specialChars,
            sdkEphemeralPublicKey = specialChars,
            sdkReferenceNumber = specialChars,
            sdkTransactionId = specialChars
        )

        // Assert
        assertEquals(specialChars, requestParams.sdkAppId)
        assertEquals(specialChars, requestParams.deviceData)
        assertEquals(specialChars, requestParams.sdkEphemeralPublicKey)
        assertEquals(specialChars, requestParams.sdkReferenceNumber)
        assertEquals(specialChars, requestParams.sdkTransactionId)
    }

    @Test
    fun `MPThreeDSRequestParams equality should work correctly`() {
        // Arrange
        val requestParams1 = MPThreeDSRequestParams(
            sdkAppId = "app-id-123",
            deviceData = "device-data-456",
            sdkEphemeralPublicKey = "public-key-789",
            sdkReferenceNumber = "ref-number-101",
            sdkTransactionId = "transaction-id-202"
        )

        val requestParams2 = MPThreeDSRequestParams(
            sdkAppId = "app-id-123",
            deviceData = "device-data-456",
            sdkEphemeralPublicKey = "public-key-789",
            sdkReferenceNumber = "ref-number-101",
            sdkTransactionId = "transaction-id-202"
        )

        val requestParams3 = MPThreeDSRequestParams(
            sdkAppId = "different-app-id",
            deviceData = "device-data-456",
            sdkEphemeralPublicKey = "public-key-789",
            sdkReferenceNumber = "ref-number-101",
            sdkTransactionId = "transaction-id-202"
        )

        // Act & Assert
        assertEquals(requestParams1, requestParams2)
        assertNotEquals(requestParams1, requestParams3)
        assertEquals(requestParams1.hashCode(), requestParams2.hashCode())
    }

    @Test
    fun `MPThreeDSRequestParams should have proper toString implementation`() {
        // Arrange
        val requestParams = MPThreeDSRequestParams(
            sdkAppId = "app-id-123",
            deviceData = "device-data-456",
            sdkEphemeralPublicKey = "public-key-789",
            sdkReferenceNumber = "ref-number-101",
            sdkTransactionId = "transaction-id-202"
        )

        // Act
        val stringRepresentation = requestParams.toString()

        // Assert
        assertNotNull(stringRepresentation)
        assertTrue(stringRepresentation.contains("app-id-123"))
        assertTrue(stringRepresentation.contains("device-data-456"))
        assertTrue(stringRepresentation.contains("public-key-789"))
        assertTrue(stringRepresentation.contains("ref-number-101"))
        assertTrue(stringRepresentation.contains("transaction-id-202"))
    }

    @Test
    fun `MPThreeDSRequestParams copy should work correctly`() {
        // Arrange
        val originalParams = MPThreeDSRequestParams(
            sdkAppId = "app-id-123",
            deviceData = "device-data-456",
            sdkEphemeralPublicKey = "public-key-789",
            sdkReferenceNumber = "ref-number-101",
            sdkTransactionId = "transaction-id-202"
        )

        // Act
        val copiedParams = originalParams.copy()
        val modifiedParams = originalParams.copy(sdkAppId = "modified-app-id")

        // Assert
        assertEquals(originalParams, copiedParams)
        assertNotEquals(originalParams, modifiedParams)
        assertEquals("modified-app-id", modifiedParams.sdkAppId)
        assertEquals("device-data-456", modifiedParams.deviceData) // Other fields unchanged
    }

    @Test
    fun `MPThreeDSRequestParams should handle Unicode characters`() {
        // Arrange
        val unicodeString = "测试数据🎯🔐"

        // Act
        val requestParams = MPThreeDSRequestParams(
            sdkAppId = unicodeString,
            deviceData = unicodeString,
            sdkEphemeralPublicKey = unicodeString,
            sdkReferenceNumber = unicodeString,
            sdkTransactionId = unicodeString
        )

        // Assert
        assertEquals(unicodeString, requestParams.sdkAppId)
        assertEquals(unicodeString, requestParams.deviceData)
        assertEquals(unicodeString, requestParams.sdkEphemeralPublicKey)
        assertEquals(unicodeString, requestParams.sdkReferenceNumber)
        assertEquals(unicodeString, requestParams.sdkTransactionId)
    }
}
