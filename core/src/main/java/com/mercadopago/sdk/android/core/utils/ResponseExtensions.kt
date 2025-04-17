package com.mercadopago.sdk.android.core.utils

import retrofit2.Response

/**
 * Maps a Retrofit [Response] to a Kotlin [Result].
 * If the response is successful, the [Result] will contain the response body.
 * If the response is not successful, the [Result] will contain an exception with the error body.
 * @return a [Result] containing the response body or an exception.
 */
fun <T> Response<T>.toKotlinResponse(): Result<T> {
    return if (isSuccessful) {
        Result.success(this.body()!!)
    } else {
        Result.failure(Exception(errorBody()?.string() ?: "Error"))
    }
}

/**
 * Maps a success result to a new data type using a mapper function.
 * @param mapper a function that maps the success data type to a new data type.
 * @return a [Result] containing the mapped data or an exception.
 */
fun <T, R> Result<T>.mapSuccess(
    mapper: T.() -> R,
): Result<R> {
    return if (isSuccess) {
        Result.success(mapper(getOrThrow()))
    } else {
        Result.failure(exceptionOrNull() ?: Exception("Error"))
    }
}
