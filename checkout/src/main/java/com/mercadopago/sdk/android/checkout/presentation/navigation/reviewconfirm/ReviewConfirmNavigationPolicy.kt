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
    ): ReviewConfirmNavigationAction =
        when (event) {
            is ReviewConfirmViewEvent.OnPaymentSuccess ->
                ReviewConfirmNavigationAction.FinishWithSuccess(event.output)
            is ReviewConfirmViewEvent.OnPaymentError ->
                ReviewConfirmNavigationAction.FinishWithError(event.error)
            is ReviewConfirmViewEvent.OnLoadFailure -> when (checkoutType) {
                is MPCheckoutType.Payment ->
                    ReviewConfirmNavigationAction.ReturnToPaymentSelectorWithGenericError
                is MPCheckoutType.CardTransaction ->
                    ReviewConfirmNavigationAction.ReturnToCardTransactionWithGenericError
                else -> ReviewConfirmNavigationAction.NavigateUp(origin)
            }
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
}
