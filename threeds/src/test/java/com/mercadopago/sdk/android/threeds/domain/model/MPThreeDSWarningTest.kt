package com.mercadopago.sdk.android.threeds.domain.model

import org.junit.Test
import org.junit.Assert.*

class MPThreeDSWarningTest {

    @Test
    fun `MPThreeDSWarning should create instance with all properties`() {
        // Arrange
        val id = "warning-id-123"
        val message = "This is a warning message"
        val severity = MPThreeDSSeverity.HIGH

        // Act
        val warning = MPThreeDSWarning(
            id = id,
            message = message,
            severity = severity
        )

        // Assert
        assertEquals(id, warning.id)
        assertEquals(message, warning.message)
        assertEquals(severity, warning.severity)
    }

    @Test
    fun `MPThreeDSWarning should handle empty strings`() {
        // Arrange & Act
        val warning = MPThreeDSWarning(
            id = "",
            message = "",
            severity = MPThreeDSSeverity.NONE
        )

        // Assert
        assertEquals("", warning.id)
        assertEquals("", warning.message)
        assertEquals(MPThreeDSSeverity.NONE, warning.severity)
    }

    @Test
    fun `MPThreeDSWarning should handle long messages`() {
        // Arrange
        val longMessage = "This is a very long warning message that contains multiple sentences and " +
                "detailed information about what went wrong during the 3DS authentication process. " +
                "It should be preserved exactly as provided without any truncation or modification."

        // Act
        val warning = MPThreeDSWarning(
            id = "long-message-warning",
            message = longMessage,
            severity = MPThreeDSSeverity.MEDIUM
        )

        // Assert
        assertEquals("long-message-warning", warning.id)
        assertEquals(longMessage, warning.message)
        assertEquals(MPThreeDSSeverity.MEDIUM, warning.severity)
    }

    @Test
    fun `MPThreeDSWarning should handle special characters`() {
        // Arrange
        val specialId = "warning-!@#$%^&*()"
        val specialMessage = "Message with special chars: <>&\"'`~[]{}|\\:;?/.,+=_-"

        // Act
        val warning = MPThreeDSWarning(
            id = specialId,
            message = specialMessage,
            severity = MPThreeDSSeverity.LOW
        )

        // Assert
        assertEquals(specialId, warning.id)
        assertEquals(specialMessage, warning.message)
        assertEquals(MPThreeDSSeverity.LOW, warning.severity)
    }

    @Test
    fun `MPThreeDSWarning equality should work correctly`() {
        // Arrange
        val warning1 = MPThreeDSWarning(
            id = "warning-1",
            message = "Test message",
            severity = MPThreeDSSeverity.HIGH
        )

        val warning2 = MPThreeDSWarning(
            id = "warning-1",
            message = "Test message",
            severity = MPThreeDSSeverity.HIGH
        )

        val warning3 = MPThreeDSWarning(
            id = "warning-2",
            message = "Test message",
            severity = MPThreeDSSeverity.HIGH
        )

        // Act & Assert
        assertEquals(warning1, warning2)
        assertNotEquals(warning1, warning3)
        assertEquals(warning1.hashCode(), warning2.hashCode())
    }

    @Test
    fun `MPThreeDSSeverity should have all expected values`() {
        // Arrange
        val expectedSeverities = listOf(
            MPThreeDSSeverity.LOW,
            MPThreeDSSeverity.MEDIUM,
            MPThreeDSSeverity.HIGH,
            MPThreeDSSeverity.NONE
        )

        // Act
        val allValues = MPThreeDSSeverity.values().toList()

        // Assert
        assertEquals(4, allValues.size)
        expectedSeverities.forEach { expectedSeverity ->
            assertTrue("Missing severity: $expectedSeverity", allValues.contains(expectedSeverity))
        }
    }

    @Test
    fun `MPThreeDSSeverity should have correct ordinal values`() {
        // Act & Assert
        assertEquals(0, MPThreeDSSeverity.LOW.ordinal)
        assertEquals(1, MPThreeDSSeverity.MEDIUM.ordinal)
        assertEquals(2, MPThreeDSSeverity.HIGH.ordinal)
        assertEquals(3, MPThreeDSSeverity.NONE.ordinal)
    }

    @Test
    fun `MPThreeDSSeverity should have correct string representation`() {
        // Act & Assert
        assertEquals("LOW", MPThreeDSSeverity.LOW.toString())
        assertEquals("MEDIUM", MPThreeDSSeverity.MEDIUM.toString())
        assertEquals("HIGH", MPThreeDSSeverity.HIGH.toString())
        assertEquals("NONE", MPThreeDSSeverity.NONE.toString())
    }

    @Test
    fun `MPThreeDSSeverity valueOf should work correctly`() {
        // Act & Assert
        assertEquals(MPThreeDSSeverity.LOW, MPThreeDSSeverity.valueOf("LOW"))
        assertEquals(MPThreeDSSeverity.MEDIUM, MPThreeDSSeverity.valueOf("MEDIUM"))
        assertEquals(MPThreeDSSeverity.HIGH, MPThreeDSSeverity.valueOf("HIGH"))
        assertEquals(MPThreeDSSeverity.NONE, MPThreeDSSeverity.valueOf("NONE"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `MPThreeDSSeverity valueOf should throw exception for invalid value`() {
        // Act
        MPThreeDSSeverity.valueOf("INVALID")
    }
}
