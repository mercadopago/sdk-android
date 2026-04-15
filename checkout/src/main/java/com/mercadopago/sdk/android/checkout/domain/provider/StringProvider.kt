package com.mercadopago.sdk.android.checkout.domain.provider

import androidx.annotation.StringRes

internal interface StringProvider {
    fun getString(
        @StringRes resId: Int,
    ): String
}
