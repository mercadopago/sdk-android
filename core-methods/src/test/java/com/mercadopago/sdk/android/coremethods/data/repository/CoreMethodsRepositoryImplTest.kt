package com.mercadopago.sdk.android.coremethods.data.repository

import com.mercadopago.sdk.android.core.data.remote.response.MPErrorResponse
import com.mercadopago.sdk.android.core.data.remote.utils.MPResponse
import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSource
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertNotNull

internal class CoreMethodsRepositoryImplTest {

    private val dataSource: CoreMethodsRemoteDataSource = mock()
    private val repository = CoreMethodsRepositoryImpl(dataSource)

    @Test
    fun `test generateCardToken returns Success`() = runBlocking {
        // Cria um CardTokenFields de teste
        val cardTokenFields = CardTokenFields(cardId = "card_123")

        // Cria uma resposta mock de sucesso
        val response = MPResponse.Success(CardTokenResponse().copy(id = "token_id"))
        whenever(dataSource.generateCardToken(any())).thenReturn(response)

        // Chama o método
        val result = repository.generateCardToken(cardTokenFields)

        // Verifica se o resultado é um sucesso
        assertTrue(result is Result.Success)
        assertEquals("token_id", (result as Result.Success).data.token)
    }

    @Test
    fun `test generateCardToken returns Error`() = runBlocking {
        // Cria um CardTokenFields de teste
        val cardTokenFields = CardTokenFields(cardId = "card_123")

        // Cria uma resposta mock de erro
        val errorResponse = MPErrorResponse(code = "400", message = "Bad Request")
        val response: MPResponse<Nothing> = MPResponse.Error(errorResponse)
        whenever(dataSource.generateCardToken(any())).thenReturn(response)

        // Chama o método
        val result = repository.generateCardToken(cardTokenFields)

        // Verifica se o resultado é um erro
        assertTrue(result is Result.Error)
        assertEquals("Bad Request", (result as Result.Error).error.message)
        assertEquals("400", result.error.code)
    }

    @Test
    fun `test generateCardToken returns Failure on exception`() = runBlocking {
        // Cria um CardTokenFields de teste
        val cardTokenFields = CardTokenFields(cardId = "card_123")

        // Faz com que a função do mock lance uma exceção
        whenever(dataSource.generateCardToken(any())).thenThrow(RuntimeException("Network Error"))

        // Chama o método
        val result = repository.generateCardToken(cardTokenFields)

        // Verifica se o resultado é uma falha
        assertTrue(result is Result.Failure)
        assertNotNull((result as Result.Failure).throwable)
        assertEquals("Network Error", result.throwable.message)
    }
}
