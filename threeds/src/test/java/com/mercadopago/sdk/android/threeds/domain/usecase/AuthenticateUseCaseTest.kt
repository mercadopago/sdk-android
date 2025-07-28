package com.mercadopago.sdk.android.threeds.domain.usecase

import app.cash.turbine.test
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import com.mercadopago.sdk.android.threeds.mockThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.mockThreeDSAuthenticationParams
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class AuthenticateUseCaseTest {

    private val repository = mockk<ThreeDSRepository>()
    private val useCase = AuthenticateUseCase(repository)

    @Test
    fun `when invoke is called Then should call repository with correct parameters`() = runTest {
        // Given
        val mockResponse = mockThreeDSAuthenticationModel()
        val token = "test_token"
        val sdkAppId = "test_sdk_app_id"
        val sdkEncData = "test_enc_data"
        val sdkEphemPubKey = "test_ephemeral_pub_key"
        val sdkMaxTimeout = "10"
        val sdkReferenceNumber = "test_reference_number"
        val sdkTransId = "test_transaction_id"

        every { repository.authenticate(any()) } returns flowOf(mockResponse)

        // When
        val result = useCase(
            token = token,
            sdkAppId = sdkAppId,
            sdkEncData = sdkEncData,
            sdkEphemPubKey = sdkEphemPubKey,
            sdkMaxTimeout = sdkMaxTimeout,
            sdkReferenceNumber = sdkReferenceNumber,
            sdkTransId = sdkTransId
        )

        // Then
        result.test {
            assertEquals(mockResponse, awaitItem())
            awaitComplete()
        }

        verify {
            repository.authenticate(match { params ->
                params.token == token &&
                params.sdkAppId == sdkAppId &&
                params.sdkEncData == sdkEncData &&
                params.sdkEphemPubKey == sdkEphemPubKey &&
                params.sdkMaxTimeout == sdkMaxTimeout &&
                params.sdkReferenceNumber == sdkReferenceNumber &&
                params.sdkTransId == sdkTransId
            })
        }
    }

    @Test
    fun `when invoke is called with different parameters Then should create correct params object`() = runTest {
        // Given
        val mockResponse = mockThreeDSAuthenticationModel("CHALLENGE")
        val mockParams = mockThreeDSAuthenticationParams()

        every { repository.authenticate(any()) } returns flowOf(mockResponse)

        // When
        val result = useCase(
            token = mockParams.token,
            sdkAppId = mockParams.sdkAppId,
            sdkEncData = mockParams.sdkEncData,
            sdkEphemPubKey = mockParams.sdkEphemPubKey,
            sdkMaxTimeout = mockParams.sdkMaxTimeout,
            sdkReferenceNumber = mockParams.sdkReferenceNumber,
            sdkTransId = mockParams.sdkTransId
        )

        // Then
        result.test {
            assertEquals(mockResponse, awaitItem())
            awaitComplete()
        }

        verify(exactly = 1) { repository.authenticate(any()) }
    }
}
