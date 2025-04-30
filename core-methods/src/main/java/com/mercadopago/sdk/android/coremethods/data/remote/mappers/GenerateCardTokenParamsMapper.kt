package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.request.BuyerIdentificationBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.domain.model.params.BuyerIdentificationParam
import com.mercadopago.sdk.android.coremethods.domain.model.params.GenerateCardTokenParams

internal fun GenerateCardTokenParams.toRequest() =
    CardTokenBodyRequest(
        cardId = this.cardId,
        esc = this.esc,
        requireEsc = requireEsc,
        cardNumber = cardNumber,
        securityCode = securityCode,
        expirationMonth = expirationMonth,
        expirationYear = expirationYear,
        buyerIdentification = buyerIdentification?.toBuyerIdentificationRequest(),
        device = device,
    )

internal fun BuyerIdentificationParam.toBuyerIdentificationRequest() =
    BuyerIdentificationBodyRequest(
        name = name,
        number = number,
        type = type,
    )
