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

internal class CoreMethodsRemoteDataSourceTest {

    private val service: CoreMethodsService = mock()
    private val remoteDataSource = CoreMethodsRemoteDataSourceImpl(service)

    @Test
    fun `test generateCardToken calls service and returns success`() = runBlocking {
        val cardTokenRequest = CardTokenBodyRequest(cardId = "card_123")

        val cardTokenResponse = CardTokenResponse(id = "token_id")
        val mpResponse: MPResponse<CardTokenResponse> = MPResponse.Success(cardTokenResponse)

        whenever(service.createToken(any())).thenReturn(mpResponse)

        val result = remoteDataSource.generateCardToken(cardTokenRequest)

        assertTrue(result is MPResponse.Success)
        assertEquals("token_id", (result as MPResponse.Success).response.token)
    }

    @Test
    fun `test generateCardToken calls service and returns error`() = runBlocking {
        val cardTokenRequest = CardTokenBodyRequest(cardId = "card_123")

        val errorResponse = MPErrorResponse(code = "400", message = "Bad Request")
        val mpResponse: MPResponse<CardTokenResponse> = MPResponse.Error(errorResponse)

        whenever(service.createToken(any())).thenReturn(mpResponse)

        val result = remoteDataSource.generateCardToken(cardTokenRequest)

        assertTrue(result is MPResponse.Error)
        assertEquals("Bad Request", (result as MPResponse.Error).errorResponse.message)
        assertEquals("400", result.errorResponse.code)
    }
}
