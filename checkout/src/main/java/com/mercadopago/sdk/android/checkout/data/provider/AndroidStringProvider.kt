package com.mercadopago.sdk.android.checkout.data.provider

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import com.mercadopago.sdk.android.checkout.domain.mapper.CountryCodeToLocaleMapper
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider
import com.mercadopago.sdk.android.domain.model.CountryCode

internal class AndroidStringProvider(
    private val context: Context,
    private val countryCode: CountryCode?,
) : StringProvider {
    private val localizedContext: Context by lazy {
        val locale = CountryCodeToLocaleMapper.map(countryCode)
        val configuration = Configuration(context.resources.configuration).apply {
            setLocale(locale)
        }
        context.createConfigurationContext(configuration)
    }

    override fun getString(
        @StringRes resId: Int,
    ) = localizedContext.getString(resId)
}
