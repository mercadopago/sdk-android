package com.mercadopago.sdk.android.checkout.presentation.state

import kotlin.test.Test
import kotlin.test.assertEquals

internal class CheckoutDestinationTest {
    private val destinations = listOf<CheckoutDestination>(
        CheckoutDestination.Payment,
        CheckoutDestination.Form,
        CheckoutDestination.SecurityCode,
        CheckoutDestination.Installment,
        CheckoutDestination.OfflineMethodSelector,
        CheckoutDestination.ReviewConfirm,
    )

    @Test
    fun `fixture contains every declared checkout destination`() {
        val declaredDestinations = CheckoutDestination::class.sealedSubclasses
            .mapNotNull { it.objectInstance }
            .toSet()

        assertEquals(declaredDestinations, destinations.toSet())
    }

    @Test
    fun `destinations are distinct singletons`() {
        assertEquals(destinations.size, destinations.toSet().size)
    }
}
