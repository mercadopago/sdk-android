package com.mercadopago.sdk.android.coremethods.data.datasource

import com.mercadopago.sdk.android.core.data.remote.response.MPErrorResponse
import com.mercadopago.sdk.android.core.data.remote.utils.MPResponse
import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSourceImpl
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.Test

internal class CoreMethodsRemoteDataSourceImplTest {

    private val service: CoreMethodsService = mock()
    private val remoteDataSource = CoreMethodsRemoteDataSourceImpl(service)

    @Test
    fun `test generateCardToken calls service and returns success`() = runBlocking {
        // Cria uma instância de CardTokenBodyRequest
        val cardTokenRequest = CardTokenBodyRequest(cardId = "card_123")

        // Cria uma resposta mock de sucesso
        val cardTokenResponse = CardTokenResponse(id = "token_id")
        val mpResponse: MPResponse<CardTokenResponse> = MPResponse.Success(cardTokenResponse)

        // Configura o mock para retornar a resposta de sucesso
        whenever(service.createToken(any())).thenReturn(mpResponse)

        // Chama o método
        val result = remoteDataSource.generateCardToken(cardTokenRequest)

        // Verifica se o resultado é uma resposta de sucesso
        assertTrue(result is MPResponse.Success)
        assertEquals("token_id", (result as MPResponse.Success).response.id)
    }

    @Test
    fun `test generateCardToken calls service and returns error`() = runBlocking {
        // Cria uma instância de CardTokenBodyRequest
        val cardTokenRequest = CardTokenBodyRequest(cardId = "card_123")

        // Cria uma resposta mock de erro
        val errorResponse = MPErrorResponse(code = "400", message = "Bad Request")
        val mpResponse: MPResponse<CardTokenResponse> = MPResponse.Error(errorResponse)

        // Configura o mock para retornar a resposta de erro
        whenever(service.createToken(any())).thenReturn(mpResponse)

        // Chama o método
        val result = remoteDataSource.generateCardToken(cardTokenRequest)

        // Verifica se o resultado é uma resposta de erro
        assertTrue(result is MPResponse.Error)
        assertEquals("Bad Request", (result as MPResponse.Error).errorResponse.message)
        assertEquals("400", result.errorResponse.code)
    }
}
