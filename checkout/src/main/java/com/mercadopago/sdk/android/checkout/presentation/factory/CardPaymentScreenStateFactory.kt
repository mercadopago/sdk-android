package com.mercadopago.sdk.android.checkout.presentation.factory

import com.mercadopago.android.sdk.checkout.R
import com.mercadopago.sdk.android.checkout.domain.provider.StringProvider

internal class CardPaymentScreenStateFactory(
    private val stringProvider: StringProvider,
) {
    fun getGenericErrorMessage() = stringProvider.getString(R.string.card_form_generic_error)

    fun getStringProvider() = stringProvider
}
