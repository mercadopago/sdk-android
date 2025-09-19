package com.mercadopago.sdk.android.threeds.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MPThreeDSChallengeErrorTest {
    @Test
    fun `MPThreeDSChallengeError should create instance with all properties`() {
        // Arrange
        val code = "PROTOCOL_ERROR"
        val message = "Protocol error occurred"
        val details = "Additional error details"
        val cause = RuntimeException("Root cause")

        // Act
        val error = MPThreeDSChallengeError(
            code = code,
            message = message,
            details = details,
            cause = cause,
        )

        // Assert
        assertEquals(code, error.code)
        assertEquals(message, error.message)
        assertEquals(details, error.details)
        assertEquals(cause, error.cause)
    }

    @Test
    fun `MPThreeDSChallengeError should create instance with minimal properties`() {
        // Arrange
        val code = "RUNTIME_ERROR"
        val message = "Runtime error occurred"

        // Act
        val error = MPThreeDSChallengeError(
            code = code,
            message = message,
        )

        // Assert
        assertEquals(code, error.code)
        assertEquals(message, error.message)
        assertNull(error.details)
        assertNull(error.cause)
    }

    @Test
    fun `MPThreeDSChallengeError should inherit from Exception`() {
        // Arrange
        val error = MPThreeDSChallengeError(
            code = "TEST_ERROR",
            message = "Test error message",
        )

        // Act & Assert
        assertTrue(error is Exception)
        assertTrue(error is Throwable)
    }

    @Test
    fun `MPThreeDSChallengeError should handle empty strings`() {
        // Arrange & Act
        val error = MPThreeDSChallengeError(
            code = "",
            message = "",
            details = "",
        )

        // Assert
        assertEquals("", error.code)
        assertEquals("", error.message)
        assertEquals("", error.details)
    }

    @Test
    fun `fromException should create error from generic exception`() {
        // Arrange
        val originalException = RuntimeException("Original error message")

        // Act
        val error = MPThreeDSChallengeError.fromException(originalException)

        // Assert
        assertEquals("UNKNOWN_ERROR", error.code)
        assertEquals("Original error message", error.message)
        assertEquals(originalException, error.cause)
    }

    @Test
    fun `fromException should handle exception with null message`() {
        // Arrange
        val originalException = RuntimeException()

        // Act
        val error = MPThreeDSChallengeError.fromException(originalException)

        // Assert
        assertEquals("UNKNOWN_ERROR", error.code)
        assertEquals("Unknown error occurred", error.message)
        assertEquals(originalException, error.cause)
    }

    @Test
    fun `fromException should handle different exception types`() {
        // Arrange
        val exceptions = listOf(
            IllegalArgumentException("Invalid argument"),
            NullPointerException("Null pointer"),
            IllegalStateException("Invalid state"),
        )

        // Act & Assert
        exceptions.forEach { exception ->
            val error = MPThreeDSChallengeError.fromException(exception)
            assertEquals("UNKNOWN_ERROR", error.code)
            assertEquals(exception.message, error.message)
            assertEquals(exception, error.cause)
        }
    }

    @Test
    fun `authenticationFailed should create authentication error`() {
        // Arrange
        val reason = "Invalid credentials"

        // Act
        val error = MPThreeDSChallengeError.authenticationFailed(reason)

        // Assert
        assertEquals("AUTHENTICATION_FAILED", error.code)
        assertEquals("3DS Authentication failed: Invalid credentials", error.message)
        assertNull(error.details)
        assertNull(error.cause)
    }

    @Test
    fun `authenticationFailed should handle empty reason`() {
        // Arrange
        val reason = ""

        // Act
        val error = MPThreeDSChallengeError.authenticationFailed(reason)

        // Assert
        assertEquals("AUTHENTICATION_FAILED", error.code)
        assertEquals("3DS Authentication failed: ", error.message)
    }

    @Test
    fun `challengeFailed should create challenge error`() {
        // Arrange
        val reason = "User cancelled challenge"

        // Act
        val error = MPThreeDSChallengeError.challengeFailed(reason)

        // Assert
        assertEquals("CHALLENGE_FAILED", error.code)
        assertEquals("3DS Challenge failed: User cancelled challenge", error.message)
        assertNull(error.details)
        assertNull(error.cause)
    }

    @Test
    fun `challengeFailed should handle empty reason`() {
        // Arrange
        val reason = ""

        // Act
        val error = MPThreeDSChallengeError.challengeFailed(reason)

        // Assert
        assertEquals("CHALLENGE_FAILED", error.code)
        assertEquals("3DS Challenge failed: ", error.message)
    }

    @Test
    fun `challengeFailed should handle special characters in reason`() {
        // Arrange
        val reason = "Special chars: <>&\"'`~[]{}|\\:;?/.,+=_-!@#$%^*()"

        // Act
        val error = MPThreeDSChallengeError.challengeFailed(reason)

        // Assert
        assertEquals("CHALLENGE_FAILED", error.code)
        assertEquals("3DS Challenge failed: Special chars: <>&\"'`~[]{}|\\:;?/.,+=_-!@#$%^*()", error.message)
    }

    @Test
    fun `factory methods should create different error types`() {
        // Arrange
        val exception = RuntimeException("Test exception")
        val authReason = "Authentication reason"
        val challengeReason = "Challenge reason"

        // Act
        val unknownError = MPThreeDSChallengeError.fromException(exception)
        val authError = MPThreeDSChallengeError.authenticationFailed(authReason)
        val challengeError = MPThreeDSChallengeError.challengeFailed(challengeReason)

        // Assert
        assertEquals("UNKNOWN_ERROR", unknownError.code)
        assertEquals("AUTHENTICATION_FAILED", authError.code)
        assertEquals("CHALLENGE_FAILED", challengeError.code)

        assertNotEquals(unknownError.code, authError.code)
        assertNotEquals(authError.code, challengeError.code)
        assertNotEquals(unknownError.code, challengeError.code)
    }

    @Test
    fun `error should maintain stack trace when thrown`() {
        // Arrange
        val error = MPThreeDSChallengeError(
            code = "TEST_ERROR",
            message = "Test error for stack trace",
        )

        // Act & Assert
        try {
            throw error
        } catch (e: MPThreeDSChallengeError) {
            assertEquals("TEST_ERROR", e.code)
            assertEquals("Test error for stack trace", e.message)
            assertNotNull(e.stackTrace)
            assertTrue(e.stackTrace.isNotEmpty())
        }
    }
}
