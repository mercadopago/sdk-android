package com.mercadopago.sdk.android.mpextended.data.datasource

import com.google.gson.Gson
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.mpextended.data.datasource.remote.MPExtendedRemoteDataSourceImpl
import com.mercadopago.sdk.android.mpextended.data.remote.request.MPDeviceSessionIdRequest
import com.mercadopago.sdk.android.mpextended.data.remote.response.DeviceSessionIdResponse
import com.mercadopago.sdk.android.mpextended.data.remote.service.MPExtendedService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import retrofit2.Response
import kotlin.test.Test

internal class MPExtendedRemoteDataSourceImplTest {
    private val service: MPExtendedService = mockk()
    private val dataSource = MPExtendedRemoteDataSourceImpl(service)

    @Test
    fun `when service returns success with body then returns Result Success`() = runBlocking {
        val request = MPDeviceSessionIdRequest(fingerprint = null, siteId = "MLB")
        val response = DeviceSessionIdResponse(sessionId = "session_123")
        coEvery { service.getDeviceSession(request) } returns Response.success(response)

        val result = dataSource.getDeviceSessionId(request)

        assertTrue(result is Result.Success)
        assertEquals("session_123", (result as Result.Success).data.session)
    }

    @Test
    fun `when service returns error then returns Result Error`() = runBlocking {
        val request = MPDeviceSessionIdRequest(fingerprint = null, siteId = "MLB")
        val errorBody = ErrorBody(code = 400, message = "Bad Request")
        val responseBody = Gson().toJson(errorBody).toResponseBody("application/json".toMediaTypeOrNull())
        coEvery { service.getDeviceSession(request) } returns Response.error(400, responseBody)

        val result = dataSource.getDeviceSessionId(request)

        assertTrue(result is Result.Error)
        assertEquals("Bad Request", ((result as Result.Error).error as ResultError.Request).message)
    }

    @Test
    fun `when service returns success with null body then returns EMPTY_BODY_ERROR`() = runBlocking {
        val request = MPDeviceSessionIdRequest(fingerprint = null, siteId = "MLB")
        coEvery { service.getDeviceSession(request) } returns Response.success(null)

        val result = dataSource.getDeviceSessionId(request)

        assertTrue(result is Result.Error)
        assertEquals("EMPTY_BODY", ((result as Result.Error).error as ResultError.Request).code)
    }

    private data class ErrorBody(val code: Int, val message: String)
}
