package com.mercadopago.sdk.android.checkout.core.extensions

import com.mercadopago.sdk.android.checkout.core.MercadoPagoCheckout
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext

/**
 * Enables the Review and Confirm screen for the Payment flow.
 *
 * Store details are configured via `sellerInfo` on [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType.Payment].
 * Calling this method twice replaces the previous configuration.
 * Not available for CardSave — compile-time restriction via type constraint.
 *
 * @param onEmailChangeRequested Called when the buyer taps "Modificar" on the email row. The SDK
 * closes the checkout and hands control back to you without reporting a cancellation. When `null`
 * the email row has no change action.
 */
@JvmName("withReviewAndConfirmPayment")
fun MercadoPagoCheckout.Builder<MPPaymentData.Payment, MPUserCancelledContext.Payment>.withReviewAndConfirm(
    onEmailChangeRequested: (() -> Unit)? = null,
) = apply {
    screenConfigs.removeIf { it is ScreenConfig.ReviewAndConfirm }
    screenConfigs.add(ScreenConfig.ReviewAndConfirm(onEmailChangeRequested = onEmailChangeRequested))
}

/**
 * Enables the Review and Confirm screen for the CardTransaction flow.
 *
 * Store details are configured via `sellerInfo` on
 * [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType.CardTransaction].
 * Calling this method twice replaces the previous configuration.
 * Not available for CardSave — compile-time restriction via type constraint.
 */
@JvmName("withReviewAndConfirmCardTransaction")
fun MercadoPagoCheckout.Builder<
    MPPaymentData.CardTransaction,
    MPUserCancelledContext.CardTransaction,
    >.withReviewAndConfirm() =
    apply {
        screenConfigs.removeIf { it is ScreenConfig.ReviewAndConfirm }
        screenConfigs.add(ScreenConfig.ReviewAndConfirm())
    }
