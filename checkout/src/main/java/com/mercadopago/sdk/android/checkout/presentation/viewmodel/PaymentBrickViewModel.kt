package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.buildScreensParam
import com.mercadopago.sdk.android.checkout.core.model.internal.hasReviewAndConfirm
import com.mercadopago.sdk.android.checkout.core.model.internal.toCheckoutType
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.extensions.fold
import com.mercadopago.sdk.android.checkout.domain.mapper.toMPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickFooterOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeState
import com.mercadopago.sdk.android.checkout.domain.model.isTicket
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.model.toProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchMethodSelectionScreenUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickInitializationUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GenerateTokenWithCardIdUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.GetSecurityCodeScreenUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.extensions.extractAmountDigits
import com.mercadopago.sdk.android.checkout.presentation.extensions.parseFormattedAmount
import com.mercadopago.sdk.android.checkout.presentation.mapper.toScreenState
import com.mercadopago.sdk.android.checkout.presentation.shared.FooterState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import com.mercadopago.sdk.android.checkout.presentation.usecase.CancelledPaymentContextUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Suppress("LongParameterList", "TooManyFunctions")
internal class PaymentBrickViewModel(
    private val checkoutConfiguration: CheckoutConfiguration?,
    private val fetchInitializationUseCase: FetchPaymentBrickInitializationUseCase,
    private val processOrderUseCase: ProcessOrderUseCase,
    private val getSecurityCodeScreenUseCase: GetSecurityCodeScreenUseCase,
    private val fetchMethodSelectionScreenUseCase: FetchMethodSelectionScreenUseCase,
    private val generateTokenWithCardIdUseCase: GenerateTokenWithCardIdUseCase,
    private val cancelledPaymentContextUseCase: CancelledPaymentContextUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow(PaymentBrickScreenState(isLoading = true))
    val viewState: StateFlow<PaymentBrickScreenState> = _viewState.asStateFlow()

    private val _viewEvent = MutableStateFlow<PaymentBrickViewEvent?>(null)
    val viewEvent: StateFlow<PaymentBrickViewEvent?> = _viewEvent.asStateFlow()

    private var initializationOutput: PaymentBrickInitializationOutput? = null
    private var pendingInstallmentPayment: PendingInstallmentPayment? = null

    init {
        loadInitialization()
    }

    private fun loadInitialization() {
        val params = checkoutConfiguration?.buildInitializationParams() ?: run {
            _viewState.value = PaymentBrickScreenState(isLoading = false)
            return
        }
        viewModelScope.launch {
            _viewState.value = PaymentBrickScreenState(isLoading = true)
            when (val result = fetchInitializationUseCase(params)) {
                is Result.Success -> {
                    initializationOutput = result.data
                    _viewState.value = result.data.toScreenState()
                }
                is Result.Error -> {
                    _viewEvent.value = PaymentBrickViewEvent.OnFailure(result.error)
                }
            }
        }
    }

    fun processOrder(
        cardId: String,
        token: String = "",
    ) {
        val method = findMethodByOptionId(cardId)
        val paymentType = checkoutConfiguration?.checkoutType as? MPCheckoutType.Payment
        if (method == null || paymentType == null) return
        if (showInstallments(method, token.takeIf(String::isNotEmpty))) return

        val params = method.toProcessOrderParams(
            order = paymentType.order,
            checkoutType = checkoutConfiguration.toCheckoutType(),
            token = token,
            amount = initializationOutput?.footer?.totalAmount?.extractAmountDigits(),
        )
        if (checkoutConfiguration.hasReviewAndConfirm()) {
            _viewEvent.value = PaymentBrickViewEvent.OnPaymentReadyForReview(params)
        } else {
            viewModelScope.launch {
                _viewState.value = _viewState.value.copy(isLoading = true)
                processOrderUseCase(params).fold(
                    onSuccess = { orderOutput ->
                        _viewState.value = _viewState.value.copy(isLoading = false)
                        CheckoutCallbackHolder.notify(
                            MercadoPagoCheckoutResult.Success(
                                MPPaymentData.Payment(
                                    orderId = orderOutput.id,
                                    orderStatus = orderOutput.status,
                                    paymentMethodId = method.cardData?.paymentMethodId.orEmpty(),
                                    paymentTypeId = method.cardData?.paymentTypeId.orEmpty(),
                                ),
                            ),
                        )
                    },
                    onError = { error ->
                        _viewState.value = _viewState.value.copy(isLoading = false)
                        CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(error))
                    },
                )
            }
        }
    }

    fun onOptionSelected(
        optionId: String,
    ) {
        val method = findMethodByOptionId(optionId)
        val cardData = method?.cardData
        val securityCodeConfig = cardData?.let { data ->
            getSecurityCodeScreenUseCase(data.securityCode)?.let { result ->
                method?.toSecurityCodeScreenConfig(
                    footer = initializationOutput?.footer,
                    result = result,
                )
            }
        }

        when {
            securityCodeConfig != null -> {
                _viewEvent.value = PaymentBrickViewEvent.OnSecurityCodeRequired(securityCodeConfig)
            }
            method != null && cardData?.installments?.quotas?.isNotEmpty() == true -> {
                showInstallments(method)
            }
            cardData != null &&
                checkoutConfiguration.hasReviewAndConfirm() &&
                cardData.installments?.quotas.isNullOrEmpty() -> {
                tokenizeAndProcessOrder(cardData.id)
            }
            method?.isTicket == true -> {
                val screenData = fetchMethodSelectionScreenUseCase(method)
                _viewEvent.value = if (screenData != null) {
                    PaymentBrickViewEvent.OnOfflineMethodSelected(screenData)
                } else {
                    PaymentBrickViewEvent.OnOptionSelected(optionId)
                }
            }
            else -> {
                _viewEvent.value = PaymentBrickViewEvent.OnOptionSelected(optionId)
            }
        }
    }

    internal fun processOrder(
        params: ProcessOrderParams,
    ) {
        if (checkoutConfiguration.hasReviewAndConfirm()) {
            _viewEvent.value = PaymentBrickViewEvent.OnPaymentReadyForReview(params)
        } else {
            viewModelScope.launch {
                _viewState.value = _viewState.value.copy(isLoading = true)
                processOrderUseCase(params).fold(
                    onSuccess = { orderOutput ->
                        _viewState.value = _viewState.value.copy(isLoading = false)
                        val paymentData = MPPaymentData.Payment(
                            orderId = orderOutput.id,
                            orderStatus = orderOutput.status,
                            paymentMethodId = params.paymentMethodId,
                            paymentTypeId = params.paymentMethodType,
                        )
                        CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(paymentData))
                    },
                    onError = { error ->
                        CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(error))
                        _viewState.value = _viewState.value.copy(isLoading = false)
                    },
                )
            }
        }
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
        _viewState.value = _viewState.value.copy(isLoading = false)
    }

    fun onBackPressed() {
        _viewEvent.value = PaymentBrickViewEvent.OnUserCancelled(cancelledPaymentContextUseCase())
    }

    fun onInstallmentConfirmed(
        installment: Int,
    ) {
        if (_viewState.value.isLoading) return

        val pendingPayment = pendingInstallmentPayment
        val installmentAmount = _viewState.value.pendingInstallmentData
            ?.quotas
            ?.firstOrNull { it.installments == installment }
            ?.installmentAmount
            ?.toPlainString()
        if (pendingPayment == null || installmentAmount == null) return

        _viewState.value = _viewState.value.copy(isLoading = true)

        val onTokenReady: (String) -> Unit = { token ->
            processConfirmedInstallment(
                cardId = pendingPayment.cardId,
                token = token,
                installment = installment,
                installmentAmount = installmentAmount,
            )
        }

        pendingPayment.token?.let(onTokenReady)
            ?: tokenizeCard(pendingPayment.cardId, onTokenReady)
    }

    fun markScreenPresented(
        screen: Screen,
    ) {
        cancelledPaymentContextUseCase.markScreenPresented(screen)
    }

    fun onTokenError() {
        _viewState.value = _viewState.value.copy(isLoading = false)
    }

    private fun tokenizeAndProcessOrder(
        cardId: String,
    ) {
        if (_viewState.value.isLoading || _viewEvent.value != null) return
        _viewState.value = _viewState.value.copy(isLoading = true)
        tokenizeCard(cardId) { token ->
            _viewState.value = _viewState.value.copy(isLoading = false)
            processOrder(cardId = cardId, token = token)
        }
    }

    private fun tokenizeCard(
        cardId: String,
        onSuccess: (String) -> Unit,
    ) {
        viewModelScope.launch {
            generateTokenWithCardIdUseCase(cardId).fold(
                onSuccess = onSuccess,
                onError = { handleTokenizationError() },
            )
        }
    }

    private fun handleTokenizationError() {
        _viewState.value = _viewState.value.copy(isLoading = false)
        _viewEvent.value = PaymentBrickViewEvent.OnTokenizationError
    }

    private fun processConfirmedInstallment(
        cardId: String,
        token: String,
        installment: Int,
        installmentAmount: String,
    ) {
        val method = findMethodByOptionId(cardId)
        val paymentType = checkoutConfiguration?.checkoutType as? MPCheckoutType.Payment
        if (method == null || paymentType == null) {
            _viewState.value = _viewState.value.copy(isLoading = false)
            return
        }

        pendingInstallmentPayment = PendingInstallmentPayment(cardId, token)
        val params = method.toProcessOrderParams(
            order = paymentType.order,
            checkoutType = checkoutConfiguration.toCheckoutType(),
            token = token,
            installments = installment,
            amount = initializationOutput?.footer?.totalAmount?.extractAmountDigits(),
        ).copy(installmentAmount = installmentAmount)

        processOrder(params)
    }

    private fun findMethodByOptionId(
        optionId: String,
    ): PaymentMethodOutput? =
        initializationOutput?.sections
            ?.flatMap { it.methods }
            ?.firstOrNull { method ->
                if (method.cardData != null) method.cardData.id == optionId else method.type == optionId
            }

    private fun showInstallments(
        method: PaymentMethodOutput,
        token: String? = null,
    ): Boolean {
        val cardData = method.cardData
        val installmentData = cardData?.installments
            ?.takeIf { installments -> installments.quotas.isNotEmpty() }
            ?.toMPInstallmentData()

        return if (cardData != null && installmentData != null) {
            pendingInstallmentPayment = PendingInstallmentPayment(cardData.id, token)
            _viewState.value = _viewState.value.copy(pendingInstallmentData = installmentData)
            _viewEvent.value = PaymentBrickViewEvent.OnInstallmentsRequired(
                installmentData = installmentData,
                paymentData = MPPaymentData.Payment(
                    orderId = UNPROCESSED_ORDER_VALUE,
                    orderStatus = UNPROCESSED_ORDER_VALUE,
                    paymentMethodId = cardData.paymentMethodId,
                    paymentTypeId = cardData.paymentTypeId,
                ),
            )
            true
        } else {
            false
        }
    }

    private fun PaymentMethodOutput.toSecurityCodeScreenConfig(
        footer: PaymentBrickFooterOutput?,
        result: Pair<String, SecurityCodeState>,
    ): SecurityCodeScreenConfig {
        val cardData = requireNotNull(cardData)
        return SecurityCodeScreenConfig(
            title = result.first,
            securityCodeState = result.second,
            footerState = run {
                val amount = footer?.totalAmount?.parseFormattedAmount()
                FooterState(
                    title = footer?.totalLabel.orEmpty(),
                    currencySymbol = amount?.currencySymbol.orEmpty(),
                    amountIntegerPart = amount?.integerPart.orEmpty(),
                    amountDecimalPart = amount?.decimalPart.orEmpty(),
                    buttonLabel = cardData.securityCode.screen?.buttonLabel,
                    isVisible = true,
                )
            },
            cardId = cardData.id,
            cardTitle = title,
            cardDescription = subtitle,
            cardImageUrl = iconUrl,
            paymentMethodId = cardData.paymentMethodId,
            paymentTypeId = cardData.paymentTypeId,
            issuerId = cardData.issuerId.toString(),
        )
    }

    private fun CheckoutConfiguration.buildInitializationParams(): FetchPaymentBrickInitializationParams? {
        val paymentType = checkoutType as? MPCheckoutType.Payment ?: return null
        return FetchPaymentBrickInitializationParams(
            orderId = paymentType.order.orderId,
            clientToken = paymentType.order.clientToken,
            screens = buildScreensParam(),
        )
    }

    private companion object {
        const val UNPROCESSED_ORDER_VALUE = ""
    }
}

private class PendingInstallmentPayment(
    val cardId: String,
    val token: String?,
)
