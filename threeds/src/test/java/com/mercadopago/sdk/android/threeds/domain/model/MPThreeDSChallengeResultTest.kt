package com.mercadopago.sdk.android.threeds.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class MPThreeDSChallengeResultTest {
    @Test
    fun `OnSuccess should contain MPThreeDSAuthenticated result`() {
        // Arrange
        val challengeModel = MPThreeDSChallengeModel(
            threeDSServerTransID = "server-trans-123",
            acsReferenceNumber = "acs-ref-456",
            dsTransID = "ds-trans-789",
            acsTransID = "acs-trans-101",
            acsSignedContent = "signed-content-abc",
        )

        val authenticated = MPThreeDSAuthenticated(
            challengeResponse = challengeModel,
            challengeCompleted = true,
        )

        // Act
        val onSuccessResult = MPThreeDSChallengeResult.OnSuccess(authenticated)

        // Assert
        assertTrue(onSuccessResult is MPThreeDSChallengeResult.OnSuccess)
        assertEquals(authenticated, onSuccessResult.result)
        assertEquals(challengeModel, onSuccessResult.result.challengeResponse)
        assertTrue(onSuccessResult.result.challengeCompleted)
    }

    @Test
    fun `OnSuccess should handle challenge not completed`() {
        // Arrange
        val challengeModel = MPThreeDSChallengeModel(
            threeDSServerTransID = "server-trans-123",
            acsReferenceNumber = "acs-ref-456",
            dsTransID = "ds-trans-789",
            acsTransID = "acs-trans-101",
            acsSignedContent = "signed-content-abc",
        )

        val authenticated = MPThreeDSAuthenticated(
            challengeResponse = challengeModel,
            challengeCompleted = false,
        )

        // Act
        val onSuccessResult = MPThreeDSChallengeResult.OnSuccess(authenticated)

        // Assert
        assertFalse(onSuccessResult.result.challengeCompleted)
    }

    @Test
    fun `OnError should contain MPThreeDSChallengeError`() {
        // Arrange
        val error = MPThreeDSChallengeError(
            code = "PROTOCOL_ERROR",
            message = "Protocol error occurred during challenge",
            details = "Additional error details",
        )

        // Act
        val onErrorResult = MPThreeDSChallengeResult.OnError(error)

        // Assert
        assertTrue(onErrorResult is MPThreeDSChallengeResult.OnError)
        assertEquals(error, onErrorResult.error)
        assertEquals("PROTOCOL_ERROR", onErrorResult.error.code)
        assertEquals("Protocol error occurred during challenge", onErrorResult.error.message)
        assertEquals("Additional error details", onErrorResult.error.details)
    }

    @Test
    fun `OnError should handle error without details`() {
        // Arrange
        val error = MPThreeDSChallengeError(
            code = "RUNTIME_ERROR",
            message = "Runtime error occurred",
        )

        // Act
        val onErrorResult = MPThreeDSChallengeResult.OnError(error)

        // Assert
        assertEquals("RUNTIME_ERROR", onErrorResult.error.code)
        assertEquals("Runtime error occurred", onErrorResult.error.message)
        assertNull(onErrorResult.error.details)
    }

    @Test
    fun `OnCancel should be singleton object`() {
        // Act
        val onCancel1 = MPThreeDSChallengeResult.OnCancel
        val onCancel2 = MPThreeDSChallengeResult.OnCancel

        // Assert
        assertTrue(onCancel1 is MPThreeDSChallengeResult.OnCancel)
        assertSame(onCancel1, onCancel2)
    }

    @Test
    fun `OnTimedOut should be singleton object`() {
        // Act
        val onTimedOut1 = MPThreeDSChallengeResult.OnTimedOut
        val onTimedOut2 = MPThreeDSChallengeResult.OnTimedOut

        // Assert
        assertTrue(onTimedOut1 is MPThreeDSChallengeResult.OnTimedOut)
        assertSame(onTimedOut1, onTimedOut2)
    }

    @Test
    fun `sealed class should have all expected subclasses`() {
        // Arrange
        val challengeModel = MPThreeDSChallengeModel(
            threeDSServerTransID = "test",
            acsReferenceNumber = "test",
            dsTransID = "test",
            acsTransID = "test",
            acsSignedContent = "test",
        )

        val authenticated = MPThreeDSAuthenticated(challengeModel, true)
        val error = MPThreeDSChallengeError("TEST_ERROR", "Test error message")

        val results: List<MPThreeDSChallengeResult> = listOf(
            MPThreeDSChallengeResult.OnSuccess(authenticated),
            MPThreeDSChallengeResult.OnError(error),
            MPThreeDSChallengeResult.OnCancel,
            MPThreeDSChallengeResult.OnTimedOut,
        )

        // Act & Assert
        results.forEach { result ->
            when (result) {
                is MPThreeDSChallengeResult.OnSuccess -> {
                    assertNotNull(result.result)
                    assertTrue(result.result is MPThreeDSAuthenticated)
                }
                is MPThreeDSChallengeResult.OnError -> {
                    assertNotNull(result.error)
                    assertTrue(result.error is MPThreeDSChallengeError)
                }
                is MPThreeDSChallengeResult.OnCancel -> {
                    // OnCancel has no additional properties to test
                    assertTrue(true)
                }
                is MPThreeDSChallengeResult.OnTimedOut -> {
                    // OnTimedOut has no additional properties to test
                    assertTrue(true)
                }
            }
        }
    }

    @Test
    fun `when expressions should handle all result types`() {
        // Arrange
        val challengeModel = MPThreeDSChallengeModel("test", "test", "test", "test", "test")
        val authenticated = MPThreeDSAuthenticated(challengeModel, true)
        val error = MPThreeDSChallengeError("ERROR", "Error message")

        val results = listOf(
            MPThreeDSChallengeResult.OnSuccess(authenticated),
            MPThreeDSChallengeResult.OnError(error),
            MPThreeDSChallengeResult.OnCancel,
            MPThreeDSChallengeResult.OnTimedOut,
        )

        // Act & Assert
        results.forEach { result ->
            val resultType = when (result) {
                is MPThreeDSChallengeResult.OnSuccess -> "success"
                is MPThreeDSChallengeResult.OnError -> "error"
                is MPThreeDSChallengeResult.OnCancel -> "cancel"
                is MPThreeDSChallengeResult.OnTimedOut -> "timeout"
            }

            assertNotNull("Result type should not be null", resultType)
            assertTrue(
                "Result type should be valid",
                listOf("success", "error", "cancel", "timeout").contains(resultType),
            )
        }
    }
}
