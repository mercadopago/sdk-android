package com.mercadopago.sdk.android.checkout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.extensions.fold
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.PaymentMethodOutput
import com.mercadopago.sdk.android.checkout.domain.model.Screen
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.usecase.FetchPaymentBrickInitializationUseCase
import com.mercadopago.sdk.android.checkout.domain.usecase.ProcessOrderUseCase
import com.mercadopago.sdk.android.checkout.presentation.mapper.toScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickScreenState
import com.mercadopago.sdk.android.checkout.presentation.state.PaymentBrickViewEvent
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val DEFAULT_INSTALLMENTS = 1
private const val SAVED_CARD_TYPE = "saved_card"

internal class PaymentBrickViewModel(
    private val checkoutConfiguration: CheckoutConfiguration?,
    private val fetchInitializationUseCase: FetchPaymentBrickInitializationUseCase,
    private val processOrderUseCase: ProcessOrderUseCase,
) : ViewModel() {
    private val _viewState = MutableStateFlow(PaymentBrickScreenState(isLoading = true))
    val viewState: StateFlow<PaymentBrickScreenState> = _viewState.asStateFlow()

    private val _viewEvent = MutableStateFlow<PaymentBrickViewEvent?>(null)
    val viewEvent: StateFlow<PaymentBrickViewEvent?> = _viewEvent.asStateFlow()

    private var initializationOutput: PaymentBrickInitializationOutput? = null
    private val visitedScreens = mutableListOf(Screen.PAYMENT_METHOD_SELECTOR)

    init {
        loadInitialization()
    }

    private fun loadInitialization() {
        val params = checkoutConfiguration?.buildInitializationParams() ?: run {
            _viewState.value = PaymentBrickScreenState(isError = true, isLoading = false)
            return
        }
        viewModelScope.launch {
            _viewState.value = PaymentBrickScreenState(isLoading = true)
            when (val result = fetchInitializationUseCase(params)) {
                is Result.Success -> {
                    initializationOutput = result.data
                    _viewState.value = result.data.toScreenState()
                }
                is Result.Error -> _viewState.value = PaymentBrickScreenState(isError = true)
            }
        }
    }

    fun processPaymentMethod(
        optionId: String,
    ) {
        val method = findMethodByOptionId(optionId) ?: return
        val paymentType = checkoutConfiguration?.checkoutType as? MPCheckoutType.Payment ?: return
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            processOrderUseCase(
                ProcessOrderParams(
                    orderId = paymentType.order.orderId,
                    amount = paymentType.order.amount.toPlainString(),
                    paymentMethodId = method.cardData?.paymentMethodId.orEmpty(),
                    paymentMethodType = method.cardData?.paymentTypeId.orEmpty(),
                    token = "",
                    installments = DEFAULT_INSTALLMENTS,
                ),
            ).fold(
                onSuccess = { orderOutput ->
                    _viewState.value = _viewState.value.copy(isLoading = false)
                    val paymentData = MPPaymentData.Payment(
                        orderId = orderOutput.id,
                        orderStatus = orderOutput.status,
                        transactionAmount = paymentType.order.amount,
                        paymentMethodId = method.cardData?.paymentMethodId.orEmpty(),
                        paymentTypeId = method.cardData?.paymentTypeId.orEmpty(),
                        payer = null,
                        installment = null,
                        issuerId = method.cardData?.issuerId?.toString(),
                    )
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Success(paymentData))
                },
                onError = { error ->
                    CheckoutCallbackHolder.notify(MercadoPagoCheckoutResult.Error(error))
                    _viewState.value = _viewState.value.copy(isLoading = false, isError = true)
                },
            )
        }
    }

    fun onOptionSelected(
        optionId: String,
    ) {
        val method = findMethodByOptionId(optionId)
        val cardData = method?.cardData

        if (method?.type == SAVED_CARD_TYPE && cardData != null) {
            val screen = cardData.securityCode.screen
            if (screen != null) {
                _viewEvent.value = PaymentBrickViewEvent.NavigateToCVV(
                    securityCodeScreen = screen,
                    cvvExpectedLength = cardData.securityCode.length,
                    optionId = optionId,
                )
            } else {
                processPaymentMethod(optionId)
            }
        } else {
            _viewEvent.value = PaymentBrickViewEvent.OnOptionSelected(optionId)
        }
    }

    fun onViewEventConsumed() {
        _viewEvent.value = null
    }

    fun onBackPressed() {
        CheckoutCallbackHolder.notify(
            MercadoPagoCheckoutResult.UserCancelled(
                MPUserCancelledContext.Payment(screens = visitedScreens.toList()),
            ),
        )
    }

    fun markScreenPresented(
        screen: Screen,
    ) {
        if (!visitedScreens.contains(screen)) {
            visitedScreens.add(screen)
        }
    }

    private fun findMethodByOptionId(
        optionId: String,
    ): PaymentMethodOutput? =
        initializationOutput?.sections
            ?.flatMap { it.methods }
            ?.firstOrNull { method ->
                // saved_card: matched by unique card ID; others (new_card, ticket) by type string
                if (method.cardData != null) method.cardData.id == optionId else method.type == optionId
            }

    private fun CheckoutConfiguration.buildInitializationParams(): FetchPaymentBrickInitializationParams? {
        val paymentType = checkoutType as? MPCheckoutType.Payment ?: return null
        return FetchPaymentBrickInitializationParams(
            orderId = paymentType.order.orderId,
            totalAmount = paymentType.order.amount.toPlainString(),
            cardIds = paymentType.cardIds?.joinToString(","),
        )
    }
}
