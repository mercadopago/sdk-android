package com.mercadopago.sdk.android.checkout.domain.exception

import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class ExceptionFactoryTest {
    private val localized = ErrorLocalized.TOKENIZATION
    private val message = "error message"

    @Test
    fun `when code is NETWORK then mapRequestError returns NetworkError with NETWORK_CONNECTION_FAILED`() {
        val error = ResponseError(code = ERROR_CODE_NETWORK, message = message)

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, networkError.code)
        assertEquals(message, networkError.messageError)
        assertEquals(localized.name, networkError.localized)
        assertNull(networkError.throwable)
    }

    @Test
    fun `when code is CONNECTION then mapRequestError returns NetworkError with NETWORK_CONNECTION_FAILED`() {
        val error = ResponseError(code = ERROR_CODE_CONNECTION, message = message)

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, networkError.code)
    }

    @Test
    fun `when code is NO_INTERNET then mapRequestError returns NetworkError with NETWORK_CONNECTION_FAILED`() {
        val error = ResponseError(code = ERROR_CODE_NO_INTERNET, message = message)

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, networkError.code)
    }

    @Test
    fun `when code is UNREACHABLE then mapRequestError returns NetworkError with NETWORK_CONNECTION_FAILED`() {
        val error = ResponseError(code = ERROR_CODE_UNREACHABLE, message = message)

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, networkError.code)
    }

    @Test
    fun `when code is TIMEOUT then mapRequestError returns NetworkError with NETWORK_TIMEOUT`() {
        val error = ResponseError(code = ERROR_CODE_TIMEOUT, message = message)

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_TIMEOUT, networkError.code)
        assertEquals(message, networkError.messageError)
        assertEquals(localized.name, networkError.localized)
        assertNull(networkError.throwable)
    }

    @Test
    fun `when httpStatus is 4xx then mapRequestError returns ServiceError with SERVICE_ERROR`() {
        val userErrorMessage = "user facing error message"
        val error = ResponseError(
            code = "bad_request",
            message = message,
            userErrorMessage = userErrorMessage,
            httpStatus = 400,
        )

        val result = ExceptionFactory.mapRequestError(
            error = error,
            localized = localized,
        )

        val serviceError = assertIs<MercadoPagoCheckoutError.ServiceError>(result)
        assertEquals(ErrorCode.SERVICE_ERROR, serviceError.code)
        assertEquals(userErrorMessage, serviceError.messageError)
        assertEquals(localized.name, serviceError.localized)
        assertNull(serviceError.throwable)
    }

    @Test
    fun `when httpStatus is 5xx then mapRequestError returns ServiceError with SERVICE_ERROR`() {
        val error = ResponseError(code = "internal_server_error", message = message, httpStatus = 500)

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val serviceError = assertIs<MercadoPagoCheckoutError.ServiceError>(result)
        assertEquals(ErrorCode.SERVICE_ERROR, serviceError.code)
    }

    @Test
    fun `when code does not match any pattern then mapRequestError returns ServiceError`() {
        val error = ResponseError(code = "unknown_code", message = message)

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        assertIs<MercadoPagoCheckoutError.ServiceError>(result)
    }

    @Test
    fun `when userErrorMessage is set then mapRequestError uses it as messageError`() {
        val userMsg = "Mensagem para o usuário"
        val error = ResponseError(
            code = "bad_request",
            message = "debug message",
            userErrorMessage = userMsg,
            httpStatus = 400,
        )

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val serviceError = assertIs<MercadoPagoCheckoutError.ServiceError>(result)
        assertEquals(userMsg, serviceError.messageError)
    }

    @Test
    fun `when errorCode field is set then it takes priority over code for classification`() {
        val error = ResponseError(
            code = "bad_request",
            errorCode = "PAYMENT_METHOD_UNAVAILABLE",
            message = message,
            httpStatus = 400,
        )

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        assertIs<MercadoPagoCheckoutError.ServiceError>(result)
    }

    @Test
    fun `when mapError with Success then returns Success unchanged`() {
        val data = "payload"
        val result: Result<String, ResponseError> = Result.Success(data)

        val mapped = with(ExceptionFactory) { result.mapError(localized = localized) }

        val success = assertIs<Result.Success<String>>(mapped)
        assertEquals(data, success.data)
    }

    @Test
    fun `when mapError with network error then returns Result Error with NetworkError`() {
        val result: Result<String, ResponseError> = Result.Error(
            ResponseError(code = ERROR_CODE_NO_INTERNET, message = message),
        )

        val mapped = with(ExceptionFactory) { result.mapError(localized = localized) }

        val error = assertIs<Result.Error<MercadoPagoCheckoutError>>(mapped)
        assertIs<MercadoPagoCheckoutError.NetworkError>(error.error)
    }

    @Test
    fun `when mapToCheckoutError with Success then returns Success unchanged`() {
        val data = "payload"
        val result: Result<String, ResultError> = Result.Success(data)

        val mapped = with(ExceptionFactory) { result.mapToCheckoutError(localized = localized) }

        val success = assertIs<Result.Success<String>>(mapped)
        assertEquals(data, success.data)
    }

    @Test
    fun `when mapToCheckoutError with Request network error then returns Error with NetworkError`() {
        val result: Result<String, ResultError> = Result.Error(
            ResultError.Request(message = message, code = ERROR_CODE_NO_INTERNET),
        )

        val mapped = with(ExceptionFactory) { result.mapToCheckoutError(localized = localized) }

        val error = assertIs<Result.Error<MercadoPagoCheckoutError>>(mapped)
        assertIs<MercadoPagoCheckoutError.NetworkError>(error.error)
    }

    @Test
    fun `when mapToCheckoutError with Request unknown error then returns Error with ServiceError`() {
        val result: Result<String, ResultError> = Result.Error(
            ResultError.Request(message = message, code = "bad_request"),
        )

        val mapped = with(ExceptionFactory) { result.mapToCheckoutError(localized = localized) }

        val error = assertIs<Result.Error<MercadoPagoCheckoutError>>(mapped)
        assertIs<MercadoPagoCheckoutError.ServiceError>(error.error)
    }

    @Test
    fun `when mapToCheckoutError with Validation error then returns Error with ServiceError`() {
        val result: Result<String, ResultError> = Result.Error(
            ResultError.Validation(message = message),
        )

        val mapped = with(ExceptionFactory) { result.mapToCheckoutError(localized = localized) }

        val error = assertIs<Result.Error<MercadoPagoCheckoutError>>(mapped)
        assertIs<MercadoPagoCheckoutError.ServiceError>(error.error)
    }
}
