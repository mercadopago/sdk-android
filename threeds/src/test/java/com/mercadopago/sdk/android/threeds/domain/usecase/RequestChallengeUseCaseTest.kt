package com.mercadopago.sdk.android.threeds.domain.usecase

import android.app.Activity
import com.mercadopago.sdk.android.threeds.domain.adapter.ThreeDSSDKAdapter
import com.mercadopago.sdk.android.threeds.domain.callback.MPThreeDSChallengeDelegate
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeError
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSDirectoryServer
import com.mercadopago.sdk.android.threeds.mockThreeDSAuthRequestParameters
import com.mercadopago.sdk.android.threeds.mockThreeDSAuthenticationModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException
import java.security.GeneralSecurityException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class RequestChallengeUseCaseTest {

    private val authenticateUseCase = mockk<AuthenticateUseCase>()
    private val threeDSSDKAdapter = mockk<ThreeDSSDKAdapter>()
    private val delegate = mockk<MPThreeDSChallengeDelegate>(relaxed = true)
    private val activity = mockk<Activity>()

    private val useCase = RequestChallengeUseCase(
        authenticateUseCase = authenticateUseCase,
        threeDSSDKAdapter = threeDSSDKAdapter
    )

    @Test
    fun `when challenge succeeds with AUTHORIZED response Then should call onSuccess without challenge`() = runTest {
        // Given
        val cardToken = "test_token"
        val paymentMethodId = "visa"
        val timeout = 5000
        val mockAuthParams = mockThreeDSAuthRequestParameters()
        val mockAuthResponse = mockThreeDSAuthenticationModel("AUTHORIZED")
        val successSlot = slot<MPThreeDSAuthenticated>()

        coEvery { threeDSSDKAdapter.initialize() } just runs
        coEvery { threeDSSDKAdapter.createTransaction(any()) } just runs
        coEvery { threeDSSDKAdapter.getAuthenticationRequestParameters() } returns mockAuthParams
        every { authenticateUseCase(any(), any(), any(), any(), any(), any(), any()) } returns flowOf(mockAuthResponse)
        every { delegate.onSuccess(capture(successSlot)) } just runs

        // When
        useCase(activity, cardToken, paymentMethodId, delegate, timeout)

        // Then
        coVerify { threeDSSDKAdapter.initialize() }
        coVerify { threeDSSDKAdapter.createTransaction(MPThreeDSDirectoryServer.paymentMethodDirectoryServer(paymentMethodId)) }
        coVerify { threeDSSDKAdapter.getAuthenticationRequestParameters() }
        verify { authenticateUseCase(cardToken, mockAuthParams.sdkAppId, mockAuthParams.deviceData, mockAuthParams.sdkEphemeralPublicKey, "10", mockAuthParams.sdkReferenceNumber, mockAuthParams.sdkTransactionId) }
        verify { delegate.onSuccess(any()) }

        val capturedResult = successSlot.captured
        assertEquals(false, capturedResult.challengeCompleted)
        assertEquals(mockAuthResponse.acsReferenceNumber, capturedResult.challengeResponse.acsReferenceNumber)
    }

    @Test
    fun `when challenge succeeds with CHALLENGE response Then should call doChallenge`() = runTest {
        // Given
        val cardToken = "test_token"
        val paymentMethodId = "mastercard"
        val timeout = 5000
        val mockAuthParams = mockThreeDSAuthRequestParameters()
        val mockAuthResponse = mockThreeDSAuthenticationModel("CHALLENGE")

        coEvery { threeDSSDKAdapter.initialize() } just runs
        coEvery { threeDSSDKAdapter.createTransaction(any()) } just runs
        coEvery { threeDSSDKAdapter.getAuthenticationRequestParameters() } returns mockAuthParams
        coEvery { threeDSSDKAdapter.doChallenge(any(), any(), any(), any()) } just runs
        every { authenticateUseCase(any(), any(), any(), any(), any(), any(), any()) } returns flowOf(mockAuthResponse)

        // When
        useCase(activity, cardToken, paymentMethodId, delegate, timeout)

        // Then
        coVerify { threeDSSDKAdapter.doChallenge(activity, mockAuthResponse, delegate, timeout) }
    }

    @Test
    fun `when authentication fails with unexpected response Then should call onError`() = runTest {
        // Given
        val cardToken = "test_token"
        val paymentMethodId = "visa"
        val timeout = 5000
        val mockAuthParams = mockThreeDSAuthRequestParameters()
        val mockAuthResponse = mockThreeDSAuthenticationModel("FAILED")
        val errorSlot = slot<MPThreeDSChallengeError>()

        coEvery { threeDSSDKAdapter.initialize() } just runs
        coEvery { threeDSSDKAdapter.createTransaction(any()) } just runs
        coEvery { threeDSSDKAdapter.getAuthenticationRequestParameters() } returns mockAuthParams
        every { authenticateUseCase(any(), any(), any(), any(), any(), any(), any()) } returns flowOf(mockAuthResponse)
        every { delegate.onError(capture(errorSlot)) } just runs

        // When
        useCase(activity, cardToken, paymentMethodId, delegate, timeout)

        // Then
        verify { delegate.onError(any()) }
        val capturedError = errorSlot.captured
        assertEquals("AUTHENTICATION_FAILED", capturedError.code)
        assertTrue(capturedError.message.contains("Authentication response: FAILED"))
    }

    @Test
    fun `when getAuthenticationRequestParameters returns null Then should complete without errors`() = runTest {
        // Given
        val cardToken = "test_token"
        val paymentMethodId = "visa"
        val timeout = 5000

        coEvery { threeDSSDKAdapter.initialize() } just runs
        coEvery { threeDSSDKAdapter.createTransaction(any()) } just runs
        coEvery { threeDSSDKAdapter.getAuthenticationRequestParameters() } returns null

        // When
        useCase(activity, cardToken, paymentMethodId, delegate, timeout)

        // Then
        // When getAuthenticationRequestParameters returns null, the flow should complete successfully
        // The adapter methods should still be called for initialization and transaction creation
        coVerify { threeDSSDKAdapter.initialize() }
        coVerify { threeDSSDKAdapter.createTransaction(any()) }
        coVerify { threeDSSDKAdapter.getAuthenticationRequestParameters() }
    }

    @Test
    fun `when IOException occurs Then should call onError with network error`() = runTest {
        // Given
        val cardToken = "test_token"
        val paymentMethodId = "visa"
        val timeout = 5000
        val ioException = IOException("Network error")
        val errorSlot = slot<MPThreeDSChallengeError>()

        coEvery { threeDSSDKAdapter.initialize() } throws ioException
        every { delegate.onError(capture(errorSlot)) } just runs

        // When
        useCase(activity, cardToken, paymentMethodId, delegate, timeout)

        // Then
        verify { delegate.onError(any()) }
        val capturedError = errorSlot.captured
        assertEquals("UNKNOWN_ERROR", capturedError.code)
        assertEquals(ioException, capturedError.cause)
    }

    @Test
    fun `when GeneralSecurityException occurs Then should call onError with security error`() = runTest {
        // Given
        val cardToken = "test_token"
        val paymentMethodId = "visa"
        val timeout = 5000
        val securityException = GeneralSecurityException("Security error")
        val errorSlot = slot<MPThreeDSChallengeError>()

        coEvery { threeDSSDKAdapter.initialize() } throws securityException
        every { delegate.onError(capture(errorSlot)) } just runs

        // When
        useCase(activity, cardToken, paymentMethodId, delegate, timeout)

        // Then
        verify { delegate.onError(any()) }
        val capturedError = errorSlot.captured
        assertEquals("UNKNOWN_ERROR", capturedError.code)
        assertEquals(securityException, capturedError.cause)
    }

    @Test
    fun `when IllegalStateException occurs Then should call onError with state error`() = runTest {
        // Given
        val cardToken = "test_token"
        val paymentMethodId = "visa"
        val timeout = 5000
        val illegalStateException = IllegalStateException("State error")
        val errorSlot = slot<MPThreeDSChallengeError>()

        coEvery { threeDSSDKAdapter.initialize() } throws illegalStateException
        every { delegate.onError(capture(errorSlot)) } just runs

        // When
        useCase(activity, cardToken, paymentMethodId, delegate, timeout)

        // Then
        verify { delegate.onError(any()) }
        val capturedError = errorSlot.captured
        assertEquals("UNKNOWN_ERROR", capturedError.code)
        assertEquals(illegalStateException, capturedError.cause)
    }
}
