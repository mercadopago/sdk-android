package com.mercadopago.sdk.android.checkout.domain.exception

import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorCode
import com.mercadopago.sdk.android.analytics.domain.models.NativeErrorDiagnostic
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ObservedCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import java.util.Locale

internal object ObservedCheckoutErrorFactory {
    fun from(
        error: ResponseError,
        localized: ErrorLocalized,
    ): ObservedCheckoutError {
        val publicError = ExceptionFactory.mapRequestError(error, localized)
        val retainedCode = error.errorCode ?: error.code
        val status = error.httpStatus?.takeIf { it in MIN_HTTP_STATUS..MAX_HTTP_STATUS }
        return observed(publicError, retainedCode, status, isValidation = false)
    }

    fun from(
        error: ResultError,
        localized: ErrorLocalized,
    ): ObservedCheckoutError {
        val responseError = when (error) {
            is ResultError.Request -> ResponseError(code = error.code, message = error.message)
            is ResultError.Validation -> ResponseError(code = null, message = error.message)
        }
        val publicError = ExceptionFactory.mapRequestError(responseError, localized)
        return observed(
            publicError = publicError,
            retainedCode = (error as? ResultError.Request)?.code,
            status = null,
            isValidation = error is ResultError.Validation,
        )
    }

    private fun observed(
        publicError: MercadoPagoCheckoutError,
        retainedCode: String?,
        status: Int?,
        isValidation: Boolean,
    ): ObservedCheckoutError {
        val normalizedCode = retainedCode?.uppercase(Locale.ROOT)
        val nativeCode = classify(publicError, normalizedCode, status, isValidation)
        val diagnostic = diagnostic(normalizedCode, status, isValidation)
        return ObservedCheckoutError(publicError, nativeCode, status, diagnostic)
    }

    @Suppress("CyclomaticComplexMethod")
    private fun classify(
        publicError: MercadoPagoCheckoutError,
        normalizedCode: String?,
        status: Int?,
        isValidation: Boolean,
    ): NativeErrorCode =
        when {
            publicError is MercadoPagoCheckoutError.ConfigurationError -> NativeErrorCode.SDK_CONFIGURATION_INVALID
            normalizedCode in CONFIGURATION_CODES -> NativeErrorCode.SDK_CONFIGURATION_INVALID
            status == HTTP_UNAUTHORIZED || status == HTTP_FORBIDDEN -> NativeErrorCode.SDK_CONFIGURATION_INVALID
            isValidation -> NativeErrorCode.INPUT_VALIDATION_FAILED
            normalizedCode in CONNECTION_CODES ||
                publicError.errorCode == ErrorCode.NETWORK_CONNECTION_FAILED ->
                NativeErrorCode.CONNECTION_UNAVAILABLE
            normalizedCode in TIMEOUT_CODES ||
                publicError.errorCode == ErrorCode.NETWORK_TIMEOUT ||
                status == HTTP_REQUEST_TIMEOUT || status == HTTP_GATEWAY_TIMEOUT -> NativeErrorCode.REQUEST_TIMEOUT
            normalizedCode == EMPTY_BODY -> NativeErrorCode.RESPONSE_CONTRACT_INVALID
            publicError is MercadoPagoCheckoutError.UnknownError || normalizedCode in UNKNOWN_CODES ->
                NativeErrorCode.OPERATION_FAILED
            publicError is MercadoPagoCheckoutError.ServiceError -> NativeErrorCode.UPSTREAM_REJECTED
            else -> NativeErrorCode.OPERATION_FAILED
        }

    private fun diagnostic(
        normalizedCode: String?,
        status: Int?,
        isValidation: Boolean,
    ): NativeErrorDiagnostic? =
        when {
            status == HTTP_UNAUTHORIZED -> NativeErrorDiagnostic.HTTP_UNAUTHORIZED
            status == HTTP_FORBIDDEN -> NativeErrorDiagnostic.HTTP_FORBIDDEN
            isValidation -> NativeErrorDiagnostic.VALIDATION
            normalizedCode in CONNECTION_CODES -> NativeErrorDiagnostic.OFFLINE
            normalizedCode in TIMEOUT_CODES ||
                status == HTTP_REQUEST_TIMEOUT ||
                status == HTTP_GATEWAY_TIMEOUT -> NativeErrorDiagnostic.TIMEOUT
            normalizedCode == EMPTY_BODY -> NativeErrorDiagnostic.EMPTY_BODY
            else -> null
        }

    internal fun <T> Result<T, ResponseError>.mapResponseObserved(
        localized: ErrorLocalized,
    ): Result<T, ObservedCheckoutError> =
        when (this) {
            is Result.Success -> Result.Success(data)
            is Result.Error -> Result.Error(from(error, localized))
        }

    internal fun <T> Result<T, ResultError>.mapResultObserved(
        localized: ErrorLocalized,
    ): Result<T, ObservedCheckoutError> =
        when (this) {
            is Result.Success -> Result.Success(data)
            is Result.Error -> Result.Error(from(error, localized))
        }

    private const val EMPTY_BODY = "EMPTY_BODY"
    private const val MIN_HTTP_STATUS = 100
    private const val MAX_HTTP_STATUS = 599
    private const val HTTP_UNAUTHORIZED = 401
    private const val HTTP_FORBIDDEN = 403
    private const val HTTP_REQUEST_TIMEOUT = 408
    private const val HTTP_GATEWAY_TIMEOUT = 504
    private val CONFIGURATION_CODES = setOf("CONFIGURATION_ERROR", "INTEGRATION_ERROR")
    private val UNKNOWN_CODES = setOf("EXCEPTION", "UNKNOWN_ERROR")
    private val CONNECTION_CODES = setOf(
        "NETWORK_CONNECTION_FAILED",
        "NO_INTERNET",
        "CONNECTION",
        "NETWORK",
        "UNREACHABLE",
    )
    private val TIMEOUT_CODES = setOf("NETWORK_TIMEOUT", "TIMEOUT")
}
