package com.mercadopago.sdk.android.coremethods.data.datasource.mappers

import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.coremethods.domain.model.MPError
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult
import okhttp3.ResponseBody
import retrofit2.Response

internal const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

internal val EMPTY_BODY_ERROR = MPResult.Error(
    MPError.Request(
        code = "200",
        message = "empty body",
    ),
)

internal fun ResponseBody?.toResultError(): MPError.Request {
    val errorBody = this?.string()
    val gson = GsonBuilder().create()

    return errorBody?.let {
        gson.fromJson(it, MPError.Request::class.java)
    } ?: MPError.Request(
        message = UNKNOWN_ERROR,
        code = UNKNOWN_ERROR,
    )
}

internal fun <T> Response<T>.toInternalResponse(): MPResult<T, MPError> {
    return if (isSuccessful) {
        val result = this.body() ?: return EMPTY_BODY_ERROR
        MPResult.Success<T>(result)
    } else {
        MPResult.Error<MPError>(errorBody().toResultError())
    }
}

internal fun <T, R> MPResult<T, MPError>.mapSuccess(mapper: T.() -> R): MPResult<R, MPError> =
    when (this) {
        is MPResult.Success -> {
            MPResult.Success(mapper(data))
        }

        is MPResult.Error -> MPResult.Error(error)
    }
