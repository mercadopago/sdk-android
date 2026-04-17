package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardDataByBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentScreenState
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.presentation.validation.CardPaymentValidator
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextFieldEvent
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class CardPaymentViewModelTrackingTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockMPAnalytics = mockk<MPAnalytics>(relaxed = true)
    private val stateFactory = mockk<CardPaymentScreenStateFactory>(relaxed = true)
    private val getCardDataByBinUseCase = mockk<GetCardDataByBinUseCase>(relaxed = true)
    private val initializeCardFormUseCase = mockk<InitializeCardFormUseCase>(relaxed = true)
    private val generateTokenUseCase = mockk<GenerateTokenUseCase>(relaxed = true)
    private val cancelledFormContextUseCase = mockk<CancelledFormContextUseCase>(relaxed = true)
    private val validator = mockk<CardPaymentValidator>(relaxed = true)

    private val checkoutConfiguration = CheckoutConfiguration(
        checkoutType = mockk<CheckoutType.CardForm>(relaxed = true),
        paymentMethods = listOf<PaymentMethod>(
            PaymentMethod.Card(
                allowedTypes = listOf(CardType.CREDIT, CardType.DEBIT),
                allowedBrands = listOf(CardBrand.Visa, CardBrand.Mastercard),
            ),
        ),
    )

    @Before
    fun setup() {
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns mockMPAnalytics
        every { stateFactory.createInitialState() } returns CardPaymentScreenState()
        every { stateFactory.getOptionalFieldText() } returns ""
        every { stateFactory.getGenericErrorMessage() } returns "Error"
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

    private fun makeViewModel(
        config: CheckoutConfiguration? = checkoutConfiguration,
    ) = CardPaymentViewModel(
        stateFactory = stateFactory,
        checkoutConfiguration = config,
        getCardDataByBinUseCase = getCardDataByBinUseCase,
        initializeCardFormUseCase = initializeCardFormUseCase,
        generateTokenUseCase = generateTokenUseCase,
        cancelledFormContextUseCase = cancelledFormContextUseCase,
        validator = validator,
    )

    // region Initialize

    @Test
    fun `when getIdentificationTypes fails then tracks initialize_error event`() = runTest {
        val error = MercadoPagoCheckoutError.NetworkError(
            code = ErrorCode.NETWORK_CONNECTION_FAILED,
            messageError = "Connection failed",
            localized = "checkout",
            throwable = null,
        )
        coEvery { initializeCardFormUseCase(any(), any(), any()) } returns Result.Error(error)
        val viewModel = makeViewModel()

        viewModel.getIdentificationTypes()

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/initialize_error"))
    }

    // endregion

    // region Input Validation

    @Test
    fun `when card number loses focus then tracks input_validation`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.IsValid(isValid = true))
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnFocusChanged(isFocused = false))

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/input_validation"))
    }

    @Test
    fun `when expiration date IsValid event then tracks input_validation`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.IsValid(isValid = false))

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/input_validation"))
    }

    // endregion

    // region Dropdown Selection

    @Test
    fun `when identification type selected then tracks dropdown_selection`() = runTest {
        val viewModel = makeViewModel()
        val mockType = mockk<com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType>(relaxed = true)

        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnTypeSelected(mockType))

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/dropdown_selection"))
    }

    // endregion

    // region Submit

    @Test
    fun `when validateFieldsAndTokenize called then tracks submit event`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token123"))
        val viewModel = makeViewModel()

        viewModel.validateFieldsAndTokenize(
            cardNumberState = mockk(relaxed = true),
            expirationDateState = mockk(relaxed = true),
            securityCodeState = mockk(relaxed = true),
        )

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/submit"))
    }

    // endregion

    // region User Canceled

    @Test
    fun `when onBackPressed then tracks user_canceled_error event`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onBackPressed()

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/user_canceled_error"))
    }

    // endregion
}
