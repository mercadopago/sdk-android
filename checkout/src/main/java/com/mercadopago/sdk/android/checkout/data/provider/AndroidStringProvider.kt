package com.mercadopago.sdk.android.checkout.data.provider

import android.content.Context
import androidx.annotation.StringRes
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider

internal class AndroidStringProvider(
    private val context: Context,
) : StringProvider {
    override fun getString(
        @StringRes resId: Int,
    ) = context.getString(resId)
}
