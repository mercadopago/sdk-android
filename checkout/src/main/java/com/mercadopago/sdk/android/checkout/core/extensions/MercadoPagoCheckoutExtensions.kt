package com.mercadopago.sdk.android.checkout.core.extensions

import com.mercadopago.sdk.android.checkout.core.MercadoPagoCheckout
import com.mercadopago.sdk.android.checkout.core.model.MPPaymentMethodConfig
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
    screenConfigs.removeAll { it is ScreenConfig.ReviewAndConfirm }
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
        screenConfigs.removeAll { it is ScreenConfig.ReviewAndConfirm }
        screenConfigs.add(ScreenConfig.ReviewAndConfirm())
    }

/**
 * Enables the Status Screen shown at the end of the checkout flow for the Payment flow.
 *
 * Disabled by default. Calling this method twice replaces the previous configuration.
 * Not available for CardSave — compile-time restriction via type constraint.
 * Store details are read from the same `sellerInfo` on
 * [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType.Payment] used by Review and Confirm.
 *
 * @param onExit Required. Called when the buyer closes Status Screen (Volver, system back, or a load
 * failure). The SDK closes the checkout and hands control back to you without reporting a
 * cancellation, the same way [withReviewAndConfirm]'s `onEmailChangeRequested` does.
 */
@JvmName("withStatusScreenPayment")
fun MercadoPagoCheckout.Builder<MPPaymentData.Payment, MPUserCancelledContext.Payment>.withStatusScreen(
    onExit: () -> Unit,
) = apply {
    screenConfigs.removeAll { it is ScreenConfig.StatusScreen }
    screenConfigs.add(ScreenConfig.StatusScreen(onExit = onExit))
}

/**
 * Enables the Status Screen shown at the end of the checkout flow for the CardTransaction flow.
 *
 * Disabled by default. Calling this method twice replaces the previous configuration.
 * Not available for CardSave — compile-time restriction via type constraint.
 * Store details are read from the same `sellerInfo` on
 * [com.mercadopago.sdk.android.checkout.core.model.MPCheckoutType.CardTransaction] used by Review and Confirm.
 *
 * @param onExit Required. Called when the buyer closes Status Screen (Volver, system back, or a load
 * failure). The SDK closes the checkout and hands control back to you without reporting a
 * cancellation, the same way [withReviewAndConfirm]'s `onEmailChangeRequested` does.
 */
@JvmName("withStatusScreenCardTransaction")
fun MercadoPagoCheckout.Builder<
    MPPaymentData.CardTransaction,
    MPUserCancelledContext.CardTransaction,
    >.withStatusScreen(
    onExit: () -> Unit,
) = apply {
    screenConfigs.removeAll { it is ScreenConfig.StatusScreen }
    screenConfigs.add(ScreenConfig.StatusScreen(onExit = onExit))
}

/**
 * Sets the payment methods offered by the CardSave checkout.
 *
 * Only available for the CardSave flow. Calling this method twice replaces the previous configuration.
 *
 * @param paymentMethodConfigs List of payment methods. Defaults to [MPPaymentMethodConfig.defaults].
 */
@JvmName("setPaymentMethodConfigurationCardSave")
fun MercadoPagoCheckout.Builder<MPPaymentData.CardSave, MPUserCancelledContext.CardSave>.setPaymentMethodConfiguration(
    paymentMethodConfigs: List<MPPaymentMethodConfig> = MPPaymentMethodConfig.defaults,
) = apply { this.paymentMethodConfigs = paymentMethodConfigs }
