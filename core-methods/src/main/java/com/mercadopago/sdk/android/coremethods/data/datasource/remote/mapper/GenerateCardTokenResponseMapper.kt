package com.mercadopago.sdk.android.coremethods.data.datasource.remote.mapper

import com.mercadopago.sdk.android.coremethods.data.remote.response.CardHolderResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.IdentificationResponse
import com.mercadopago.sdk.android.coremethods.domain.model.CardHolder
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.Identification

internal fun CardTokenResponse.toModel() =
    CardToken(
        token = this.id.toString(),
        publicKey = this.publicKey,
        firstSixDigits = this.firstSixDigits,
        expirationMonth = this.expirationMonth,
        expirationYear = this.expirationYear,
        cardHolder = this.cardholder?.toModel(),
        lastFourDigits = this.lastFourDigits,
        status = this.status,
        dateCreated = this.dateCreated,
        dateLastUpdated = this.dateLastUpdated,
        dateDue = this.dateDue,
        luhnValidation = this.luhnValidation,
        liveMode = this.liveMode,
        requireEsc = this.requireEsc,
        cardNumberLength = this.cardNumberLength,
        securityCodeLength = this.securityCodeLength,
        truncCardNumber = this.truncCardNumber,
    )

internal fun CardHolderResponse.toModel() =
    CardHolder(
        identification = this.identification?.toModel(),
        name = this.name,
    )

internal fun IdentificationResponse.toModel() = Identification(this.type)
