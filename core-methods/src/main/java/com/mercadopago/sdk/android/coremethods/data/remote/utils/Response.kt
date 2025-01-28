package com.mercadopago.sdk.android.coremethods.data.remote.utils

/**
 * Response sealed class handles with the success and error response
 *
 */
internal sealed class Response<out A, out B> {

    data class Success<A> constructor(
        val response: A
    ) : Response<A, Nothing>()

    data class Error<B> constructor(
        val errorResponse: B
    ) : Response<Nothing, B>()
}
