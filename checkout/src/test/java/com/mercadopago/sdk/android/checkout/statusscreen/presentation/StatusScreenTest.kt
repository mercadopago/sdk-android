package com.mercadopago.sdk.android.checkout.statusscreen.presentation

import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooter
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooterButton
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenHeader
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData
import com.mercadopago.sdk.android.checkout.statusscreen.domain.usecase.GetStatusScreenUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.foundation.theme.MercadoPagoTheme
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
internal class StatusScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val getStatusScreenUseCase = mockk<GetStatusScreenUseCase>()

    private val viewData = StatusScreenViewData(
        statusType = "approved",
        header = StatusScreenHeader(title = "Pagaste (\$ 900)", iconUrl = "https://example.com/icon.png"),
        body = listOf(
            StatusScreenBodyItem.ListItem(
                title = "Seller name",
                subtitle = null,
                imageUrl = null,
                leadingType = null,
                leadingValue = null,
            ),
        ),
        footer = StatusScreenFooter(buttons = listOf(StatusScreenFooterButton.Back(label = "Volver"))),
    )

    private val ticketPayment = OrderProcessOutput.PaymentProcessed(
        id = "PAY_1",
        status = "pending",
        statusDetail = null,
        amount = null,
        paymentMethod = OrderProcessOutput.PaymentProcessedMethod(
            id = "bolbradesco",
            type = "ticket",
            installments = null,
            barcodeContent = "23793381286000000000300000000403",
            ticketUrl = "https://www.mercadopago.com/ticket/1",
            redirectUrl = null,
        ),
    )

    private fun setScreen(
        onBack: () -> Unit = {},
        onChangePaymentMethod: () -> Unit = {},
        onOpenUrl: (String) -> Unit = {},
        paymentProcessed: OrderProcessOutput.PaymentProcessed? = null,
    ) {
        val viewModel = StatusScreenViewModel(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = null,
            sellerInfo = null,
            getStatusScreenUseCase = getStatusScreenUseCase,
            paymentProcessed = paymentProcessed,
        )
        composeTestRule.setContent {
            MercadoPagoTheme {
                StatusScreen(
                    viewModel = viewModel,
                    actions = StatusScreenActions(
                        onBack = onBack,
                        onOpenPdf = {},
                        onChangePaymentMethod = onChangePaymentMethod,
                        onCopy = {},
                        onOpenUrl = onOpenUrl,
                    ),
                )
            }
        }
    }

    @Test
    fun `given a successful fetch when the screen is shown then the ready content is displayed`() {
        // Given
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Success(viewData)

        // When
        setScreen()

        // Then
        composeTestRule.onNodeWithText("Pagaste (\$ 900)").assertIsDisplayed()
    }

    @Test
    fun `given a failed fetch when the screen is shown then the fallback is rendered without a title`() {
        // Given
        failLoad()
        var backCount = 0

        // When
        setScreen(onBack = { backCount++ })
        composeTestRule.waitForIdle()

        // Then
        composeTestRule.onNodeWithText("No pudimos cargar el detalle de tu pago").assertDoesNotExist()
        composeTestRule.onNodeWithText("Volver").assertIsDisplayed()
        assertEquals(0, backCount)
    }

    @Test
    fun `given a failed fetch with a cached ticket then the fallback shows the barcode and ticket action`() {
        // Given
        failLoad()

        // When
        setScreen(paymentProcessed = ticketPayment)
        composeTestRule.waitForIdle()

        // Then
        composeTestRule.onNodeWithText("23793381286000000000300000000403").assertIsDisplayed()
        composeTestRule.onNodeWithText("Ver factura").assertIsDisplayed()
    }

    @Test
    fun `given the fallback has a ticket action when the buyer taps it then the ticket url is opened`() {
        // Given
        failLoad()
        var openedUrl: String? = null
        setScreen(onOpenUrl = { openedUrl = it }, paymentProcessed = ticketPayment)
        composeTestRule.waitForIdle()

        // When
        composeTestRule.onNodeWithText("Ver factura").performClick()
        composeTestRule.waitForIdle()

        // Then
        assertEquals("https://www.mercadopago.com/ticket/1", openedUrl)
    }

    @Test
    fun `given the fallback is shown when the buyer taps back then onBack fires exactly once`() {
        // Given
        failLoad()
        var backCount = 0
        setScreen(onBack = { backCount++ })
        composeTestRule.waitForIdle()

        // When
        composeTestRule.onNodeWithText("Volver").performClick()
        composeTestRule.waitForIdle()

        // Then
        assertEquals(1, backCount)
    }

    @Test
    fun `given a successful fetch when the screen is shown then FLAG_SECURE is set on the window`() {
        // Given
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Success(viewData)

        // When
        setScreen()

        // Then
        val flags = composeTestRule.activity.window.attributes.flags
        assertEquals(WindowManager.LayoutParams.FLAG_SECURE, flags and WindowManager.LayoutParams.FLAG_SECURE)
    }

    @Test
    fun `given a failed fetch when the screen is shown then FLAG_SECURE is not set on the window`() {
        // Given
        failLoad()

        // When
        setScreen()
        composeTestRule.waitForIdle()

        // Then
        val flags = composeTestRule.activity.window.attributes.flags
        assertEquals(0, flags and WindowManager.LayoutParams.FLAG_SECURE)
    }

    @Test
    fun `given a rejected screen when change payment method is tapped then the retry action is invoked`() {
        // Given
        val rejectedViewData = viewData.copy(
            statusType = "rejected",
            canRetry = true,
            footer = StatusScreenFooter(
                buttons = listOf(
                    StatusScreenFooterButton.ChangePaymentMethod(label = "Pagar con otro medio"),
                    StatusScreenFooterButton.Back(label = "Volver"),
                ),
            ),
        )
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Success(rejectedViewData)
        var retryCount = 0

        // When
        setScreen(onChangePaymentMethod = { retryCount++ })
        composeTestRule.onNodeWithText("Pagar con otro medio").performClick()

        // Then
        assertEquals(1, retryCount)
    }

    private fun failLoad() {
        val error = ResponseError(code = "NO_INTERNET", message = "Sem conexão com a internet")
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Error(error)
    }
}
