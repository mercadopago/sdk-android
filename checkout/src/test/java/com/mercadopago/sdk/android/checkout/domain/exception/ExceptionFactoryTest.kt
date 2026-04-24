package com.mercadopago.sdk.android.checkout.domain.exception

import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
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
    fun `when code contains NETWORK then mapRequestError returns NetworkError with NETWORK_CONNECTION_FAILED`() {
        val error = ResultError.Request(message = message, code = "NETWORK_ERROR")

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, networkError.code)
        assertEquals(message, networkError.messageError)
        assertEquals(localized.name, networkError.localized)
        assertNull(networkError.throwable)
    }

    @Test
    fun `when code contains CONNECTION then mapRequestError returns NetworkError with NETWORK_CONNECTION_FAILED`() {
        val error = ResultError.Request(message = message, code = "CONNECTION_RESET")

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, networkError.code)
    }

    @Test
    fun `when code contains NO_INTERNET then mapRequestError returns NetworkError with NETWORK_CONNECTION_FAILED`() {
        val error = ResultError.Request(message = message, code = "NO_INTERNET")

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, networkError.code)
    }

    @Test
    fun `when code contains UNREACHABLE then mapRequestError returns NetworkError with NETWORK_CONNECTION_FAILED`() {
        val error = ResultError.Request(message = message, code = "HOST_UNREACHABLE")

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, networkError.code)
    }

    @Test
    fun `when code contains TIMEOUT then mapRequestError returns NetworkError with NETWORK_TIMEOUT`() {
        val error = ResultError.Request(message = message, code = "REQUEST_TIMEOUT")

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_TIMEOUT, networkError.code)
        assertEquals(message, networkError.messageError)
        assertEquals(localized.name, networkError.localized)
        assertNull(networkError.throwable)
    }

    @Test
    fun `when code is unknown then mapRequestError returns ServiceError with SERVICE_ERROR`() {
        val error = ResultError.Request(message = message, code = "404")

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val serviceError = assertIs<MercadoPagoCheckoutError.ServiceError>(result)
        assertEquals(ErrorCode.SERVICE_ERROR, serviceError.code)
        assertEquals(message, serviceError.messageError)
        assertEquals(localized.name, serviceError.localized)
        assertNull(serviceError.throwable)
    }

    @Test
    fun `when code contains lowercase network keyword then mapRequestError returns NetworkError`() {
        val error = ResultError.Request(message = message, code = "network_failure")

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, networkError.code)
    }

    @Test
    fun `when code contains timeout then mapRequestError returns NetworkError with NETWORK_TIMEOUT`() {
        val error = ResultError.Request(message = message, code = "timeout")

        val result = ExceptionFactory.mapRequestError(error = error, localized = localized)

        val networkError = assertIs<MercadoPagoCheckoutError.NetworkError>(result)
        assertEquals(ErrorCode.NETWORK_TIMEOUT, networkError.code)
    }

    @Test
    fun `when mapValidationError called then returns ServiceError with SERVICE_ERROR`() {
        val error = ResultError.Validation(message = message)

        val result = ExceptionFactory.mapValidationError(error = error, localized = localized)

        assertEquals(ErrorCode.SERVICE_ERROR, result.code)
        assertEquals(message, result.messageError)
        assertEquals(localized.name, result.localized)
        assertNull(result.throwable)
    }

    @Test
    fun `when mapToCheckoutError with Success then returns Success unchanged`() {
        val data = "payload"
        val result: Result<String, ResultError> = Result.Success(data)

        val mapped = result.mapToCheckoutError(localized = localized)

        val success = assertIs<Result.Success<String>>(mapped)
        assertEquals(data, success.data)
    }

    @Test
    fun `when mapToCheckoutError with Request network error then returns Error with NetworkError`() {
        val result: Result<String, ResultError> = Result.Error(
            ResultError.Request(message = message, code = "NETWORK_FAILURE"),
        )

        val mapped = result.mapToCheckoutError(localized = localized)

        val error = assertIs<Result.Error<MercadoPagoCheckoutError>>(mapped)
        assertIs<MercadoPagoCheckoutError.NetworkError>(error.error)
    }

    @Test
    fun `when mapToCheckoutError with Request unknown error then returns Error with ServiceError`() {
        val result: Result<String, ResultError> = Result.Error(
            ResultError.Request(message = message, code = "500"),
        )

        val mapped = result.mapToCheckoutError(localized = localized)

        val error = assertIs<Result.Error<MercadoPagoCheckoutError>>(mapped)
        assertIs<MercadoPagoCheckoutError.ServiceError>(error.error)
    }

    @Test
    fun `when mapToCheckoutError with Validation error then returns Error with ServiceError`() {
        val result: Result<String, ResultError> = Result.Error(
            ResultError.Validation(message = message),
        )

        val mapped = result.mapToCheckoutError(localized = localized)

        val error = assertIs<Result.Error<MercadoPagoCheckoutError>>(mapped)
        assertIs<MercadoPagoCheckoutError.ServiceError>(error.error)
    }
}
