package com.mercadopago.sdk.android.checkout.core.model.internal

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
internal sealed class ScreenConfig : Parcelable {
    @Parcelize
    internal data class ReviewAndConfirm(
        @IgnoredOnParcel
        val onEmailChangeRequested: (() -> Unit)? = null,
    ) : ScreenConfig()

    @Parcelize
    internal data class StatusScreen(
        @IgnoredOnParcel
        val onExit: (() -> Unit)? = null,
    ) : ScreenConfig()
}
