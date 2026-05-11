package com.mercadopago.sdk.android.checkout.domain.model

internal data class ResponseError(
    val code: String?,
    val errorCode: String? = null,
    val message: String?,
    val userErrorMessage: String? = null,
    val httpStatus: Int? = null,
)
