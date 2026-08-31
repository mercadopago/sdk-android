package com.mercadopago.sdk.android.checkout.presentation.navigation

import com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder
import com.mercadopago.sdk.android.checkout.domain.callback.MercadoPagoCheckoutResult
import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MethodSelectionScreenData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.presentation.state.CheckoutDestination
import com.mercadopago.sdk.android.checkout.presentation.state.SecurityCodeScreenConfig

@Suppress("TooManyFunctions")
internal class CheckoutNavigator(
    private val navigationHost: CheckoutNavigationHost,
    private val flowViewModel: CheckoutFlowViewModel,
    private val notifyResult: (MercadoPagoCheckoutResult<*, *>) -> Unit = CheckoutCallbackHolder::notify,
    private val dismissCheckout: () -> Unit = CheckoutCallbackHolder::dismiss,
) {
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
    ) {
        flowViewModel.setInstallmentContext(installmentData, paymentData)
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

    fun navigateUpFromInstallments() {
        navigateUp(flowViewModel::clearInstallmentContext)
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
    ) {
        flowViewModel.clearAll()
        notifyResult(result)
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
}
