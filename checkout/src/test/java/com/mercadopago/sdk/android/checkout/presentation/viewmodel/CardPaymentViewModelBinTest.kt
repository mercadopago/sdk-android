package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.core.model.CardBrand
import com.mercadopago.sdk.android.checkout.core.model.CardType
import com.mercadopago.sdk.android.checkout.core.model.CheckoutType
import com.mercadopago.sdk.android.checkout.core.model.PaymentMethod
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.BinSecurityCodeConfig
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFormTranslations
import com.mercadopago.sdk.android.checkout.domain.model.CardNumberConfig
import com.mercadopago.sdk.android.checkout.domain.model.FieldTranslation
import com.mercadopago.sdk.android.checkout.domain.model.InstallmentsFieldTranslation
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeFieldTranslation
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.InitializeCardFormUseCase
import com.mercadopago.sdk.android.checkout.presentation.factory.CardPaymentScreenStateFactory
import com.mercadopago.sdk.android.checkout.presentation.state.CardNumberErrorType
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledFormContextUseCase
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.cardnumber.CardNumberTextFieldEvent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
internal class CardPaymentViewModelBinTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockMPAnalytics = mockk<MPAnalytics>(relaxed = true)
    private val stateFactory = mockk<CardPaymentScreenStateFactory>(relaxed = true)
    private val getCardBinUseCase = mockk<GetCardBinUseCase>(relaxed = true)
    private val initializeCardFormUseCase = mockk<InitializeCardFormUseCase>(relaxed = true)
    private val generateTokenUseCase = mockk<GenerateTokenUseCase>(relaxed = true)
    private val cancelledFormContextUseCase = mockk<CancelledFormContextUseCase>(relaxed = true)

    private val checkoutConfiguration = CheckoutConfiguration(
        checkoutType = mockk<CheckoutType.CardForm>(relaxed = true),
        paymentMethods = listOf(
            PaymentMethod.Card(
                allowedTypes = listOf(CardType.CREDIT),
                allowedBrands = listOf(CardBrand.Visa),
            ),
        ),
    )

    private val fullTranslations = CardFormTranslations(
        cardNumber = FieldTranslation(label = "Número", placeholder = "•••• ••••", helper = "", error = null),
        cardHolderName = FieldTranslation(label = "Titular", placeholder = "Nome", helper = "", error = null),
        expirationDate = FieldTranslation(label = "Vencimento", placeholder = "MM/AA", helper = "", error = null),
        securityCode = SecurityCodeFieldTranslation(
            label = "CVV",
            placeholder = "123",
            helper = "",
            tooltip = "3 dígitos no verso",
            error = null,
        ),
        identification = FieldTranslation(label = "CPF", placeholder = "000.000.000-00", helper = "", error = null),
        installments = InstallmentsFieldTranslation(label = "Parcelas", installmentsSelectorPlaceholder = "Selecione"),
    )

    @Before
    fun setup() {
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns mockMPAnalytics
        every { stateFactory.getGenericErrorMessage() } returns "Error"
        mockkObject(CheckoutCallbackHolder)
        every { CheckoutCallbackHolder.notify(any()) } returns Unit
    }

    @After
    fun tearDown() {
        unmockkObject(MPAnalytics.Companion)
        unmockkObject(CheckoutCallbackHolder)
    }

    private fun makeViewModel() = CardPaymentViewModel(
        stateFactory = stateFactory,
        checkoutConfiguration = checkoutConfiguration,
        getCardBinUseCase = getCardBinUseCase,
        initializeCardFormUseCase = initializeCardFormUseCase,
        generateTokenUseCase = generateTokenUseCase,
        cancelledFormContextUseCase = cancelledFormContextUseCase,
    )

    @Test
    fun `when BIN call succeeds then card number maxLength is updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = CardNumberConfig(length = 16, validation = "standard", mask = null),
            securityCode = BinSecurityCodeConfig(mode = "mandatory", length = 3, cardLocation = "back"),
            issuers = listOf(BinIssuer(id = 1L, name = "Banco", secureThumbnail = null)),
            quotas = listOf(
                Quota(quantity = 1, installmentAmount = "100", totalAmount = "100", label = "1x", discountRate = 0.0),
            ),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals(16, viewModel.viewState.value.cardNumberState.maxLength)
    }

    @Test
    fun `when BIN call succeeds then security code maxLength is updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = BinSecurityCodeConfig(mode = "mandatory", length = 3, cardLocation = "back"),
            issuers = emptyList(),
            quotas = emptyList(),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals(3, viewModel.viewState.value.secureCodeState.maxLength)
        assertEquals(false, viewModel.viewState.value.secureCodeState.optional)
    }

    @Test
    fun `when BIN call succeeds with optional security code then optional is true`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = BinSecurityCodeConfig(mode = "optional", length = 0, cardLocation = "back"),
            issuers = emptyList(),
            quotas = emptyList(),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals(true, viewModel.viewState.value.secureCodeState.optional)
    }

    @Test
    fun `when BIN call succeeds with translations then field labels are updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = emptyList(),
            translations = fullTranslations,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        val state = viewModel.viewState.value
        assertEquals("Número", state.cardNumberState.label)
        assertEquals("•••• ••••", state.cardNumberState.placeHolder)
        assertEquals("Titular", state.cardHolderState.label)
        assertEquals("Nome", state.cardHolderState.placeHolder)
        assertEquals("Vencimento", state.expirationDateState.label)
        assertEquals("MM/AA", state.expirationDateState.placeHolder)
        assertEquals("CVV", state.secureCodeState.label)
        assertEquals("123", state.secureCodeState.placeHolder)
        assertEquals("3 dígitos no verso", state.secureCodeState.messageTooltip)
    }

    @Test
    fun `when BIN call succeeds with translations then translations stored in state`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = emptyList(),
            translations = fullTranslations,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertNotNull(viewModel.viewState.value.cardFormTranslations)
    }

    @Test
    fun `when BIN call succeeds without translations then cardFormTranslations is null`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = emptyList(),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertNull(viewModel.viewState.value.cardFormTranslations)
    }

    @Test
    fun `when BIN call succeeds then paymentState is updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = emptyList(),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals("visa", viewModel.viewState.value.paymentState.paymentMethodId)
        assertEquals("credit_card", viewModel.viewState.value.paymentState.paymentTypeId)
    }

    @Test
    fun `when BIN call fails then state is not changed by error`() = runTest {
        val error = MercadoPagoCheckoutError.NetworkError(
            code = ErrorCode.NETWORK_CONNECTION_FAILED,
            messageError = "Network error",
            localized = "bin",
            throwable = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Error(error)

        val viewModel = makeViewModel()
        val stateBefore = viewModel.viewState.value
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals(stateBefore.cardFormTranslations, viewModel.viewState.value.cardFormTranslations)
        assertEquals(stateBefore.cardNumberState.label, viewModel.viewState.value.cardNumberState.label)
    }

    @Test
    fun `when BIN length is less than 6 then getCardBin is not called`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("12345"))

        coVerify(exactly = 0) { getCardBinUseCase(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `when BIN call succeeds with issuers then cardIssuers state is updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = listOf(BinIssuer(id = 1L, name = "Banco do Brasil", secureThumbnail = "https://thumb.png")),
            quotas = emptyList(),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        val issuers = viewModel.viewState.value.cardIssuers
        assertEquals(1, issuers.size)
        assertEquals("1", issuers.first().id)
        assertEquals("https://thumb.png", issuers.first().thumbnail)
    }

    @Test
    fun `when BIN call succeeds with issuers then image is set on card number state`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = listOf(BinIssuer(id = 1L, name = "Banco", secureThumbnail = "https://thumb.png")),
            quotas = emptyList(),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals("https://thumb.png", viewModel.viewState.value.cardNumberState.image)
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
        assertEquals(true, installmentsState.showList)
        assertEquals(2, installmentsState.installments.size)
        assertEquals(1, installmentsState.installments.first().instalments)
        assertEquals(100.00f, installmentsState.installments.first().installmentAmount)
    }

    @Test
    fun `when BIN call succeeds with empty quotas then installments showList is false`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = emptyList(),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertEquals(false, viewModel.viewState.value.installmentsState.showList)
    }

    @Test
    fun `when BIN returns brand not in allowedBrands then CardBrandNotAccepted error is set`() = runTest {
        val data = CardBinData(
            id = "amex",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = emptyList(),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        val errorTypes = viewModel.viewState.value.cardNumberState.errorTypes
        assertEquals(true, errorTypes.any { it is CardNumberErrorType.CardBrandNotAccepted })
    }

    @Test
    fun `when BIN returns type not in allowedTypes then CardTypeNotAccepted error is set`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "debit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = emptyList(),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        val errorTypes = viewModel.viewState.value.cardNumberState.errorTypes
        assertEquals(true, errorTypes.any { it is CardNumberErrorType.CardTypeNotAccepted })
    }

    @Test
    fun `when BIN returns null translations then existing translations in state are preserved`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = emptyList(),
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)

        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        assertNull(viewModel.viewState.value.cardFormTranslations)
    }
}
