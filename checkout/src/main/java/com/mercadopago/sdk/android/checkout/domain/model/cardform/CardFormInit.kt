package com.mercadopago.sdk.android.checkout.domain.model.cardform

import com.mercadopago.sdk.android.checkout.data.remote.response.CardNumberConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.ExpirationDateConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.HolderNameConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.IdentificationType
import com.mercadopago.sdk.android.checkout.data.remote.response.SecurityCodeConfig
import com.mercadopago.sdk.android.checkout.data.remote.response.Translations

internal data class CardFormInit(
    val identificationTypes: List<IdentificationType>,
    val cardNumber: CardNumberConfig,
    val securityCode: SecurityCodeConfig,
    val holderName: HolderNameConfig,
    val expirationDate: ExpirationDateConfig,
    val translations: Translations,
)
