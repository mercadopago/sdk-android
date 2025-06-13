package com.mercadopago.sdk.android.coremethods.data.datasource.mappers

import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.coremethods.domain.model.MPResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult
import okhttp3.ResponseBody
import retrofit2.Response

internal const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

internal val EMPTY_BODY_ERROR = MPResult.Error(
    MPResultError.Request(
        code = "200",
        message = "empty body",
    ),
)

internal fun ResponseBody?.toResultError(): MPResultError.Request {
    val errorBody = this?.string()
    val gson = GsonBuilder().create()

    return errorBody?.let {
        gson.fromJson(it, MPResultError.Request::class.java)
    } ?: MPResultError.Request(
        message = UNKNOWN_ERROR,
        code = UNKNOWN_ERROR,
    )
}

internal fun <T> Response<T>.toInternalResponse(): MPResult<T, MPResultError> {
    return if (isSuccessful) {
        val result = this.body() ?: return EMPTY_BODY_ERROR
        MPResult.Success<T>(result)
    } else {
        MPResult.Error<MPResultError>(errorBody().toResultError())
    }
}

internal fun <T, R> MPResult<T, MPResultError>.mapSuccess(mapper: T.() -> R): MPResult<R, MPResultError> =
    when (this) {
        is MPResult.Success -> {
            MPResult.Success(mapper(data))
        }

        is MPResult.Error -> MPResult.Error(error)
    }
