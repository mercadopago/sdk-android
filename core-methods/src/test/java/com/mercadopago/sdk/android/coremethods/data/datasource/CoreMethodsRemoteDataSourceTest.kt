package com.mercadopago.sdk.android.coremethods.data.datasource

import com.google.gson.Gson
import com.mercadopago.sdk.android.coremethods.data.datasource.remote.CoreMethodsRemoteDataSourceImpl
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardIssuersRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.CardTokenBodyRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.InstallmentsRequest
import com.mercadopago.sdk.android.coremethods.data.remote.request.PaymentMethodsRequest
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardIssuerResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.CardTokenResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.IdentificationTypesResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.InstallmentsResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.PaymentMethodResponse
import com.mercadopago.sdk.android.coremethods.data.remote.service.CoreMethodsService
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import retrofit2.Response
import kotlin.test.Test

internal class CoreMethodsRemoteDataSourceTest {
    private val service: CoreMethodsService = mockk()
    private val remoteDataSource = CoreMethodsRemoteDataSourceImpl(service)

    @Test
    fun `test generateCardToken calls service and returns success`() =
        runBlocking {
            val cardTokenRequest = CardTokenBodyRequest(cardId = "card_123")

            val cardTokenResponse = CardTokenResponse(id = "token_id")
            val mpResponse: Response<CardTokenResponse> = Response.success(cardTokenResponse)

            coEvery { service.createToken(any()) } returns mpResponse

            val result = remoteDataSource.generateCardToken(cardTokenRequest)

            assertTrue(result is Result.Success)
            assertEquals("token_id", (result as Result.Success).data.token)
        }

    @Test
    fun `test generateCardToken calls service and returns error`() =
        runBlocking {
            val cardTokenRequest = CardTokenBodyRequest(cardId = "card_123")

            val errorBody = ErrorBody()

            val gson = Gson()
            val jsonErrorBody = gson.toJson(errorBody)
            val responseBody: ResponseBody =
                jsonErrorBody.toResponseBody("application/json".toMediaTypeOrNull())

            val mpResponse: Response<CardTokenResponse> = Response.error(400, responseBody)

            coEvery { service.createToken(any()) } returns mpResponse

            val result = remoteDataSource.generateCardToken(cardTokenRequest)

            assertTrue(result is Result.Error)
            assertEquals(
                "Bad Request",
                ((result as Result.Error).error as ResultError.Request).message,
            )
        }

    @Test
    fun `test getInstallments calls service and returns success`() =
        runBlocking {
            val installmentsRequest = InstallmentsRequest()

            val installmentsResponse = InstallmentsResponse(paymentMethodId = "payment")
            val mpResponse: Response<List<InstallmentsResponse>> =
                Response.success(listOf(installmentsResponse))

            coEvery { service.getInstallments(any(), any(), any(), any()) } returns mpResponse

            val result = remoteDataSource.getInstallments(installmentsRequest)

            assertTrue(result is Result.Success)
            assertEquals("payment", (result as Result.Success).data[0].paymentMethodId)
        }

    @Test
    fun `test getInstallments calls service and returns error`() =
        runBlocking {
            val installmentsRequest = InstallmentsRequest()

            val errorBody = ErrorBody()

            val gson = Gson()
            val jsonErrorBody = gson.toJson(errorBody)
            val responseBody: ResponseBody =
                jsonErrorBody.toResponseBody("application/json".toMediaTypeOrNull())

            val mpResponse: Response<List<InstallmentsResponse>> = Response.error(400, responseBody)

            coEvery { service.getInstallments(any(), any(), any(), any()) } returns mpResponse

            val result = remoteDataSource.getInstallments(installmentsRequest)

            assertTrue(result is Result.Error)
            assertEquals(
                "Bad Request",
                ((result as Result.Error).error as ResultError.Request).message,
            )
        }

    @Test
    fun `test getIdentificationTypes calls service and returns success`() =
        runBlocking {
            val responseBody = IdentificationTypesResponse(id = "identification")
            val response: Response<List<IdentificationTypesResponse>> =
                Response.success(listOf(responseBody))

            coEvery { service.getIdentificationTypes() } returns response

            val result = remoteDataSource.getIdentificationTypes()

            assertTrue(result is Result.Success)
            assertEquals("identification", (result as Result.Success).data[0].id)
        }

    @Test
    fun `test getIdentificationTypes calls service and returns error`() =
        runBlocking {
            val request = InstallmentsRequest()

            val errorBody = ErrorBody()

            val gson = Gson()
            val jsonErrorBody = gson.toJson(errorBody)
            val responseBody: ResponseBody =
                jsonErrorBody.toResponseBody("application/json".toMediaTypeOrNull())

            val response: Response<List<IdentificationTypesResponse>> =
                Response.error(400, responseBody)

            coEvery { service.getIdentificationTypes() } returns response

            val result = remoteDataSource.getIdentificationTypes()

            assertTrue(result is Result.Error)
            assertEquals(
                "Bad Request",
                ((result as Result.Error).error as ResultError.Request).message,
            )
        }

    @Test
    fun `test getCardIssuers calls service and returns success`() =
        runBlocking {
            val request = CardIssuersRequest()

            val responseBody = CardIssuerResponse(id = "01")
            val response: Response<List<CardIssuerResponse>> =
                Response.success(listOf(responseBody))

            coEvery { service.getCardIssuers(any(), any(), any()) } returns response

            val result = remoteDataSource.getCardIssuers(request)

            assertTrue(result is Result.Success)
            assertEquals("01", (result as Result.Success).data[0].id)
        }

    @Test
    fun `test getCardIssuers calls service and returns error`() =
        runBlocking {
            val request = CardIssuersRequest()

            val errorBody = ErrorBody()

            val gson = Gson()
            val jsonErrorBody = gson.toJson(errorBody)
            val responseBody: ResponseBody =
                jsonErrorBody.toResponseBody("application/json".toMediaTypeOrNull())

            val response: Response<List<CardIssuerResponse>> = Response.error(400, responseBody)

            coEvery { service.getCardIssuers(any(), any(), any()) } returns response

            val result = remoteDataSource.getCardIssuers(request)

            assertTrue(result is Result.Error)
            assertEquals(
                "Bad Request",
                ((result as Result.Error).error as ResultError.Request).message,
            )
        }

    @Test
    fun `test getPaymentMethods calls service and returns success`() =
        runBlocking {
            val request = PaymentMethodsRequest()

            val responseBody = PaymentMethodResponse(id = "01")
            val response: Response<List<PaymentMethodResponse>> =
                Response.success(listOf(responseBody))

            coEvery { service.getPaymentMethods(any(), any()) } returns response

            val result = remoteDataSource.getPaymentMethods(request)

            assertTrue(result is Result.Success)
            assertEquals("01", (result as Result.Success).data[0].id)
        }

    @Test
    fun `test getPaymentMethods calls service and returns error`() =
        runBlocking {
            val request = PaymentMethodsRequest()

            val errorBody = ErrorBody()

            val gson = Gson()
            val jsonErrorBody = gson.toJson(errorBody)
            val responseBody: ResponseBody =
                jsonErrorBody.toResponseBody("application/json".toMediaTypeOrNull())

            val response: Response<List<PaymentMethodResponse>> = Response.error(400, responseBody)

            coEvery { service.getPaymentMethods(any(), any()) } returns response

            val result = remoteDataSource.getPaymentMethods(request)

            assertTrue(result is Result.Error)
            assertEquals(
                "Bad Request",
                ((result as Result.Error).error as ResultError.Request).message,
            )
        }

    data class ErrorBody(
        val code: Int = 400,
        val message: String = "Bad Request",
    )
}
