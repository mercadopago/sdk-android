package com.mercadopago.sdk.android.checkout.statusscreen.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooter
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooterButton
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenHeader
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenIcon
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenListItemStyle
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSegmentState
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenSubtitleSegment
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
internal class StatusScreenContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val sellerRow = StatusScreenBodyItem.ListItem(
        title = "Seller name",
        subtitle = null,
        imageUrl = "https://example.com/seller.png",
        leadingType = null,
        leadingValue = null,
    )

    private val paymentRow = StatusScreenBodyItem.ListItem(
        title = "Visa Crédito •••• 1234 · 3x \$ 300",
        subtitle = "\$ 900 sin interés",
        imageUrl = null,
        leadingType = "icon",
        leadingValue = "card",
    )

    private val barcode = StatusScreenBodyItem.Barcode(
        content = "34191.75537",
        codeFormatted = "34191.75537",
        copyLabel = "Copiar código",
        copyFeedback = "Código copiado",
        imageDataUri = null,
        label = null,
        icon = null,
    )

    private fun setContent(
        body: List<StatusScreenBodyItem> = listOf(sellerRow, paymentRow),
        headerSubtitle: String? = null,
        buttons: List<StatusScreenFooterButton> = listOf(StatusScreenFooterButton.Back(label = "Volver")),
        onBack: () -> Unit = {},
        onOpenPdf: (String) -> Unit = {},
        onCopy: () -> Unit = {},
        onChangePaymentMethod: () -> Unit = {},
        onOpenUrl: (String) -> Unit = {},
    ) {
        composeTestRule.setContent {
            MercadoPagoTheme {
                StatusScreenContent(
                    data = StatusScreenViewData(
                        statusType = "approved",
                        header = StatusScreenHeader(
                            title = "Pagaste (\$ 900)",
                            iconUrl = "https://example.com/icon.png",
                            subtitle = headerSubtitle,
                        ),
                        body = body,
                        footer = StatusScreenFooter(buttons = buttons),
                    ),
                    onBack = onBack,
                    onOpenPdf = onOpenPdf,
                    onChangePaymentMethod = onChangePaymentMethod,
                    onCopy = onCopy,
                    onOpenUrl = onOpenUrl,
                )
            }
        }
    }

    @Test
    fun `given an approved status when content is rendered then header and ordered two-row body are displayed`() {
        // Given / When
        setContent()

        // Then
        composeTestRule.onNodeWithText("Pagaste (\$ 900)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Seller name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Visa Crédito •••• 1234 · 3x \$ 300").assertIsDisplayed()
    }

    @Test
    fun `given a pending header only when content is rendered then the title and back button are displayed`() {
        // Given / When
        setContent(body = emptyList())

        // Then
        composeTestRule.onNodeWithText("Pagaste (\$ 900)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Volver").assertIsDisplayed()
    }

    @Test
    fun `given a pending message followed by a row when content is rendered then both are displayed`() {
        // Given / When
        setContent(body = listOf(StatusScreenBodyItem.Message(text = "Estamos revisando tu pago"), sellerRow))

        // Then
        composeTestRule.onNodeWithText("Estamos revisando tu pago").assertIsDisplayed()
        composeTestRule.onNodeWithText("Seller name").assertIsDisplayed()
    }

    @Test
    fun `given an open url button when it is clicked then onOpenUrl receives the url`() {
        // Given
        var openedUrl: String? = null
        setContent(
            body = emptyList(),
            buttons = listOf(
                StatusScreenFooterButton.Back(label = "Volver"),
                StatusScreenFooterButton.OpenUrl(label = "Ver comprobante", url = "https://example.com/ticket"),
            ),
            onOpenUrl = { openedUrl = it },
        )

        // When
        composeTestRule.onNodeWithText("Ver comprobante").performClick()

        // Then
        kotlin.test.assertEquals("https://example.com/ticket", openedUrl)
    }

    @Test
    fun `given a message body item when content is rendered then its text is displayed`() {
        // Given / When
        setContent(body = listOf(StatusScreenBodyItem.Message(text = "Estamos revisando tu pago")))

        // Then
        composeTestRule.onNodeWithText("Estamos revisando tu pago").assertIsDisplayed()
    }

    @Test
    fun `given a list item with subtitle segments when content is rendered then the whole subtitle is displayed`() {
        // Given
        val rowWithSegments = paymentRow.copy(
            subtitle = null,
            subtitleSegments = listOf(
                StatusScreenSubtitleSegment(text = "\$ 1.000 (1x \$ 1.000 "),
                StatusScreenSubtitleSegment(text = "sin interés", state = StatusScreenSegmentState.Success),
                StatusScreenSubtitleSegment(text = ")"),
            ),
        )

        // When
        setContent(body = listOf(sellerRow, rowWithSegments))

        // Then
        composeTestRule.onNodeWithText("\$ 1.000 (1x \$ 1.000 sin interés)").assertIsDisplayed()
    }

    @Test
    fun `given a barcode body item when content is rendered then the digitable line is displayed`() {
        // Given / When
        setContent(body = listOf(sellerRow, paymentRow, barcode))

        // Then
        composeTestRule.onNodeWithText("34191.75537").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun `given a barcode without an icon when content is rendered then the copy action is hidden`() {
        // Given / When
        setContent(body = listOf(sellerRow, paymentRow, barcode))

        // Then
        composeTestRule.onNodeWithContentDescription("Copiar código").assertDoesNotExist()
    }

    @Test
    fun `given a barcode with a Copy icon when content is rendered then the copy action is displayed`() {
        // Given
        val barcodeWithIcon = barcode.copy(icon = StatusScreenIcon.Copy)

        // When
        setContent(body = listOf(sellerRow, paymentRow, barcodeWithIcon))

        // Then
        composeTestRule.onNodeWithContentDescription("Copiar código").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun `given only the required back button when footer is rendered then open_pdf action is not displayed`() {
        // Given / When
        setContent(buttons = listOf(StatusScreenFooterButton.Back(label = "Volver")))

        // Then
        composeTestRule.onNodeWithText("Volver").assertIsDisplayed()
    }

    @Test
    fun `given both footer actions when open_pdf is tapped then the receipt url is not dropped`() {
        // Given
        var sharedUrl: String? = null
        setContent(
            buttons = listOf(
                StatusScreenFooterButton.Back(label = "Volver"),
                StatusScreenFooterButton.OpenPdf(label = "Ver factura", receiptUrl = "https://example.com/receipt.pdf"),
            ),
            onOpenPdf = { url -> sharedUrl = url },
        )

        // When
        composeTestRule.onNodeWithText("Ver factura").performClick()

        // Then
        kotlin.test.assertEquals("https://example.com/receipt.pdf", sharedUrl)
    }

    @Test
    fun `given the back button when it is tapped then onBack is invoked`() {
        // Given
        var backTapped = false
        setContent(onBack = { backTapped = true })

        // When
        composeTestRule.onNodeWithText("Volver").performClick()

        // Then
        kotlin.test.assertTrue(backTapped)
    }

    @Test
    fun `given change_payment_method and back when footer is rendered then both buttons are displayed`() {
        // Given / When
        setContent(
            buttons = listOf(
                StatusScreenFooterButton.ChangePaymentMethod(label = "Pagar con otro medio"),
                StatusScreenFooterButton.Back(label = "Volver"),
            ),
        )

        // Then
        composeTestRule.onNodeWithText("Pagar con otro medio").assertIsDisplayed()
        composeTestRule.onNodeWithText("Volver").assertIsDisplayed()
    }

    @Test
    fun `given the change_payment_method button when it is tapped then onChangePaymentMethod is invoked`() {
        // Given
        var changeTapped = false
        var backTapped = false
        setContent(
            buttons = listOf(
                StatusScreenFooterButton.ChangePaymentMethod(label = "Pagar con otro medio"),
                StatusScreenFooterButton.Back(label = "Volver"),
            ),
            onBack = { backTapped = true },
            onChangePaymentMethod = { changeTapped = true },
        )

        // When
        composeTestRule.onNodeWithText("Pagar con otro medio").performClick()

        // Then
        kotlin.test.assertTrue(changeTapped)
        kotlin.test.assertFalse(backTapped)
    }

    @Test
    fun `given a rejected footer without change_payment_method when rendered then only back is displayed`() {
        // Given / When
        setContent(buttons = listOf(StatusScreenFooterButton.Back(label = "Volver")))

        // Then
        composeTestRule.onNodeWithText("Volver").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pagar con otro medio").assertDoesNotExist()
    }

    @Test
    fun `given change_payment_method then back when footer is rendered then buttons keep the given order`() {
        // Given / When
        setContent(
            buttons = listOf(
                StatusScreenFooterButton.ChangePaymentMethod(label = "Pagar con otro medio"),
                StatusScreenFooterButton.Back(label = "Volver"),
            ),
        )

        // Then
        val changeTop = composeTestRule.onNodeWithText("Pagar con otro medio").fetchSemanticsNode().boundsInRoot.top
        val backTop = composeTestRule.onNodeWithText("Volver").fetchSemanticsNode().boundsInRoot.top
        kotlin.test.assertTrue(changeTop < backTop)
    }

    @Test
    fun `given a header subtitle when content is rendered then the subtitle is displayed`() {
        // Given / When
        setContent(headerSubtitle = "Santander Crédito •••• 1234")

        // Then
        composeTestRule.onNodeWithText("Santander Crédito •••• 1234").assertIsDisplayed()
    }

    @Test
    fun `given a simple row with a Bill leading icon when content is rendered then the row title is displayed`() {
        // Given
        val billRow = StatusScreenBodyItem.ListItem(
            title = "N.º de operación 1234567890",
            subtitle = null,
            imageUrl = null,
            leadingType = "icon",
            leadingValue = "bill",
            leadingIcon = StatusScreenIcon.Bill,
            style = StatusScreenListItemStyle.Simple,
        )

        // When
        setContent(body = listOf(billRow))

        // Then
        composeTestRule.onNodeWithText("N.º de operación 1234567890").assertIsDisplayed()
    }
}
