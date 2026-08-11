package com.mercadopago.sdk.android.checkout.core.model.internal

import android.os.Parcelable
import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import kotlinx.parcelize.Parcelize

@Parcelize
internal sealed class ScreenConfig : Parcelable {
    @Parcelize
    internal data class ReviewAndConfirm(
        val seller: MPSellerInfo? = null,
        val onEmailChangeRequested: (() -> Unit)? = null,
    ) : ScreenConfig()
}
