package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class PaymentMethodExtensionsTest {
    private fun binData(
        id: String?,
        paymentTypeId: String?,
    ) = CardBinData(
        id = id,
        paymentTypeId = paymentTypeId,
        cardNumber = null,
        securityCode = null,
        issuers = emptyList(),
        quotas = emptyList(),
        translations = null,
    )

    // region matchesCardBrand

    @Test
    fun `matchesCardBrand returns true when cardBrands list is empty`() {
        assertTrue(binData("amex", null).matchesCardBrand(emptyList()))
    }

    @Test
    fun `matchesCardBrand returns true when id matches a brand`() {
        assertTrue(binData("visa", null).matchesCardBrand(listOf(CardBrand.Visa)))
    }

    @Test
    fun `matchesCardBrand is case insensitive`() {
        assertTrue(binData("VISA", null).matchesCardBrand(listOf(CardBrand.Visa)))
    }

    @Test
    fun `matchesCardBrand returns false when id does not match any brand`() {
        assertFalse(binData("amex", null).matchesCardBrand(listOf(CardBrand.Visa, CardBrand.Mastercard)))
    }

    @Test
    fun `matchesCardBrand returns false when id is null`() {
        assertFalse(binData(null, null).matchesCardBrand(listOf(CardBrand.Visa)))
    }

    // endregion

    // region matchesCardType

    @Test
    fun `matchesCardType returns true when cardTypes list is empty`() {
        assertTrue(binData(null, "debit_card").matchesCardType(emptyList()))
    }

    @Test
    fun `matchesCardType returns true when paymentTypeId matches a type`() {
        assertTrue(binData(null, "credit_card").matchesCardType(listOf(CardType.CREDIT)))
    }

    @Test
    fun `matchesCardType is case insensitive`() {
        assertTrue(binData(null, "CREDIT_CARD").matchesCardType(listOf(CardType.CREDIT)))
    }

    @Test
    fun `matchesCardType returns false when paymentTypeId does not match any type`() {
        assertFalse(binData(null, "debit_card").matchesCardType(listOf(CardType.CREDIT)))
    }

    @Test
    fun `matchesCardType returns false when paymentTypeId is null`() {
        assertFalse(binData(null, null).matchesCardType(listOf(CardType.CREDIT)))
    }

    // endregion
}
