package com.mercadopago.sdk.android.core.utils

import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private data class TransportFailureBody(val code: String, val message: String)

class SafeApiCallTest {

    @Test
    fun `given call succeeds then safeApiCall returns the same Response`() = runBlocking {
        val response = Response.success("body")

        val result = safeApiCall { response }

        assertSame(response, result)
    }

    @Test
    fun `given call returns HTTP error then safeApiCall returns it untouched`() = runBlocking {
        val errorBody = """{"code":"404","message":"Not Found"}""".toResponseBody()
        val response = Response.error<String>(404, errorBody)

        val result = safeApiCall { response }

        assertSame(response, result)
    }

    @Test
    fun `given call throws SocketTimeoutException then safeApiCall returns error Response with TIMEOUT code`() =
        runBlocking {
            val result = safeApiCall<String> { throw SocketTimeoutException("timeout") }

            assertFalse(result.isSuccessful)
            val body = Gson().fromJson(result.errorBody()?.string(), TransportFailureBody::class.java)
            assertEquals(NETWORK_ERROR_CODE_TIMEOUT, body.code)
        }

    @Test
    fun `given call throws UnknownHostException then safeApiCall returns error Response with NO_INTERNET code`() =
        runBlocking {
            val result = safeApiCall<String> { throw UnknownHostException("no dns") }

            val body = Gson().fromJson(result.errorBody()?.string(), TransportFailureBody::class.java)
            assertEquals(NETWORK_ERROR_CODE_NO_INTERNET, body.code)
        }

    @Test
    fun `given call throws ConnectException then safeApiCall returns error Response with CONNECTION code`() =
        runBlocking {
            val result = safeApiCall<String> { throw ConnectException("refused") }

            val body = Gson().fromJson(result.errorBody()?.string(), TransportFailureBody::class.java)
            assertEquals(NETWORK_ERROR_CODE_CONNECTION, body.code)
        }

    @Test
    fun `given call throws generic IOException then safeApiCall returns error Response with NETWORK code`() =
        runBlocking {
            val result = safeApiCall<String> { throw IOException("broken pipe") }

            val body = Gson().fromJson(result.errorBody()?.string(), TransportFailureBody::class.java)
            assertEquals(NETWORK_ERROR_CODE_NETWORK, body.code)
        }

    @Test(expected = IllegalStateException::class)
    fun `given call throws a non-IOException then safeApiCall does not catch it`(): Unit = runBlocking {
        safeApiCall<String> { throw IllegalStateException("bug") }
    }
}
