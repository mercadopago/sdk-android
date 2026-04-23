package com.mercadopago.sdk.android.checkout.presentation.extensions

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import io.mockk.every
import io.mockk.mockk
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class StringExtensionsTest {
    private val stringProvider = mockk<StringProvider>()

    @Test
    fun `given locale with known currency then getCurrencyString returns currency symbol`() {
        val result = Locale("pt", "BR").getCurrencyString()

        assertEquals("R$", result)
    }

    @Test
    fun `given null locale then getCurrencyString uses default locale`() {
        val result = null.getCurrencyString()

        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `given string with all same digits then hasAllSameDigits returns true`() {
        assertTrue("1111".hasAllSameDigits())
    }

    @Test
    fun `given string with different digits then hasAllSameDigits returns false`() {
        assertFalse("1234".hasAllSameDigits())
    }

    @Test
    fun `given string with letters only then hasAllSameDigits returns false`() {
        assertFalse("aaaa".hasAllSameDigits())
    }

    @Test
    fun `given empty string then hasAllSameDigits returns false`() {
        assertFalse("".hasAllSameDigits())
    }

    @Test
    fun `given string mixed with same digit chars then hasAllSameDigits uses only digits`() {
        assertTrue("1a1b1".hasAllSameDigits())
    }

    @Test
    fun `given string shorter than previous then isBeingCleared returns true`() {
        assertTrue("abc".isBeingCleared("abcd"))
    }

    @Test
    fun `given string same length as previous then isBeingCleared returns false`() {
        assertFalse("abcd".isBeingCleared("abcd"))
    }

    @Test
    fun `given string longer than previous then isBeingCleared returns false`() {
        assertFalse("abcde".isBeingCleared("abcd"))
    }

    @Test
    fun `given credit card type value then toCardTypeErrorMessage returns combined message`() {
        every { stringProvider.getString(R.string.card_form_error_card_type_not_accepted) } returns "Not accepted"
        every { stringProvider.getString(R.string.card_type_credit_card) } returns "credit"

        val result = CardType.CREDIT.value.toCardTypeErrorMessage(stringProvider)

        assertEquals("Not accepted credit", result)
    }

    @Test
    fun `given debit card type value then toCardTypeErrorMessage returns combined message`() {
        every { stringProvider.getString(R.string.card_form_error_card_type_not_accepted) } returns "Not accepted"
        every { stringProvider.getString(R.string.card_type_debit_card) } returns "debit"

        val result = CardType.DEBIT.value.toCardTypeErrorMessage(stringProvider)

        assertEquals("Not accepted debit", result)
    }

    @Test
    fun `given prepaid card type value then toCardTypeErrorMessage returns combined message`() {
        every { stringProvider.getString(R.string.card_form_error_card_type_not_accepted) } returns "Not accepted"
        every { stringProvider.getString(R.string.card_type_prepaid) } returns "prepaid"

        val result = CardType.PREPAID.value.toCardTypeErrorMessage(stringProvider)

        assertEquals("Not accepted prepaid", result)
    }

    @Test
    fun `given unknown card type value then toCardTypeErrorMessage returns base message only`() {
        every { stringProvider.getString(R.string.card_form_error_card_type_not_accepted) } returns "Not accepted"

        val result = "unknown_type".toCardTypeErrorMessage(stringProvider)

        assertEquals("Not accepted", result)
    }

    @Test
    fun `given CardBrand then toCardBrandErrorMessage returns base message with brand name`() {
        val brandRes = R.string.card_form_error_card_brand_not_accepted
        every { stringProvider.getString(brandRes) } returns "Brand not accepted"

        val result = CardBrand.Visa.toCardBrandErrorMessage(stringProvider)

        assertEquals("Brand not accepted visa", result)
    }
}
