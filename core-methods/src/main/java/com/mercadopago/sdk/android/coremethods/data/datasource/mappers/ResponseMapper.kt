package com.mercadopago.sdk.android.coremethods.data.datasource.mappers

import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import okhttp3.ResponseBody
import retrofit2.Response

internal const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

internal val EMPTY_BODY_ERROR = Result.Error(
    ResultError.Request(
        code = 200,
        message = "empty body"
    )
)

internal fun ResponseBody?.toResultError(): ResultError.Request {
    val errorBody = this?.string()
    val gson = GsonBuilder().create()

    return try {
        gson.fromJson(errorBody, ResultError.Request::class.java)
    } catch (_: Exception) {
        ResultError.Request(
            message = UNKNOWN_ERROR,
            code = -1,
        )
    }
}

internal fun <T> Response<T>.toInternalResponse(): Result<T, ResultError> {
    return if (isSuccessful) {
        val result = this.body() ?: return EMPTY_BODY_ERROR
        Result.Success<T>(result)
    } else {
        Result.Error<ResultError>(errorBody().toResultError())
    }
}

internal fun <T, R> Result<T, ResultError>.mapSuccess(
    mapper: T.() -> R,
): Result<R, ResultError> = when (this) {
    is Result.Success -> {
        Result.Success(mapper(data))
    }

    is Result.Error -> Result.Error(error)
}
