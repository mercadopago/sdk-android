package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class GetCardIssuersUseCaseTest {
    private val coreMethods = mockk<CoreMethods>()
    private val useCase = GetCardIssuersUseCase(coreMethods)

    private val bin = "123456"
    private val paymentMethodId = "visa"

    @Test
    fun `given coreMethods returns success then returns issuers`() = runTest {
        val issuers = listOf(CardIssuer(id = "issuer-1"), CardIssuer(id = "issuer-2"))
        coEvery { coreMethods.getCardIssuers(bin, paymentMethodId) } returns Result.Success(issuers)

        val result = useCase(bin, paymentMethodId)

        assertIs<Result.Success<List<CardIssuer>>>(result)
        assertEquals(issuers, result.data)
    }

    @Test
    fun `given coreMethods returns empty list then returns empty success`() = runTest {
        coEvery { coreMethods.getCardIssuers(bin, paymentMethodId) } returns Result.Success(emptyList())

        val result = useCase(bin, paymentMethodId)

        assertIs<Result.Success<List<CardIssuer>>>(result)
        assertEquals(emptyList(), result.data)
    }

    @Test
    fun `given coreMethods returns request network error then returns NetworkError`() = runTest {
        val requestError = ResultError.Request(message = "Connection failed", code = "NETWORK_ERROR")
        coEvery { coreMethods.getCardIssuers(bin, paymentMethodId) } returns Result.Error(requestError)

        val result = useCase(bin, paymentMethodId)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, result.error.errorCode)
        assertEquals(ErrorLocalized.ISSUERS.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns generic request error then returns ServiceError`() = runTest {
        val requestError = ResultError.Request(message = "Server error", code = "SERVER_ERROR")
        coEvery { coreMethods.getCardIssuers(bin, paymentMethodId) } returns Result.Error(requestError)

        val result = useCase(bin, paymentMethodId)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.ISSUERS.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns validation error then returns ServiceError`() = runTest {
        val validationError = ResultError.Validation(message = "Invalid request")
        coEvery { coreMethods.getCardIssuers(bin, paymentMethodId) } returns Result.Error(validationError)

        val result = useCase(bin, paymentMethodId)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.ISSUERS.name, result.error.errorLocalized)
    }
}
