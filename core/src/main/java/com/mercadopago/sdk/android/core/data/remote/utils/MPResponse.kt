package com.mercadopago.sdk.android.core.data.remote.utils

import com.mercadopago.sdk.android.core.data.remote.response.MPErrorResponse

/**
 * Mercado Pago Response sealed class
 * This response return [A] generic type if is success and [MPErrorResponse] if is a error
 * @param A is a generic parameter thats can be any type of class
 * @see MPErrorResponse
 */

sealed class MPResponse<out A, out B> {

    data class Success<A> constructor(
        val response: A
    ) : MPResponse<A, Nothing>()

    data class Error<B> constructor(
        val errorResponse: B
    ) : MPResponse<Nothing, B>()
}
