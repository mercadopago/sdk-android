package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.core.data.remote.response.MPErrorResponse
import com.mercadopago.sdk.android.core.data.remote.utils.MPResponse
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal fun <T> MPResponse<T, MPErrorResponse>.toResult(): Result<T, ResultError> = when (this) {
    is MPResponse.Success -> Result.Success(this.response)
    is MPResponse.Error -> Result.Error(this.errorResponse.toResultError())
}
