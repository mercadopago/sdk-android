package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.params.InitializeCardFormParams
import com.mercadopago.sdk.android.checkout.domain.repository.CardFormRepository
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

internal class InitializeCardFormUseCaseTest {
    private val cardFormRepository = mockk<CardFormRepository>()
    private val useCase = InitializeCardFormUseCase(cardFormRepository)

    private val amount = "100.00"
    private val checkoutType = "card_form"
    private val params = InitializeCardFormParams(amount = amount, checkoutType = checkoutType)

    @Test
    fun `given fetchInitialization succeeds then returns Success with CardFormInitializationOutput`() = runTest {
        coEvery { cardFormRepository.fetchInitialization(params) } returns
            Result.Success(mockk(relaxed = true))

        val result = useCase(amount, checkoutType)

        assertIs<Result.Success<CardFormInitializationOutput>>(result)
    }

    @Test
    fun `given request error then returns ServiceError localized to CARD_FORM_INITIALIZATION`() = runTest {
        val error = ResultError.Request(message = "Service unavailable", code = "SERVICE_ERROR")
        coEvery { cardFormRepository.fetchInitialization(params) } returns Result.Error(error)

        val result = useCase(amount, checkoutType)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        val checkoutError = result.error
        assertIs<MercadoPagoCheckoutError.ServiceError>(checkoutError)
        assertEquals(ErrorCode.SERVICE_ERROR, checkoutError.errorCode)
        assertEquals(ErrorLocalized.CARD_FORM_INITIALIZATION.name, checkoutError.errorLocalized)
    }

    @Test
    fun `given network error then returns NetworkError localized to CARD_FORM_INITIALIZATION`() = runTest {
        val error = ResultError.Request(message = "Connection failed", code = "NETWORK_CONNECTION_FAILED")
        coEvery { cardFormRepository.fetchInitialization(params) } returns Result.Error(error)

        val result = useCase(amount, checkoutType)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        val checkoutError = result.error
        assertIs<MercadoPagoCheckoutError.NetworkError>(checkoutError)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, checkoutError.errorCode)
        assertEquals(ErrorLocalized.CARD_FORM_INITIALIZATION.name, checkoutError.errorLocalized)
    }

    @Test
    fun `given fetchInitialization throws exception then propagates exception`() = runTest {
        coEvery { cardFormRepository.fetchInitialization(params) } throws RuntimeException("Unexpected error")

        assertFailsWith<RuntimeException> { useCase(amount, checkoutType) }
    }

    @Test
    fun `given invoke is called then passes params to repository`() = runTest {
        coEvery { cardFormRepository.fetchInitialization(params) } returns
            Result.Success(mockk(relaxed = true))

        useCase(amount, checkoutType)

        coVerify { cardFormRepository.fetchInitialization(params) }
    }
}
