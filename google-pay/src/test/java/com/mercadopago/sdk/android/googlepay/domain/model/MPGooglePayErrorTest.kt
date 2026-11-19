package com.mercadopago.sdk.android.googlepay.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

internal class MPGooglePayErrorTest {
    private val unavailableCodes = setOf(
        "SDK_NOT_INITIALIZED",
        "INVALID_CONFIGURATION",
        "GOOGLE_PLAY_SERVICES_UNAVAILABLE",
        "WALLET_NOT_ENABLED",
        "NO_SUPPORTED_NETWORKS",
        "NO_ELIGIBLE_PAYMENT_METHOD",
        "WALLETS_FETCH_FAILED",
        "IS_READY_TO_PAY_FAILED",
    )

    private val paymentCodes = setOf(
        "GOOGLE_PAY_DEVELOPER_ERROR",
        "GOOGLE_PAY_INTERNAL_ERROR",
        "GOOGLE_PAY_ERROR",
        "UNSUPPORTED_CARD_NETWORK",
        "TOKENIZATION_FAILED",
        "NETWORK_ERROR",
        "FLOW_INTERRUPTED",
    )

    private fun codesOf(
        type: MPGooglePayErrorType,
    ) =
        MPGooglePayErrorCode.entries.filter { it.type == type }.map { it.name }.toSet()

    @Test
    fun `unavailable codes match the closed list`() {
        assertEquals(unavailableCodes, codesOf(MPGooglePayErrorType.UNAVAILABLE))
    }

    @Test
    fun `payment codes match the closed list`() {
        assertEquals(paymentCodes, codesOf(MPGooglePayErrorType.PAYMENT))
    }

    @Test
    fun `the public enum exposes exactly the closed list`() {
        assertEquals(unavailableCodes + paymentCodes, MPGooglePayErrorCode.entries.map { it.name }.toSet())
    }

    @Test
    fun `the type of the error is derived from its code`() {
        MPGooglePayErrorCode.entries.forEach { code ->
            assertEquals(code.type, MPGooglePayError(code, "message").type)
        }
    }

    @Test
    fun `toError builds error with the code and message`() {
        val error = MPGooglePayErrorCode.WALLET_NOT_ENABLED.toError("not enabled")

        assertEquals(MPGooglePayErrorType.UNAVAILABLE, error.type)
        assertEquals(MPGooglePayErrorCode.WALLET_NOT_ENABLED, error.code)
        assertEquals("not enabled", error.message)
    }

    @Test
    fun `payment code produces payment error`() {
        val error = MPGooglePayErrorCode.NETWORK_ERROR.toError("timeout")

        assertEquals(MPGooglePayErrorType.PAYMENT, error.type)
        assertEquals(MPGooglePayErrorCode.NETWORK_ERROR, error.code)
    }
}
