package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFieldConfig
import com.mercadopago.sdk.android.checkout.domain.model.CardHolderField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberValidation
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsDisplayType
import com.mercadopago.sdk.android.checkout.domain.model.LengthRange
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeField
import com.mercadopago.sdk.android.checkout.domain.model.Validation
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberState
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeState
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class CardBinStateMapperTest {
    private val baseState = CardPaymentScreenState(
        cardNumberState = CardNumberState(
            label = "Número",
            placeHolder = "0000",
            maxLength = 16,
        ),
        secureCodeState = SecurityCodeState(
            label = "CVV",
            maxLength = 3,
        ),
    )

    private val emptyBinData = CardBinData(
        id = null,
        paymentTypeId = null,
        cardNumber = null,
        securityCode = null,
        holderName = null,
        expirationDate = null,
        issuers = emptyList(),
        quotas = emptyList(),
        displayType = InstallmentsDisplayType.RadioButton,
        currencySymbol = "",
        installmentsTitle = "",
        installmentsTotalLabel = "",
        installmentsPayButtonLabel = "",
    )

    @Test
    fun `given cardNumber with length then updates maxLength`() {
        val data = emptyBinData.copy(
            cardNumber = cardNumberField(maxLength = 13),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals(13, result.cardNumberState.maxLength)
    }

    @Test
    fun `given null cardNumber then keeps current maxLength`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals(16, result.cardNumberState.maxLength)
    }

    @Test
    fun `given issuers then image remains null`() {
        val data = emptyBinData.copy(
            issuers = listOf(
                BinIssuer(id = "1", name = "Visa"),
                BinIssuer(id = "2", name = "MC"),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertNull(result.cardNumberState.image)
    }

    @Test
    fun `given empty issuers then image is null`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertNull(result.cardNumberState.image)
    }

    @Test
    fun `given cardNumberField with translations then updates label and placeholder`() {
        val data = emptyBinData.copy(
            cardNumber = cardNumberField(
                label = "Card number",
                placeholder = "#### #### #### ####",
                maxLength = 16,
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Card number", result.cardNumberState.label)
        assertEquals("#### #### #### ####", result.cardNumberState.placeHolder)
    }

    @Test
    fun `given null cardNumberField then keeps current label`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals("Número", result.cardNumberState.label)
        assertEquals("0000", result.cardNumberState.placeHolder)
    }

    @Test
    fun `given securityCode with length then updates secureCode maxLength`() {
        val data = emptyBinData.copy(securityCode = securityCodeField(length = 4))

        val result = baseState.applyCardBinData(data)

        assertEquals(4, result.secureCodeState.maxLength)
    }

    @Test
    fun `given null securityCode then optional is true`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertTrue(result.secureCodeState.optional)
    }

    @Test
    fun `given securityCode with length zero then optional is true`() {
        val data = emptyBinData.copy(securityCode = securityCodeField(length = 0))

        val result = baseState.applyCardBinData(data)

        assertTrue(result.secureCodeState.optional)
    }

    @Test
    fun `given securityCode with positive length then optional is false`() {
        val data = emptyBinData.copy(securityCode = securityCodeField(length = 3))

        val result = baseState.applyCardBinData(data)

        assertFalse(result.secureCodeState.optional)
    }

    @Test
    fun `given null securityCode then keeps current maxLength`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertEquals(3, result.secureCodeState.maxLength)
    }

    @Test
    fun `given issuers then maps to CardIssuer list with id`() {
        val data = emptyBinData.copy(
            issuers = listOf(BinIssuer(id = "42", name = "Visa")),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals(1, result.cardIssuers.size)
        assertEquals("42", result.cardIssuers.first().id)
        assertNull(result.cardIssuers.first().thumbnail)
    }

    @Test
    fun `given empty issuers then cardIssuers is empty`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertTrue(result.cardIssuers.isEmpty())
    }

    @Test
    fun `given non-empty quotas then showList is true`() {
        val data = emptyBinData.copy(
            quotas = listOf(
                Quota(
                    installments = 1,
                    installmentAmount = BigDecimal.valueOf(100.0),
                    totalAmount = BigDecimal.valueOf(100.0),
                ),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertTrue(result.installmentsState.showList)
    }

    @Test
    fun `given empty quotas then showList is false`() {
        val result = baseState.applyCardBinData(emptyBinData)

        assertFalse(result.installmentsState.showList)
    }

    @Test
    fun `given quotas then exposes them on installments state`() {
        val data = emptyBinData.copy(
            quotas = listOf(
                Quota(
                    installments = 3,
                    installmentAmount = BigDecimal.valueOf(33.33),
                    totalAmount = BigDecimal.valueOf(100.0),
                ),
            ),
        )

        val result = baseState.applyCardBinData(data)

        val quota = result.installmentsState.installments.first()
        assertEquals(3, quota.installments)
        assertEquals(0, BigDecimal.valueOf(33.33).compareTo(quota.installmentAmount))
        assertEquals(0, BigDecimal.valueOf(100.0).compareTo(quota.totalAmount))
    }

    @Test
    fun `given binData with id and paymentTypeId then updates paymentState`() {
        val data = emptyBinData.copy(id = "visa", paymentTypeId = "credit_card")

        val result = baseState.applyCardBinData(data)

        assertEquals("visa", result.paymentState.paymentMethodId)
        assertEquals("credit_card", result.paymentState.paymentTypeId)
    }

    @Test
    fun `given displayType RadioButton then exposes it on installments state`() {
        val data = emptyBinData.copy(displayType = InstallmentsDisplayType.RadioButton)

        val result = baseState.applyCardBinData(data)

        assertEquals(InstallmentsDisplayType.RadioButton, result.installmentsState.displayType)
    }

    @Test
    fun `given displayType Chevron then exposes it on installments state`() {
        val data = emptyBinData.copy(displayType = InstallmentsDisplayType.Chevron)

        val result = baseState.applyCardBinData(data)

        assertEquals(InstallmentsDisplayType.Chevron, result.installmentsState.displayType)
    }

    @Test
    fun `given installments translations then updates installments labels`() {
        val data = emptyBinData.copy(
            installmentsTitle = "Cuotas",
            installmentsTotalLabel = "Total",
            installmentsPayButtonLabel = "Pagar",
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Cuotas", result.installmentsState.title)
        assertEquals("Total", result.installmentsState.totalLabel)
        assertEquals("Pagar", result.installmentsState.payButtonLabel)
    }

    @Test
    fun `given currency symbol then exposes it on screen state`() {
        val data = emptyBinData.copy(currencySymbol = "R$")

        val result = baseState.applyCardBinData(data)

        assertEquals("R$", result.currencySymbol)
    }

    @Test
    fun `given holderName field then updates cardHolder label`() {
        val data = emptyBinData.copy(
            holderName = CardHolderField(
                label = "Titular",
                placeholder = "Maria",
                helper = "",
                validation = emptyValidation(),
                config = emptyFieldConfig(),
            ),
        )

        val result = baseState.applyCardBinData(data)

        assertEquals("Titular", result.cardHolderState.label)
        assertEquals("Maria", result.cardHolderState.placeHolder)
    }

    private fun cardNumberField(
        label: String = "",
        placeholder: String = "",
        maxLength: Int = 16,
    ) = CardNumberField(
        label = label,
        placeholder = placeholder,
        validation = CardNumberValidation(
            errorEmpty = "",
            errorIncomplete = "",
            errorInvalid = "",
            errorMethodNotAllowed = "",
            errorTypeNotAllowed = "",
        ),
        config = CardFieldConfig(type = "text", length = LengthRange(min = maxLength, max = maxLength)),
    )

    private fun securityCodeField(
        length: Int,
    ) = SecurityCodeField(
        label = "",
        placeholder = "",
        helper = "",
        tooltip = "",
        validation = emptyValidation(),
        config = CardFieldConfig(type = "text", length = LengthRange(min = length, max = length)),
    )

    private fun emptyValidation() = Validation(errorEmpty = "", errorIncomplete = "", errorInvalid = "")

    private fun emptyFieldConfig() = CardFieldConfig(type = "", length = LengthRange(min = 0, max = 0))
}
