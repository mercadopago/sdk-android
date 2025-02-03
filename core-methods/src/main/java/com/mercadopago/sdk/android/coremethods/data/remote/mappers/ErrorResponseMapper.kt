package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.core.data.remote.response.MPErrorResponse
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError

internal fun MPErrorResponse.toResultError() =
    ResultError(
        message = this.message,
        code = this.code
    )
