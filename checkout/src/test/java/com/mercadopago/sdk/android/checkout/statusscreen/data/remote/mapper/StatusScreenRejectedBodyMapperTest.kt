package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenBodyNodeResponse
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class StatusScreenRejectedBodyMapperTest {
    @Test
    fun `given a rejected response with seller, payment and message rows then body is mapped in order`() {
        // Given
        val response = rejectedResponse(canRetry = true)

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(3, body.size)
        assertEquals("Seller name", assertIs<StatusScreenBodyItem.ListItem>(body[0]).title)
        assertEquals("Visa Crédito •••• 1234 · 3x $ 300", assertIs<StatusScreenBodyItem.ListItem>(body[1]).title)
        assertEquals(
            "Usá otro medio de pago o intentá de nuevo.",
            assertIs<StatusScreenBodyItem.Message>(body[2]).text,
        )
    }

    @Test
    fun `given a rejected body in a different order then it is mapped in the received order`() {
        // Given
        val response =
            rejectedResponse(canRetry = true, body = listOf(messageNode(), sellerListItem(), paymentListItem()))

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertIs<StatusScreenBodyItem.Message>(body[0])
        assertIs<StatusScreenBodyItem.ListItem>(body[1])
        assertIs<StatusScreenBodyItem.ListItem>(body[2])
    }

    @Test
    fun `given a rejected body with an extra known component then it is mapped too`() {
        // Given
        val response = rejectedResponse(
            canRetry = true,
            body = listOf(sellerListItem(), paymentListItem(), messageNode(), barcodeNode()),
        )

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(4, body.size)
        assertIs<StatusScreenBodyItem.Barcode>(body[3])
    }

    @Test
    fun `given a rejected body without the message then the remaining rows are mapped`() {
        // Given
        val response = rejectedResponse(canRetry = true, body = listOf(sellerListItem(), paymentListItem()))

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(2, body.size)
    }

    @Test
    fun `given a rejected body with an unknown component then only the unknown one is dropped`() {
        // Given
        val unknown = StatusScreenBodyNodeResponse(component = "MPUnknown", data = messageNode().data)
        val response = rejectedResponse(canRetry = true, body = listOf(sellerListItem(), unknown, messageNode()))

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(2, body.size)
        assertIs<StatusScreenBodyItem.ListItem>(body[0])
        assertIs<StatusScreenBodyItem.Message>(body[1])
    }

    @Test
    fun `given a rejected body with a blank or missing message text then the message is dropped`() {
        // Given
        val response = rejectedResponse(
            canRetry = true,
            body = listOf(sellerListItem(), paymentListItem(), messageNode(text = "  "), messageNode(text = null)),
        )

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(2, body.size)
        assertTrue(body.none { it is StatusScreenBodyItem.Message })
    }

    @Test
    fun `given a rejected body with an empty list then it still maps`() {
        // Given
        val response = rejectedResponse(canRetry = true, body = emptyList())

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(emptyList(), body)
    }
}
