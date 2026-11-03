package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenButtonResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenFooterResponse
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenButtonStyle
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooterButton
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class StatusScreenFooterMapperTest {
    @Test
    fun `given approved response then footer preserves back and open_pdf order`() {
        // Given
        val response = approvedResponse()

        // When
        val buttons = assertNotNull(response.toViewDataOrNull()).footer.buttons

        // Then
        assertEquals(2, buttons.size)
        assertIs<StatusScreenFooterButton.Back>(buttons[0])
        val openPdf = assertIs<StatusScreenFooterButton.OpenPdf>(buttons[1])
        assertEquals("https://www.mercadopago.com/receipt/abc", openPdf.receiptUrl)
    }

    @Test
    fun `given buttons with recognized styles then each style is mapped`() {
        // Given
        val response = approvedResponse(
            footerResponse = footer(
                buttons = listOf(
                    StatusScreenButtonResponse(
                        label = "Volver",
                        action = "back_action",
                        receiptUrl = null,
                        style = "transparent",
                    ),
                    StatusScreenButtonResponse(
                        label = "Ver factura",
                        action = "open_pdf",
                        receiptUrl = "https://x.com/r",
                        style = "loud",
                    ),
                ),
            ),
        )

        // When
        val buttons = assertNotNull(response.toViewDataOrNull()).footer.buttons

        // Then
        assertEquals(StatusScreenButtonStyle.Transparent, buttons[0].style)
        assertEquals(StatusScreenButtonStyle.Loud, buttons[1].style)
    }

    @Test
    fun `given a button with an unknown or missing style then style is null without dropping the button`() {
        // Given
        val response = approvedResponse(
            footerResponse = footer(
                buttons = listOf(
                    StatusScreenButtonResponse(
                        label = "Volver",
                        action = "back_action",
                        receiptUrl = null,
                        style = "unknown",
                    ),
                    StatusScreenButtonResponse(
                        label = "Ver factura",
                        action = "open_pdf",
                        receiptUrl = "https://x.com/r",
                        style = null,
                    ),
                ),
            ),
        )

        // When
        val buttons = assertNotNull(response.toViewDataOrNull()).footer.buttons

        // Then
        assertNull(buttons[0].style)
        assertNull(buttons[1].style)
    }

    @Test
    fun `given open_pdf button carries the receipt url in value instead of receipt_url then it is still mapped`() {
        // Given
        val response = approvedResponse(
            footerResponse = footer(
                buttons = listOf(
                    StatusScreenButtonResponse(label = "Volver", action = "back_action", receiptUrl = null),
                    StatusScreenButtonResponse(
                        label = "Ver factura",
                        action = "open_pdf",
                        receiptUrl = null,
                        value = "https://www.mercadopago.com/receipt/abc",
                    ),
                ),
            ),
        )

        // When
        val buttons = assertNotNull(response.toViewDataOrNull()).footer.buttons

        // Then
        val openPdf = assertIs<StatusScreenFooterButton.OpenPdf>(buttons[1])
        assertEquals("https://www.mercadopago.com/receipt/abc", openPdf.receiptUrl)
    }

    @Test
    fun `given footer buttons are missing then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(footerResponse = StatusScreenFooterResponse(buttons = null))

        // When
        val viewData = response.toViewDataOrNull()

        // Then
        assertNull(viewData)
    }

    @Test
    fun `given footer has zero buttons then it still maps to an empty button list`() {
        // Given
        val response = approvedResponse(footerResponse = footer(buttons = emptyList()))

        // When
        val buttons = assertNotNull(response.toViewDataOrNull()).footer.buttons

        // Then
        assertEquals(emptyList(), buttons)
    }

    @Test
    fun `given footer has more than the usual two buttons then all of them are mapped`() {
        // Given
        val response = approvedResponse(
            footerResponse = footer(
                buttons = listOf(
                    StatusScreenButtonResponse(label = "Volver", action = "back_action", receiptUrl = null),
                    StatusScreenButtonResponse(
                        label = "Ver factura",
                        action = "open_pdf",
                        receiptUrl = "https://x.com/r",
                    ),
                    StatusScreenButtonResponse(label = "Otro", action = "back_action", receiptUrl = null),
                ),
            ),
        )

        // When
        val buttons = assertNotNull(response.toViewDataOrNull()).footer.buttons

        // Then
        assertEquals(3, buttons.size)
    }

    @Test
    fun `given open_pdf button has a non-https receipt url then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(
            footerResponse = footer(
                buttons = listOf(
                    StatusScreenButtonResponse(label = "Volver", action = "back_action", receiptUrl = null),
                    StatusScreenButtonResponse(
                        label = "Ver factura",
                        action = "open_pdf",
                        receiptUrl = "http://x.com/r",
                    ),
                ),
            ),
        )

        // When
        val viewData = response.toViewDataOrNull()

        // Then
        assertNull(viewData)
    }

    @Test
    fun `given footer button has an unknown action then toViewDataOrNull returns null`() {
        // Given
        val response = approvedResponse(
            footerResponse = footer(
                buttons = listOf(
                    StatusScreenButtonResponse(label = "Volver", action = "back_action", receiptUrl = null),
                    StatusScreenButtonResponse(label = "Genérico", action = "value", receiptUrl = null),
                ),
            ),
        )

        // When
        val viewData = response.toViewDataOrNull()

        // Then
        assertNull(viewData)
    }

    @Test
    fun `given approved response without can_retry then it keeps mapping back and open_pdf`() {
        // Given
        val response = approvedResponse()

        // When
        val buttons = assertNotNull(response.toViewDataOrNull()).footer.buttons

        // Then
        assertNull(response.canRetry)
        assertIs<StatusScreenFooterButton.Back>(buttons[0])
        assertIs<StatusScreenFooterButton.OpenPdf>(buttons[1])
    }
}
