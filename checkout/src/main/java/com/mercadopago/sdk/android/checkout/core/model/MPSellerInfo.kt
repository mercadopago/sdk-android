package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Seller information displayed on the Review and Confirm screen.
 *
 * All fields are optional and independent of each other. When provided via
 * [com.mercadopago.sdk.android.checkout.core.MercadoPagoCheckout.Builder.withReviewAndConfirm],
 * the BFF uses this data to compose the screen header. Products and coupons are
 * resolved by the BFF from the order — they are not sent through [MPSellerInfo].
 *
 * @param name Display name of the store (optional).
 * @param logoUrl URL of the store logo (optional).
 */
@Parcelize
data class MPSellerInfo(
    val name: String? = null,
    val logoUrl: String? = null,
) : Parcelable
