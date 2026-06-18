package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickCardOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickCardParams
import com.mercadopago.sdk.android.checkout.domain.repository.PaymentBrickCardRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class FetchPaymentBrickCardUseCaseTest {
    private val repository = mockk<PaymentBrickCardRepository>()
    private val useCase = FetchPaymentBrickCardUseCase(repository)

    private val params = FetchPaymentBrickCardParams(orderId = "ORDER_123", bin = "503143")

    @Test
    fun `given repository returns success then returns Result Success`() = runTest {
        coEvery { repository.fetch(params) } returns Result.Success(mockk(relaxed = true))

        val result = useCase(params)

        assertIs<Result.Success<PaymentBrickCardOutput>>(result)
    }

    @Test
    fun `given repository returns error then returns ServiceError localized to PAYMENT_BRICK_CARD`() = runTest {
        val error = ResponseError(code = "500", message = "Error", httpStatus = 500)
        coEvery { repository.fetch(params) } returns Result.Error(error)

        val result = useCase(params)

        val checkoutError = assertIs<Result.Error<MercadoPagoCheckoutError>>(result).error
        assertIs<MercadoPagoCheckoutError.ServiceError>(checkoutError)
        assertEquals(ErrorCode.SERVICE_ERROR, checkoutError.errorCode)
        assertEquals(ErrorLocalized.PAYMENT_BRICK_CARD.name, checkoutError.errorLocalized)
    }

    @Test
    fun `given repository is called then delegates params`() = runTest {
        coEvery { repository.fetch(params) } returns Result.Success(mockk(relaxed = true))

        useCase(params)

        coVerify(exactly = 1) { repository.fetch(params) }
    }
}
