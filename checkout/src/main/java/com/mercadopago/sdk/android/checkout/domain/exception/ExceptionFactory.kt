package com.mercadopago.sdk.android.checkout.domain.exception

import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

internal const val ERROR_CODE_NETWORK = "NETWORK"
internal const val ERROR_CODE_CONNECTION = "CONNECTION"
internal const val ERROR_CODE_NO_INTERNET = "NO_INTERNET"
internal const val ERROR_CODE_UNREACHABLE = "UNREACHABLE"
internal const val ERROR_CODE_TIMEOUT = "TIMEOUT"

internal object ExceptionFactory {
    fun mapRequestError(
        error: ResponseError,
        localized: ErrorLocalized,
    ): MercadoPagoCheckoutError {
        val errorCode = error.errorCode ?: error.code
        val message = error.userErrorMessage ?: error.message ?: ""
        return when {
            isTimeoutError(errorCode) ->
                MercadoPagoCheckoutError.NetworkError(
                    code = ErrorCode.NETWORK_TIMEOUT,
                    messageError = message,
                    localized = localized.name,
                    throwable = null,
                )

            isNetworkError(errorCode) ->
                MercadoPagoCheckoutError.NetworkError(
                    code = ErrorCode.NETWORK_CONNECTION_FAILED,
                    messageError = message,
                    localized = localized.name,
                    throwable = null,
                )

            else ->
                MercadoPagoCheckoutError.ServiceError(
                    code = ErrorCode.SERVICE_ERROR,
                    messageError = message,
                    localized = localized.name,
                    throwable = null,
                )
        }
    }

    internal fun <T> Result<T, ResponseError>.mapError(
        localized: ErrorLocalized,
    ): Result<T, MercadoPagoCheckoutError> =
        when (this) {
            is Result.Success -> Result.Success(data)
            is Result.Error -> Result.Error(
                mapRequestError(
                    error = error,
                    localized = localized,
                ),
            )
        }

    internal fun <T> Result<T, ResultError>.mapToCheckoutError(
        localized: ErrorLocalized,
    ): Result<T, MercadoPagoCheckoutError> =
        when (this) {
            is Result.Success -> Result.Success(data)
            is Result.Error -> {
                val responseError = when (val e = error) {
                    is ResultError.Request -> ResponseError(code = e.code, message = e.message)
                    is ResultError.Validation -> ResponseError(code = null, message = e.message)
                }
                Result.Error(
                    mapRequestError(
                        error = responseError,
                        localized = localized,
                    ),
                )
            }
        }

    private fun isNetworkError(
        code: String?,
    ): Boolean =
        code == ERROR_CODE_NETWORK ||
            code == ERROR_CODE_CONNECTION ||
            code == ERROR_CODE_NO_INTERNET ||
            code == ERROR_CODE_UNREACHABLE

    private fun isTimeoutError(
        code: String?,
    ): Boolean = code == ERROR_CODE_TIMEOUT
}
