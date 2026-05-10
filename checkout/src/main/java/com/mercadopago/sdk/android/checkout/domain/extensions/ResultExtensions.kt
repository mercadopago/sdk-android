package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.checkout.domain.exception.ERROR_CODE_CONNECTION
import com.mercadopago.sdk.android.checkout.domain.exception.ERROR_CODE_NETWORK
import com.mercadopago.sdk.android.checkout.domain.exception.ERROR_CODE_NO_INTERNET
import com.mercadopago.sdk.android.checkout.domain.exception.ERROR_CODE_TIMEOUT
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.delay
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal inline fun <A, B, C> Result<A, B>.fold(
    onSuccess: (A) -> C,
    onError: (B) -> C,
): C =
    when (this) {
        is Result.Success -> onSuccess(data)
        is Result.Error -> onError(error)
    }

internal inline fun <A, B, C> Result<A, B>.flatMap(
    transform: (A) -> Result<C, B>,
): Result<C, B> =
    when (this) {
        is Result.Success -> transform(data)
        is Result.Error -> this
    }

internal inline fun <A, B, C> Result<A, B>.map(
    transform: (A) -> C,
): Result<C, B> =
    when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> this
    }

internal inline fun <A, B> Result<A, B>.onSuccess(
    action: (A) -> Unit,
): Result<A, B> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

internal inline fun <A, B> Result<A, B>.onError(
    action: (B) -> Unit,
): Result<A, B> {
    if (this is Result.Error) {
        action(error)
    }
    return this
}

private const val HTTP_SERVER_ERROR_MIN = 500

private fun Exception.toResponseError(): ResponseError =
    when (this) {
        is SocketTimeoutException -> ResponseError(code = ERROR_CODE_TIMEOUT, message = message)
        is UnknownHostException -> ResponseError(code = ERROR_CODE_NO_INTERNET, message = message)
        is ConnectException -> ResponseError(code = ERROR_CODE_CONNECTION, message = message)
        is IOException -> ResponseError(code = ERROR_CODE_NETWORK, message = message)
        else -> ResponseError(code = "EXCEPTION", message = message ?: "An error occurred")
    }

private fun Exception.toResultError(): ResultError =
    when (this) {
        is SocketTimeoutException -> ResultError.Request(code = ERROR_CODE_TIMEOUT, message = message ?: "")
        is UnknownHostException -> ResultError.Request(code = ERROR_CODE_NO_INTERNET, message = message ?: "")
        is ConnectException -> ResultError.Request(code = ERROR_CODE_CONNECTION, message = message ?: "")
        is IOException -> ResultError.Request(code = ERROR_CODE_NETWORK, message = message ?: "")
        else -> ResultError.Request(code = "EXCEPTION", message = message ?: "An error occurred")
    }

@Suppress("TooGenericExceptionCaught")
internal suspend inline fun <T> withErrorHandling(
    crossinline block: suspend () -> Result<T, ResponseError>,
): Result<T, ResponseError> =
    try {
        block()
    } catch (e: Exception) {
        Result.Error(e.toResponseError())
    }

@Suppress("TooGenericExceptionCaught")
internal suspend inline fun <T> withRetry(
    maxAttempts: Int = 2,
    delayMillis: Long = 500L,
    crossinline shouldRetry: (ResultError) -> Boolean = { it !is ResultError.Validation },
    crossinline block: suspend () -> Result<T, ResultError>,
): Result<T, ResultError> {
    var lastResult: Result<T, ResultError>? = null

    repeat(maxAttempts) { attempt ->
        val result = try {
            block()
        } catch (e: Exception) {
            Result.Error(e.toResultError())
        }

        lastResult = result

        val shouldContinue = when (result) {
            is Result.Success -> false
            is Result.Error -> {
                val canRetry = shouldRetry(result.error)
                val hasMoreAttempts = attempt < maxAttempts - 1
                canRetry && hasMoreAttempts
            }
        }

        if (!shouldContinue) {
            return result
        }

        delay(delayMillis)
    }

    return lastResult ?: Result.Error(
        ResultError.Request(code = "RETRY_EXHAUSTED", message = "Max retry attempts reached"),
    )
}

@Suppress("TooGenericExceptionCaught")
internal suspend inline fun <T> withServiceRetry(
    maxAttempts: Int = 2,
    delayMillis: Long = 500L,
    crossinline shouldRetry: (ResponseError) -> Boolean = { error ->
        error.httpStatus == null || error.httpStatus >= HTTP_SERVER_ERROR_MIN
    },
    crossinline block: suspend () -> Result<T, ResponseError>,
): Result<T, ResponseError> {
    var lastResult: Result<T, ResponseError>? = null

    repeat(maxAttempts) { attempt ->
        val result = try {
            block()
        } catch (e: Exception) {
            Result.Error(e.toResponseError())
        }

        lastResult = result

        val shouldContinue = when (result) {
            is Result.Success -> false
            is Result.Error -> {
                val canRetry = shouldRetry(result.error)
                val hasMoreAttempts = attempt < maxAttempts - 1
                canRetry && hasMoreAttempts
            }
        }

        if (!shouldContinue) {
            return result
        }

        delay(delayMillis)
    }

    return lastResult ?: Result.Error(ResponseError(code = "RETRY_EXHAUSTED", message = "Max retry attempts reached"))
}
