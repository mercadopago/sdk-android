package com.mercadopago.sdk.android.checkout.domain.exception

import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result

private const val ERROR_CODE_NETWORK = "NETWORK"
private const val ERROR_CODE_CONNECTION = "CONNECTION"
private const val ERROR_CODE_NO_INTERNET = "NO_INTERNET"
private const val ERROR_CODE_UNREACHABLE = "UNREACHABLE"
private const val ERROR_CODE_TIMEOUT = "TIMEOUT"

internal object ExceptionFactory {
    fun mapRequestError(
        error: ResultError.Request,
        localized: ErrorLocalized,
    ): MercadoPagoCheckoutError {
        return when {
            error.code.isNullOrEmpty() -> {
                MercadoPagoCheckoutError.ServiceError(
                    code = ErrorCode.SERVICE_ERROR,
                    messageError = error.message,
                    localized = localized.name,
                    throwable = null,
                )
            }
            isNetworkError(error.code) -> {
                MercadoPagoCheckoutError.NetworkError(
                    code = ErrorCode.NETWORK_CONNECTION_FAILED,
                    messageError = error.message,
                    localized = localized.name,
                    throwable = null,
                )
            }
            isTimeoutError(error.code) -> {
                MercadoPagoCheckoutError.NetworkError(
                    code = ErrorCode.NETWORK_TIMEOUT,
                    messageError = error.message,
                    localized = localized.name,
                    throwable = null,
                )
            }
            else -> {
                MercadoPagoCheckoutError.ServiceError(
                    code = ErrorCode.SERVICE_ERROR,
                    messageError = error.message,
                    localized = localized.name,
                    throwable = null,
                )
            }
        }
    }

    fun mapValidationError(
        error: ResultError.Validation,
        localized: ErrorLocalized,
    ): MercadoPagoCheckoutError.ServiceError {
        return MercadoPagoCheckoutError.ServiceError(
            code = ErrorCode.SERVICE_ERROR,
            messageError = error.message,
            localized = localized.name,
            throwable = null,
        )
    }

    private fun isNetworkError(
        code: String,
    ): Boolean =
        code.contains(ERROR_CODE_NETWORK, ignoreCase = true) ||
            code.contains(ERROR_CODE_CONNECTION, ignoreCase = true) ||
            code.contains(ERROR_CODE_NO_INTERNET, ignoreCase = true) ||
            code.contains(ERROR_CODE_UNREACHABLE, ignoreCase = true)

    private fun isTimeoutError(
        code: String,
    ): Boolean = code.contains(ERROR_CODE_TIMEOUT, ignoreCase = true)
}

internal fun <T> Result<T, ResultError>.mapToCheckoutError(
    localized: ErrorLocalized,
): Result<T, MercadoPagoCheckoutError> =
    when (this) {
        is Result.Success -> Result.Success(data)
        is Result.Error -> {
            val checkoutError = when (val error = this.error) {
                is ResultError.Request -> ExceptionFactory.mapRequestError(error = error, localized = localized)
                is ResultError.Validation -> ExceptionFactory.mapValidationError(error = error, localized = localized)
            }
            Result.Error(checkoutError)
        }
    }
