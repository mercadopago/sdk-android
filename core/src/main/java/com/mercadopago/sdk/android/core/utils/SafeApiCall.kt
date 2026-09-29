package com.mercadopago.sdk.android.core.utils

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/** Transport failure code for [SocketTimeoutException]. */
const val NETWORK_ERROR_CODE_TIMEOUT = "TIMEOUT"

/** Transport failure code for [UnknownHostException]. */
const val NETWORK_ERROR_CODE_NO_INTERNET = "NO_INTERNET"

/** Transport failure code for [ConnectException]. */
const val NETWORK_ERROR_CODE_CONNECTION = "CONNECTION"

/** Transport failure code for any other [IOException]. */
const val NETWORK_ERROR_CODE_NETWORK = "NETWORK"

private const val TRANSPORT_FAILURE_HTTP_CODE = 599
private val GSON = Gson()

/**
 * Runs a Retrofit suspend [call] and converts a transport-level failure (no connectivity, DNS
 * resolution failure, timeout) into a synthetic error [Response] carrying a `{"code", "message"}`
 * body, instead of letting the exception escape the suspend function uncaught.
 *
 * Retrofit's suspend functions returning [Response] only avoid throwing for HTTP-level errors
 * (4xx/5xx); an [IOException] before any HTTP response exists is thrown directly. Every module's
 * datasource already parses [Response.errorBody] as `{"code", "message"}` JSON, so wrapping the
 * call here makes that existing error handling cover transport failures for free.
 */
suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Response<T> =
    try {
        call()
    } catch (e: IOException) {
        Response.error(TRANSPORT_FAILURE_HTTP_CODE, e.toTransportFailureBody())
    }

private fun IOException.toTransportFailureBody() =
    GSON.toJson(TransportFailureBody(code = transportErrorCode(), message = message ?: transportErrorCode()))
        .toResponseBody(null)

private fun IOException.transportErrorCode(): String = when (this) {
    is SocketTimeoutException -> NETWORK_ERROR_CODE_TIMEOUT
    is UnknownHostException -> NETWORK_ERROR_CODE_NO_INTERNET
    is ConnectException -> NETWORK_ERROR_CODE_CONNECTION
    else -> NETWORK_ERROR_CODE_NETWORK
}

private data class TransportFailureBody(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
)
