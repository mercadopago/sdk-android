package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.CardIssuerResponse
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer

internal fun CardIssuerResponse.toModel() =
    CardIssuer(
        id = this.id,
        merchantAccountId = this.merchantAccountId,
        processingMode = this.processingMode,
        status = this.status,
        thumbnail = this.thumbnail
    )
