package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.request.InstallmentsRequest
import com.mercadopago.sdk.android.coremethods.domain.model.params.GetInstallmentParams

internal fun GetInstallmentParams.toRequest() =
    InstallmentsRequest(
        productId = this.productId,
        bin = this.bin,
        processingMode = this.processingMode,
        amount = this.amount,
    )
