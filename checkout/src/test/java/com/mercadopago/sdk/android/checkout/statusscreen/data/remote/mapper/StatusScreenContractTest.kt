package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import com.google.gson.Gson
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenResponse
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooterButton
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenIcon
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenListItemStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class StatusScreenContractTest {
    private fun approvedCardResponse(): StatusScreenResponse {
        val json = checkNotNull(javaClass.classLoader?.getResourceAsStream("statusscreen/approved_card.json")) {
            "Missing statusscreen/approved_card.json fixture"
        }.bufferedReader().readText()
        return Gson().fromJson(json, StatusScreenResponse::class.java)
    }

    private fun rejectedRetryResponse(
        canRetryJson: String,
    ): StatusScreenResponse {
        val json = """
            {
              "status_type": "rejected",
              $canRetryJson
              "header": {
                "title": "No pudimos procesar tu pago",
                "icon": "https://http2.mlstatic.com/storage/cho_off-feedback-error"
              },
              "body": [
                { "component": "MPListItem", "data": { "title": "Seller name" } },
                { "component": "MPListItem", "data": { "title": "Visa •••• 1234", "subtitle": "3x" } },
                { "component": "MPMessage", "data": { "text": "Usá otro medio de pago." } }
              ],
              "footer": {
                "buttons": [
                  { "label": "Pagar con otro medio", "action": "change_payment_method", "style": "loud" },
                  { "label": "Volver", "action": "back_action", "style": "transparent" }
                ]
              }
            }
        """.trimIndent()
        return Gson().fromJson(json, StatusScreenResponse::class.java)
    }

    @Test
    fun `given the approved_card fixture when decoded then header title and icon match the contract`() {
        // Given
        val response = approvedCardResponse()

        // When
        val viewData = assertNotNull(response.toViewDataOrNull())

        // Then
        assertEquals("Pagaste (\$ 900)", viewData.header.title)
        assertEquals(
            "https://http2.mlstatic.com/storage/mobile-on-demand-resources/image/cho_off-feedback-sucess",
            viewData.header.iconUrl,
        )
    }

    @Test
    fun `given the approved_card fixture when decoded then body preserves seller then payment order`() {
        // Given
        val response = approvedCardResponse()

        // When
        val body = assertNotNull(response.toViewDataOrNull()).body

        // Then
        assertEquals(2, body.size)
        val seller = assertIs<StatusScreenBodyItem.ListItem>(body[0])
        assertEquals("Seller name", seller.title)
        val payment = assertIs<StatusScreenBodyItem.ListItem>(body[1])
        assertEquals("Visa Crédito •••• 1234 · 3x \$ 300", payment.title)
        assertEquals("icon", payment.leadingType)
        assertEquals("card", payment.leadingValue)
    }

    @Test
    fun `given the approved_card fixture when decoded then footer preserves back and open_pdf actions`() {
        // Given
        val response = approvedCardResponse()

        // When
        val buttons = assertNotNull(response.toViewDataOrNull()).footer.buttons

        // Then
        assertEquals(2, buttons.size)
        assertIs<StatusScreenFooterButton.Back>(buttons[0])
        val openPdf = assertIs<StatusScreenFooterButton.OpenPdf>(buttons[1])
        assertEquals("https://www.mercadopago.com/receipt/abc", openPdf.receiptUrl)
    }

    @Test
    fun `given the approved_card fixture without can_retry when decoded then canRetry is null`() {
        // Given
        val response = approvedCardResponse()

        // When
        val canRetry = response.canRetry

        // Then
        assertNull(canRetry)
    }

    @Test
    fun `given a rejected json with can_retry true when decoded then change_payment_method is mapped`() {
        // Given
        val response = rejectedRetryResponse(canRetryJson = "\"can_retry\": true,")

        // When
        val buttons = assertNotNull(response.toViewDataOrNull()).footer.buttons

        // Then
        assertEquals(true, response.canRetry)
        assertIs<StatusScreenFooterButton.ChangePaymentMethod>(buttons[0])
        assertIs<StatusScreenFooterButton.Back>(buttons[1])
    }

    @Test
    fun `given a rejected json without can_retry when decoded then the footer is mapped as received`() {
        // Given
        val response = rejectedRetryResponse(canRetryJson = "")

        // When
        val buttons = assertNotNull(response.toViewDataOrNull()).footer.buttons

        // Then
        assertNull(response.canRetry)
        assertIs<StatusScreenFooterButton.ChangePaymentMethod>(buttons[0])
        assertIs<StatusScreenFooterButton.Back>(buttons[1])
    }

    @Test
    fun `given a rejected payload with header subtitle and a simple bill row when decoded then all of them are kept`() {
        // Given
        val json = """
            {
              "status_type": "rejected",
              "can_retry": true,
              "header": {
                "title": "Tu pago fue rechazado",
                "subtitle": "Santander Crédito •••• 1234",
                "icon": "https://http2.mlstatic.com/storage/cho_off-feedback-error"
              },
              "body": [
                {
                  "component": "MPListItem",
                  "data": {
                    "title": "N.º de operación 1234567890",
                    "leading_type": "icon",
                    "leading_value": "bill",
                    "style": "simple"
                  }
                }
              ],
              "footer": {
                "buttons": [
                  { "label": "Elegir otro medio", "action": "change_payment_method", "style": "loud" },
                  { "label": "Volver", "action": "back_action", "style": "transparent" }
                ]
              }
            }
        """.trimIndent()
        val response = Gson().fromJson(json, StatusScreenResponse::class.java)

        // When
        val viewData = assertNotNull(response.toViewDataOrNull())

        // Then
        assertEquals("Santander Crédito •••• 1234", viewData.header.subtitle)
        val row = assertIs<StatusScreenBodyItem.ListItem>(viewData.body.single())
        assertEquals("N.º de operación 1234567890", row.title)
        assertEquals(StatusScreenListItemStyle.Simple, row.style)
        assertEquals(StatusScreenIcon.Bill, row.leadingIcon)
    }
}
