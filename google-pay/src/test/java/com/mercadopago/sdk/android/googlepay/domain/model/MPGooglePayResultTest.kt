package com.mercadopago.sdk.android.googlepay.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

internal class MPGooglePayResultTest {
    @Test
    fun `result variants are exhaustive in a when`() {
        val error = MPGooglePayError(MPGooglePayErrorCode.NETWORK_ERROR, "x")
        val results = listOf(
            MPGooglePayResult.Ready,
            MPGooglePayResult.Success("data"),
            MPGooglePayResult.Error(error),
            MPGooglePayResult.Cancelled,
        )

        val labels = results.map {
            when (it) {
                MPGooglePayResult.Ready -> "ready"
                is MPGooglePayResult.Success -> "success:${it.paymentData}"
                is MPGooglePayResult.Error -> "error:${it.error.code}"
                MPGooglePayResult.Cancelled -> "cancelled"
            }
        }

        assertEquals(listOf("ready", "success:data", "error:NETWORK_ERROR", "cancelled"), labels)
    }

    @Test
    fun `error wraps the given error`() {
        val error = MPGooglePayError(MPGooglePayErrorCode.WALLET_NOT_ENABLED, "x")

        assertSame(error, MPGooglePayResult.Error(error).error)
    }
}
