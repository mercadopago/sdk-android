package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class GenerateTokenWithCardIdUseCaseTest {
    private val coreMethods = mockk<CoreMethods>()
    private val useCase = GenerateTokenWithCardIdUseCase(coreMethods)

    private val cardId = "card-abc-123"
    private val securityCodeState = mockk<PCIFieldState>(relaxed = true)

    @Test
    fun `given saved card without security code then returns token string`() = runTest {
        val expectedToken = "token-xyz-456"
        coEvery {
            coreMethods.generateCardToken(cardId)
        } returns Result.Success(CardToken(token = expectedToken))

        val result = useCase(cardId)

        assertIs<Result.Success<String>>(result)
        assertEquals(expectedToken, result.data)
    }

    @Test
    fun `given saved card tokenization error then maps checkout error`() = runTest {
        val requestError = ResultError.Request(message = "Connection failed", code = "NETWORK")
        coEvery {
            coreMethods.generateCardToken(cardId)
        } returns Result.Error(requestError)

        val result = useCase(cardId)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorLocalized.TOKENIZATION.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns success then returns token string`() = runTest {
        val expectedToken = "token-xyz-456"
        coEvery {
            coreMethods.generateCardTokenWithSecurityCode(cardId, securityCodeState)
        } returns Result.Success(CardToken(token = expectedToken))

        val result = useCase(cardId, securityCodeState)

        assertIs<Result.Success<String>>(result)
        assertEquals(expectedToken, result.data)
    }

    @Test
    fun `given coreMethods returns request network error then returns NetworkError`() = runTest {
        val requestError = ResultError.Request(message = "Connection failed", code = "NETWORK")
        coEvery {
            coreMethods.generateCardTokenWithSecurityCode(cardId, securityCodeState)
        } returns Result.Error(requestError)

        val result = useCase(cardId, securityCodeState)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, result.error.errorCode)
        assertEquals(ErrorLocalized.TOKENIZATION.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns timeout error then returns NetworkError with timeout code`() = runTest {
        val requestError = ResultError.Request(message = "Timeout", code = "TIMEOUT")
        coEvery {
            coreMethods.generateCardTokenWithSecurityCode(cardId, securityCodeState)
        } returns Result.Error(requestError)

        val result = useCase(cardId, securityCodeState)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_TIMEOUT, result.error.errorCode)
        assertEquals(ErrorLocalized.TOKENIZATION.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns generic request error then returns ServiceError`() = runTest {
        val requestError = ResultError.Request(message = "Server error", code = "SERVER_ERROR")
        coEvery {
            coreMethods.generateCardTokenWithSecurityCode(cardId, securityCodeState)
        } returns Result.Error(requestError)

        val result = useCase(cardId, securityCodeState)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.TOKENIZATION.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns validation error then returns ServiceError`() = runTest {
        val validationError = ResultError.Validation(message = "Invalid security code")
        coEvery {
            coreMethods.generateCardTokenWithSecurityCode(cardId, securityCodeState)
        } returns Result.Error(validationError)

        val result = useCase(cardId, securityCodeState)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.TOKENIZATION.name, result.error.errorLocalized)
    }
}
