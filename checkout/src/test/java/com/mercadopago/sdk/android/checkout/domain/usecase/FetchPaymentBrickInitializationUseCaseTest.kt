package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.checkout.domain.repository.PaymentBrickInitializationRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class FetchPaymentBrickInitializationUseCaseTest {
    private val repository = mockk<PaymentBrickInitializationRepository>()
    private val useCase = FetchPaymentBrickInitializationUseCase(repository)

    private val params = FetchPaymentBrickInitializationParams(
        orderId = "ORDER_123",
        totalAmount = "500.00",
    )

    @Test
    fun `given repository returns success then returns Result Success`() = runTest {
        coEvery { repository.fetch(params) } returns Result.Success(mockk(relaxed = true))

        val result = useCase(params)

        assertIs<Result.Success<PaymentBrickInitializationOutput>>(result)
    }

    @Test
    fun `given repository returns service error then errorLocalized is PAYMENT_INITIALIZATION`() = runTest {
        val error = ResponseError(code = "500", message = "Internal error", httpStatus = 500)
        coEvery { repository.fetch(params) } returns Result.Error(error)

        val result = useCase(params)

        val checkoutError = assertIs<Result.Error<MercadoPagoCheckoutError>>(result).error
        assertIs<MercadoPagoCheckoutError.ServiceError>(checkoutError)
        assertEquals(ErrorCode.SERVICE_ERROR, checkoutError.errorCode)
        assertEquals(ErrorLocalized.PAYMENT_INITIALIZATION.name, checkoutError.errorLocalized)
    }

    @Test
    fun `given repository returns network error then errorLocalized is PAYMENT_INITIALIZATION`() = runTest {
        val error = ResponseError(code = "TIMEOUT", message = "Timeout", httpStatus = null)
        coEvery { repository.fetch(params) } returns Result.Error(error)

        val result = useCase(params)

        val checkoutError = assertIs<Result.Error<MercadoPagoCheckoutError>>(result).error
        assertIs<MercadoPagoCheckoutError.NetworkError>(checkoutError)
        assertEquals(ErrorLocalized.PAYMENT_INITIALIZATION.name, checkoutError.errorLocalized)
    }

    @Test
    fun `given repository is called then delegates params to repository`() = runTest {
        coEvery { repository.fetch(params) } returns Result.Success(mockk(relaxed = true))

        useCase(params)

        coVerify(exactly = 1) { repository.fetch(params) }
    }
}
