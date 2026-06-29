package com.mercadopago.sdk.android.checkout.presentation.usecase

import com.mercadopago.sdk.android.checkout.domain.exception.ErrorCode
import com.mercadopago.sdk.android.checkout.domain.exception.ErrorLocalized
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.coremethods.domain.interactor.CoreMethods
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
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

internal class GenerateTokenUseCaseTest {
    private val coreMethods = mockk<CoreMethods>()
    private val useCase = GenerateTokenUseCase(coreMethods)

    private val cardNumberState = mockk<PCIFieldState>(relaxed = true)
    private val expirationDateState = mockk<PCIFieldState>(relaxed = true)
    private val securityCodeState = mockk<PCIFieldState>(relaxed = true)
    private val buyerIdentification = BuyerIdentification(name = "APRO", number = "012345678", type = "CPF")

    @Test
    fun `given coreMethods returns success then returns card token`() = runTest {
        val token = CardToken(token = "token-abc-123")
        coEvery {
            coreMethods.generateCardToken(cardNumberState, expirationDateState, securityCodeState, buyerIdentification)
        } returns Result.Success(token)

        val result = useCase(cardNumberState, expirationDateState, securityCodeState, buyerIdentification)

        assertIs<Result.Success<CardToken>>(result)
        assertEquals(token, result.data)
    }

    @Test
    fun `given coreMethods returns request network error then returns NetworkError`() = runTest {
        val requestError = ResultError.Request(message = "Connection failed", code = "NETWORK")
        coEvery {
            coreMethods.generateCardToken(cardNumberState, expirationDateState, securityCodeState, buyerIdentification)
        } returns Result.Error(requestError)

        val result = useCase(cardNumberState, expirationDateState, securityCodeState, buyerIdentification)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_CONNECTION_FAILED, result.error.errorCode)
        assertEquals(ErrorLocalized.TOKENIZATION.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns request timeout error then returns NetworkError with timeout code`() = runTest {
        val requestError = ResultError.Request(message = "Timeout", code = "TIMEOUT")
        coEvery {
            coreMethods.generateCardToken(cardNumberState, expirationDateState, securityCodeState, buyerIdentification)
        } returns Result.Error(requestError)

        val result = useCase(cardNumberState, expirationDateState, securityCodeState, buyerIdentification)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.NetworkError>(result.error)
        assertEquals(ErrorCode.NETWORK_TIMEOUT, result.error.errorCode)
        assertEquals(ErrorLocalized.TOKENIZATION.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns generic request error then returns ServiceError`() = runTest {
        val requestError = ResultError.Request(message = "Server error", code = "SERVER_ERROR")
        coEvery {
            coreMethods.generateCardToken(cardNumberState, expirationDateState, securityCodeState, buyerIdentification)
        } returns Result.Error(requestError)

        val result = useCase(cardNumberState, expirationDateState, securityCodeState, buyerIdentification)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.TOKENIZATION.name, result.error.errorLocalized)
    }

    @Test
    fun `given coreMethods returns validation error then returns ServiceError`() = runTest {
        val validationError = ResultError.Validation(message = "Invalid card data")
        coEvery {
            coreMethods.generateCardToken(cardNumberState, expirationDateState, securityCodeState, buyerIdentification)
        } returns Result.Error(validationError)

        val result = useCase(cardNumberState, expirationDateState, securityCodeState, buyerIdentification)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertIs<MercadoPagoCheckoutError.ServiceError>(result.error)
        assertEquals(ErrorCode.SERVICE_ERROR, result.error.errorCode)
        assertEquals(ErrorLocalized.TOKENIZATION.name, result.error.errorLocalized)
    }
}
