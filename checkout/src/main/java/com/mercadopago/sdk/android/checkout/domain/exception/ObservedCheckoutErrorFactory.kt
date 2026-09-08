package com.mercadopago.sdk.android.checkout.domain.exception

import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorEvidenceCode
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorInput
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorResponseState
import com.mercadopago.sdk.android.analytics.domain.classifier.NativeErrorType
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
        return ObservedCheckoutError(
            publicError,
            neutralInput(publicError, retainedCode, error.httpStatus),
        )
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
        return ObservedCheckoutError(
            publicError,
            neutralInput(
                publicError = publicError,
                retainedCode = (error as? ResultError.Request)?.code,
                isValidation = error is ResultError.Validation,
                isEmptyBody = error is ResultError.Request &&
                    error.code == EMPTY_BODY_STATUS && error.message == EMPTY_BODY_MESSAGE,
            ),
        )
    }

    @Suppress("CyclomaticComplexMethod")
    private fun neutralInput(
        publicError: MercadoPagoCheckoutError,
        retainedCode: String? = null,
        httpStatus: Int? = null,
        isValidation: Boolean = false,
        isEmptyBody: Boolean = false,
    ): NativeErrorInput {
        val normalizedCode = retainedCode?.uppercase(Locale.ROOT)
        val type = when {
            isValidation -> NativeErrorType.VALIDATION
            publicError is MercadoPagoCheckoutError.UnknownError || normalizedCode in UNKNOWN_CODES ->
                NativeErrorType.UNKNOWN
            publicError is MercadoPagoCheckoutError.ServiceError -> NativeErrorType.SERVICE
            else -> NativeErrorType.REQUEST
        }
        val code = when {
            publicError is MercadoPagoCheckoutError.ConfigurationError -> NativeErrorEvidenceCode.CONFIGURATION
            normalizedCode == CONFIGURATION_ERROR -> NativeErrorEvidenceCode.CONFIGURATION
            normalizedCode == INTEGRATION_ERROR -> NativeErrorEvidenceCode.INTEGRATION
            normalizedCode == HTTP_UNAUTHORIZED_CODE -> NativeErrorEvidenceCode.HTTP_UNAUTHORIZED
            normalizedCode == HTTP_FORBIDDEN_CODE -> NativeErrorEvidenceCode.HTTP_FORBIDDEN
            normalizedCode in CONNECTION_CODES ||
                publicError.errorCode == ErrorCode.NETWORK_CONNECTION_FAILED -> NativeErrorEvidenceCode.OFFLINE
            normalizedCode in TIMEOUT_CODES ||
                publicError.errorCode == ErrorCode.NETWORK_TIMEOUT -> NativeErrorEvidenceCode.TIMEOUT
            normalizedCode == EXCEPTION -> NativeErrorEvidenceCode.EXCEPTION
            normalizedCode == UNKNOWN_ERROR -> NativeErrorEvidenceCode.UNKNOWN_ERROR
            else -> null
        }
        val responseState = NativeErrorResponseState.EMPTY_BODY.takeIf {
            normalizedCode == EMPTY_BODY || isEmptyBody
        }
        return NativeErrorInput.create(type, code, httpStatus, responseState)
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
    private const val EMPTY_BODY_STATUS = "200"
    private const val EMPTY_BODY_MESSAGE = "empty body"
    private const val CONFIGURATION_ERROR = "CONFIGURATION_ERROR"
    private const val INTEGRATION_ERROR = "INTEGRATION_ERROR"
    private const val EXCEPTION = "EXCEPTION"
    private const val UNKNOWN_ERROR = "UNKNOWN_ERROR"
    private const val HTTP_UNAUTHORIZED_CODE = "401"
    private const val HTTP_FORBIDDEN_CODE = "403"
    private val UNKNOWN_CODES = setOf(EXCEPTION, UNKNOWN_ERROR)
    private val CONNECTION_CODES = setOf(
        "NETWORK_CONNECTION_FAILED",
        "NO_INTERNET",
        "CONNECTION",
        "NETWORK",
        "UNREACHABLE",
    )
    private val TIMEOUT_CODES = setOf("NETWORK_TIMEOUT", "TIMEOUT", "408", "504")
}
