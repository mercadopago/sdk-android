package com.mercadopago.sdk.android.core.utils

import com.mercadopago.sdk.android.core.exception.NetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response

/**
 * Transforms a [Flow] of [Response] objects into a [Flow] of the response body, handling success and errors.
 *
 * This function simplifies working with network responses by extracting the body in case of success,
 * and throwing a [NetworkException] with the error message in case of failure.
 *
 * @return A [Flow] emitting the response body of successful responses, or `null` if the body is null.
 * If the response is not successful, a [NetworkException] is thrown.
 * @throws NetworkException if the response is not successful, containing the error message.
 */
fun <T> Flow<Response<T>>.mapToFlow(): Flow<T?> = map { response ->
    if (response.isSuccessful) {
        response.body()
    } else {
        throw NetworkException(response.errorBody()?.string() ?: "Error")
    }
}
