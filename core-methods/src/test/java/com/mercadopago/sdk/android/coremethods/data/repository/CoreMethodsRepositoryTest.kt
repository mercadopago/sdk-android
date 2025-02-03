package com.mercadopago.sdk.android.coremethods.data.repository

import com.mercadopago.sdk.android.core.data.remote.response.MPErrorResponse
import com.mercadopago.sdk.android.core.data.remote.utils.MPResponse
import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSource
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse
import com.mercadopago.sdk.android.coremethods.domain.model.CardTokenFields
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import kotlin.test.Test

internal class CoreMethodsRepositoryTest {

    private val dataSource: CoreMethodsRemoteDataSource = mock()
    private val repository = CoreMethodsRepositoryImpl(dataSource)

    @Test
    fun `test generateCardToken returns Success`() = runBlocking {
        val cardTokenFields = CardTokenFields(cardId = "card_123")

        val response = MPResponse.Success(CardTokenResponse().copy(id = "token_id"))
        whenever(dataSource.generateCardToken(any())).thenReturn(response)

        val result = repository.generateCardToken(cardTokenFields)

        assertTrue(result is Result.Success)
        assertEquals("token_id", (result as Result.Success).data.token)
    }

    @Test
    fun `test generateCardToken returns Error`() = runBlocking {
        val cardTokenFields = CardTokenFields(cardId = "card_123")

        val errorResponse = MPErrorResponse(code = "400", message = "Bad Request")
        val response: MPResponse<Nothing> = MPResponse.Error(errorResponse)
        whenever(dataSource.generateCardToken(any())).thenReturn(response)

        val result = repository.generateCardToken(cardTokenFields)

        assertTrue(result is Result.Error)
        assertEquals("Bad Request", (result as Result.Error).error.message)
        assertEquals("400", result.error.code)
    }
}
