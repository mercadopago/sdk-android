package com.mercadopago.sdk.android.threeds.domain.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class MPThreeDSChallengeErrorTest {

    @Test
    fun `when MPThreeDSChallengeError is created with all parameters Then should set all fields correctly`() {
        // Given
        val code = "TEST_ERROR"
        val message = "Test error message"
        val details = "Test error details"
        val cause = RuntimeException("Test cause")

        // When
        val error = MPThreeDSChallengeError(
            code = code,
            message = message,
            details = details,
            cause = cause
        )

        // Then
        assertEquals(code, error.code)
        assertEquals(message, error.message)
        assertEquals(details, error.details)
        assertEquals(cause, error.cause)
        assertTrue(error is Exception)
    }

    @Test
    fun `when MPThreeDSChallengeError is created with minimal parameters Then should set required fields`() {
        // Given
        val code = "MINIMAL_ERROR"
        val message = "Minimal error message"

        // When
        val error = MPThreeDSChallengeError(
            code = code,
            message = message
        )

        // Then
        assertEquals(code, error.code)
        assertEquals(message, error.message)
        assertEquals(null, error.details)
        assertEquals(null, error.cause)
    }

    @Test
    fun `when fromException is called with RuntimeException Then should create error with UNKNOWN_ERROR code`() {
        // Given
        val exception = RuntimeException("Test runtime exception")

        // When
        val error = MPThreeDSChallengeError.fromException(exception)

        // Then
        assertEquals("UNKNOWN_ERROR", error.code)
        assertEquals("Test runtime exception", error.message)
        assertEquals(exception, error.cause)
        assertEquals(null, error.details)
    }

    @Test
    fun `when fromException is called with exception without message Then should use default message`() {
        // Given
        val exception = RuntimeException()

        // When
        val error = MPThreeDSChallengeError.fromException(exception)

        // Then
        assertEquals("UNKNOWN_ERROR", error.code)
        assertEquals("Unknown error occurred", error.message)
        assertEquals(exception, error.cause)
    }

    @Test
    fun `when fromException is called with IOException Then should handle correctly`() {
        // Given
        val exception = java.io.IOException("Network error")

        // When
        val error = MPThreeDSChallengeError.fromException(exception)

        // Then
        assertEquals("UNKNOWN_ERROR", error.code)
        assertEquals("Network error", error.message)
        assertEquals(exception, error.cause)
    }

    @Test
    fun `when authenticationFailed is called Then should create error with AUTHENTICATION_FAILED code`() {
        // Given
        val reason = "Invalid credentials"

        // When
        val error = MPThreeDSChallengeError.authenticationFailed(reason)

        // Then
        assertEquals("AUTHENTICATION_FAILED", error.code)
        assertEquals("3DS Authentication failed: Invalid credentials", error.message)
        assertEquals(null, error.details)
        assertEquals(null, error.cause)
    }

    @Test
    fun `when authenticationFailed is called with empty reason Then should handle correctly`() {
        // Given
        val reason = ""

        // When
        val error = MPThreeDSChallengeError.authenticationFailed(reason)

        // Then
        assertEquals("AUTHENTICATION_FAILED", error.code)
        assertEquals("3DS Authentication failed: ", error.message)
    }

    @Test
    fun `when challengeFailed is called Then should create error with CHALLENGE_FAILED code`() {
        // Given
        val reason = "User cancelled challenge"

        // When
        val error = MPThreeDSChallengeError.challengeFailed(reason)

        // Then
        assertEquals("CHALLENGE_FAILED", error.code)
        assertEquals("3DS Challenge failed: User cancelled challenge", error.message)
        assertEquals(null, error.details)
        assertEquals(null, error.cause)
    }

    @Test
    fun `when challengeFailed is called with empty reason Then should handle correctly`() {
        // Given
        val reason = ""

        // When
        val error = MPThreeDSChallengeError.challengeFailed(reason)

        // Then
        assertEquals("CHALLENGE_FAILED", error.code)
        assertEquals("3DS Challenge failed: ", error.message)
    }

    @Test
    fun `when challengeFailed is called with timeout reason Then should create appropriate error`() {
        // Given
        val reason = "Challenge timed out after 5 minutes"

        // When
        val error = MPThreeDSChallengeError.challengeFailed(reason)

        // Then
        assertEquals("CHALLENGE_FAILED", error.code)
        assertEquals("3DS Challenge failed: Challenge timed out after 5 minutes", error.message)
    }
}
