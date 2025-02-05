package com.mercadopago.sdk.android.coremethods.data.datasource

import com.google.gson.Gson
import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSourceImpl
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import retrofit2.Response
import kotlin.test.Test

internal class CoreMethodsRemoteDataSourceTest {

    private val service: CoreMethodsService = mockk()
    private val remoteDataSource = CoreMethodsRemoteDataSourceImpl(service)

    @Test
    fun `test generateCardToken calls service and returns success`() = runBlocking {
        val cardTokenRequest = CardTokenBodyRequest(cardId = "card_123")

        val cardTokenResponse = CardTokenResponse(id = "token_id")
        val mpResponse: Response<CardTokenResponse> = Response.success(cardTokenResponse)

        coEvery { service.createToken(any()) } returns mpResponse

        val result = remoteDataSource.generateCardToken(cardTokenRequest)

        assertTrue(result is Result.Success)
        assertEquals("token_id", (result as Result.Success).data.token)
    }

    @Test
    fun `test generateCardToken calls service and returns error`() = runBlocking {
        val cardTokenRequest = CardTokenBodyRequest(cardId = "card_123")

        val errorBody = ResultError(code = "400", message = "Bad Request")

        val gson = Gson()
        val jsonErrorBody = gson.toJson(errorBody)
        val responseBody: ResponseBody = ResponseBody.create(
            "application/json".toMediaTypeOrNull(),
            jsonErrorBody
        )

        val mpResponse: Response<CardTokenResponse> = Response.error(400, responseBody)

        coEvery { service.createToken(any()) } returns mpResponse

        val result = remoteDataSource.generateCardToken(cardTokenRequest)

        assertTrue(result is Result.Error)
        assertEquals("Bad Request", (result as Result.Error).error.message)
        assertEquals("400", result.error.code)
    }
}
