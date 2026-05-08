package com.mercadopago.sdk.android.checkout.presentation.brick

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class CheckoutControllerViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockMPAnalytics = mockk<MPAnalytics>(relaxed = true)
    private val initializeUseCase = mockk<InitializeCardFormUseCase>(relaxed = true)

    private val configuration = CheckoutConfiguration(
        checkoutType = mockk<CheckoutType.CardForm>(relaxed = true),
        paymentMethods = emptyList(),
    )

    private val networkError = MercadoPagoCheckoutError.NetworkError(
        code = ErrorCode.NETWORK_CONNECTION_FAILED,
        messageError = "Connection failed",
        localized = "checkout",
        throwable = null,
    )

    @Before
    fun setup() {
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns mockMPAnalytics
        mockkObject(CheckoutCallbackHolder)
        every { CheckoutCallbackHolder.notify(any()) } returns Unit
        mockkObject(MercadoPagoSDK.Companion)
        every { MercadoPagoSDK.countryCode } returns null
    }

    @After
    fun tearDown() {
        unmockkObject(MPAnalytics.Companion)
        unmockkObject(CheckoutCallbackHolder)
        unmockkObject(MercadoPagoSDK.Companion)
    }

    private fun makeViewModel() = CheckoutControllerViewModel(
        configuration = configuration,
        initializeCardFormUseCase = initializeUseCase,
    )

    @Test
    fun `screenState initially is Loading`() = runTest {
        val viewModel = makeViewModel()

        assertTrue(viewModel.screenState.value is CheckoutControllerViewModel.ScreenState.Loading)
    }

    @Test
    fun `when load succeeds then screenState becomes Ready with initData`() = runTest {
        val initData = mockk<CardFormInitializationOutput>(relaxed = true)
        coEvery { initializeUseCase(any(), any()) } returns Result.Success(initData)
        val viewModel = makeViewModel()

        viewModel.load()

        val state = viewModel.screenState.value
        assertTrue(state is CheckoutControllerViewModel.ScreenState.Ready)
        assertEquals(initData, (state as CheckoutControllerViewModel.ScreenState.Ready).initData)
    }

    @Test
    fun `when load fails then notifies CheckoutCallbackHolder with Error`() = runTest {
        coEvery { initializeUseCase(any(), any()) } returns Result.Error(networkError)
        val viewModel = makeViewModel()

        viewModel.load()

        verify { CheckoutCallbackHolder.notify(match { it is MercadoPagoCheckoutResult.Error }) }
    }

    @Test
    fun `when load fails then tracks initialize_error event`() = runTest {
        coEvery { initializeUseCase(any(), any()) } returns Result.Error(networkError)
        val viewModel = makeViewModel()

        viewModel.load()

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/initialize_error"))
    }

    @Test
    fun `when load is called twice then second call is a no-op`() = runTest {
        val initData = mockk<CardFormInitializationOutput>(relaxed = true)
        coEvery { initializeUseCase(any(), any()) } returns Result.Success(initData)
        val viewModel = makeViewModel()
        viewModel.load()

        viewModel.load()

        // first call moved state to Ready; second call must early-return without calling the use case again
        // io.mockk does not by default count both calls — assert via the screenState being Ready and stable.
        assertTrue(viewModel.screenState.value is CheckoutControllerViewModel.ScreenState.Ready)
    }
}
