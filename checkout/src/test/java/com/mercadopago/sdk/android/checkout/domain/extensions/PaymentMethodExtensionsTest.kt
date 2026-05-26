package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.coremethods.domain.model.CardModel
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.SecurityCodeModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod as CheckoutPaymentMethod

internal class PaymentMethodExtensionsTest {
    @Test
    fun `given paymentMethod with securityCode then toSecurityCode maps all fields`() {
        val paymentMethod = PaymentMethod(
            card = CardModel(securityCode = SecurityCodeModel(length = 4, mode = "optional", location = "front")),
        )

        val result = paymentMethod.toSecurityCode()

        assertEquals(4, result.length)
        assertEquals("optional", result.mode)
        assertEquals("front", result.location)
    }

    @Test
    fun `given paymentMethod with null card then toSecurityCode uses defaults`() {
        val paymentMethod = PaymentMethod(card = null)

        val result = paymentMethod.toSecurityCode()

        assertEquals(3, result.length)
        assertEquals("mandatory", result.mode)
        assertEquals("back", result.location)
    }

    @Test
    fun `given paymentMethod with null securityCode fields then toSecurityCode uses defaults`() {
        val paymentMethod = PaymentMethod(card = CardModel(securityCode = SecurityCodeModel()))

        val result = paymentMethod.toSecurityCode()

        assertEquals(3, result.length)
        assertEquals("mandatory", result.mode)
        assertEquals("back", result.location)
    }

    @Test
    fun `given paymentMethod with issuer_id in additionalInfoNeeded then hasIssuers returns true`() {
        val paymentMethod = PaymentMethod(id = "visa", additionalInfoNeeded = listOf("issuer_id"))

        assertTrue(paymentMethod.hasIssuers())
    }

    @Test
    fun `given paymentMethod without issuer_id then hasIssuers returns false`() {
        val paymentMethod = PaymentMethod(id = "visa", additionalInfoNeeded = listOf("cardholder_name"))

        assertFalse(paymentMethod.hasIssuers())
    }

    @Test
    fun `given paymentMethod with null additionalInfoNeeded then hasIssuers returns false`() {
        val paymentMethod = PaymentMethod(id = "visa", additionalInfoNeeded = null)

        assertFalse(paymentMethod.hasIssuers())
    }

    @Test
    fun `given paymentMethod with null id then hasIssuers returns false`() {
        val paymentMethod = PaymentMethod(id = null, additionalInfoNeeded = listOf("issuer_id"))

        assertFalse(paymentMethod.hasIssuers())
    }

    @Test
    fun `given null list then extractCardFilters returns empty pairs`() {
        val result = null.extractCardFilters()

        assertEquals(emptyList(), result.first)
        assertEquals(emptyList(), result.second)
    }

    @Test
    fun `given list without Card type then extractCardFilters returns empty pairs`() {
        val result = listOf<CheckoutPaymentMethod>().extractCardFilters()

        assertEquals(emptyList(), result.first)
        assertEquals(emptyList(), result.second)
    }

    @Test
    fun `given list with Card type then extractCardFilters returns its filters`() {
        val excludedTypes = listOf(CardType.CREDIT)
        val excludedMethods = listOf(CardBrand.Visa)
        val paymentMethods = listOf(
            CheckoutPaymentMethod.Card(excludedPaymentTypes = excludedTypes, excludedPaymentMethods = excludedMethods),
        )

        val result = paymentMethods.extractCardFilters()

        assertEquals(excludedTypes, result.first)
        assertEquals(excludedMethods, result.second)
    }
}
