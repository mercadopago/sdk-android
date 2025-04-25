package com.mercadopago.sdk.android.coremethods.data.datasource.remote.mapper

import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken

internal fun CardTokenResponse.toModel() =
    CardToken(
        token = this.id.toString(),
    )
