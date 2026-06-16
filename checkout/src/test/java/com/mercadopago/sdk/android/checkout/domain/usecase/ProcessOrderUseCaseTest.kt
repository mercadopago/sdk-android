package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.checkout.domain.repository.OrderRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class ProcessOrderUseCaseTest {
    private val repository = mockk<OrderRepository>()
    private val useCase = ProcessOrderUseCase(repository)

    private val params = ProcessOrderParams(
        orderId = "ORD_123",
        amount = "100.00",
        paymentMethodId = "visa",
        paymentMethodType = "credit_card",
        token = "TOKEN_ABC",
        installments = 3,
        clientToken = "test-token",
    )

    @Test
    fun `given repository returns success then returns Result Success with OrderProcessOutput`() = runTest {
        val output = OrderProcessOutput(id = "ORD_123", status = "approved")
        coEvery { repository.process(params) } returns Result.Success(output)

        val result = useCase(params)

        val success = assertIs<Result.Success<OrderProcessOutput>>(result)
        assertEquals("ORD_123", success.data.id)
        assertEquals("approved", success.data.status)
    }

    @Test
    fun `given repository returns service error then returns ServiceError localized to ORDER_PROCESS`() = runTest {
        val error = ResponseError(code = "bad_request", message = "Service unavailable", httpStatus = 400)
        coEvery { repository.process(params) } returns Result.Error(error)

        val result = useCase(params)

        val checkoutError = assertIs<Result.Error<MercadoPagoCheckoutError>>(result).error
        assertIs<MercadoPagoCheckoutError.ServiceError>(checkoutError)
        assertEquals(ErrorCode.SERVICE_ERROR, checkoutError.errorCode)
        assertEquals(ErrorLocalized.ORDER_PROCESS.name, checkoutError.errorLocalized)
    }

    @Test
    fun `given repository returns network error then returns NetworkError localized to ORDER_PROCESS`() = runTest {
        val error = ResponseError(code = "NO_INTERNET", message = "No internet connection")
        coEvery { repository.process(params) } returns Result.Error(error)

        val result = useCase(params)

        val checkoutError = assertIs<Result.Error<MercadoPagoCheckoutError>>(result).error
        assertIs<MercadoPagoCheckoutError.NetworkError>(checkoutError)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, checkoutError.errorCode)
        assertEquals(ErrorLocalized.ORDER_PROCESS.name, checkoutError.errorLocalized)
    }

    @Test
    fun `given repository throws exception then withErrorHandling catches it and returns Result Error`() = runTest {
        coEvery { repository.process(params) } throws RuntimeException("Unexpected failure")

        val result = useCase(params)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
    }

    @Test
    fun `given repository fails then process is called exactly once — no retry`() = runTest {
        val error = ResponseError(code = "SERVER_ERROR", message = "Internal error", httpStatus = 500)
        coEvery { repository.process(params) } returns Result.Error(error)

        useCase(params)

        coVerify(exactly = 1) { repository.process(params) }
    }

    @Test
    fun `given invoke is called then delegates params to repository`() = runTest {
        coEvery { repository.process(params) } returns Result.Success(mockk(relaxed = true))

        useCase(params)

        coVerify { repository.process(params) }
    }
}
