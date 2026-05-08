package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.analytics.domain.models.Metric
import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.DocumentTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.FieldTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsHeaderTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.InstallmentsTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.LengthConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeTranslations
import com.mercadopago.sdk.android.checkout.data.remote.response.Translations
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.BinIssuer
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.Quota
import com.mercadopago.sdk.android.checkout.domain.usecase.GetCardBinUseCase
import com.mercadopago.sdk.android.checkout.presentation.state.MessageError
import com.mercadopago.sdk.android.checkout.presentation.usecase.GenerateTokenUseCase
import com.mercadopago.sdk.android.checkout.utils.MainDispatcherRule
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
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
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Suppress("LargeClass")
@OptIn(ExperimentalCoroutinesApi::class)
internal class CardPaymentViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockMPAnalytics = mockk<MPAnalytics>(relaxed = true)
    private val getCardBinUseCase = mockk<GetCardBinUseCase>(relaxed = true)
    private val generateTokenUseCase = mockk<GenerateTokenUseCase>(relaxed = true)

    private val networkError = MercadoPagoCheckoutError.NetworkError(
        code = ErrorCode.NETWORK_CONNECTION_FAILED,
        messageError = "Connection failed",
        localized = "checkout",
        throwable = null,
    )

    private val fullTranslations = Translations(
        cardFormTitle = "",
        cardFormFooterButtonLabel = "",
        cardNumber = FieldTranslations(
            label = "Número",
            placeholder = "•••• ••••",
            errorEmptyField = "",
            errorIncompleteField = "",
            errorInvalidField = "",
        ),
        holderName = FieldTranslations(
            label = "Titular",
            placeholder = "Nome",
            errorEmptyField = "",
            errorIncompleteField = "",
            errorInvalidField = "",
        ),
        expirationDate = FieldTranslations(
            label = "Vencimento",
            placeholder = "MM/AA",
            errorEmptyField = "",
            errorIncompleteField = "",
            errorInvalidField = "",
        ),
        securityCode = SecurityCodeTranslations(
            label = "CVV",
            placeholder = "123",
            tooltip = "3 dígitos no verso",
            errorEmptyField = "",
            errorIncompleteField = "",
        ),
        document = DocumentTranslations(
            label = "",
            errorEmptyField = "",
            errorIncompleteField = "",
            errorInvalidField = "",
        ),
        installments = InstallmentsTranslations(
            header = InstallmentsHeaderTranslations(chevron = "", radio = "", title = ""),
            interestFreeLabel = "",
            totalLabel = "",
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
        initData: CardFormInitializationOutput = mockk(relaxed = true),
    ) = CardPaymentViewModel(
        initializationOutput = initData,
        getCardBinUseCase = getCardBinUseCase,
        generateTokenUseCase = generateTokenUseCase,
    )

    // ── card number events ────────────────────────────────────────────────────

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

    // ── BIN events ────────────────────────────────────────────────────────────

    @Test
    fun `when BIN length is less than 6 then getCardBin is not called`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("12345"))

        coVerify(exactly = 0) { getCardBinUseCase(any(), any(), any(), any(), any()) }
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
                    installments = 1,
                    installmentAmount = BigDecimal.valueOf(100.0),
                    totalAmount = BigDecimal.valueOf(100.0),
                ),
            ),
            installmentsSelectionType = null,
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
            securityCode = SecurityCodeConfig(type = "text", length = 3, mode = "mandatory", cardLocation = "back"),
            issuers = emptyList(),
            quotas = emptyList(),
            installmentsSelectionType = null,
            translations = null,
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
            securityCode = SecurityCodeConfig(type = "text", length = 0, mode = "optional", cardLocation = "back"),
            issuers = emptyList(),
            quotas = emptyList(),
            installmentsSelectionType = null,
            translations = null,
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
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = emptyList(),
            installmentsSelectionType = null,
            translations = fullTranslations,
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
            issuers = emptyList(),
            quotas = emptyList(),
            installmentsSelectionType = null,
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
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = listOf(
                BinIssuer(id = 1L, name = "Banco do Brasil", secureThumbnail = "https://thumb.png"),
            ),
            quotas = emptyList(),
            installmentsSelectionType = null,
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
    fun `when BIN call succeeds with quotas then installments state is updated`() = runTest {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = listOf(
                Quota(
                    installments = 1,
                    installmentAmount = BigDecimal.valueOf(100.0),
                    totalAmount = BigDecimal.valueOf(100.0),
                ),
                Quota(
                    installments = 3,
                    installmentAmount = BigDecimal.valueOf(34.0),
                    totalAmount = BigDecimal.valueOf(102.0),
                ),
            ),
            installmentsSelectionType = null,
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        val viewModel = makeViewModel()

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))

        val installmentsState = viewModel.viewState.value.installmentsState
        assertTrue(installmentsState.showList)
        assertEquals(2, installmentsState.installments.size)
        assertEquals(1, installmentsState.installments.first().installments)
    }

    // ── expiration date events ────────────────────────────────────────────────

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

    // ── security code events ──────────────────────────────────────────────────

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

    // ── card holder events ────────────────────────────────────────────────────

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

    // ── identification events ─────────────────────────────────────────────────

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

    // ── tooltip and message ───────────────────────────────────────────────────

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

    // ── back pressed ──────────────────────────────────────────────────────────

    @Test
    fun `when onBackPressed then tracks user_canceled_error event`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onBackPressed()

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/user_canceled_error"))
    }

    // ── submit ────────────────────────────────────────────────────────────────

    @Test
    fun `when onSubmit with no errors then calls generateTokenUseCase`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel()

        viewModel.onSubmit()

        coVerify { generateTokenUseCase(any(), any(), any(), any()) }
    }

    @Test
    fun `when onSubmit succeeds then tracks submit event`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel()

        viewModel.onSubmit()

        val metricSlot = slot<Metric>()
        verify { mockMPAnalytics.trackMetric(capture(metricSlot)) }
        assertTrue(metricSlot.captured.path.endsWith("/submit"))
    }

    @Test
    fun `when onSubmit is called then isLoading is false after completion`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModel()

        viewModel.onSubmit()

        assertFalse(viewModel.viewState.value.isLoading)
    }

    // ── installments navigation ───────────────────────────────────────────────

    private fun makeViewModelWithInstallments(): CardPaymentViewModel {
        val data = CardBinData(
            id = "visa",
            paymentTypeId = "credit_card",
            cardNumber = null,
            securityCode = null,
            issuers = emptyList(),
            quotas = listOf(
                Quota(
                    installments = 1,
                    installmentAmount = BigDecimal.valueOf(100.0),
                    totalAmount = BigDecimal.valueOf(100.0),
                ),
                Quota(
                    installments = 3,
                    installmentAmount = BigDecimal.valueOf(34.0),
                    totalAmount = BigDecimal.valueOf(102.0),
                ),
            ),
            installmentsSelectionType = null,
            translations = null,
        )
        coEvery { getCardBinUseCase(any(), any(), any(), any(), any()) } returns Result.Success(data)
        return makeViewModel().also {
            it.onCardNumberEvent(CardNumberTextFieldEvent.OnBinChanged("123456"))
        }
    }

    @Test
    fun `when onSubmit and showList true then tokenizes before navigating`() = runTest {
        coEvery {
            generateTokenUseCase(any(), any(), any(), any())
        } returns Result.Success(CardToken(token = "token_abc"))
        val viewModel = makeViewModelWithInstallments()

        viewModel.onSubmit()

        coVerify { generateTokenUseCase(any(), any(), any(), any()) }
    }

    // ── isBeingCleared branches ───────────────────────────────────────────────

    @Test
    fun `when card number length goes from non-zero to zero then clears errorTypes`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnLengthChanged(length = 4))

        viewModel.onCardNumberEvent(CardNumberTextFieldEvent.OnLengthChanged(length = 0))

        assertTrue(viewModel.viewState.value.cardNumberState.errorTypes.isEmpty())
    }

    @Test
    fun `when expiration date length goes from non-zero to zero then no error is shown`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.OnLengthChanged(length = 3))

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.OnLengthChanged(length = 0))

        assertEquals("", viewModel.viewState.value.expirationDateState.error)
    }

    @Test
    fun `when security code length goes from non-zero to zero then no error is shown`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = 2))

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = 0))

        assertEquals("", viewModel.viewState.value.secureCodeState.error)
    }

    @Test
    fun `when security code length reaches maxLength then applies validation`() = runTest {
        val viewModel = makeViewModel()
        val maxLength = viewModel.viewState.value.secureCodeState.maxLength

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnLengthChanged(length = maxLength))

        assertEquals(maxLength, viewModel.viewState.value.secureCodeState.length)
    }

    @Test
    fun `when card holder value goes from filled to empty then no error is shown`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onCardHolderEvent(SimpleTextFieldEvent.OnValueChanged("John Doe"))

        viewModel.onCardHolderEvent(SimpleTextFieldEvent.OnValueChanged(""))

        assertEquals("", viewModel.viewState.value.cardHolderState.error)
    }

    @Test
    fun `when identification value goes from filled to empty then no error is shown`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnValueChanged("12345678"))

        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnValueChanged(""))

        assertEquals("", viewModel.viewState.value.identificationTypeState.error)
    }

    @Test
    fun `when identification value reaches maxLength then validation is applied`() = runTest {
        val viewModel = makeViewModel()
        viewModel.onIdentificationEvent(
            IdentificationTextFieldEvent.OnTypeSelected(
                IdentificationType(id = "CPF", minLength = 11, maxLength = 11, name = "CPF"),
            ),
        )

        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnValueChanged("12345678901"))

        assertEquals("12345678901", viewModel.viewState.value.identificationTypeState.value)
    }

    // ── focus gain branches ───────────────────────────────────────────────────

    @Test
    fun `when ExpirationDate OnFocusChanged true then isFocused is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.OnFocusChanged(isFocused = true))

        assertTrue(viewModel.viewState.value.expirationDateState.isFocused)
    }

    @Test
    fun `when ExpirationDate OnFocusChanged false then isFocused is false`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onExpirationDateEvent(ExpirationDateTextFieldEvent.OnFocusChanged(isFocused = false))

        assertFalse(viewModel.viewState.value.expirationDateState.isFocused)
    }

    @Test
    fun `when SecurityCode OnFocusChanged true then isFocused is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnFocusChanged(isFocused = true))

        assertTrue(viewModel.viewState.value.secureCodeState.isFocused)
    }

    @Test
    fun `when SecurityCode OnFocusChanged false then isFocused is false`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onSecurityCodeEvent(SecurityCodeTextFieldEvent.OnFocusChanged(isFocused = false))

        assertFalse(viewModel.viewState.value.secureCodeState.isFocused)
    }

    @Test
    fun `when CardHolder OnFocusChanged true then isFocused is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onCardHolderEvent(SimpleTextFieldEvent.OnFocusChanged(isFocused = true))

        assertTrue(viewModel.viewState.value.cardHolderState.isFocused)
    }

    @Test
    fun `when Identification OnFocusChanged true then isFocused is true`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnFocusChanged(isFocused = true))

        assertTrue(viewModel.viewState.value.identificationTypeState.isFocused)
    }

    @Test
    fun `when Identification OnFocusChanged false then isFocused is false`() = runTest {
        val viewModel = makeViewModel()

        viewModel.onIdentificationEvent(IdentificationTextFieldEvent.OnFocusChanged(isFocused = false))

        assertFalse(viewModel.viewState.value.identificationTypeState.isFocused)
    }
}
