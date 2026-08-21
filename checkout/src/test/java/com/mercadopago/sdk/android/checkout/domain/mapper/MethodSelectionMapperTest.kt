package com.mercadopago.sdk.android.checkout.domain.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.MethodSelectionOptionResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.MethodSelectionScreenButtonResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.MethodSelectionScreenFooterResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.MethodSelectionScreenResponse
import com.mercadopago.sdk.android.checkout.domain.model.SelectionDisplayType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class MethodSelectionMapperTest {
    private val optionResponse = MethodSelectionOptionResponse(
        id = "bolbradesco",
        name = "Boleto Bradesco",
        subtitle = "Vence em 3 dias",
        iconUrl = "https://icon.url/boleto.png",
    )

    private val footerWithButton = MethodSelectionScreenFooterResponse(
        totalLabel = "Total",
        totalAmount = "R$ 100,00",
        button = MethodSelectionScreenButtonResponse(label = "Confirmar"),
    )

    private val footerWithoutButton = MethodSelectionScreenFooterResponse(
        totalLabel = "Total",
        totalAmount = "R$ 100,00",
        button = null,
    )

    @Test
    fun `given chevron selectionType when toDomain then returns CHEVRON layoutType`() {
        val response = MethodSelectionScreenResponse(
            headerTitle = "Escolha o boleto",
            selectionType = "chevron",
            footer = footerWithButton,
            options = listOf(optionResponse),
        )

        val result = response.toDomain()

        assertEquals(SelectionDisplayType.Chevron, result.selectionType)
    }

    @Test
    fun `given radio_button selectionType when toDomain then returns RADIO_BUTTON layoutType`() {
        val response = MethodSelectionScreenResponse(
            headerTitle = "Escolha o boleto",
            selectionType = "radio_button",
            footer = footerWithButton,
            options = listOf(optionResponse),
        )

        val result = response.toDomain()

        assertEquals(SelectionDisplayType.RadioButton, result.selectionType)
    }

    @Test
    fun `given unknown selectionType when toDomain then returns RADIO_BUTTON as fallback`() {
        val response = MethodSelectionScreenResponse(
            headerTitle = "Escolha o boleto",
            selectionType = "unknown_value",
            footer = footerWithButton,
            options = listOf(optionResponse),
        )

        val result = response.toDomain()

        assertEquals(SelectionDisplayType.RadioButton, result.selectionType)
    }

    @Test
    fun `given null button in footer when toDomain then button is null`() {
        val response = MethodSelectionScreenResponse(
            headerTitle = "Escolha",
            selectionType = "chevron",
            footer = footerWithoutButton,
            options = listOf(optionResponse),
        )

        val result = response.toDomain()

        assertNull(result.footer?.button)
    }

    @Test
    fun `given button present when toDomain then button label is mapped`() {
        val response = MethodSelectionScreenResponse(
            headerTitle = "Escolha",
            selectionType = "radio_button",
            footer = footerWithButton,
            options = listOf(optionResponse),
        )

        val result = response.toDomain()

        assertNotNull(result.footer?.button)
        assertEquals("Confirmar", result.footer?.button?.label)
    }

    @Test
    fun `given option response when toDomain then all fields are mapped`() {
        val response = MethodSelectionScreenResponse(
            headerTitle = "Escolha o boleto",
            selectionType = "chevron",
            footer = footerWithoutButton,
            options = listOf(optionResponse),
        )

        val result = response.toDomain()

        assertEquals(1, result.options.size)
        with(result.options[0]) {
            assertEquals("bolbradesco", id)
            assertEquals("Boleto Bradesco", name)
            assertEquals("Vence em 3 dias", subtitle)
            assertEquals("https://icon.url/boleto.png", iconUrl)
        }
    }

    @Test
    fun `given footer response when toDomain then totalLabel and totalAmount are mapped`() {
        val response = MethodSelectionScreenResponse(
            headerTitle = "Escolha",
            selectionType = "chevron",
            footer = footerWithButton,
            options = emptyList(),
        )

        val result = response.toDomain()

        assertEquals("Total", result.footer?.totalLabel)
        assertEquals("R$ 100,00", result.footer?.totalAmount)
    }
}
