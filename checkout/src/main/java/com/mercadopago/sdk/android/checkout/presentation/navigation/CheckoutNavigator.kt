package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.core.model.internal.CheckoutConfiguration
import com.mercadopago.sdk.android.checkout.core.model.internal.getSellerInfo
import com.mercadopago.sdk.android.checkout.core.model.internal.hasStatusScreen
import com.mercadopago.sdk.android.checkout.core.model.internal.orderStatusOrNull
import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.errorFallbackPayment
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.rejectedFallbackPayment

@Suppress("TooManyFunctions")
internal class CheckoutNavigator(
    private val navigationHost: CheckoutNavigationHost,
    private val flowViewModel: CheckoutFlowViewModel,
    private val checkoutConfiguration: CheckoutConfiguration? = null,
    private val notifyResult: (MercadoPagoCheckoutResult<*, *>) -> Unit = CheckoutCallbackHolder::notify,
    private val dismissCheckout: () -> Boolean = CheckoutCallbackHolder::dismiss,
    private val notifyResultKeepingHostOpen: (MercadoPagoCheckoutResult<*, *>) -> Unit =
        CheckoutCallbackHolder::notifyKeepingHostOpen,
) {
    val orderProcessedActions = OrderProcessedActions(
        onFinishWithProcessedOrder = ::finishCheckout,
        onNavigateToStatusScreenForResolution = ::navigateToStatusScreenForResolution,
    )

    fun openForm() {
        navigationHost.navigate(CheckoutDestination.Form)
    }

    fun openSecurityCode(
        config: SecurityCodeScreenConfig,
    ) {
        flowViewModel.setSecurityCodeContext(config)
        navigationHost.navigate(CheckoutDestination.SecurityCode)
    }

    fun openInstallments(
        installmentData: MPInstallmentData,
        paymentData: MPPaymentData,
        origin: ReviewOrigin,
    ) {
        flowViewModel.setInstallmentContext(installmentData, paymentData, origin)
        navigationHost.navigate(CheckoutDestination.Installment)
    }

    fun openOfflineMethodSelector(
        screenData: MethodSelectionScreenData,
    ) {
        flowViewModel.setOfflineMethodSelectorContext(screenData)
        navigationHost.navigate(CheckoutDestination.OfflineMethodSelector)
    }

    fun openReview(
        params: ProcessOrderParams,
        origin: ReviewOrigin,
    ) {
        flowViewModel.setReviewContext(params, origin)
        navigationHost.navigate(CheckoutDestination.ReviewConfirm)
    }

    fun navigateUpFromSecurityCode() {
        navigateUp(flowViewModel::clearSecurityCodeContext)
    }

    fun completeSecurityCode(
        onCompleted: () -> Boolean,
    ) {
        navigateUp {
            val keepContextForInstallments = onCompleted()
            if (!keepContextForInstallments) {
                flowViewModel.clearSecurityCodeContext()
            }
        }
    }

    fun navigateUpFromInstallments() {
        val securityCodeConfig = flowViewModel.state.value.installmentContext?.securityCodeConfig
        navigateUp {
            flowViewModel.clearInstallmentContext()
            securityCodeConfig?.let(::openSecurityCode)
        }
    }

    fun navigateUpFromOfflineMethodSelector() {
        navigateUp(flowViewModel::clearOfflineMethodSelectorContext)
    }

    fun navigateUpFromReview(
        origin: ReviewOrigin,
    ) {
        flowViewModel.clearReviewContext()

        val navigated = when (origin) {
            ReviewOrigin.Payment -> navigationHost.popTo(CheckoutDestination.Payment)
            ReviewOrigin.Form -> navigationHost.popTo(CheckoutDestination.Form)
            ReviewOrigin.Installment -> navigationHost.popTo(CheckoutDestination.Installment)
            ReviewOrigin.OfflineMethodSelector -> navigationHost.popTo(CheckoutDestination.OfflineMethodSelector)
        }

        if (!navigated) {
            navigationHost.pop()
        }
    }

    fun returnToPaymentSelector() {
        returnToPaymentSelector(feedback = null)
    }

    fun returnToPaymentSelectorWithGenericError() {
        returnToPaymentSelector(feedback = PaymentFeedback.GenericError)
    }

    private fun returnToPaymentSelector(
        feedback: PaymentFeedback?,
    ) {
        flowViewModel.clearAll()
        feedback?.let(flowViewModel.paymentFeedback::show)
        val navigated = navigationHost.popTo(CheckoutDestination.Payment)
        if (!navigated) {
            navigationHost.navigate(CheckoutDestination.Payment)
        }
    }

    fun recoverTo(
        startDestination: CheckoutDestination,
    ) {
        flowViewModel.clearAll()
        val navigated = navigationHost.popTo(startDestination)
        if (!navigated) {
            navigationHost.navigate(startDestination)
        }
    }

    fun finishCheckout(
        result: MercadoPagoCheckoutResult<*, *>,
        processOrderParams: ProcessOrderParams? = null,
        paymentProcessed: OrderProcessOutput.PaymentProcessed? = null,
    ) {
        val orderId = when (result) {
            is MercadoPagoCheckoutResult.Success<*> -> result.paymentData.orderIdOrNull()
            is MercadoPagoCheckoutResult.Error ->
                processOrderParams?.orderId?.takeIf { result.error.errorCode == ErrorCode.PAYMENT_REJECTED }
            else -> null
        }
        if (orderId != null && processOrderParams != null && checkoutConfiguration.hasStatusScreen()) {
            flowViewModel.clearAll()
            notifyResultKeepingHostOpen(result)
            val orderStatus = (result as? MercadoPagoCheckoutResult.Success<*>)?.paymentData?.orderStatusOrNull()
            openStatusScreen(orderId, processOrderParams, paymentProcessed, orderStatus)
            return
        }
        flowViewModel.clearAll()
        notifyResult(result)
    }

    fun navigateToStatusScreenForResolution(
        processOrderParams: ProcessOrderParams,
        processError: MercadoPagoCheckoutError? = null,
        processStatusDetail: String? = null,
    ) {
        flowViewModel.clearAll()
        processError?.let { notifyResultKeepingHostOpen(MercadoPagoCheckoutResult.Error(it)) }
        openStatusScreen(
            orderId = processOrderParams.orderId,
            processOrderParams = processOrderParams,
            paymentProcessed = processStatusDetail?.let(::rejectedFallbackPayment)
                ?: processError?.let { errorFallbackPayment() },
        )
    }

    fun closeStatusScreen(
        onExit: (() -> Unit)?,
    ) {
        flowViewModel.clearStatusScreenContext()
        if (dismissCheckout()) {
            onExit?.invoke()
        }
    }

    private fun openStatusScreen(
        orderId: String,
        processOrderParams: ProcessOrderParams,
        paymentProcessed: OrderProcessOutput.PaymentProcessed? = null,
        orderStatus: String? = null,
    ) {
        flowViewModel.setStatusScreenContext(
            StatusScreenContext(
                orderId = orderId,
                clientToken = processOrderParams.clientToken,
                lastFourDigits = processOrderParams.lastFourDigits,
                sellerInfo = checkoutConfiguration.getSellerInfo(),
                checkoutType = processOrderParams.checkoutType.takeIf { it.isNotEmpty() },
                paymentProcessed = paymentProcessed,
                orderStatus = orderStatus,
            ),
        )
        navigationHost.navigate(CheckoutDestination.StatusScreen)
    }

    fun finishForEmailChange(
        onEmailChangeRequested: (() -> Unit)?,
    ) {
        flowViewModel.clearAll()
        dismissCheckout()
        onEmailChangeRequested?.invoke()
    }

    private fun navigateUp(
        clearContext: () -> Unit,
    ) {
        if (navigationHost.pop()) {
            clearContext()
        }
    }

    private fun MPPaymentData.orderIdOrNull(): String? =
        when (this) {
            is MPPaymentData.Payment -> orderId
            is MPPaymentData.CardTransaction -> orderId
            is MPPaymentData.CardSave -> null
        }
}
