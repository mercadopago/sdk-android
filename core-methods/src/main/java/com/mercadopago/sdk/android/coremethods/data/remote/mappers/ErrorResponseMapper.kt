package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.ErrorResponse
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError

internal fun ErrorResponse.toResultError() =
    ResultError(
        message = this.message,
        code = this.code
    )
