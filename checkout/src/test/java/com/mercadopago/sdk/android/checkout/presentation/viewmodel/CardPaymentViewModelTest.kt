package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.checkout.core.model.MPCardBrand
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.LengthConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfig
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.MessageError
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import io.mockk.coEvery
import io.mockk.coVerify
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class CardPaymentViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockMPAnalytics = mockk<MPAnalytics>(relaxed = true)
    private val getCardBinUseCase = mockk<GetCardBinUseCase>(relaxed = true)
    private val initializeCardFormUseCase = mockk<InitializeCardFormUseCase>(relaxed = true)
    private val generateTokenUseCase = mockk<GenerateTokenUseCase>(relaxed = true)
    private val cardPaymentScreenStateFactory = mockk<CardPaymentScreenStateFactory>(relaxed = true)

    private val checkoutConfiguration = CheckoutConfiguration(
        checkoutType = mockk<MPCheckoutType.CardTransaction>(relaxed = true),
        paymentMethodConfigs = listOf(
            MPPaymentMethodConfig.Card(
                excludedPaymentTypes = listOf(MPCardType.CREDIT, MPCardType.DEBIT),
                excludedPaymentMethods = listOf(MPCardBrand.Visa, MPCardBrand.Mastercard),
            ),
        ),
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
        mockkObject(MercadoPagoSDK.Companion)
        every { MercadoPagoSDK.countryCode } returns null
        mockkObject(CheckoutCallbackHolder)
        every { CheckoutCallbackHolder.notify(any()) } returns Unit
    }

    @After
    fun tearDown() {
        unmockkObject(MPAnalytics.Companion)
        unmockkObject(MercadoPagoSDK.Companion)
        unmockkObject(CheckoutCallbackHolder)
    }

    private fun makeViewModel(
        config: CheckoutConfiguration? = checkoutConfiguration,
    ) = CardPaymentViewModel(
        checkoutConfiguration = config,
        getCardBinUseCase = getCardBinUseCase,
        initializeCardFormUseCase = initializeCardFormUseCase,
        generateTokenUseCase = generateTokenUseCase,
        cardPaymentScreenStateFactory = cardPaymentScreenStateFactory,
    )

    @Test
    fun `when initialization succeeds then isLoading is false`() = runTest {
        coEvery { initializeCardFormUseCase(any(), any()) } returns
            Result.Success(mockk<CardFormInitializationOutput>(relaxed = true))
        val viewModel = makeViewModel()

        viewModel.initialization()

        assertFalse(viewModel.viewState.value.isLoading)
    }

    @Test
    fun `when initialization fails then isLoading is false`() = runTest {
        coEvery { initializeCardFormUseCase(any(), any()) } returns Result.Error(networkError)
        val viewModel = makeViewModel()

        viewModel.initialization()

        assertFalse(viewModel.viewState.value.isLoading)
    }

    @Test
    fun `when initialization fails then emits OnFailure view event`() = runTest {
        coEvery { initializeCardFormUseCase(any(), any()) } returns Result.Error(networkError)
        val viewModel = makeViewModel()

        viewModel.initialization()

        assertTrue(viewModel.viewEvent.value is CardPaymentViewEvent.OnFailure)
    }

    @Test
    fun `when initialization fails then tracks initialize_error event`() = runTest {
        coEvery { initializeCardFormUseCase(any(), any()) } returns Result.Error(networkError)
        val viewModel = makeViewModel()

        viewModel.initialization()

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/initialize_error"))
    }

    @Test
    fun `when OnLengthChanged then updates card number length`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnLengthChanged(length = 8))

        assertEquals(8, viewModel.viewState.value.cardNumberState.length)
    }

    @Test
    fun `when OnLastFourDigitsFilled then updates lastFourDigits`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnLastFourDigitsFilled("5678"))

        assertEquals("5678", viewModel.viewState.value.cardNumberState.lastFourDigits)
    }

    @Test
    fun `when IsValid true then cardNumber isValid is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.IsValid(isValid = true))

        assertTrue(viewModel.viewState.value.cardNumberState.isValid)
    }

    @Test
    fun `when IsValid false then cardNumber isValid is false`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.IsValid(isValid = false))

        assertFalse(viewModel.viewState.value.cardNumberState.isValid)
    }

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
    fun `when OnFocusChanged true then cardNumber isFocused is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnFocusChanged(isFocused = true))

        assertTrue(viewModel.viewState.value.cardNumberState.isFocused)
    }

    @Test
    fun `when BIN call succeeds then card number maxLength is updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = CardNumberConfig(type = "standard", length = LengthConfig(min = 16, max = 16), mask = ""),
            securityCode = SecurityCodeConfig(type = "text", length = 3, mode = "mandatory", cardLocation = "back"),
            issuers = listOf(BinIssuer(id = 1L, name = "Banco", secureThumbnail = null)),
            quotas = listOf(
                Quota(
                    quantity = 1,
                    installmentAmount = "100",
                    totalAmount = "100",
                    label = "1x",
                    discountRate = 0.0,
                ),
            ),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals(16, viewModel.viewState.value.cardNumberState.maxLength)
    }

    @Test
    fun `when BIN call succeeds with quotas then installments state is updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = listOf(
                Quota(
                    quantity = 1,
                    installmentAmount = "100.00",
                    totalAmount = "100.00",
                    label = "1x sem juros",
                    discountRate = 0.0,
                ),
                Quota(
                    quantity = 3,
                    installmentAmount = "34.00",
                    totalAmount = "102.00",
                    label = "3x",
                    discountRate = 0.0,
                ),
            ),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        val installmentsState = viewModel.viewState.value.installmentsState
        assertTrue(installmentsState.showList)
        assertEquals(2, installmentsState.installments.size)
        assertEquals(1, installmentsState.installments.first().instalments)
    }

    @Test
    fun `when onTooltipClick called once then showTooltip is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onTooltipClick()

        assertTrue(viewModel.viewState.value.showTooltip)
    }

    @Test
    fun `when onTooltipClick called twice then showTooltip is false`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onTooltipClick()
        viewModel.onTooltipClick()

        assertFalse(viewModel.viewState.value.showTooltip)
    }

    @Test
    fun `when onMessageClick then showMessage is false and messageError is reset`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onMessageClick()

        assertFalse(viewModel.viewState.value.showMessage)
        assertEquals(MessageError(), viewModel.viewState.value.messageError)
    }

    @Test
    fun `when onBackPressed then emits OnUserCancelled view event`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onBackPressed()

        assertTrue(viewModel.viewEvent.value is CardPaymentViewEvent.OnUserCancelled)
    }

    @Test
    fun `when onBackPressed then tracks user_canceled_error event`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onBackPressed()

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/user_canceled_error"))
    }

    @Test
    fun `when onBackPressed with UiButton reason then emits OnUserCancelled view event`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onBackPressed(reason = CancelReason.UiButton)

        assertTrue(viewModel.viewEvent.value is CardPaymentViewEvent.OnUserCancelled)
    }

    @Test
    fun `when onSubmit with no errors then calls generateTokenUseCase`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel()

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        coVerify { generateTokenUseCase(any(), any(), any(), any()) }
    }

    @Test
    fun `when onSubmit succeeds then tracks submit event`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel()

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/submit"))
    }

    @Test
    fun `when onSubmit succeeds then emits OnSuccess view event`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel()

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        assertTrue(viewModel.viewEvent.value is CardPaymentViewEvent.OnSuccess)
    }

    @Test
    fun `when onSubmit fails then emits OnFailure view event`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Error(networkError)
        val viewModel = makeViewModel()

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        assertTrue(viewModel.viewEvent.value is CardPaymentViewEvent.OnFailure)
    }

    @Test
    fun `when onSubmit is called then isLoading is false after completion`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel()

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        assertFalse(viewModel.viewState.value.isLoading)
    }

    @Test
    fun `when onSubmit succeeds with CardSave checkoutType then emits OnSuccess view event`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel(
            config = CheckoutConfiguration(
                checkoutType = MPCheckoutType.CardSave,
                paymentMethodConfigs = emptyList(),
            ),
        )

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        assertTrue(viewModel.viewEvent.value is CardPaymentViewEvent.OnSuccess)
    }

    @Test
    fun `when onSubmit succeeds with null config then emits OnSuccess view event`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel(config = null)

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        assertTrue(viewModel.viewEvent.value is CardPaymentViewEvent.OnSuccess)
    }
}
