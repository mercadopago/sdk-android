package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.checkout.domain.mapper.CountryCodeToLocaleMapper
import com.mercadopago.sdk.android.domain.model.CountryCode

internal class CardFormInitUseCase(
    private val countryCode: CountryCode?,
    private val cardFormService: CardFormService,
) {
    suspend operator fun invoke() {
        cardFormService.initialization(
            locale = CountryCodeToLocaleMapper.toLocaleString(countryCode),
        )
    }
}
