package com.mercadopago.sdk.android.coremethods.data.repository

import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSource
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import kotlin.test.Test

internal class CoreMethodsRepositoryTest {

    private val dataSource: CoreMethodsRemoteDataSource = mockk()
    private val repository = CoreMethodsRepositoryImpl(dataSource)

    @Test
    fun `test generateCardToken returns Success`() = runBlocking {
        val cardTokenFields = CardTokenFields(cardId = "card_123")

        val response = Result.Success(CardToken(token = "token_id"))
        coEvery { dataSource.generateCardToken(any()) } returns response

        val result = repository.generateCardToken(cardTokenFields)

        assertTrue(result is Result.Success)
        assertEquals("token_id", (result as Result.Success).data.token)
    }

    @Test
    fun `test generateCardToken returns Error`() = runBlocking {
        val cardTokenFields = CardTokenFields(cardId = "card_123")

        val errorResponse = ResultError(code = "400", message = "Bad Request")
        val response: Result<CardToken, ResultError> = Result.Error(errorResponse)
        coEvery { dataSource.generateCardToken(any()) } returns response

        val result = repository.generateCardToken(cardTokenFields)

        assertTrue(result is Result.Error)
        assertEquals("Bad Request", (result as Result.Error).error.message)
        assertEquals("400", result.error.code)
    }
}
