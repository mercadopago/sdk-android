@file:Suppress("NoUnusedImports")

package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.checkout.analytics.OrderSubmitEventData
import com.mercadopago.sdk.android.checkout.core.model.MPCardBrand
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFieldConfig
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberValidation
import com.mercadopago.sdk.android.checkout.domain.model.LengthRange
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeField
import com.mercadopago.sdk.android.checkout.domain.model.Validation
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.model.CancelReason
import com.mercadopago.sdk.android.checkout.presentation.state.CardPaymentViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.MessageError
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.securitycode.SecurityCodeTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.simpletextfield.SimpleTextFieldEvent
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
import kotlin.test.assertSame
import kotlin.test.assertTrue

@Suppress("LargeClass")
@OptIn(ExperimentalCoroutinesApi::class)
internal class CardPaymentViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockMPAnalytics = mockk<MPAnalytics>(relaxed = true)
    private val getCardBinUseCase = mockk<GetCardBinUseCase>(relaxed = true)
    private val initializeCardFormUseCase = mockk<InitializeCardFormUseCase>(relaxed = true)
    private val generateTokenUseCase = mockk<GenerateTokenUseCase>(relaxed = true)
    private val processOrderUseCase = mockk<ProcessOrderUseCase>(relaxed = true)
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
    private val observedNetworkError = ObservedCheckoutError(
        publicError = networkError,
        nativeCode = NativeErrorCode.CONNECTION_UNAVAILABLE,
    )

    @Before
    fun setup() {
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns mockMPAnalytics
        every { mockMPAnalytics.trackError(any(), any()) } answers {
            mockMPAnalytics.trackMetric(secondArg<(String) -> Metric>().invoke("event-id"))
        }
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
        processOrderUseCase = processOrderUseCase,
        cardPaymentScreenStateFactory = cardPaymentScreenStateFactory,
    )

    private suspend fun CardPaymentViewModel.setupWithAmount(
        amount: java.math.BigDecimal = java.math.BigDecimal("100.00"),
    ) {
        coEvery { initializeCardFormUseCase(any(), any(), any()) } returns
            Result.Success(
                mockk<CardFormInitializationOutput>(relaxed = true) {
                    every { this@mockk.amount } returns amount
                },
            )
        initialization()
    }

    @Test
    fun `when initialization succeeds then isLoading is false`() = runTest {
        coEvery { initializeCardFormUseCase(any(), any(), any()) } returns
            Result.Success(mockk<CardFormInitializationOutput>(relaxed = true))
        val viewModel = makeViewModel()

        viewModel.initialization()

        assertFalse(viewModel.viewState.value.isLoading)
    }

    @Test
    fun `when initialization fails then isLoading is false`() = runTest {
        coEvery { initializeCardFormUseCase(any(), any(), any()) } returns Result.Error(observedNetworkError)
        val viewModel = makeViewModel()

        viewModel.initialization()

        assertFalse(viewModel.viewState.value.isLoading)
    }

    @Test
    fun `when initialization fails then emits OnFailure view event`() = runTest {
        coEvery { initializeCardFormUseCase(any(), any(), any()) } returns Result.Error(observedNetworkError)
        val viewModel = makeViewModel()

        viewModel.initialization()

        val event = viewModel.viewEvent.value as CardPaymentViewEvent.OnFailure
        assertSame(networkError, event.error)
    }

    @Test
    fun `when initialization fails then tracks initialize_error event`() = runTest {
        coEvery { initializeCardFormUseCase(any(), any(), any()) } returns Result.Error(observedNetworkError)
        val viewModel = makeViewModel()

        viewModel.initialization()

        val metricSlots = mutableListOf<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlots)) }
        assertTrue(metricSlots.any { it.path.endsWith("/initialize_error") })
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

        val metricSlots = mutableListOf<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlots)) }
        assertTrue(metricSlots.any { it.path.endsWith("/input_validation") })
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
            cardNumber = CardNumberField(
                label = "",
                placeholder = "",
                validation = CardNumberValidation(
                    errorEmpty = "",
                    errorIncomplete = "",
                    errorInvalid = "",
                    errorMethodNotAllowed = "",
                    errorTypeNotAllowed = "",
                ),
                config = CardFieldConfig(type = "standard", length = LengthRange(min = 16, max = 16)),
            ),
            securityCode = SecurityCodeField(
                label = "",
                placeholder = "",
                helper = "",
                tooltip = "",
                validation = Validation(errorEmpty = "", errorIncomplete = "", errorInvalid = ""),
                config = CardFieldConfig(type = "text", length = LengthRange(min = 3, max = 3)),
            ),
            holderName = null,
            expirationDate = null,
            issuers = listOf(BinIssuer(id = "1", name = "Banco")),
            installmentData = MPInstallmentData(),
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
            holderName = null,
            expirationDate = null,
            issuers = emptyList(),
            installmentData = MPInstallmentData(
                quotas = listOf(
                    Quota(
                        installments = 1,
                        installmentAmount = java.math.BigDecimal("100.00"),
                        totalAmount = java.math.BigDecimal("100.00"),
                    ),
                    Quota(
                        installments = 3,
                        installmentAmount = java.math.BigDecimal("34.00"),
                        totalAmount = java.math.BigDecimal("102.00"),
                    ),
                ),
            ),
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        val installmentsState = viewModel.viewState.value.installmentsState
        assertTrue(installmentsState.showList)
        assertEquals(2, installmentsState.installments.size)
        assertEquals(1, installmentsState.installments.first().installments)
    }

    @Test
    fun `when ExpirationDate IsValid then updates isValid`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.IsValid(isValid = true))

        assertTrue(viewModel.viewState.value.expirationDateState.isValid)
    }

    @Test
    fun `when ExpirationDate IsValid event then tracks input_validation`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.IsValid(isValid = false))

        val metricSlots = mutableListOf<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlots)) }
        assertTrue(metricSlots.any { it.path.endsWith("/input_validation") })
    }

    @Test
    fun `when ExpirationDate OnLengthChanged then updates length`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.OnLengthChanged(length = 3))

        assertEquals(3, viewModel.viewState.value.expirationDateState.length)
    }

    @Test
    fun `when ExpirationDate OnInputFilled then updates filled`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.OnInputFilled(isFilled = true))

        assertTrue(viewModel.viewState.value.expirationDateState.filled)
    }

    @Test
    fun `when SecurityCode OnLengthChanged then updates length`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = 2))

        assertEquals(2, viewModel.viewState.value.secureCodeState.length)
    }

    @Test
    fun `when SecurityCode OnInputFilled then updates filled`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnInputFilled(isFilled = true))

        assertTrue(viewModel.viewState.value.secureCodeState.filled)
    }

    @Test
    fun `when CardHolder OnValueChanged then updates value`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardHolderEvent(SimpleTextFieldEvent.OnValueChanged("John Doe"))

        assertEquals("John Doe", viewModel.viewState.value.cardHolderState.value)
    }

    @Test
    fun `when CardHolder OnFocusChanged false then isFocused is false`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardHolderEvent(SimpleTextFieldEvent.OnFocusChanged(isFocused = false))

        assertFalse(viewModel.viewState.value.cardHolderState.isFocused)
    }

    @Test
    fun `when Identification OnValueChanged then updates value`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnValueChanged("12345678"))

        assertEquals("12345678", viewModel.viewState.value.identificationTypeState.value)
    }

    @Test
    fun `when Identification OnTypeSelected then tracks dropdown_selection`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onIdentificationEvent(
            IdentificationTextFieldEvent.OnTypeSelected(mockk<IdentificationType>(relaxed = true)),
        )

        val metricSlots = mutableListOf<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlots)) }
        assertTrue(metricSlots.any { it.path.endsWith("/dropdown_selection") })
    }

    @Test
    fun `when Identification OnTypeSelected then updates selected and clears placeholder`() = runTest {
        val viewModel = makeViewModel()
        val identificationType = mockk<IdentificationType>(relaxed = true)

        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnTypeSelected(identificationType))

        assertEquals(identificationType, viewModel.viewState.value.identificationTypeState.selected)
        assertEquals("", viewModel.viewState.value.identificationTypeState.placeHolder)
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

        val metricSlots = mutableListOf<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlots)) }
        assertTrue(metricSlots.any { it.path.endsWith("/user_canceled_error") })
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
    fun `when onSubmit succeeds with CardSave then tracks submit event`() = runTest {
        val cardSaveConfig = CheckoutConfiguration(
            checkoutType = MPCheckoutType.CardSave,
            paymentMethodConfigs = emptyList(),
        )
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel(config = cardSaveConfig)

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        val metricSlots = mutableListOf<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlots)) }
        assertTrue(metricSlots.any { it.path.endsWith("/submit") })
    }

    @Test
    fun `when onInstallmentConfirmed succeeds then tracks order submit event with order data`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        coEvery { processOrderUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "order_123", status = "opened"))
        val viewModel = makeViewModel()
        viewModel.setupWithAmount()
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()

        viewModel.onInstallmentConfirmed(installment = 3)

        val metricSlots = mutableListOf<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlots)) }
        val orderSubmitMetric = metricSlots.first { it.path.endsWith("/order/submit") }
        val data = orderSubmitMetric.data as OrderSubmitEventData
        assertEquals("order_123", data.orderId)
        assertEquals("opened", data.orderStatus)
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
        } returns Result.Error(observedNetworkError)
        val viewModel = makeViewModel()

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        val event = viewModel.viewEvent.value as CardPaymentViewEvent.OnFailure
        assertSame(networkError, event.error)
    }

    @Test
    fun `when onSubmit is called then button is not loading after completion`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel()

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        assertFalse(viewModel.viewState.value.footerState.isButtonLoading)
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
    fun `when onSubmit succeeds with null config then emits OnFailure unsupported type`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel(config = null)

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        assertTrue(viewModel.viewEvent.value is CardPaymentViewEvent.OnFailure)
    }

    @Test
    fun `when onSubmit succeeds with CardSave then processOrderUseCase is not called`() = runTest {
        val cardSaveConfig = CheckoutConfiguration(
            checkoutType = MPCheckoutType.CardSave,
            paymentMethodConfigs = emptyList(),
        )
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel(config = cardSaveConfig)

        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )

        coVerify(exactly = 0) { processOrderUseCase(any()) }
    }

    @Test
    fun `when onInstallmentConfirmed without prior submit then calls processOrderUseCase with empty token`() = runTest {
        coEvery { processOrderUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "", status = ""))
        val viewModel = makeViewModel()
        viewModel.setupWithAmount()
        val paramsSlot = slot<ProcessOrderParams>()

        viewModel.onInstallmentConfirmed(installment = 3)

        coVerify { processOrderUseCase(capture(paramsSlot)) }
        assertEquals("", paramsSlot.captured.token)
    }

    @Test
    fun `when processOrder fails without prior submit then notifies callback with error`() = runTest {
        coEvery { processOrderUseCase(any()) } returns Result.Error(observedNetworkError)
        val viewModel = makeViewModel()
        viewModel.setupWithAmount()

        viewModel.onInstallmentConfirmed(installment = 3)

        val notifySlot = slot<MercadoPagoCheckoutResult<*, *>>()
        verify(exactly = 1) { CheckoutCallbackHolder.notify(capture(notifySlot)) }
        val result = notifySlot.captured as MercadoPagoCheckoutResult.Error
        assertSame(networkError, result.error)
    }

    @Test
    fun `when onInstallmentConfirmed succeeds then notifies callback with success`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        coEvery { processOrderUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "order_123", status = "opened"))
        val viewModel = makeViewModel()
        viewModel.setupWithAmount()
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()

        viewModel.onInstallmentConfirmed(installment = 3)

        val notifySlot = slot<MercadoPagoCheckoutResult<*, *>>()
        verify(exactly = 1) { CheckoutCallbackHolder.notify(capture(notifySlot)) }
        assertTrue(notifySlot.captured is MercadoPagoCheckoutResult.Success<*>)
    }

    @Test
    fun `when onInstallmentConfirmed succeeds then payment contains orderId and status from response`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        coEvery { processOrderUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "order_123", status = "opened"))
        val viewModel = makeViewModel()
        viewModel.setupWithAmount()
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()

        viewModel.onInstallmentConfirmed(installment = 3)

        val notifySlot = slot<MercadoPagoCheckoutResult<*, *>>()
        verify(exactly = 1) { CheckoutCallbackHolder.notify(capture(notifySlot)) }
        val captured = notifySlot.captured as MercadoPagoCheckoutResult.Success<*>
        val payment = captured.paymentData as MPPaymentData.CardTransaction
        assertEquals("order_123", payment.orderId)
        assertEquals("opened", payment.orderStatus)
    }

    @Test
    fun `when onInstallmentConfirmed succeeds then payment contains selected installment`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        coEvery { processOrderUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "order_123", status = "opened"))
        val viewModel = makeViewModel()
        viewModel.setupWithAmount()
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()

        viewModel.onInstallmentConfirmed(installment = 6)

        val notifySlot = slot<MercadoPagoCheckoutResult<*, *>>()
        verify(exactly = 1) { CheckoutCallbackHolder.notify(capture(notifySlot)) }
        val captured = notifySlot.captured as MercadoPagoCheckoutResult.Success<*>
        captured.paymentData as MPPaymentData.CardTransaction
    }

    @Test
    fun `when onInstallmentConfirmed then calls processOrderUseCase with selected installments`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        coEvery { processOrderUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "order_123", status = "opened"))
        val viewModel = makeViewModel()
        viewModel.setupWithAmount()
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()
        val paramsSlot = slot<ProcessOrderParams>()

        viewModel.onInstallmentConfirmed(installment = 6)

        coVerify { processOrderUseCase(capture(paramsSlot)) }
        assertEquals(6, paramsSlot.captured.installments)
    }

    @Test
    fun `when onInstallmentConfirmed fails then notifies callback with error`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        coEvery { processOrderUseCase(any()) } returns Result.Error(observedNetworkError)
        val viewModel = makeViewModel()
        viewModel.setupWithAmount()
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()

        viewModel.onInstallmentConfirmed(installment = 3)

        val notifySlot = slot<MercadoPagoCheckoutResult<*, *>>()
        verify(exactly = 1) { CheckoutCallbackHolder.notify(capture(notifySlot)) }
        assertTrue(notifySlot.captured is MercadoPagoCheckoutResult.Error)
    }

    @Test
    fun `when onInstallmentConfirmed succeeds then isLoading is false`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        coEvery { processOrderUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "order_123", status = "opened"))
        val viewModel = makeViewModel()
        viewModel.setupWithAmount()
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()

        viewModel.onInstallmentConfirmed(installment = 3)

        assertFalse(viewModel.viewState.value.isLoading)
    }

    @Test
    fun `when onInstallmentConfirmed then calls processOrderUseCase with token from generateToken`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        coEvery { processOrderUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "order_123", status = "opened"))
        val viewModel = makeViewModel()
        viewModel.setupWithAmount()
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()
        val paramsSlot = slot<ProcessOrderParams>()

        viewModel.onInstallmentConfirmed(installment = 1)

        coVerify { processOrderUseCase(capture(paramsSlot)) }
        assertEquals("token_abc", paramsSlot.captured.token)
    }

    @Test
    fun `when onInstallmentConfirmed then calls processOrderUseCase with paymentMethod from state`() = runTest {
        val data = binData(id = "visa", paymentTypeId = "credit_card")
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        coEvery { processOrderUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "order_123", status = "opened"))
        val viewModel = makeViewModel()
        viewModel.setupWithAmount()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()
        val paramsSlot = slot<ProcessOrderParams>()

        viewModel.onInstallmentConfirmed(installment = 1)

        coVerify { processOrderUseCase(capture(paramsSlot)) }
        assertEquals("visa", paramsSlot.captured.paymentMethodId)
        assertEquals("credit_card", paramsSlot.captured.paymentMethodType)
    }

    private fun binData(
        id: String,
        paymentTypeId: String,
    ) = CardBinData(
        id = id,
        paymentTypeId = paymentTypeId,
        cardNumber = null,
        securityCode = null,
        holderName = null,
        expirationDate = null,
        issuers = emptyList(),
        installmentData = MPInstallmentData(),
    )
}
