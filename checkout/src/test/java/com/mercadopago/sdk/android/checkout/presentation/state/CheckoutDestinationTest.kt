package com.mercadopago.sdk.android.checkout.presentation.state

import kotlin.test.Test
import kotlin.test.assertTrue

internal class CheckoutDestinationTest {
    @Test
    fun `all destinations are checkout destinations`() {
        // Given / When
        val destinations = listOf(
            CheckoutDestination.Form,
            CheckoutDestination.Installment,
            CheckoutDestination.ReviewConfirm,
        )

        // Then
        assertTrue(destinations.all { it is CheckoutDestination })
    }

    @Test
    fun `destinations are distinct singletons`() {
        // Given / When
        val destinations = setOf(
            CheckoutDestination.Form,
            CheckoutDestination.Installment,
            CheckoutDestination.ReviewConfirm,
        )

        // Then
        assertTrue(destinations.size == 3)
    }

    @Test
    fun `given ReviewConfirm then is a CheckoutDestination`() {
        // Given / When
        val destination = CheckoutDestination.ReviewConfirm

        // Then
        assertTrue(destination is CheckoutDestination)
    }
}
