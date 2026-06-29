package com.mercadopago.sdk.android.checkout.data.remote.mapper

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import okhttp3.ResponseBody
import retrofit2.Response

internal const val UNKNOWN_ERROR = "UNKNOWN_ERROR"

internal val EMPTY_BODY_ERROR = Result.Error(
    ResponseError(
        code = "EMPTY_BODY",
        message = "empty body",
    ),
)

internal fun <T> Response<T>.toInternalResponse(): Result<T, ResponseError> {
    return if (isSuccessful) {
        val result = this.body() ?: return EMPTY_BODY_ERROR
        Result.Success<T>(result)
    } else {
        Result.Error(errorBody().toResultError(httpStatus = code()))
    }
}

internal fun ResponseBody?.toResultError(
    httpStatus: Int,
): ResponseError {
    val errorBody = this?.string()
    val gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    return errorBody?.let {
        gson.fromJson(it, ResponseError::class.java)?.copy(httpStatus = httpStatus)
    } ?: ResponseError(
        code = UNKNOWN_ERROR,
        message = UNKNOWN_ERROR,
        httpStatus = httpStatus,
    )
}
