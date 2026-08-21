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
 */
@JvmName("withReviewAndConfirmPayment")
fun MercadoPagoCheckout.Builder<MPPaymentData.Payment, MPUserCancelledContext.Payment>.withReviewAndConfirm() =
    apply {
        screenConfigs.removeIf { it is ScreenConfig.ReviewAndConfirm }
        screenConfigs.add(ScreenConfig.ReviewAndConfirm())
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
