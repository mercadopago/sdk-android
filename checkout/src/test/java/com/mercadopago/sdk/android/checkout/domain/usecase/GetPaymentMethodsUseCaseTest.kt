package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class GetPaymentMethodsUseCaseTest {
    private val coreMethods = mockk<CoreMethods>()
    private val useCase = GetPaymentMethodsUseCase(coreMethods)

    private val bin = "123456"

    @Test
    fun `given coreMethods returns success then returns payment methods`() = runTest {
        val paymentMethods = listOf(PaymentMethod(id = "visa"))
        coEvery { coreMethods.getPaymentMethods(bin) } returns Result.Success(paymentMethods)

        val result = useCase(bin)

        assertIs<Result.Success<List<PaymentMethod>>>(result)
        assertEquals(paymentMethods, result.data)
    }

    @Test
    fun `given coreMethods returns request network error then returns NetworkError`() = runTest {
        val requestError = ResultError.Request(message = "Connection failed", code = "NETWORK_ERROR")
        coEvery { coreMethods.getPaymentMethods(bin) } returns Result.Error(requestError)

        val result = useCase(bin)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, result.error.errorCode)
        assertEquals(ErrorLocalized.PAYMENT_METHODS.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns request timeout error then returns NetworkError with timeout code`() = runTest {
        val requestError = ResultError.Request(message = "Timeout", code = "TIMEOUT_ERROR")
        coEvery { coreMethods.getPaymentMethods(bin) } returns Result.Error(requestError)

        val result = useCase(bin)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_TIMEOUT, result.error.errorCode)
        assertEquals(ErrorLocalized.PAYMENT_METHODS.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns generic request error then returns ServiceError`() = runTest {
        val requestError = ResultError.Request(message = "Service unavailable", code = "SERVER_ERROR")
        coEvery { coreMethods.getPaymentMethods(bin) } returns Result.Error(requestError)

        val result = useCase(bin)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.PAYMENT_METHODS.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns validation error then returns ServiceError`() = runTest {
        val validationError = ResultError.Validation(message = "Invalid bin")
        coEvery { coreMethods.getPaymentMethods(bin) } returns Result.Error(validationError)

        val result = useCase(bin)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.PAYMENT_METHODS.name, result.error.errorLocalized)
    }
}
