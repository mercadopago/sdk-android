package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError

/**
 * One-shot events emitted by the checkout screens for the
 * [com.mercadopago.sdk.android.checkout.presentation.CheckoutController] to consume.
 *
 * The controller is the single place that turns these events into navigation or into
 * checkout callbacks ([com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder]),
 * so the callback logic lives in one place instead of being scattered across ViewModels.
 */
internal sealed interface PaymentBrickViewEvent {
    /**
     * The user selected a payment option in the PaymentBrick screen.
     *
     * @property optionId Identifier of the selected payment option.
     */
    data class OnOptionSelected(val optionId: String) : PaymentBrickViewEvent
}

/**
 * One-shot events emitted by the card form screen for the
 * [com.mercadopago.sdk.android.checkout.presentation.CheckoutController] to consume.
 *
 * The ViewModel only emits the outcome; the controller decides whether to navigate to the
 * next screen or to notify the checkout callback
 * ([com.mercadopago.sdk.android.checkout.domain.callback.CheckoutCallbackHolder]).
 */
internal sealed interface CardPaymentViewEvent {
    /**
     * Card tokenization succeeded.
     *
     * @property payment The payment data resulting from the successful tokenization.
     */
    data class OnSuccess(val payment: MPPaymentData) : CardPaymentViewEvent

    /**
     * Card form initialization or tokenization failed.
     *
     * @property error Details of the error that occurred.
     */
    data class OnFailure(val error: MercadoPagoCheckoutError) : CardPaymentViewEvent

    /**
     * The user cancelled the card form before completion.
     *
     * @property context Information about the form state when cancelled.
     */
    data class OnUserCancelled(val context: MPUserCancelledContext) : CardPaymentViewEvent

    /**
     * The user pressed the back button while the card form was open.
     *
     * @property context Information about the form state when the user navigated back.
     */
    data class OnBackPressed(val context: MPUserCancelledContext) : CardPaymentViewEvent
}
