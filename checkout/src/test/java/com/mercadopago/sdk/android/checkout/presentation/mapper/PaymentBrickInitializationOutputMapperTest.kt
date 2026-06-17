package com.mercadopago.sdk.android.checkout.presentation.mapper

import com.mercadopago.sdk.android.checkout.domain.model.CardDataOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickFooterOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentSectionOutput
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeOutput
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class PaymentBrickInitializationOutputMapperTest {
    private fun buildOutput(
        headerTitle: String = "Elegí cómo pagar",
        sections: List<PaymentSectionOutput> = emptyList(),
    ) = PaymentBrickInitializationOutput(
        headerTitle = headerTitle,
        sections = sections,
        footer = PaymentBrickFooterOutput(totalLabel = "Total", totalAmount = "$ 500"),
    )

    @Test
    fun `given output then header title maps to screen state title`() {
        val state = buildOutput(headerTitle = "Choose payment").toScreenState()

        assertEquals("Choose payment", state.title)
    }

    @Test
    fun `given output then sections and options count are preserved`() {
        val output = buildOutput(
            sections = listOf(
                PaymentSectionOutput(
                    title = "Section",
                    methods = listOf(
                        PaymentMethodOutput(type = "new_card", title = "New card"),
                        PaymentMethodOutput(type = "ticket", title = "Cash"),
                    ),
                ),
            ),
        )

        val state = output.toScreenState()

        assertEquals(1, state.sections.size)
        assertEquals("Section", state.sections.first().title)
        assertEquals(2, state.sections.first().options.size)
    }

    @Test
    fun `given saved card method then id uses card data id`() {
        val output = buildOutput(
            sections = listOf(
                PaymentSectionOutput(
                    title = "Section",
                    methods = listOf(
                        PaymentMethodOutput(
                            type = "saved_card",
                            title = "Visa **** 1234",
                            cardData = savedCardData(id = "CARD_ID_123"),
                        ),
                    ),
                ),
            ),
        )

        val option = output.toScreenState().sections.first().options.first()

        assertEquals("CARD_ID_123", option.id)
        assertEquals("Visa **** 1234", option.title)
    }

    @Test
    fun `given method without card data then id uses method type`() {
        val output = buildOutput(
            sections = listOf(
                PaymentSectionOutput(
                    title = "Section",
                    methods = listOf(
                        PaymentMethodOutput(type = "new_card", title = "New card"),
                    ),
                ),
            ),
        )

        val option = output.toScreenState().sections.first().options.first()

        assertEquals("new_card", option.id)
    }

    @Test
    fun `given method with icon and subtitle then thumbnail url and description are mapped`() {
        val output = buildOutput(
            sections = listOf(
                PaymentSectionOutput(
                    title = "Section",
                    methods = listOf(
                        PaymentMethodOutput(
                            type = "ticket",
                            title = "Cash",
                            subtitle = "Pago Fácil",
                            iconUrl = "https://example.com/icon.png",
                        ),
                    ),
                ),
            ),
        )

        val option = output.toScreenState().sections.first().options.first()

        assertEquals("https://example.com/icon.png", option.thumbnailUrl)
        assertEquals("Pago Fácil", option.description)
    }

    @Test
    fun `given method without subtitle then description is null`() {
        val output = buildOutput(
            sections = listOf(
                PaymentSectionOutput(
                    title = "Section",
                    methods = listOf(
                        PaymentMethodOutput(type = "new_card", title = "New card", subtitle = null),
                    ),
                ),
            ),
        )

        val option = output.toScreenState().sections.first().options.first()

        assertNull(option.description)
    }

    @Test
    fun `given output then footer state is null (mapped in A8)`() {
        val state = buildOutput().toScreenState()

        assertNull(state.footerState)
    }

    @Test
    fun `given output then isLoading and isError are false`() {
        val state = buildOutput().toScreenState()

        assertEquals(false, state.isLoading)
        assertEquals(false, state.isError)
    }

    private fun savedCardData(
        id: String = "card_id",
    ) = CardDataOutput(
        id = id,
        bin = "503143",
        lastFourDigits = "1234",
        paymentMethodId = "visa",
        paymentTypeId = "credit_card",
        issuerId = 1,
        securityCode = SecurityCodeOutput(length = 3, screen = null),
        installments = null,
    )
}
