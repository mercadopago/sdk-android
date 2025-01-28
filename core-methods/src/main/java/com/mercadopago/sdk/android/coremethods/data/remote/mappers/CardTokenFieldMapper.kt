package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.request.BuyerIdentificationBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields

internal fun CardTokenFields.toCardTokenRequest() =
    CardTokenBodyRequest(
        cardId = this.cardId,
        esc = this.esc,
        requireEsc = requireEsc,
        cardNumber = cardNumber,
        securityCode = securityCode,
        expirationMonth = expirationMonth,
        expirationYear = expirationYear,
        buyerIdentification = buyerIdentification?.toBuyerIdentificationRequest()
    )

internal fun BuyerIdentification.toBuyerIdentificationRequest() =
    BuyerIdentificationBodyRequest(
        name = name,
        number = number,
        type = type
    )
