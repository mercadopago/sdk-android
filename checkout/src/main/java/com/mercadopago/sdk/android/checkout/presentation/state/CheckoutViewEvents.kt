package com.mercadopago.sdk.android.checkout.presentation.state

import com.mercadopago.sdk.android.checkout.domain.model.MPInstallmentData
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.SecurityCodeScreenOutput

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

    /**
     * The selected saved card requires CVV entry before processing.
     *
     * @property securityCodeScreen BFF-supplied CVV screen configuration.
     * @property cvvExpectedLength Expected digit count for the CVV (3 or 4).
     * @property optionId Card identifier — used after CVV is confirmed to call processPaymentMethod.
     */
    data class NavigateToCVV(
        val securityCodeScreen: SecurityCodeScreenOutput,
        val cvvExpectedLength: Int,
        val optionId: String,
    ) : PaymentBrickViewEvent
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
     * Card tokenization succeeded and installments data is available.
     *
     * @property payment The payment data resulting from the successful tokenization.
     * @property installment The installment data returned by the BFF.
     */
    data class OnSuccess(
        val payment: MPPaymentData,
        val installment: MPInstallmentData,
    ) : CardPaymentViewEvent

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

internal sealed interface InstallmentViewEvent {
    data class OnSuccess(val installment: Int) : InstallmentViewEvent

    data class OnFailure(val error: MercadoPagoCheckoutError) : InstallmentViewEvent

    data class OnUserCancelled(val context: MPUserCancelledContext) : InstallmentViewEvent
}
