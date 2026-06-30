package com.mercadopago.sdk.android.checkout.presentation.state

import kotlin.test.Test
import kotlin.test.assertTrue

internal class CheckoutDestinationTest {
    @Test
    fun `all destinations are checkout destinations`() {
        val destinations = listOf(
            CheckoutDestination.Form,
            CheckoutDestination.Installment,
        )

        assertTrue(destinations.all { it is CheckoutDestination })
    }

    @Test
    fun `destinations are distinct singletons`() {
        val destinations = setOf(
            CheckoutDestination.Form,
            CheckoutDestination.Installment,
        )

        assertTrue(destinations.size == 2)
    }
}
