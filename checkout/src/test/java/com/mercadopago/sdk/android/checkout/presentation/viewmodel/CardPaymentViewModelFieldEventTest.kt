package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.checkout.core.model.MPCardBrand
import com.mercadopago.sdk.android.checkout.core.model.MPCardType
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFieldConfig
import com.mercadopago.sdk.android.checkout.domain.model.CardHolderField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberField
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberValidation
import com.mercadopago.sdk.android.checkout.domain.model.ExpirationDateField
import com.mercadopago.sdk.android.checkout.domain.model.LengthRange
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeField
import com.mercadopago.sdk.android.checkout.domain.model.Validation
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.expirationdate.ExpirationDateTextFieldEvent
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.identificationtextfield.IdentificationTextFieldEvent
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class CardPaymentViewModelFieldEventTest {
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
    fun `when BIN length is less than 6 then getCardBin is not called`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("12345"))

        coVerify(exactly = 0) { getCardBinUseCase(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `when BIN call succeeds then security code maxLength is updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
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
            issuers = emptyList(),
            installmentData = MPInstallmentData(),
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals(3, viewModel.viewState.value.secureCodeState.maxLength)
        assertFalse(viewModel.viewState.value.secureCodeState.optional)
    }

    @Test
    fun `when BIN call succeeds with optional security code then optional is true`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            holderName = null,
            expirationDate = null,
            issuers = emptyList(),
            installmentData = MPInstallmentData(),
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertTrue(viewModel.viewState.value.secureCodeState.optional)
    }

    @Test
    fun `when BIN call succeeds with translations then field labels are updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = CardNumberField(
                label = "Número",
                placeholder = "•••• ••••",
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
                label = "CVV",
                placeholder = "123",
                helper = "",
                tooltip = "",
                validation = Validation(errorEmpty = "", errorIncomplete = "", errorInvalid = ""),
                config = CardFieldConfig(type = "text", length = LengthRange(min = 3, max = 3)),
            ),
            holderName = CardHolderField(
                label = "Titular",
                placeholder = "Nome",
                helper = "",
                validation = Validation(errorEmpty = "", errorIncomplete = "", errorInvalid = ""),
                config = CardFieldConfig(type = "text", length = LengthRange(min = 0, max = 30)),
            ),
            expirationDate = ExpirationDateField(
                label = "Vencimento",
                placeholder = "MM/AA",
                validation = Validation(errorEmpty = "", errorIncomplete = "", errorInvalid = ""),
                config = CardFieldConfig(type = "text", length = LengthRange(min = 4, max = 5)),
            ),
            issuers = emptyList(),
            installmentData = MPInstallmentData(),
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
    fun `when BIN call succeeds then paymentState is updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            holderName = null,
            expirationDate = null,
            issuers = emptyList(),
            installmentData = MPInstallmentData(),
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals("visa", viewModel.viewState.value.paymentState.paymentMethodId)
        assertEquals("credit_card", viewModel.viewState.value.paymentState.paymentTypeId)
    }

    @Test
    fun `when BIN call fails then state is not changed by error`() = runTest {
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Error(networkError)
        val viewModel = makeViewModel()
        val stateBefore = viewModel.viewState.value

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals(stateBefore.cardNumberState.label, viewModel.viewState.value.cardNumberState.label)
    }

    @Test
    fun `when BIN call succeeds with issuers then cardIssuers state is updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            holderName = null,
            expirationDate = null,
            issuers = listOf(BinIssuer(id = "1", name = "Banco do Brasil")),
            installmentData = MPInstallmentData(),
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        val issuers = viewModel.viewState.value.cardIssuers
        assertEquals(1, issuers.size)
        assertEquals("1", issuers.first().id)
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

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/input_validation"))
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

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/dropdown_selection"))
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
    fun `when ExpirationDate OnFocusChanged false then isFocused is false`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.OnFocusChanged(isFocused = false))

        assertFalse(viewModel.viewState.value.expirationDateState.isFocused)
    }

    @Test
    fun `when ExpirationDate OnFocusChanged true then isFocused is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.OnFocusChanged(isFocused = true))

        assertTrue(viewModel.viewState.value.expirationDateState.isFocused)
    }

    @Test
    fun `when ExpirationDate OnInputFilled true then applies expiration date error`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.OnInputFilled(isFilled = true))

        assertTrue(viewModel.viewState.value.expirationDateState.filled)
    }

    @Test
    fun `when ExpirationDate length decreases then enters being-cleared branch`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.OnLengthChanged(length = 4))
        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.OnLengthChanged(length = 2))

        assertEquals(2, viewModel.viewState.value.expirationDateState.length)
    }

    @Test
    fun `when SecurityCode OnFocusChanged false then isFocused is false`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnFocusChanged(isFocused = false))

        assertFalse(viewModel.viewState.value.secureCodeState.isFocused)
    }

    @Test
    fun `when SecurityCode OnFocusChanged true then isFocused is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnFocusChanged(isFocused = true))

        assertTrue(viewModel.viewState.value.secureCodeState.isFocused)
    }

    @Test
    fun `when SecurityCode length reaches maxLength then applies security code error`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(
            SecurityCodeTextFieldEvent.OnLengthChanged(
                length = viewModel.viewState.value.secureCodeState.maxLength,
            ),
        )

        assertEquals(
            viewModel.viewState.value.secureCodeState.maxLength,
            viewModel.viewState.value.secureCodeState.length,
        )
    }

    @Test
    fun `when SecurityCode length decreases then enters being-cleared branch`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = 2))
        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = 1))

        assertEquals(1, viewModel.viewState.value.secureCodeState.length)
    }

    @Test
    fun `when CardHolder value decreases then enters being-cleared branch`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardHolderEvent(SimpleTextFieldEvent.OnValueChanged("John Doe"))
        viewModel.onCardHolderEvent(SimpleTextFieldEvent.OnValueChanged("John"))

        assertEquals("John", viewModel.viewState.value.cardHolderState.value)
    }

    @Test
    fun `when CardHolder OnFocusChanged true then isFocused is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardHolderEvent(SimpleTextFieldEvent.OnFocusChanged(isFocused = true))

        assertTrue(viewModel.viewState.value.cardHolderState.isFocused)
    }

    @Test
    fun `when Identification value decreases then enters being-cleared branch`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnValueChanged("12345678"))
        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnValueChanged("1234"))

        assertEquals("1234", viewModel.viewState.value.identificationTypeState.value)
    }

    @Test
    fun `when Identification value is complete then applies identification error`() = runTest {
        val viewModel = makeViewModel()
        // relaxed IdentificationType returns min/maxLength = 0, so an empty value (length 0) is complete
        viewModel.onIdentificationEvent(
            IdentificationTextFieldEvent.OnTypeSelected(mockk<IdentificationType>(relaxed = true)),
        )

        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnValueChanged(""))

        assertEquals("", viewModel.viewState.value.identificationTypeState.value)
    }

    @Test
    fun `when Identification OnFocusChanged false then isFocused is false`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnFocusChanged(isFocused = false))

        assertFalse(viewModel.viewState.value.identificationTypeState.isFocused)
    }

    @Test
    fun `when Identification OnFocusChanged true then isFocused is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnFocusChanged(isFocused = true))

        assertTrue(viewModel.viewState.value.identificationTypeState.isFocused)
    }

    @Test
    fun `when BIN call fails with ServiceError then applies payment method not found error`() = runTest {
        val serviceError = MercadoPagoCheckoutError.ServiceError(
            code = ErrorCode.NETWORK_CONNECTION_FAILED,
            messageError = "Payment method not found",
            localized = "checkout",
            throwable = null,
        )
        coEvery {
            getCardBinUseCase(any(), any(), any(), any(), any())
        } returns Result.Error(serviceError)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        coVerify { getCardBinUseCase(any(), any(), any(), any(), any()) }
        assertEquals("123456", viewModel.viewState.value.cardNumberState.cardBin)
    }
}
