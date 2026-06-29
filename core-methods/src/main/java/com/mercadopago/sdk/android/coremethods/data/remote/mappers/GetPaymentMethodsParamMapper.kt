package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.request.PaymentMethodsRequest
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetPaymentMethodsParams

internal fun GetPaymentMethodsParams.toRequest() =
    PaymentMethodsRequest(
        bin = this.bin,
    )
