package com.mercadopago.sdk.android.checkout.core.extensions

import com.mercadopago.sdk.android.checkout.core.MercadoPagoCheckout
import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.core.model.internal.ScreenConfig
import com.mercadopago.sdk.android.checkout.domain.model.MPPaymentData
import com.mercadopago.sdk.android.checkout.domain.model.MPUserCancelledContext

/**
 * Enables the Review and Confirm screen for the Payment flow.
 *
 * When provided, [seller] is used by the BFF to compose the screen header (name and logo).
 * Calling this method twice replaces the previous configuration.
 * Not available for CardSave — compile-time restriction via type constraint.
 *
 * @param seller Optional store information to display on the screen header.
 */
@JvmName("withReviewAndConfirmPayment")
fun MercadoPagoCheckout.Builder<MPPaymentData.Payment, MPUserCancelledContext.Payment>.withReviewAndConfirm(
    seller: MPSellerInfo? = null,
) = apply {
    screenConfigs.removeIf { it is ScreenConfig.ReviewAndConfirm }
    screenConfigs.add(ScreenConfig.ReviewAndConfirm(seller = seller))
}

/**
 * Enables the Review and Confirm screen for the CardTransaction flow.
 *
 * When provided, [seller] is used by the BFF to compose the screen header (name and logo).
 * Calling this method twice replaces the previous configuration.
 * Not available for CardSave — compile-time restriction via type constraint.
 *
 * @param seller Optional store information to display on the screen header.
 */
@Suppress("MaxLineLength")
@JvmName("withReviewAndConfirmCardTransaction")
fun MercadoPagoCheckout.Builder<MPPaymentData.CardTransaction, MPUserCancelledContext.CardTransaction>.withReviewAndConfirm(
    seller: MPSellerInfo? = null,
) = apply {
    screenConfigs.removeIf { it is ScreenConfig.ReviewAndConfirm }
    screenConfigs.add(ScreenConfig.ReviewAndConfirm(seller = seller))
}
