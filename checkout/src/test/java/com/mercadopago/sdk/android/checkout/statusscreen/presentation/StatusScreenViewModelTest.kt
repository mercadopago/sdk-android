package com.mercadopago.sdk.android.checkout.statusscreen.presentation

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenCloseEventData
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenCloseSource
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenLoadEventData
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenLoadOutcome
import com.mercadopago.sdk.android.checkout.analytics.StatusScreenViewEventData
import com.mercadopago.sdk.android.checkout.analytics.metricStatusScreenClose
import com.mercadopago.sdk.android.checkout.analytics.metricStatusScreenContractViolation
import com.mercadopago.sdk.android.checkout.analytics.metricStatusScreenLoad
import com.mercadopago.sdk.android.checkout.analytics.metricStatusScreenRender
import com.mercadopago.sdk.android.checkout.analytics.metricStatusScreenRetry
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenBodyItem
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooter
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenFooterButton
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenHeader
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.invalidStatusScreenResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.domain.usecase.GetStatusScreenUseCase
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class StatusScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getStatusScreenUseCase = mockk<GetStatusScreenUseCase>()

    @After
    fun tearDown() {
        unmockkObject(MPAnalytics.Companion)
    }

    private val viewData = StatusScreenViewData(
        statusType = "approved",
        header = StatusScreenHeader(title = "Pagaste (\$ 900)", iconUrl = "https://example.com/icon.png"),
        body = listOf(
            StatusScreenBodyItem.ListItem(
                title = "Seller name",
                subtitle = null,
                imageUrl = "https://example.com/seller.png",
                leadingType = null,
                leadingValue = null,
            ),
            StatusScreenBodyItem.ListItem(
                title = "Visa Crédito •••• 1234 · 3x \$ 300",
                subtitle = "\$ 900 sin interés",
                imageUrl = null,
                leadingType = "icon",
                leadingValue = "card",
            ),
        ),
        footer = StatusScreenFooter(buttons = listOf(StatusScreenFooterButton.Back(label = "Volver"))),
    )

    private fun makeViewModel() = StatusScreenViewModel(
        clientToken = "token-abc",
        orderId = "order-123",
        lastFourDigits = null,
        sellerInfo = null,
        getStatusScreenUseCase = getStatusScreenUseCase,
    )

    @Test
    fun `given a cached processed payment when the ViewModel is constructed then it is available`() {
        // Given
        val paymentProcessed = OrderProcessOutput.PaymentProcessed(
            id = "PAY_1",
            status = "processed",
            statusDetail = "accredited",
            amount = BigDecimal("900.00"),
            paymentMethod = OrderProcessOutput.PaymentProcessedMethod(
                id = "visa",
                type = "credit_card",
                installments = 3,
                barcodeContent = null,
                ticketUrl = null,
                redirectUrl = null,
            ),
        )

        // When
        val viewModel = StatusScreenViewModel(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = "1234",
            sellerInfo = null,
            getStatusScreenUseCase = getStatusScreenUseCase,
            paymentProcessed = paymentProcessed,
        )

        // Then
        assertSame(paymentProcessed, viewModel.paymentProcessed)
    }

    @Test
    fun `given no cached processed payment when the ViewModel is constructed then none is available`() {
        // Given / When
        val viewModel = makeViewModel()

        // Then
        assertNull(viewModel.paymentProcessed)
    }

    @Test
    fun `given a new ViewModel when no load is triggered then state is Idle`() {
        // Given / When
        val viewModel = makeViewModel()

        // Then
        assertIs<StatusScreenState.Idle>(viewModel.state.value)
    }

    @Test
    fun `given a successful fetch when load is called then state becomes Ready`() = runTest {
        // Given
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Success(viewData)
        val viewModel = makeViewModel()

        // When
        viewModel.load()

        // Then
        val state = viewModel.state.value
        assertIs<StatusScreenState.Ready>(state)
        assertIs<StatusScreenViewData>(state.data)
    }

    @Test
    fun `given a cached payment when the fetch fails then Unavailable carries the fallback inputs`() = runTest {
        // Given
        val cachedPayment = OrderProcessOutput.PaymentProcessed(
            id = "PAY_1",
            status = "pending",
            statusDetail = null,
            amount = BigDecimal("900.00"),
            paymentMethod = null,
        )
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Error(ResponseError(code = "NO_INTERNET", message = "Sem conexão com a internet"))
        val viewModel = StatusScreenViewModel(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = "1234",
            sellerInfo = null,
            getStatusScreenUseCase = getStatusScreenUseCase,
            paymentProcessed = cachedPayment,
        )

        // When
        viewModel.load()

        // Then
        val state = assertIs<StatusScreenState.Unavailable>(viewModel.state.value)
        assertSame(cachedPayment, state.paymentProcessed)
        assertEquals("1234", state.lastFourDigits)
    }

    @Test
    fun `given a failed fetch when load is called then state becomes Unavailable`() = runTest {
        // Given
        val error = ResponseError(code = "NO_INTERNET", message = "Sem conexão com a internet")
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Error(error)
        val viewModel = makeViewModel()

        // When
        viewModel.load()

        // Then
        assertIs<StatusScreenState.Unavailable>(viewModel.state.value)
    }

    @Test
    fun `given the process failed without status detail when load is called then the BFF is still called`() = runTest {
        // Given
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Success(viewData)
        val viewModel = StatusScreenViewModel(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = null,
            sellerInfo = null,
            getStatusScreenUseCase = getStatusScreenUseCase,
        )

        // When
        viewModel.load()

        // Then
        assertIs<StatusScreenState.Ready>(viewModel.state.value)
        coVerify(exactly = 1) {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        }
    }

    @Test
    fun `given a load already in flight when load is called again then use case runs only once`() = runTest {
        // Given
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Success(viewData)
        val viewModel = makeViewModel()

        // When
        viewModel.load()
        viewModel.load()
        viewModel.load()

        // Then
        coVerify(exactly = 1) {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        }
    }

    @Test
    fun `given an idle ViewModel when trackRender is called then it does not throw`() {
        // Given
        val viewModel = makeViewModel()

        // When / Then
        viewModel.trackRender()
    }

    @Test
    fun `given an idle ViewModel when trackClose is called then it does not throw`() {
        // Given
        val viewModel = makeViewModel()

        // When / Then
        viewModel.trackClose(StatusScreenCloseSource.Back)
    }

    @Test
    fun `given a ready ViewModel when trackRender and trackClose are called then it does not throw`() = runTest {
        // Given
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Success(viewData)
        val viewModel = makeViewModel()
        viewModel.load()

        // When / Then
        viewModel.trackRender()
        viewModel.trackClose(StatusScreenCloseSource.Dismiss)
    }

    @Test
    fun `given a pending response when it renders and closes then the shared tracking fires for pending`() = runTest {
        // Given
        val analytics = mockk<MPAnalytics>(relaxed = true)
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns analytics
        val pendingViewData = viewData.copy(
            statusType = "pending",
            body = listOf(StatusScreenBodyItem.Message(text = "Estamos revisando tu pago")),
        )
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Success(pendingViewData)
        val viewModel = makeViewModel()

        // When
        viewModel.load()
        viewModel.trackRender()
        viewModel.trackClose(StatusScreenCloseSource.Back)

        // Then
        val metrics = mutableListOf<Metric>()
        verify { analytics.trackMetric(capture(metrics)) }
        assertEquals(
            listOf(
                metricStatusScreenLoad(outcome = StatusScreenLoadOutcome.Started, statusType = null).path,
                metricStatusScreenLoad(outcome = StatusScreenLoadOutcome.Success, statusType = "approved").path,
                metricStatusScreenRender(statusType = "approved").path,
                metricStatusScreenClose(source = StatusScreenCloseSource.Back, statusType = "approved").path,
            ),
            metrics.map { it.path },
        )
        assertEquals(
            StatusScreenLoadEventData(outcome = StatusScreenLoadOutcome.Success.trackingValue, statusType = "pending"),
            metrics[1].data,
        )
        assertEquals(StatusScreenViewEventData(statusType = "pending"), metrics[2].data)
        assertEquals(
            StatusScreenCloseEventData(source = StatusScreenCloseSource.Back.trackingValue, statusType = "pending"),
            metrics[3].data,
        )
    }

    @Test
    fun `given a rejected response when it renders then the render event carries can_retry`() = runTest {
        // Given
        val analytics = mockk<MPAnalytics>(relaxed = true)
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns analytics
        val rejectedViewData = viewData.copy(statusType = "rejected", canRetry = true)
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Success(rejectedViewData)
        val viewModel = makeViewModel()

        // When
        viewModel.load()
        viewModel.trackRender()

        // Then
        val metrics = mutableListOf<Metric>()
        verify { analytics.trackMetric(capture(metrics)) }
        assertEquals(StatusScreenViewEventData(statusType = "rejected", canRetry = true), metrics[2].data)
    }

    @Test
    fun `given a contract violation when load fails then contract_violation is tracked instead of a load failure`() =
        runTest {
            // Given
            val analytics = mockk<MPAnalytics>(relaxed = true)
            mockkObject(MPAnalytics.Companion)
            every { MPAnalytics.tryGetInstance() } returns analytics
            coEvery {
                getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
            } returns Result.Error(invalidStatusScreenResponseError())
            val viewModel = makeViewModel()

            // When
            viewModel.load()

            // Then
            val metrics = mutableListOf<Metric>()
            verify { analytics.trackMetric(capture(metrics)) }
            assertEquals(
                listOf(
                    metricStatusScreenLoad(outcome = StatusScreenLoadOutcome.Started, statusType = null).path,
                    metricStatusScreenContractViolation().path,
                ),
                metrics.map { it.path },
            )
            assertEquals(
                StatusScreenLoadEventData(outcome = StatusScreenLoadOutcome.Rejected.trackingValue, statusType = null),
                metrics[1].data,
            )
            assertIs<StatusScreenState.Unavailable>(viewModel.state.value)
        }

    @Test
    fun `given a dependency failure when load fails then load failure is tracked`() = runTest {
        // Given
        val analytics = mockk<MPAnalytics>(relaxed = true)
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns analytics
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Error(ResponseError(code = "internal_error", message = "Internal error", httpStatus = 500))
        val viewModel = makeViewModel()

        // When
        viewModel.load()

        // Then
        val metrics = mutableListOf<Metric>()
        verify { analytics.trackMetric(capture(metrics)) }
        assertEquals(
            StatusScreenLoadEventData(outcome = StatusScreenLoadOutcome.Failure.trackingValue, statusType = null),
            metrics[1].data,
        )
    }

    @Test
    fun `given a rejected response when retry is tapped twice then retry is tracked once`() = runTest {
        // Given
        val analytics = mockk<MPAnalytics>(relaxed = true)
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns analytics
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Success(viewData.copy(statusType = "rejected", canRetry = true))
        val viewModel = makeViewModel()

        // When
        viewModel.load()
        viewModel.trackRetry()
        viewModel.trackRetry()

        // Then
        val metrics = mutableListOf<Metric>()
        verify { analytics.trackMetric(capture(metrics)) }
        val retries = metrics.filter { it.path == metricStatusScreenRetry(statusType = null).path }
        assertEquals(
            StatusScreenLoadEventData(outcome = StatusScreenLoadOutcome.Success.trackingValue, statusType = "rejected"),
            retries.single().data,
        )
    }

    @Test
    fun `given a checkout type when load is called then it is forwarded to the use case`() = runTest {
        // Given
        coEvery {
            getStatusScreenUseCase(
                clientToken = any(),
                orderId = any(),
                lastFourDigits = any(),
                sellerInfo = any(),
                checkoutType = any(),
            )
        } returns Result.Success(viewData)
        val viewModel = StatusScreenViewModel(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = null,
            sellerInfo = null,
            checkoutType = "payment",
            getStatusScreenUseCase = getStatusScreenUseCase,
        )

        // When
        viewModel.load()

        // Then
        coVerify {
            getStatusScreenUseCase(
                clientToken = "token-abc",
                orderId = "order-123",
                lastFourDigits = null,
                sellerInfo = null,
                checkoutType = "payment",
            )
        }
    }

    @Test
    fun `given retry was tracked when the screen closes then close is not tracked`() = runTest {
        // Given
        val analytics = mockk<MPAnalytics>(relaxed = true)
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns analytics
        coEvery {
            getStatusScreenUseCase(clientToken = any(), orderId = any(), lastFourDigits = any(), sellerInfo = any())
        } returns Result.Success(viewData.copy(statusType = "rejected", canRetry = true))
        val viewModel = makeViewModel()

        // When
        viewModel.load()
        viewModel.trackRetry()
        viewModel.trackClose(StatusScreenCloseSource.Back)

        // Then
        val metrics = mutableListOf<Metric>()
        verify { analytics.trackMetric(capture(metrics)) }
        val closePath = metricStatusScreenClose(source = StatusScreenCloseSource.Back, statusType = null).path
        assertTrue(metrics.none { it.path == closePath })
    }
}
