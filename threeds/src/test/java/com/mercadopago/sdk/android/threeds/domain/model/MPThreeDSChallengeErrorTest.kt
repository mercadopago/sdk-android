package com.mercadopago.sdk.android.threeds.domain.model

import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class MPThreeDSChallengeErrorTest {

    @Test
    fun `when fromException is called with exception Then should create error with correct properties`() {
        // Given
        val originalException = IOException("Network connection failed")

        // When
        val error = MPThreeDSChallengeError.fromException(originalException)

        // Then
        assertEquals("UNKNOWN_ERROR", error.code)
        assertEquals("Network connection failed", error.message)
        assertEquals(originalException, error.cause)
        assertNull(error.details)
    }

    @Test
    fun `when fromException is called with exception without message Then should use default message`() {
        // Given
        val originalException = RuntimeException()

        // When
        val error = MPThreeDSChallengeError.fromException(originalException)

        // Then
        assertEquals("UNKNOWN_ERROR", error.code)
        assertEquals("Unknown error occurred", error.message)
        assertEquals(originalException, error.cause)
    }

    @Test
    fun `when authenticationFailed is called Then should create authentication error`() {
        // Given
        val reason = "Invalid card token"

        // When
        val error = MPThreeDSChallengeError.authenticationFailed(reason)

        // Then
        assertEquals("AUTHENTICATION_FAILED", error.code)
        assertEquals("3DS Authentication failed: Invalid card token", error.message)
        assertNull(error.cause)
        assertNull(error.details)
    }

    @Test
    fun `when challengeFailed is called Then should create challenge error`() {
        // Given
        val reason = "User cancelled challenge"

        // When
        val error = MPThreeDSChallengeError.challengeFailed(reason)

        // Then
        assertEquals("CHALLENGE_FAILED", error.code)
        assertEquals("3DS Challenge failed: User cancelled challenge", error.message)
        assertNull(error.cause)
        assertNull(error.details)
    }

    @Test
    fun `when MPThreeDSChallengeError is created directly Then should have correct properties`() {
        // Given
        val code = "CUSTOM_ERROR"
        val message = "Custom error message"
        val details = "Additional error details"
        val cause = IllegalStateException("State error")

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
    }
}
