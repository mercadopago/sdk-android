package com.mercadopago.sdk.android.checkout.presentation.validation

import com.mercadopago.sdk.android.checkout.presentation.state.EmailFieldState
import com.mercadopago.sdk.android.checkout.presentation.state.ValidationState
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
internal class EmailVerifierTest {
    private val verifier = EmailVerifier()

    private val state = EmailFieldState(
        validation = ValidationState(
            errorEmpty = "Field is empty",
            errorInvalid = "Invalid email",
        ),
    )

    @Test
    fun `given valid email then returns no error`() {
        assertEquals("", verifier.verify("user@example.com", state))
    }

    @Test
    fun `given empty value then returns empty error`() {
        assertEquals(state.validation.errorEmpty, verifier.verify("", state))
    }

    @Test
    fun `given blank value then returns empty error`() {
        assertEquals(state.validation.errorEmpty, verifier.verify("   ", state))
    }

    @Test
    fun `given malformed email then returns invalid error`() {
        assertEquals(state.validation.errorInvalid, verifier.verify("user@", state))
    }
}
