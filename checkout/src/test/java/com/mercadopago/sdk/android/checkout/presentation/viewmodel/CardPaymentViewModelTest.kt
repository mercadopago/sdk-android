package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
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
import com.mercadopago.sdk.android.checkout.domain.model.CardHolderField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberValidation
import com.mercadopago.sdk.android.checkout.domain.model.ExpirationDateField
import com.mercadopago.sdk.android.checkout.domain.model.LengthRange
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
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
import com.mercadopago.sdk.android.checkout.presentation.state.InstallmentsDisplayType
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
import kotlin.test.assertNull
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

    private val emptyValidation = Validation(errorEmpty = "", errorIncomplete = "", errorInvalid = "")
    private val emptyFieldConfig = CardFieldConfig(type = "", length = LengthRange(min = 0, max = 0))

    private fun cardNumberField(
        label: String = "",
        placeholder: String = "",
        maxLength: Int = 16,
    ) = CardNumberField(
        label = label,
        placeholder = placeholder,
        validation = CardNumberValidation(
            errorEmpty = "",
            errorIncomplete = "",
            errorInvalid = "",
            errorMethodNotAllowed = "",
            errorTypeNotAllowed = "",
        ),
        config = CardFieldConfig(type = "text", length = LengthRange(min = maxLength, max = maxLength)),
    )

    private fun securityCodeField(
        label: String = "",
        placeholder: String = "",
        tooltip: String = "",
        length: Int,
    ) = SecurityCodeField(
        label = label,
        placeholder = placeholder,
        helper = "",
        tooltip = tooltip,
        validation = emptyValidation,
        config = CardFieldConfig(type = "text", length = LengthRange(min = length, max = length)),
    )

    private fun binData(
        id: String? = "visa",
        paymentTypeId: String? = "credit_card",
        cardNumber: CardNumberField? = null,
        securityCode: SecurityCodeField? = null,
        holderName: CardHolderField? = null,
        expirationDate: ExpirationDateField? = null,
        issuers: List<BinIssuer> = emptyList(),
        quotas: List<Quota> = emptyList(),
        displayType: InstallmentsDisplayType = InstallmentsDisplayType.RadioButton,
        currencySymbol: String = "",
        installmentsTitle: String = "",
        installmentsTotalLabel: String = "",
        installmentsButtonLabel: String = "",
    ) = CardBinData(
        id = id,
        paymentTypeId = paymentTypeId,
        cardNumber = cardNumber,
        securityCode = securityCode,
        holderName = holderName,
        expirationDate = expirationDate,
        issuers = issuers,
        installmentData = MPInstallmentData(
            quotas = quotas,
            display = MPInstallmentData.InstallmentDisplay(
                title = installmentsTitle,
                currencySymbol = currencySymbol,
                displayType = displayType,
                footer = MPInstallmentData.InstallmentFooterDisplay(
                    footerTitle = installmentsTotalLabel,
                    buttonLabel = installmentsButtonLabel,
                ),
            ),
        ),
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
    fun `when initialization fails then emits OnFailure viewEvent`() = runTest {
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
    fun `when BIN length is less than 6 then getCardBin is not called`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("12345"))

        coVerify(exactly = 0) { getCardBinUseCase(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `when BIN call succeeds then card number maxLength is updated`() = runTest {
        val data = binData(
            cardNumber = cardNumberField(maxLength = 16),
            securityCode = securityCodeField(length = 3),
            issuers = listOf(BinIssuer(id = "1", name = "Banco")),
            quotas = listOf(
                Quota(
                    installments = 1,
                    installmentAmount = java.math.BigDecimal("100"),
                    totalAmount = java.math.BigDecimal("100"),
                ),
            ),
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals(16, viewModel.viewState.value.cardNumberState.maxLength)
    }

    @Test
    fun `when BIN call succeeds then security code maxLength is updated`() = runTest {
        val data = binData(securityCode = securityCodeField(length = 3))
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals(3, viewModel.viewState.value.secureCodeState.maxLength)
        assertFalse(viewModel.viewState.value.secureCodeState.optional)
    }

    @Test
    fun `when BIN call succeeds with optional security code then optional is true`() = runTest {
        val data = binData(securityCode = securityCodeField(length = 0))
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertTrue(viewModel.viewState.value.secureCodeState.optional)
    }

    @Test
    fun `when BIN call succeeds with translations then field labels are updated`() = runTest {
        val data = binData(
            cardNumber = cardNumberField(label = "Número", placeholder = "•••• ••••"),
            securityCode = securityCodeField(label = "CVV", placeholder = "123", tooltip = "3 dígitos", length = 3),
            holderName = CardHolderField(
                label = "Titular",
                placeholder = "Nome",
                helper = "",
                validation = emptyValidation,
                config = emptyFieldConfig,
            ),
            expirationDate = ExpirationDateField(
                label = "Vencimento",
                placeholder = "MM/AA",
                validation = emptyValidation,
                config = emptyFieldConfig,
            ),
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        val state = viewModel.viewState.value
        assertEquals("Número", state.cardNumberState.label)
        assertEquals("Titular", state.cardHolderState.label)
        assertEquals("Vencimento", state.expirationDateState.label)
        assertEquals("CVV", state.secureCodeState.label)
    }

    @Test
    fun `when BIN succeeds with longer CVV than typed digits then secureCode shows incomplete error`() = runTest {
        val cvv4Data = binData(
            securityCode = securityCodeField(length = 4).copy(
                validation = Validation(
                    errorEmpty = "Required CVV",
                    errorIncomplete = "Digite os 4 dígitos do código de segurança",
                    errorInvalid = "Invalid CVV",
                ),
            ),
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(cvv4Data)
        val viewModel = makeViewModel()
        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = 3))

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        val secureCodeState = viewModel.viewState.value.secureCodeState
        assertEquals(4, secureCodeState.maxLength)
        assertEquals("Digite os 4 dígitos do código de segurança", secureCodeState.error)
    }

    @Test
    fun `when BIN succeeds and CVV not typed yet then secureCode has no error`() = runTest {
        val cvv4Data = binData(
            securityCode = securityCodeField(length = 4).copy(
                validation = Validation(
                    errorEmpty = "Required CVV",
                    errorIncomplete = "Incomplete CVV",
                    errorInvalid = "Invalid CVV",
                ),
            ),
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(cvv4Data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals("", viewModel.viewState.value.secureCodeState.error)
    }

    @Test
    fun `when BIN succeeds with custom mask then cardNumberState mask uses BFF value`() = runTest {
        val data = binData(
            cardNumber = cardNumberField(maxLength = 15).copy(
                config = CardFieldConfig(
                    type = "text",
                    length = LengthRange(min = 15, max = 15),
                    mask = "#### ###### #####",
                ),
            ),
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals("#### ###### #####", viewModel.viewState.value.cardNumberState.mask)
    }

    @Test
    fun `when BIN call succeeds then paymentState is updated`() = runTest {
        val data = binData()
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals("visa", viewModel.viewState.value.paymentState.paymentMethodId)
        assertEquals("credit_card", viewModel.viewState.value.paymentState.paymentTypeId)
    }

    @Test
    fun `when BIN call fails then state is not changed by error`() = runTest {
        coEvery {
            getCardBinUseCase(any(), any(), any(), any(), any())
        } returns Result.Error(networkError)
        val viewModel = makeViewModel()
        val stateBefore = viewModel.viewState.value

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals(stateBefore.cardNumberState.label, viewModel.viewState.value.cardNumberState.label)
    }

    @Test
    fun `when BIN call succeeds with issuers then cardIssuers state is updated`() = runTest {
        val data = binData(issuers = listOf(BinIssuer(id = "1", name = "Banco do Brasil")))
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        val issuers = viewModel.viewState.value.cardIssuers
        assertEquals(1, issuers.size)
        assertEquals("1", issuers.first().id)
        assertNull(issuers.first().thumbnail)
    }

    @Test
    fun `when BIN call succeeds with quotas then installments state is updated`() = runTest {
        val data = binData(
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
    fun `when onBackPressed then emits OnUserCancelled viewEvent`() = runTest {
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
    fun `when onBackPressed with UiButton reason then emits OnUserCancelled viewEvent`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onBackPressed(reason = CancelReason.UiButton)

        assertTrue(viewModel.viewEvent.value is CardPaymentViewEvent.OnUserCancelled)
    }

    @Test
    fun `when onBackPressed without installments presented then context flag is false`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onBackPressed()

        val event = viewModel.viewEvent.value as CardPaymentViewEvent.OnUserCancelled
        val context = event.context as MPUserCancelledContext.CardForm
        assertFalse(context.context.installmentsWasPresented)
    }

    @Test
    fun `when markInstallmentsPresented and onBackPressed then context flag is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.markInstallmentsPresented()
        viewModel.onBackPressed()

        val event = viewModel.viewEvent.value as CardPaymentViewEvent.OnUserCancelled
        val context = event.context as MPUserCancelledContext.CardForm
        assertTrue(context.context.installmentsWasPresented)
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
    fun `when onSubmit succeeds then emits OnSuccess viewEvent`() = runTest {
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
    fun `when onSubmit fails then emits OnFailure viewEvent`() = runTest {
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
        val paramsSlot = slot<ProcessOrderParams>()

        viewModel.onInstallmentConfirmed(installment = 3)

        coVerify { processOrderUseCase(capture(paramsSlot)) }
        assertEquals("", paramsSlot.captured.token)
    }

    @Test
    fun `when processOrder fails without prior submit then notifies callback with error`() = runTest {
        coEvery { processOrderUseCase(any()) } returns Result.Error(networkError)
        val viewModel = makeViewModel()

        viewModel.onInstallmentConfirmed(installment = 3)

        val notifySlot = slot<MercadoPagoCheckoutResult<*>>()
        verify(exactly = 1) { CheckoutCallbackHolder.notify(capture(notifySlot)) }
        assertTrue(notifySlot.captured is MercadoPagoCheckoutResult.Error)
    }

    @Test
    fun `when onInstallmentConfirmed succeeds then notifies callback with success`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        coEvery { processOrderUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "order_123", status = "opened"))
        val viewModel = makeViewModel()
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()

        viewModel.onInstallmentConfirmed(installment = 3)

        val notifySlot = slot<MercadoPagoCheckoutResult<*>>()
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
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()

        viewModel.onInstallmentConfirmed(installment = 3)

        val notifySlot = slot<MercadoPagoCheckoutResult<*>>()
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
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()

        viewModel.onInstallmentConfirmed(installment = 6)

        val notifySlot = slot<MercadoPagoCheckoutResult<*>>()
        verify(exactly = 1) { CheckoutCallbackHolder.notify(capture(notifySlot)) }
        val captured = notifySlot.captured as MercadoPagoCheckoutResult.Success<*>
        val payment = captured.paymentData as MPPaymentData.CardTransaction
        assertEquals(6, payment.installment)
    }

    @Test
    fun `when onInstallmentConfirmed then calls processOrderUseCase with selected installments`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        coEvery { processOrderUseCase(any()) } returns
            Result.Success(OrderProcessOutput(id = "order_123", status = "opened"))
        val viewModel = makeViewModel()
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
        coEvery { processOrderUseCase(any()) } returns Result.Error(networkError)
        val viewModel = makeViewModel()
        viewModel.onSubmit(
            cardNumberState = mockk<PCIFieldState>(relaxed = true),
            expirationDateState = mockk<PCIFieldState>(relaxed = true),
            securityCodeState = mockk<PCIFieldState>(relaxed = true),
        )
        viewModel.onViewEventConsumed()

        viewModel.onInstallmentConfirmed(installment = 3)

        val notifySlot = slot<MercadoPagoCheckoutResult<*>>()
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
}
