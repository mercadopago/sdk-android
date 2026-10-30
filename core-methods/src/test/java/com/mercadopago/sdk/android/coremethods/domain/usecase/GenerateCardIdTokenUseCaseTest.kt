package com.mercadopago.sdk.android.coremethods.domain.usecase

import com.mercadopago.sdk.android.coremethods.di.SecurityCodeLengthProvider
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.model.params.GenerateCardTokenParams
import com.mercadopago.sdk.android.coremethods.domain.repository.CoreMethodsRepository
import com.mercadopago.sdk.android.coremethods.domain.usecase.validations.IsSecurityCodeValidUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.di.SessionIdProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class GenerateCardIdTokenUseCaseTest {
    private val repository = mockk<CoreMethodsRepository>()
    private val sessionIdProvider = mockk<SessionIdProvider>()
    private val securityCodeLengthProvider = mockk<SecurityCodeLengthProvider>()
    private val isSecurityCodeValidUseCase = mockk<IsSecurityCodeValidUseCase>()
    private val useCase = GenerateCardIdTokenUseCase(
        repository = repository,
        sessionIdProvider = sessionIdProvider,
        securityCodeLengthProvider = securityCodeLengthProvider,
        isSecurityCodeValidUseCase = isSecurityCodeValidUseCase,
    )

    @Test
    fun `given security code is null then tokenizes saved card without validating CVV`() = runTest {
        val params = slot<GenerateCardTokenParams>()
        val expectedToken = CardToken("token_saved_123")
        every { sessionIdProvider.getSessionId() } returns "session-id"
        coEvery { repository.generateCardToken(capture(params)) } returns Result.Success(expectedToken)

        val result = useCase(
            cardId = "card-123",
            securityCode = null,
            expirationDate = null,
        )

        assertIs<Result.Success<CardToken>>(result)
        assertEquals(expectedToken, result.data)
        assertEquals("card-123", params.captured.cardId)
        assertNull(params.captured.securityCode)
        verify(exactly = 0) { securityCodeLengthProvider.getExpectedLength() }
        verify(exactly = 0) { isSecurityCodeValidUseCase(any(), any()) }
    }

    @Test
    fun `given security code is empty then returns validation error`() = runTest {
        every { securityCodeLengthProvider.getExpectedLength() } returns 3
        every { isSecurityCodeValidUseCase(0, 3) } returns false

        val result = useCase(
            cardId = "card-123",
            securityCode = "",
            expirationDate = null,
        )

        assertIs<Result.Error<ResultError>>(result)
        coVerify(exactly = 0) { repository.generateCardToken(any()) }
    }

    @Test
    fun `given security code has invalid length then returns validation error`() = runTest {
        every { securityCodeLengthProvider.getExpectedLength() } returns 3
        every { isSecurityCodeValidUseCase(2, 3) } returns false

        val result = useCase(
            cardId = "card-123",
            securityCode = "12",
            expirationDate = null,
        )

        assertIs<Result.Error<ResultError>>(result)
        coVerify(exactly = 0) { repository.generateCardToken(any()) }
    }

    @Test
    fun `given security code is valid then preserves existing tokenization flow`() = runTest {
        val params = slot<GenerateCardTokenParams>()
        val expectedToken = CardToken("token_saved_123")
        every { securityCodeLengthProvider.getExpectedLength() } returns 3
        every { isSecurityCodeValidUseCase(3, 3) } returns true
        every { sessionIdProvider.getSessionId() } returns "session-id"
        coEvery { repository.generateCardToken(capture(params)) } returns Result.Success(expectedToken)

        val result = useCase(
            cardId = "card-123",
            securityCode = "123",
            expirationDate = null,
        )

        assertIs<Result.Success<CardToken>>(result)
        assertEquals("123", params.captured.securityCode)
    }
}
