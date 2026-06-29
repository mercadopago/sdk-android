package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.request.CardIssuersRequest
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetCardIssuersParams

internal fun GetCardIssuersParams.toRequest() =
    CardIssuersRequest(
        bin = this.bin,
        paymentMethodId = this.paymentMethodId,
    )
