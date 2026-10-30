package com.mercadopago.sdk.android.checkout.presentation.navigation.reviewconfirm

import com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.presentation.navigation.ReviewOrigin
import com.mercadopago.sdk.android.checkout.presentation.state.ReviewConfirmViewEvent

internal sealed interface ReviewConfirmNavigationAction {
    data class NavigateUp(
        val origin: ReviewOrigin,
    ) : ReviewConfirmNavigationAction

    object ReturnToPaymentSelector : ReviewConfirmNavigationAction

    object ReturnToPaymentSelectorWithGenericError : ReviewConfirmNavigationAction

    object ReturnToCardTransactionWithGenericError : ReviewConfirmNavigationAction

    data class NavigateToStatusScreenForResolution(
        val processError: MercadoPagoCheckoutError? = null,
        val processStatusDetail: String? = null,
    ) : ReviewConfirmNavigationAction

    data class FinishWithSuccess(
        val output: OrderProcessOutput,
    ) : ReviewConfirmNavigationAction

    data class FinishWithError(
        val error: MercadoPagoCheckoutError,
    ) : ReviewConfirmNavigationAction

    object FinishWithCardTransactionCancellation : ReviewConfirmNavigationAction

    object FinishForEmailChange : ReviewConfirmNavigationAction
}

internal object ReviewConfirmNavigationPolicy {
    fun resolveBack(
        origin: ReviewOrigin,
    ): ReviewConfirmNavigationAction = ReviewConfirmNavigationAction.NavigateUp(origin)

    fun resolve(
        event: ReviewConfirmViewEvent,
        checkoutType: MPCheckoutType<*, *>?,
        origin: ReviewOrigin,
        hasStatusScreen: Boolean = false,
    ): ReviewConfirmNavigationAction =
        when (event) {
            is ReviewConfirmViewEvent.OnPaymentSuccess ->
                ReviewConfirmNavigationAction.FinishWithSuccess(event.output)
            is ReviewConfirmViewEvent.OnPaymentError ->
                resolvePaymentError(event.error, event.statusDetail, hasStatusScreen)
            is ReviewConfirmViewEvent.OnLoadFailure ->
                resolveLoadFailure(checkoutType, origin)
            is ReviewConfirmViewEvent.OnModifyPaymentMethod -> when (checkoutType) {
                is MPCheckoutType.Payment -> ReviewConfirmNavigationAction.ReturnToPaymentSelector
                is MPCheckoutType.CardTransaction ->
                    ReviewConfirmNavigationAction.FinishWithCardTransactionCancellation
                else -> ReviewConfirmNavigationAction.NavigateUp(origin)
            }
            ReviewConfirmViewEvent.OnModifyEmail -> when (checkoutType) {
                is MPCheckoutType.Payment -> ReviewConfirmNavigationAction.FinishForEmailChange
                else -> ReviewConfirmNavigationAction.NavigateUp(origin)
            }
        }

    private fun resolvePaymentError(
        error: MercadoPagoCheckoutError,
        statusDetail: String?,
        hasStatusScreen: Boolean,
    ): ReviewConfirmNavigationAction =
        if (hasStatusScreen) {
            ReviewConfirmNavigationAction.NavigateToStatusScreenForResolution(
                processError = error,
                processStatusDetail = statusDetail,
            )
        } else {
            ReviewConfirmNavigationAction.FinishWithError(error)
        }

    private fun resolveLoadFailure(
        checkoutType: MPCheckoutType<*, *>?,
        origin: ReviewOrigin,
    ): ReviewConfirmNavigationAction =
        when {
            checkoutType is MPCheckoutType.Payment ->
                ReviewConfirmNavigationAction.ReturnToPaymentSelectorWithGenericError
            checkoutType is MPCheckoutType.CardTransaction ->
                ReviewConfirmNavigationAction.ReturnToCardTransactionWithGenericError
            else -> ReviewConfirmNavigationAction.NavigateUp(origin)
        }
}
