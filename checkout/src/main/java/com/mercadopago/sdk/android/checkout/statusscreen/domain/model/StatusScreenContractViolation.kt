package com.mercadopago.sdk.android.checkout.statusscreen.domain.model

import com.mercadopago.sdk.android.checkout.domain.model.ResponseError

private const val ERROR_CODE_INVALID_STATUS_SCREEN_RESPONSE = "INVALID_STATUS_SCREEN_RESPONSE"
private const val HTTP_UNPROCESSABLE_ENTITY = 422

internal fun invalidStatusScreenResponseError(): ResponseError =
    ResponseError(code = ERROR_CODE_INVALID_STATUS_SCREEN_RESPONSE, message = null)

internal fun ResponseError.isStatusScreenContractViolation(): Boolean =
    code == ERROR_CODE_INVALID_STATUS_SCREEN_RESPONSE || httpStatus == HTTP_UNPROCESSABLE_ENTITY
