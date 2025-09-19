package com.mercadopago.sdk.android.threeds.data.mappers

import com.mercadopago.sdk.android.threeds.data.model.MPSeverityResponse
import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSWarningResponse
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSSeverity
import org.junit.Test
import org.junit.Assert.*

class ThreeDSWarningMapperTest {

    @Test
    fun `toModel should map MPThreeDSWarningResponse correctly`() {
        // Arrange
        val inputWarningResponse = MPThreeDSWarningResponse(
            id = "warning-id-123",
            message = "This is a warning message",
            severity = MPSeverityResponse.HIGH
        )

        // Act
        val actualWarning = inputWarningResponse.toModel()

        // Assert
        assertEquals("warning-id-123", actualWarning.id)
        assertEquals("This is a warning message", actualWarning.message)
        assertEquals(MPThreeDSSeverity.HIGH, actualWarning.severity)
    }

    @Test
    fun `toModel should handle empty strings`() {
        // Arrange
        val inputWarningResponse = MPThreeDSWarningResponse(
            id = "",
            message = "",
            severity = MPSeverityResponse.NONE
        )

        // Act
        val actualWarning = inputWarningResponse.toModel()

        // Assert
        assertEquals("", actualWarning.id)
        assertEquals("", actualWarning.message)
        assertEquals(MPThreeDSSeverity.NONE, actualWarning.severity)
    }

    @Test
    fun `toModel should map MPSeverityResponse LOW correctly`() {
        // Arrange
        val inputSeverity = MPSeverityResponse.LOW

        // Act
        val actualSeverity = inputSeverity.toModel()

        // Assert
        assertEquals(MPThreeDSSeverity.LOW, actualSeverity)
    }

    @Test
    fun `toModel should map MPSeverityResponse MEDIUM correctly`() {
        // Arrange
        val inputSeverity = MPSeverityResponse.MEDIUM

        // Act
        val actualSeverity = inputSeverity.toModel()

        // Assert
        assertEquals(MPThreeDSSeverity.MEDIUM, actualSeverity)
    }

    @Test
    fun `toModel should map MPSeverityResponse HIGH correctly`() {
        // Arrange
        val inputSeverity = MPSeverityResponse.HIGH

        // Act
        val actualSeverity = inputSeverity.toModel()

        // Assert
        assertEquals(MPThreeDSSeverity.HIGH, actualSeverity)
    }

    @Test
    fun `toModel should map MPSeverityResponse NONE correctly`() {
        // Arrange
        val inputSeverity = MPSeverityResponse.NONE

        // Act
        val actualSeverity = inputSeverity.toModel()

        // Assert
        assertEquals(MPThreeDSSeverity.NONE, actualSeverity)
    }

    @Test
    fun `toModel should map all severity levels correctly`() {
        // Arrange
        val severityMappings = mapOf(
            MPSeverityResponse.LOW to MPThreeDSSeverity.LOW,
            MPSeverityResponse.MEDIUM to MPThreeDSSeverity.MEDIUM,
            MPSeverityResponse.HIGH to MPThreeDSSeverity.HIGH,
            MPSeverityResponse.NONE to MPThreeDSSeverity.NONE
        )

        // Act & Assert
        severityMappings.forEach { (input, expected) ->
            val actualSeverity = input.toModel()
            assertEquals("Failed mapping for $input", expected, actualSeverity)
        }
    }

    @Test
    fun `toModel should handle long warning messages`() {
        // Arrange
        val longMessage = "This is a very long warning message that contains multiple sentences. " +
                "It might include details about what went wrong during the 3DS authentication process. " +
                "The message should be preserved exactly as provided without any truncation."

        val inputWarningResponse = MPThreeDSWarningResponse(
            id = "long-message-warning",
            message = longMessage,
            severity = MPSeverityResponse.MEDIUM
        )

        // Act
        val actualWarning = inputWarningResponse.toModel()

        // Assert
        assertEquals("long-message-warning", actualWarning.id)
        assertEquals(longMessage, actualWarning.message)
        assertEquals(MPThreeDSSeverity.MEDIUM, actualWarning.severity)
    }

    @Test
    fun `toModel should handle special characters in warning data`() {
        // Arrange
        val inputWarningResponse = MPThreeDSWarningResponse(
            id = "warning-!@#$%^&*()",
            message = "Message with special chars: <>&\"'`~",
            severity = MPSeverityResponse.LOW
        )

        // Act
        val actualWarning = inputWarningResponse.toModel()

        // Assert
        assertEquals("warning-!@#$%^&*()", actualWarning.id)
        assertEquals("Message with special chars: <>&\"'`~", actualWarning.message)
        assertEquals(MPThreeDSSeverity.LOW, actualWarning.severity)
    }
}
