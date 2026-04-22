package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class GetInstallmentsUseCaseTest {
    private val coreMethods = mockk<CoreMethods>()
    private val useCase = GetInstallmentsUseCase(coreMethods)

    private val bin = "123456"
    private val amount = BigDecimal("150.00")

    @Test
    fun `given coreMethods returns success then returns installments`() = runTest {
        val installments = listOf(mockk<Installment>(), mockk<Installment>())
        coEvery { coreMethods.getInstallments(bin, amount) } returns Result.Success(installments)

        val result = useCase(bin, amount)

        assertIs<Result.Success<List<Installment>>>(result)
        assertEquals(installments, result.data)
    }

    @Test
    fun `given coreMethods returns empty list then returns empty success`() = runTest {
        coEvery { coreMethods.getInstallments(bin, amount) } returns Result.Success(emptyList())

        val result = useCase(bin, amount)

        assertIs<Result.Success<List<Installment>>>(result)
        assertEquals(emptyList(), result.data)
    }

    @Test
    fun `given coreMethods returns request network error then returns NetworkError`() = runTest {
        val requestError = ResultError.Request(message = "Connection failed", code = "NETWORK_ERROR")
        coEvery { coreMethods.getInstallments(bin, amount) } returns Result.Error(requestError)

        val result = useCase(bin, amount)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, result.error.errorCode)
        assertEquals(ErrorLocalized.INSTALLMENTS.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns request timeout error then returns NetworkError with timeout code`() = runTest {
        val requestError = ResultError.Request(message = "Timeout", code = "TIMEOUT_ERROR")
        coEvery { coreMethods.getInstallments(bin, amount) } returns Result.Error(requestError)

        val result = useCase(bin, amount)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_TIMEOUT, result.error.errorCode)
        assertEquals(ErrorLocalized.INSTALLMENTS.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns generic request error then returns ServiceError`() = runTest {
        val requestError = ResultError.Request(message = "Server error", code = "SERVER_ERROR")
        coEvery { coreMethods.getInstallments(bin, amount) } returns Result.Error(requestError)

        val result = useCase(bin, amount)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.INSTALLMENTS.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns validation error then returns ServiceError`() = runTest {
        val validationError = ResultError.Validation(message = "Invalid amount")
        coEvery { coreMethods.getInstallments(bin, amount) } returns Result.Error(validationError)

        val result = useCase(bin, amount)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.INSTALLMENTS.name, result.error.errorLocalized)
    }
}
