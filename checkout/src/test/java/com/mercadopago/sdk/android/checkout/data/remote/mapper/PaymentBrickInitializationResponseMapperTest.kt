package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardData
import com.mercadopago.sdk.android.checkout.data.remote.response.Installments
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsHeader
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickFooter
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickInitializationResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentMethod
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentSection
import com.mercadopago.sdk.android.checkout.data.remote.response.Quota
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCode
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeButton
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeField
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeHeader
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeScreen
import com.mercadopago.sdk.android.checkout.data.remote.response.TicketOption
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class PaymentBrickInitializationResponseMapperTest {
    private fun buildFullResponse() = PaymentBrickInitializationResponse(
        headerTitle = "Elegí cómo pagar",
        sections = listOf(
            PaymentSection(
                title = "Otros medios de pago",
                methods = listOf(
                    savedCardWithCvvAndInstallments(),
                    savedCardWithoutCvvOrInstallments(),
                    ticketMethod(),
                    newCardMethod(),
                ),
            ),
        ),
        footer = PaymentBrickFooter(totalLabel = "Total", totalAmount = "$ 188.000"),
    )

    private fun savedCardWithCvvAndInstallments() = PaymentMethod(
        type = "saved_card",
        title = "Visa **** 1234",
        subtitle = "Visa · Crédito",
        iconUrl = "https://example.com/visa.png",
        cardData = CardData(
            id = "123456",
            bin = "503143",
            lastFourDigits = "1234",
            paymentMethodId = "visa",
            paymentTypeId = "credit_card",
            issuerId = 1,
            securityCode = SecurityCode(
                length = 3,
                screen = SecurityCodeScreen(
                    header = SecurityCodeHeader(title = "Ingresá el código de seguridad"),
                    field = SecurityCodeField(
                        label = "Código de seguridad",
                        placeholder = "Ej.: 123",
                        helper = "Está en el reverso de tu tarjeta.",
                    ),
                    button = SecurityCodeButton(label = "Continuar"),
                ),
            ),
            installments = Installments(
                header = InstallmentsHeader(title = "Elegí las cuotas"),
                totalLabel = "Total",
                payButtonLabel = "Pagar",
                selectionType = "radio_button",
                quotas = listOf(
                    Quota(
                        installments = 1,
                        installmentAmount = BigDecimal("500.00"),
                        totalAmount = BigDecimal("500.00"),
                        primaryLabel = "1x $ 500,00",
                        secondaryLabel = "",
                        state = "none",
                    ),
                    Quota(
                        installments = 3,
                        installmentAmount = BigDecimal("170.00"),
                        totalAmount = BigDecimal("510.00"),
                        primaryLabel = "3x $ 170,00",
                        secondaryLabel = "$ 510,00",
                        state = "interest_free",
                    ),
                ),
            ),
        ),
    )

    private fun savedCardWithoutCvvOrInstallments() = PaymentMethod(
        type = "saved_card",
        title = "Master **** 5678",
        subtitle = "Mastercard · Débito",
        iconUrl = "https://example.com/master.png",
        cardData = CardData(
            id = "789012",
            bin = "516105",
            lastFourDigits = "5678",
            paymentMethodId = "master",
            paymentTypeId = "debit_card",
            issuerId = 2,
            securityCode = SecurityCode(length = 3, screen = null),
            installments = null,
        ),
    )

    private fun ticketMethod() = PaymentMethod(
        type = "ticket",
        title = "Efectivo",
        subtitle = "Pago Fácil y Rapipago",
        iconUrl = null,
        options = listOf(
            TicketOption(id = "pagofacil", name = "Pago Fácil", iconUrl = "https://example.com/pagofacil.png"),
            TicketOption(id = "rapipago", name = "Rapipago", iconUrl = "https://example.com/rapipago.png"),
        ),
    )

    private fun newCardMethod() = PaymentMethod(
        type = "new_card",
        title = "Nueva tarjeta",
        subtitle = "Crédito y débito",
        iconUrl = "https://example.com/new_card.png",
    )

    @Test
    fun `given full response then header title and footer are mapped correctly`() {
        val output = buildFullResponse().toDomain()

        assertEquals("Elegí cómo pagar", output.headerTitle)
        assertEquals("Total", output.footer.totalLabel)
        assertEquals("$ 188.000", output.footer.totalAmount)
    }

    @Test
    fun `given full response then sections and methods count are preserved`() {
        val output = buildFullResponse().toDomain()

        assertEquals(1, output.sections.size)
        assertEquals("Otros medios de pago", output.sections.first().title)
        assertEquals(4, output.sections.first().methods.size)
    }

    @Test
    fun `given saved card with cvv screen and installments then all card data fields are mapped`() {
        val method = buildFullResponse().toDomain().sections.first().methods[0]

        assertEquals("saved_card", method.type)
        assertEquals("Visa **** 1234", method.title)
        assertEquals("Visa · Crédito", method.subtitle)
        assertEquals("https://example.com/visa.png", method.iconUrl)
        assertNull(method.options)

        val card = assertNotNull(method.cardData)
        assertEquals("123456", card.id)
        assertEquals("503143", card.bin)
        assertEquals("1234", card.lastFourDigits)
        assertEquals("visa", card.paymentMethodId)
        assertEquals("credit_card", card.paymentTypeId)
        assertEquals(1, card.issuerId)
    }

    @Test
    fun `given saved card with cvv screen then security code screen is mapped`() {
        val card = assertNotNull(buildFullResponse().toDomain().sections.first().methods[0].cardData)

        assertEquals(3, card.securityCode.length)
        val screen = assertNotNull(card.securityCode.screen)
        assertEquals("Ingresá el código de seguridad", screen.headerTitle)
        assertEquals("Continuar", screen.buttonLabel)
        assertEquals("Código de seguridad", screen.field.label)
        assertEquals("Ej.: 123", screen.field.placeholder)
        assertEquals("Está en el reverso de tu tarjeta.", screen.field.helper)
    }

    @Test
    fun `given saved card with installments then installment data is mapped`() {
        val card = assertNotNull(buildFullResponse().toDomain().sections.first().methods[0].cardData)
        val installments = assertNotNull(card.installments)

        assertEquals("Elegí las cuotas", installments.header.title)
        assertEquals("Total", installments.totalLabel)
        assertEquals("Pagar", installments.payButtonLabel)
        assertEquals("radio_button", installments.selectionType)
        assertEquals(2, installments.quotas.size)

        val quota = installments.quotas[1]
        assertEquals(3, quota.installments)
        assertEquals(BigDecimal("170.00"), quota.installmentAmount)
        assertEquals(BigDecimal("510.00"), quota.totalAmount)
        assertEquals("3x $ 170,00", quota.primaryLabel)
        assertEquals("$ 510,00", quota.secondaryLabel)
        assertEquals("interest_free", quota.state)
    }

    @Test
    fun `given saved card without cvv screen and without installments then optionals are null`() {
        val card = assertNotNull(buildFullResponse().toDomain().sections.first().methods[1].cardData)

        assertEquals(3, card.securityCode.length)
        assertNull(card.securityCode.screen)
        assertNull(card.installments)
    }

    @Test
    fun `given ticket method then options are mapped and card data is null`() {
        val method = buildFullResponse().toDomain().sections.first().methods[2]

        assertEquals("ticket", method.type)
        assertEquals("Efectivo", method.title)
        assertNull(method.cardData)

        val options = assertNotNull(method.options)
        assertEquals(2, options.size)
        assertEquals("pagofacil", options[0].id)
        assertEquals("Pago Fácil", options[0].name)
        assertEquals("https://example.com/pagofacil.png", options[0].iconUrl)
    }

    @Test
    fun `given new card method then neither card data nor options are present`() {
        val method = buildFullResponse().toDomain().sections.first().methods[3]

        assertEquals("new_card", method.type)
        assertEquals("Nueva tarjeta", method.title)
        assertEquals("Crédito y débito", method.subtitle)
        assertNull(method.cardData)
        assertNull(method.options)
    }

    @Test
    fun `given method with null subtitle then subtitle is null in output`() {
        val response = PaymentBrickInitializationResponse(
            headerTitle = "Title",
            sections = listOf(
                PaymentSection(
                    title = "Section",
                    methods = listOf(
                        PaymentMethod(type = "new_card", title = "New card", subtitle = null),
                    ),
                ),
            ),
            footer = PaymentBrickFooter(totalLabel = "Total", totalAmount = "$ 0"),
        )

        assertNull(response.toDomain().sections.first().methods.first().subtitle)
    }
}
