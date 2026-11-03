package com.mercadopago.sdk.android.checkout.statusscreen.data.remote.mapper

import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.PAYMENT_STATUS_ERROR
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.PAYMENT_STATUS_REJECTED
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenButtonStyle
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFallbackCopy
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooterButton
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenHeader
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenIcon
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class StatusScreenProcessedPaymentMapperTest {
    private val copy = StatusScreenFallbackCopy(
        title = "Você pagou R\$ 1.167,50",
        ticketTitle = "Pague R\$ 900,00 com Rapipago",
        paymentSubtitle = "R\$ 1.167,50 (2x R\$ 583,75)",
        backLabel = "Volver",
        openUrlLabel = "Ver comprobante",
        barcodeLabel = "Código de pago",
        barcodeCopyLabel = "Copiar código",
        barcodeCopyFeedback = "Código copiado",
        iconRes = 7,
        rejectedHeader = StatusScreenHeader(title = "No pudimos procesar tu pago", iconRes = 3),
        errorHeader = StatusScreenHeader(title = "No fue posible completar tu pago", iconRes = 5),
        pendingHeader = StatusScreenHeader(title = "Estamos procesando tu pago", iconRes = 6),
    )

    private fun payment(
        status: String? = "processed",
        methodId: String? = "visa",
        methodType: String? = null,
        installments: Int? = 3,
        amount: String? = "900.00",
        ticketUrl: String? = null,
        redirectUrl: String? = null,
        barcodeContent: String? = null,
    ) = OrderProcessOutput.PaymentProcessed(
        id = "PAY_1",
        status = status,
        statusDetail = null,
        amount = amount?.let(::BigDecimal),
        paymentMethod = OrderProcessOutput.PaymentProcessedMethod(
            id = methodId,
            type = methodType,
            installments = installments,
            barcodeContent = barcodeContent,
            ticketUrl = ticketUrl,
            redirectUrl = redirectUrl,
        ),
    )

    @Test
    fun `given a cached payment then the header has the fallback title and the bundled icon`() {
        // Given
        val payment = payment()

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = null)

        // Then
        assertEquals("Você pagou R\$ 1.167,50", viewData.header.title)
        assertEquals(7, viewData.header.iconRes)
        assertEquals("fallback", viewData.statusType)
    }

    @Test
    fun `given an error payment then the header is the error header`() {
        // Given
        val payment = payment(status = PAYMENT_STATUS_ERROR)

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = null)

        // Then
        assertEquals(copy.errorHeader, viewData.header)
    }

    @Test
    fun `given a failed payment status then the header is the error header`() {
        // Given
        val payment = payment(status = "failed")

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = "1234")

        // Then
        assertEquals(copy.errorHeader, viewData.header)
        assertTrue(viewData.body.isEmpty())
    }

    @Test
    fun `given a failed order status then the header is the error header`() {
        // Given
        val payment = payment()

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = null, orderStatus = "failed")

        // Then
        assertEquals(copy.errorHeader, viewData.header)
    }

    @Test
    fun `given a pending order status then the header is the pending header`() {
        // Given
        val orderStatuses = listOf("pending", "processing", "waiting_payment")

        // When
        val headers = orderStatuses.map {
            copy.toStatusScreenViewData(payment = payment(), lastFourDigits = null, orderStatus = it).header
        }

        // Then
        assertEquals(List(orderStatuses.size) { copy.pendingHeader }, headers)
    }

    @Test
    fun `given an action required order then the header is the ticket title with the success icon`() {
        // Given
        val payment = payment(status = "action_required", methodId = "rapipago")

        // When
        val viewData = copy.toStatusScreenViewData(
            payment = payment,
            lastFourDigits = "1234",
            orderStatus = "action_required",
        )

        // Then
        assertEquals(StatusScreenHeader(title = "Pague R\$ 900,00 com Rapipago", iconRes = 7), viewData.header)
    }

    @Test
    fun `given an action required order then the body has only the barcode and the footer keeps the ticket action`() {
        // Given
        val payment = payment(
            status = "action_required",
            barcodeContent = "34191.75537",
            ticketUrl = "https://example.com/ticket",
        )

        // When
        val viewData = copy.toStatusScreenViewData(
            payment = payment,
            lastFourDigits = "1234",
            orderStatus = "action_required",
        )

        // Then
        assertIs<StatusScreenBodyItem.Barcode>(viewData.body.single())
        assertIs<StatusScreenFooterButton.OpenUrl>(viewData.footer.buttons.first())
        assertIs<StatusScreenFooterButton.Back>(viewData.footer.buttons.last())
    }

    @Test
    fun `given a payment with action required status and no order status then it is still a ticket`() {
        // Given
        val payment = payment(status = "action_required")

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = null)

        // Then
        assertEquals(copy.ticketTitle, viewData.header.title)
    }

    @Test
    fun `given a rejected order status then the header is the rejected header`() {
        // Given
        val orderStatus = "rejected"

        // When
        val viewData = copy.toStatusScreenViewData(payment = null, lastFourDigits = null, orderStatus = orderStatus)

        // Then
        assertEquals(copy.rejectedHeader, viewData.header)
    }

    @Test
    fun `given an approved or processed order status then the header stays the generic one`() {
        // Given
        val orderStatuses = listOf("approved", "processed")

        // When
        val headers = orderStatuses.map {
            copy.toStatusScreenViewData(payment = payment(), lastFourDigits = null, orderStatus = it).header
        }

        // Then
        val generic = StatusScreenHeader(title = copy.title, iconRes = copy.iconRes)
        assertEquals(List(orderStatuses.size) { generic }, headers)
    }

    @Test
    fun `given an error payment then the body is empty even with card data`() {
        // Given
        val payment = OrderProcessOutput.PaymentProcessed(
            id = null,
            status = PAYMENT_STATUS_ERROR,
            statusDetail = null,
            amount = null,
            paymentMethod = null,
        )

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = "1234")

        // Then
        assertTrue(viewData.body.isEmpty())
        assertIs<StatusScreenFooterButton.Back>(viewData.footer.buttons.single())
    }

    @Test
    fun `given a pending order then the body drops the card but keeps the barcode`() {
        // Given
        val payment = payment(barcodeContent = "34191.75537")

        // When
        val viewData = copy.toStatusScreenViewData(
            payment = payment,
            lastFourDigits = "1234",
            orderStatus = "pending",
        )

        // Then
        assertIs<StatusScreenBodyItem.Barcode>(viewData.body.single())
    }

    @Test
    fun `given a pending order without barcode then the body is empty even with card data`() {
        // Given
        val payment = payment()

        // When
        val viewData = copy.toStatusScreenViewData(
            payment = payment,
            lastFourDigits = "1234",
            orderStatus = "processing",
        )

        // Then
        assertTrue(viewData.body.isEmpty())
        assertIs<StatusScreenFooterButton.Back>(viewData.footer.buttons.single())
    }

    @Test
    fun `given no cached payment then the header is not the error header`() {
        // Given
        val payment = null

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = null)

        // Then
        assertEquals(copy.iconRes, viewData.header.iconRes)
    }

    @Test
    fun `given a payment method and the card digits then one row is built from them`() {
        // Given
        val payment = payment(methodId = "visa")

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = "1234")

        // Then
        val row = assertIs<StatusScreenBodyItem.ListItem>(viewData.body.single())
        assertEquals("Visa •••• 1234", row.title)
        assertEquals(copy.paymentSubtitle, row.subtitle)
    }

    @Test
    fun `given an offline payment method when the payment is rejected then no card row is built`() {
        // Given
        val payment = payment(status = "rejected", methodId = "rapipago", methodType = "ticket")

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = "1234")

        // Then
        assertTrue(viewData.body.isEmpty())
    }

    @Test
    fun `given a credit card payment method then the card row is still built`() {
        // Given
        val payment = payment(methodId = "master", methodType = "credit_card")

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = "4321")

        // Then
        val row = assertIs<StatusScreenBodyItem.ListItem>(viewData.body.single())
        assertEquals("Mastercard •••• 4321", row.title)
    }

    @Test
    fun `given a method id with underscores then the row title is a readable name`() {
        // Given
        val payment = payment(methodId = "account_money")

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = null)

        // Then
        val row = assertIs<StatusScreenBodyItem.ListItem>(viewData.body.single())
        assertEquals("Account money", row.title)
    }

    @Test
    fun `given a payment without method or amount then the body is empty`() {
        // Given
        val payment = payment(methodId = null, amount = null)
        val copyWithoutSubtitle = copy.copy(paymentSubtitle = null)

        // When
        val viewData = copyWithoutSubtitle.toStatusScreenViewData(payment = payment, lastFourDigits = null)

        // Then
        assertEquals(emptyList(), viewData.body)
    }

    @Test
    fun `given a payment with barcode content then the body adds a barcode with the digitable line`() {
        // Given
        val payment = payment(barcodeContent = "23793381286000000000300000000403")

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = null)

        // Then
        val barcode = viewData.body.filterIsInstance<StatusScreenBodyItem.Barcode>().single()
        assertEquals("23793381286000000000300000000403", barcode.content)
        assertEquals("23793381286000000000300000000403", barcode.codeFormatted)
        assertEquals("Copiar código", barcode.copyLabel)
        assertEquals("Código copiado", barcode.copyFeedback)
        assertNull(barcode.imageDataUri)
        assertEquals(StatusScreenIcon.Copy, barcode.icon)
    }

    @Test
    fun `given blank or missing barcode content then no barcode is added`() {
        // Given
        val blank = payment(barcodeContent = "   ")
        val missing = payment(barcodeContent = null)

        // When
        val blankData = copy.toStatusScreenViewData(payment = blank, lastFourDigits = null)
        val missingData = copy.toStatusScreenViewData(payment = missing, lastFourDigits = null)

        // Then
        assertTrue(blankData.body.none { it is StatusScreenBodyItem.Barcode })
        assertTrue(missingData.body.none { it is StatusScreenBodyItem.Barcode })
    }

    @Test
    fun `given a ticket payment then the barcode and the open ticket action are both offered`() {
        // Given
        val payment = payment(barcodeContent = "123456", ticketUrl = "https://www.mercadopago.com/ticket/1")

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = null)

        // Then
        assertTrue(viewData.body.any { it is StatusScreenBodyItem.Barcode })
        val open = viewData.footer.buttons.filterIsInstance<StatusScreenFooterButton.OpenUrl>().single()
        assertEquals("https://www.mercadopago.com/ticket/1", open.url)
    }

    @Test
    fun `given no cached payment then the content is the generic header with only a back button`() {
        // Given / When
        val viewData = copy.toStatusScreenViewData(payment = null, lastFourDigits = null)

        // Then
        assertEquals(emptyList(), viewData.body)
        val back = assertIs<StatusScreenFooterButton.Back>(viewData.footer.buttons.single())
        assertEquals("Volver", back.label)
        assertEquals(StatusScreenButtonStyle.Transparent, back.style)
    }

    @Test
    fun `given a payment with a ticket url then the footer adds an action that opens it`() {
        // Given
        val payment = payment(ticketUrl = "https://example.com/ticket")

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = null)

        // Then
        val buttons = viewData.footer.buttons
        val open = assertIs<StatusScreenFooterButton.OpenUrl>(buttons[0])
        assertIs<StatusScreenFooterButton.Back>(buttons[1])
        assertEquals("https://example.com/ticket", open.url)
        assertEquals("Ver comprobante", open.label)
    }

    @Test
    fun `given no ticket url then the redirect url is used`() {
        // Given
        val payment = payment(redirectUrl = "https://example.com/redirect")

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = null)

        // Then
        val open = assertIs<StatusScreenFooterButton.OpenUrl>(viewData.footer.buttons.first())
        assertEquals("https://example.com/redirect", open.url)
    }

    @Test
    fun `given a url that is not https then no open action is offered`() {
        // Given
        val payment = payment(ticketUrl = "http://insecure.example.com")

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = null)

        // Then
        assertIs<StatusScreenFooterButton.Back>(viewData.footer.buttons.single())
    }

    @Test
    fun `given different payment statuses then the native content does not change`() {
        // Given
        val statuses = listOf("processed", "in_review", "something_new", "")

        // When
        val contents = statuses.map {
            copy.toStatusScreenViewData(payment = payment(status = it), lastFourDigits = null)
        }

        // Then
        assertEquals(1, contents.distinct().size)
    }

    @Test
    fun `given any payment then the footer only ever offers back and open url`() {
        // Given
        val payments = listOf(null, payment(), payment(ticketUrl = "https://example.com/ticket"))

        // When
        val actions = payments.flatMap { copy.toStatusScreenViewData(it, null).footer.buttons }

        // Then
        assertEquals(
            true,
            actions.all { it is StatusScreenFooterButton.Back || it is StatusScreenFooterButton.OpenUrl },
        )
    }

    @Test
    fun `given a rejected payment then only the header changes to the rejected copy`() {
        // Given
        val payment = payment(status = PAYMENT_STATUS_REJECTED)

        // When
        val viewData = copy.toStatusScreenViewData(payment = payment, lastFourDigits = "1234")

        // Then
        assertEquals(StatusScreenHeader(title = "No pudimos procesar tu pago", iconRes = 3), viewData.header)
        val row = assertIs<StatusScreenBodyItem.ListItem>(viewData.body.single())
        assertEquals("Visa •••• 1234", row.title)
        assertIs<StatusScreenFooterButton.Back>(viewData.footer.buttons.single())
    }

    @Test
    fun `given a payment without status then the header is the generic copy`() {
        // Given / When
        val viewData = copy.toStatusScreenViewData(payment = payment(status = null), lastFourDigits = null)

        // Then
        assertEquals(StatusScreenHeader(title = "Você pagou R\$ 1.167,50", iconRes = 7), viewData.header)
    }
}
