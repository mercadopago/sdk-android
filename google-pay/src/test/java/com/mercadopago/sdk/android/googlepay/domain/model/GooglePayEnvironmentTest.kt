package com.mercadopago.sdk.android.googlepay.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

internal class GooglePayEnvironmentTest {
    @Test
    fun `TEST- public key resolves to the test environment`() {
        assertEquals(GooglePayEnvironment.TEST, GooglePayEnvironment.fromPublicKey("TEST-abc"))
    }

    @Test
    fun `APP_USR public key resolves to the production environment`() {
        assertEquals(GooglePayEnvironment.PRODUCTION, GooglePayEnvironment.fromPublicKey("APP_USR-abc"))
    }

    @Test
    fun `blank public key resolves to production, never to test`() {
        assertEquals(GooglePayEnvironment.PRODUCTION, GooglePayEnvironment.fromPublicKey(""))
    }
}
