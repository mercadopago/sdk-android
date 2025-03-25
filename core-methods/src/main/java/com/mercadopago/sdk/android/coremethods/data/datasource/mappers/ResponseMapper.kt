package com.mercadopago.sdk.android.coremethods.data.datasource.mappers

import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import okhttp3.ResponseBody

internal const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

internal fun ResponseBody?.toResultError(): ResultError {
    val errorBody = this?.string()
    val gson = GsonBuilder().create()

    return errorBody?.let {
        gson.fromJson(it, ResultError::class.java)
    } ?: ResultError(
        code = UNKNOWN_ERROR,
        message = UNKNOWN_ERROR,
    )
}
