package com.mercadopago.sdk.android.checkout.domain.repository

import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.params.GetCardBinParams
import com.mercadopago.sdk.android.checkout.domain.model.params.InitializeCardFormParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal interface CardFormRepository {
    suspend fun fetchInitialization(
        params: InitializeCardFormParams,
    ): Result<CardFormInitializationOutput, MercadoPagoCheckoutError>

    suspend fun getCardBin(
        params: GetCardBinParams,
    ): Result<CardBinData, MercadoPagoCheckoutError>
}
