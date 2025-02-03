package com.mercadopago.sdk.android.coremethods.domain.utils

sealed class Result<out A, out B> {

    data class Success<A> constructor(
        val data: A
    ) : Result<A, Nothing>()

    data class Error<B> constructor(
        val error: B
    ) : Result<Nothing, B>()
}
