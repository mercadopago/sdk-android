package com.mercadopago.sdk.android.googlepay.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

internal class GooglePayCardNetworkTest {
    @Test
    fun `each network maps to its Google Pay network name`() {
        assertEquals("VISA", GooglePayCardNetwork.VISA.googleNetwork)
        assertEquals("MASTERCARD", GooglePayCardNetwork.MASTER.googleNetwork)
        assertEquals("AMEX", GooglePayCardNetwork.AMEX.googleNetwork)
    }

    @Test
    fun `a Google Pay network name maps back to its network`() {
        assertEquals(GooglePayCardNetwork.VISA, GooglePayCardNetwork.fromGoogleNetwork("VISA"))
        assertEquals(GooglePayCardNetwork.MASTER, GooglePayCardNetwork.fromGoogleNetwork("MASTERCARD"))
        assertEquals(GooglePayCardNetwork.AMEX, GooglePayCardNetwork.fromGoogleNetwork("AMEX"))
    }

    @Test
    fun `an unknown or missing Google Pay network name maps to null`() {
        assertNull(GooglePayCardNetwork.fromGoogleNetwork("ELO"))
        assertNull(GooglePayCardNetwork.fromGoogleNetwork("MASTER"))
        assertNull(GooglePayCardNetwork.fromGoogleNetwork(null))
    }

    @Test
    fun `the Mercado Pago payment method id still maps to its network`() {
        assertEquals(GooglePayCardNetwork.MASTER, GooglePayCardNetwork.fromPaymentMethodId("master"))
        assertNull(GooglePayCardNetwork.fromPaymentMethodId("MASTERCARD"))
    }
}
