package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenBodyNodeResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenButtonResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenHeaderResponse
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooterButton
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class StatusScreenResponseMapperTest {
    @Test
    fun `given approved response then header title and icon are mapped`() {
        // Given
        val response = approvedResponse()

        // When
        val viewData = response.toViewDataOrNull()

        // Then
        val safeViewData = assertNotNull(viewData)
        assertEquals("approved", safeViewData.statusType)
        val header = safeViewData.header
        assertEquals("Pagaste ($ 900)", header.title)
        assertEquals(
            "https://http2.mlstatic.com/storage/cho_off-feedback-success",
            header.iconUrl,
        )
    }

    @Test
    fun `given status_type is unknown then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(statusType = "something_else")

        // When / Then
        assertNull(response.toViewDataOrNull())
    }

    @Test
    fun `given status_type is null then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(statusType = null)

        // When / Then
        assertNull(response.toViewDataOrNull())
    }

    @Test
    fun `given status_type is not approved, pending or rejected then toViewDataOrNull returns null`() {
        // Given
        val responses = listOf("unknown", "", null).map { approvedResponse(statusType = it) }

        // When / Then
        responses.forEach { assertNull(it.toViewDataOrNull()) }
    }

    @Test
    fun `given header is missing then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(header = null)

        // When / Then
        assertNull(response.toViewDataOrNull())
    }

    @Test
    fun `given header icon is not https then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(
            header = StatusScreenHeaderResponse(title = "Pagaste ($ 900)", icon = "http://example.com/icon.png"),
        )

        // When / Then
        assertNull(response.toViewDataOrNull())
    }

    @Test
    fun `given header icon points to a loopback host then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(
            header = StatusScreenHeaderResponse(title = "Pagaste ($ 900)", icon = "https://127.0.0.1/icon.png"),
        )

        // When / Then
        assertNull(response.toViewDataOrNull())
    }

    @Test
    fun `given header icon points to a private network host then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(
            header = StatusScreenHeaderResponse(title = "Pagaste ($ 900)", icon = "https://192.168.0.10/icon.png"),
        )

        // When / Then
        assertNull(response.toViewDataOrNull())
    }

    @Test
    fun `given header icon points to the cloud metadata host then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(
            header = StatusScreenHeaderResponse(title = "Pagaste ($ 900)", icon = "https://169.254.169.254/icon.png"),
        )

        // When / Then
        assertNull(response.toViewDataOrNull())
    }

    @Test
    fun `given header icon points to an ipv6 link-local host then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(
            header = StatusScreenHeaderResponse(title = "Pagaste ($ 900)", icon = "https://[fe80::1]/icon.png"),
        )

        // When / Then
        assertNull(response.toViewDataOrNull())
    }

    @Test
    fun `given header icon points to an ipv6 private host then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(
            header = StatusScreenHeaderResponse(title = "Pagaste ($ 900)", icon = "https://[fc00::1]/icon.png"),
        )

        // When / Then
        assertNull(response.toViewDataOrNull())
    }

    @Test
    fun `given header icon points to localhost then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(
            header = StatusScreenHeaderResponse(title = "Pagaste ($ 900)", icon = "https://localhost/icon.png"),
        )

        // When / Then
        assertNull(response.toViewDataOrNull())
    }

    @Test
    fun `given header with subtitle then subtitle is mapped`() {
        // Given
        val response = approvedResponse(
            header = StatusScreenHeaderResponse(
                title = "Tu pago fue rechazado",
                icon = "https://example.com/icon.png",
                subtitle = "Santander Crédito •••• 1234",
            ),
        )

        // When
        val header = assertNotNull(response.toViewDataOrNull()).header

        // Then
        assertEquals("Santander Crédito •••• 1234", header.subtitle)
    }

    @Test
    fun `given header subtitle is blank then subtitle becomes null without dropping the header`() {
        // Given
        val response = approvedResponse(
            header = StatusScreenHeaderResponse(
                title = "Tu pago fue rechazado",
                icon = "https://example.com/icon.png",
                subtitle = " ",
            ),
        )

        // When
        val header = assertNotNull(response.toViewDataOrNull()).header

        // Then
        assertNull(header.subtitle)
        assertEquals("Tu pago fue rechazado", header.title)
    }

    @Test
    fun `given header title is blank then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(
            header = StatusScreenHeaderResponse(title = " ", icon = "https://example.com/icon.png"),
        )

        // When / Then
        assertNull(response.toViewDataOrNull())
    }

    @Test
    fun `given a body node with MPMessage then it maps to a Message with its text`() {
        // Given
        val response = approvedResponse(body = listOf(messageNode()), statusType = "pending")

        // When
        val viewData = response.toViewDataOrNull()

        // Then
        val message = assertIs<StatusScreenBodyItem.Message>(assertNotNull(viewData).body.single())
        assertEquals("Estamos revisando tu pago", message.text)
    }

    @Test
    fun `given an MPMessage with blank or missing text then it is dropped without invalidating the rest`() {
        // Given
        val response = approvedResponse(
            body = listOf(messageNode(text = " "), messageNode(text = null), sellerListItem()),
            statusType = "pending",
        )

        // When
        val viewData = response.toViewDataOrNull()

        // Then
        assertIs<StatusScreenBodyItem.ListItem>(assertNotNull(viewData).body.single())
    }

    @Test
    fun `given an unknown component alongside an MPMessage then only the unknown one is dropped`() {
        // Given
        val unknown = StatusScreenBodyNodeResponse(component = "MPUnknown", data = null)
        val response = approvedResponse(body = listOf(unknown, messageNode()), statusType = "pending")

        // When
        val viewData = response.toViewDataOrNull()

        // Then
        assertIs<StatusScreenBodyItem.Message>(assertNotNull(viewData).body.single())
    }

    @Test
    fun `given status_type is pending then toViewDataOrNull maps the response`() {
        // Given
        val response = approvedResponse(statusType = "pending")

        // When
        val viewData = response.toViewDataOrNull()

        // Then
        assertEquals("pending", assertNotNull(viewData).statusType)
    }

    @Test
    fun `given a pending response then body and footer are decoded with the same rules as any other status`() {
        // Given
        val response = approvedResponse(
            body = listOf(messageNode(), sellerListItem(), barcodeNode()),
            statusType = "pending",
        )

        // When
        val viewData = response.toViewDataOrNull()

        // Then
        val body = assertNotNull(viewData).body
        assertIs<StatusScreenBodyItem.Message>(body[0])
        assertIs<StatusScreenBodyItem.ListItem>(body[1])
        assertIs<StatusScreenBodyItem.Barcode>(body[2])
        assertIs<StatusScreenFooterButton.OpenPdf>(viewData.footer.buttons[1])
    }

    @Test
    fun `given a pending response with an empty body then it still maps`() {
        // Given
        val response = approvedResponse(body = emptyList(), statusType = "pending")

        // When
        val viewData = response.toViewDataOrNull()

        // Then
        assertEquals(emptyList(), assertNotNull(viewData).body)
    }

    @Test
    fun `given a pending footer with an unknown action then toViewDataOrNull returns null`() {
        // Given
        val retry = StatusScreenButtonResponse(label = "Reintentar", action = "retry", receiptUrl = null)
        val response = approvedResponse(footerResponse = footer(buttons = listOf(retry)), statusType = "pending")

        // When
        val viewData = response.toViewDataOrNull()

        // Then
        assertNull(viewData)
    }
}
