package com.mercadopago.sdk.android.threeds.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MPThreeDSDirectoryServerTest {

    @Test
    fun `VISA should have correct directory server ID and message version`() {
        // Arrange & Act
        val visaDirectoryServer = MPThreeDSDirectoryServer.VISA

        // Assert
        assertEquals("A000000003", visaDirectoryServer.directoryServerID)
        assertEquals("2.1.0", visaDirectoryServer.messageVersion)
    }

    @Test
    fun `MASTERCARD should have correct directory server ID and message version`() {
        // Arrange & Act
        val mastercardDirectoryServer = MPThreeDSDirectoryServer.MASTERCARD

        // Assert
        assertEquals("A000000004", mastercardDirectoryServer.directoryServerID)
        assertEquals("2.1.0", mastercardDirectoryServer.messageVersion)
    }

    @Test
    fun `AMEX should have correct directory server ID and message version`() {
        // Arrange & Act
        val amexDirectoryServer = MPThreeDSDirectoryServer.AMEX

        // Assert
        assertEquals("A000000025", amexDirectoryServer.directoryServerID)
        assertEquals("2.1.0", amexDirectoryServer.messageVersion)
    }

    @Test
    fun `paymentMethodDirectoryServer should return VISA for visa payment method`() {
        // Arrange & Act
        val actualDirectoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("visa")

        // Assert
        assertEquals(MPThreeDSDirectoryServer.VISA, actualDirectoryServer)
        assertEquals("A000000003", actualDirectoryServer.directoryServerID)
        assertEquals("2.1.0", actualDirectoryServer.messageVersion)
    }

    @Test
    fun `paymentMethodDirectoryServer should return VISA for debvisa payment method`() {
        // Arrange & Act
        val actualDirectoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("debvisa")

        // Assert
        assertEquals(MPThreeDSDirectoryServer.VISA, actualDirectoryServer)
    }

    @Test
    fun `paymentMethodDirectoryServer should return MASTERCARD for mastercard payment method`() {
        // Arrange & Act
        val actualDirectoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("mastercard")

        // Assert
        assertEquals(MPThreeDSDirectoryServer.MASTERCARD, actualDirectoryServer)
        assertEquals("A000000004", actualDirectoryServer.directoryServerID)
        assertEquals("2.1.0", actualDirectoryServer.messageVersion)
    }

    @Test
    fun `paymentMethodDirectoryServer should return MASTERCARD for master payment method`() {
        // Arrange & Act
        val actualDirectoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("master")

        // Assert
        assertEquals(MPThreeDSDirectoryServer.MASTERCARD, actualDirectoryServer)
    }

    @Test
    fun `paymentMethodDirectoryServer should return AMEX for amex payment method`() {
        // Arrange & Act
        val actualDirectoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("amex")

        // Assert
        assertEquals(MPThreeDSDirectoryServer.AMEX, actualDirectoryServer)
        assertEquals("A000000025", actualDirectoryServer.directoryServerID)
        assertEquals("2.1.0", actualDirectoryServer.messageVersion)
    }

    @Test
    fun `paymentMethodDirectoryServer should return AMEX for american_express payment method`() {
        // Arrange & Act
        val actualDirectoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("american_express")

        // Assert
        assertEquals(MPThreeDSDirectoryServer.AMEX, actualDirectoryServer)
    }

    @Test
    fun `paymentMethodDirectoryServer should return MASTERCARD for unknown payment method`() {
        // Arrange & Act
        val actualDirectoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("unknown_payment_method")

        // Assert
        assertEquals(MPThreeDSDirectoryServer.MASTERCARD, actualDirectoryServer)
    }

    @Test
    fun `paymentMethodDirectoryServer should return MASTERCARD for empty string`() {
        // Arrange & Act
        val actualDirectoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("")

        // Assert
        assertEquals(MPThreeDSDirectoryServer.MASTERCARD, actualDirectoryServer)
    }

    @Test
    fun `paymentMethodDirectoryServer should handle case sensitivity`() {
        // Arrange & Act
        val visaUppercase = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("VISA")
        val visaLowercase = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("visa")
        val visaMixedCase = MPThreeDSDirectoryServer.paymentMethodDirectoryServer("ViSa")

        // Assert
        assertEquals(
            MPThreeDSDirectoryServer.MASTERCARD,
            visaUppercase
        ) // Should default to MASTERCARD since "VISA" != "visa"
        assertEquals(MPThreeDSDirectoryServer.VISA, visaLowercase)
        assertEquals(
            MPThreeDSDirectoryServer.MASTERCARD,
            visaMixedCase
        ) // Should default to MASTERCARD since "ViSa" != "visa"
    }

    @Test
    fun `paymentMethodDirectoryServer should handle various payment method variations`() {
        // Arrange
        val testCases = mapOf(
            "visa" to MPThreeDSDirectoryServer.VISA,
            "debvisa" to MPThreeDSDirectoryServer.VISA,
            "mastercard" to MPThreeDSDirectoryServer.MASTERCARD,
            "master" to MPThreeDSDirectoryServer.MASTERCARD,
            "amex" to MPThreeDSDirectoryServer.AMEX,
            "american_express" to MPThreeDSDirectoryServer.AMEX,
            "discover" to MPThreeDSDirectoryServer.MASTERCARD, // Default
            "jcb" to MPThreeDSDirectoryServer.MASTERCARD, // Default
            "diners" to MPThreeDSDirectoryServer.MASTERCARD, // Default
        )

        // Act & Assert
        testCases.forEach { (paymentMethod, expectedDirectoryServer) ->
            val actualDirectoryServer = MPThreeDSDirectoryServer.paymentMethodDirectoryServer(paymentMethod)
            assertEquals("Failed for payment method: $paymentMethod", expectedDirectoryServer, actualDirectoryServer)
        }
    }

    @Test
    fun `all directory servers should have valid properties`() {
        // Arrange
        val allDirectoryServers = listOf(
            MPThreeDSDirectoryServer.VISA,
            MPThreeDSDirectoryServer.MASTERCARD,
            MPThreeDSDirectoryServer.AMEX
        )

        // Act & Assert
        allDirectoryServers.forEach { directoryServer ->
            assertNotNull("Directory server ID should not be null", directoryServer.directoryServerID)
            assertNotNull("Message version should not be null", directoryServer.messageVersion)
            assertTrue("Directory server ID should not be empty", directoryServer.directoryServerID.isNotEmpty())
            assertTrue("Message version should not be empty", directoryServer.messageVersion.isNotEmpty())
        }
    }
}
