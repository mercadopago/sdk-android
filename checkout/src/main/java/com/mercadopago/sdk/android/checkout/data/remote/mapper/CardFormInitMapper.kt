package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.domain.model.cardform.CardFormInit

internal fun CardFormInitResponse.toModel() =
    CardFormInit(
        identificationTypes = identificationTypes,
        cardNumber = cardNumber,
        securityCode = securityCode,
        holderName = holderName,
        expirationDate = expirationDate,
        translations = translations,
    )
