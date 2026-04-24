package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.domain.model.CardData
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCode
import com.mercadopago.sdk.android.coremethods.domain.model.CardModel
import com.mercadopago.sdk.android.coremethods.domain.model.LengthModel
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class CardDataExtensionsTest {
    @Test
    fun `given security code length zero then isOptional returns true`() {
        val securityCode = SecurityCode(length = 0, mode = "optional", location = "back")

        assertTrue(securityCode.isOptional())
    }

    @Test
    fun `given security code length negative then isOptional returns true`() {
        val securityCode = SecurityCode(length = -1, mode = "optional", location = "back")

        assertTrue(securityCode.isOptional())
    }

    @Test
    fun `given security code length greater than zero then isOptional returns false`() {
        val securityCode = SecurityCode(length = 3, mode = "mandatory", location = "back")

        assertFalse(securityCode.isOptional())
    }

    @Test
    fun `given CardData with card max length then getLength returns that value`() {
        val cardData = CardData(
            paymentMethod = PaymentMethod(card = CardModel(length = LengthModel(max = 16))),
            securityCode = SecurityCode(length = 3, mode = "mandatory", location = "back"),
            cardIssuer = null,
            installments = null,
        )

        assertEquals(16, cardData.getLength())
    }

    @Test
    fun `given CardData with null card then getLength returns default CARD_LENGTH_19`() {
        val cardData = CardData(
            paymentMethod = PaymentMethod(card = null),
            securityCode = SecurityCode(length = 3, mode = "mandatory", location = "back"),
            cardIssuer = null,
            installments = null,
        )

        assertEquals(CARD_LENGTH_19, cardData.getLength())
    }
}
