package com.mercadopago.sdk.android.checkout.domain.extensions

import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.delay

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

@Suppress("TooGenericExceptionCaught")
internal suspend inline fun <T> withErrorHandling(
    crossinline block: suspend () -> Result<T, ResultError>,
): Result<T, ResultError> =
    try {
        block()
    } catch (e: Exception) {
        Result.Error(
            ResultError.Request(
                message = e.message ?: "An error occurred",
                code = "EXCEPTION",
            ),
        )
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
            Result.Error(
                ResultError.Request(
                    message = e.message ?: "Network error occurred",
                    code = "NETWORK_ERROR",
                ),
            )
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
        ResultError.Request("Max retry attempts reached", code = "RETRY_EXHAUSTED"),
    )
}
