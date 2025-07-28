package com.mercadopago.sdk.android.threeds.data.datasource.remote

import app.cash.turbine.test
import com.mercadopago.sdk.android.threeds.data.remote.request.ThreeDSAuthenticationRequest
import com.mercadopago.sdk.android.threeds.data.remote.response.MPThreeDSAuthenticationResponse
import com.mercadopago.sdk.android.threeds.data.remote.service.ThreeDSService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import kotlin.test.assertEquals

internal class ThreeDSRemoteDataSourceImplTest {

    private val threeDSService = mockk<ThreeDSService>()
    private val dataSource = ThreeDSRemoteDataSourceImpl(threeDSService)

    @Test
    fun `when authenticate is successful Then should return correct model`() = runTest {
        // Given
        val request = ThreeDSAuthenticationRequest(
            token = "test_token",
            sdkAppId = "test_sdk_app_id",
            sdkEncData = "test_enc_data",
            sdkEphemPubKey = "test_ephemeral_pub_key",
            sdkMaxTimeout = "10",
            sdkReferenceNumber = "test_reference_number",
            sdkTransId = "test_transaction_id"
        )
        val mockResponse = MPThreeDSAuthenticationResponse(
            response = "AUTHORIZED",
            threeDSServerTransID = "server_trans_id",
            acsReferenceNumber = "acs_ref_number",
            dsTransID = "ds_trans_id",
            acsTransID = "acs_trans_id",
            acsSignedContent = "acs_signed_content"
        )

        coEvery { threeDSService.authenticate(request) } returns mockResponse

        // When
        val result = dataSource.authenticate(request)

        // Then
        result.test {
            val model = awaitItem()
            assertEquals("AUTHORIZED", model.response)
            assertEquals("server_trans_id", model.threeDSServerTransID)
            assertEquals("acs_ref_number", model.acsReferenceNumber)
            awaitComplete()
        }

        coVerify { threeDSService.authenticate(request) }
    }

    @Test
    fun `when authenticate returns 404 Then should return mock response`() = runTest {
        // Given
        val request = ThreeDSAuthenticationRequest(
            token = "test_token",
            sdkAppId = "test_sdk_app_id",
            sdkEncData = "test_enc_data",
            sdkEphemPubKey = "test_ephemeral_pub_key",
            sdkMaxTimeout = "10",
            sdkReferenceNumber = "test_reference_number",
            sdkTransId = "test_transaction_id"
        )

        val httpException = HttpException(
            Response.error<MPThreeDSAuthenticationResponse>(
                404,
                "Not Found".toResponseBody()
            )
        )

        coEvery { threeDSService.authenticate(request) } throws httpException

        // When
        val result = dataSource.authenticate(request)

        // Then
        result.test {
            val model = awaitItem()
            assertEquals("AUTHORIZED", model.response)
            assertEquals("mock_3ds_server_transaction_id", model.threeDSServerTransID)
            assertEquals("mock_acs_reference_number", model.acsReferenceNumber)
            assertEquals("mock_ds_transaction_id", model.dsTransID)
            assertEquals("mock_acs_transaction_id", model.acsTransID)
            assertEquals("mock_acs_signed_content", model.acsSignedContent)
            awaitComplete()
        }
    }

    @Test
    fun `when authenticate returns 500 Then should throw exception`() = runTest {
        // Given
        val request = ThreeDSAuthenticationRequest(
            token = "test_token",
            sdkAppId = "test_sdk_app_id",
            sdkEncData = "test_enc_data",
            sdkEphemPubKey = "test_ephemeral_pub_key",
            sdkMaxTimeout = "10",
            sdkReferenceNumber = "test_reference_number",
            sdkTransId = "test_transaction_id"
        )

        val httpException = HttpException(
            Response.error<MPThreeDSAuthenticationResponse>(
                500,
                "Server Error".toResponseBody()
            )
        )

        coEvery { threeDSService.authenticate(request) } throws httpException

        // When
        val result = dataSource.authenticate(request)

        // Then
        result.test {
            val exception = awaitError()
            assertEquals(httpException, exception)
        }
    }

    @Test
    fun `when authenticate throws non-http exception Then should propagate exception`() = runTest {
        // Given
        val request = ThreeDSAuthenticationRequest(
            token = "test_token",
            sdkAppId = "test_sdk_app_id",
            sdkEncData = "test_enc_data",
            sdkEphemPubKey = "test_ephemeral_pub_key",
            sdkMaxTimeout = "10",
            sdkReferenceNumber = "test_reference_number",
            sdkTransId = "test_transaction_id"
        )

        val runtimeException = RuntimeException("Network error")
        coEvery { threeDSService.authenticate(request) } throws runtimeException

        // When
        val result = dataSource.authenticate(request)

        // Then
        result.test {
            val exception = awaitError()
            assertEquals(runtimeException, exception)
        }
    }
}
