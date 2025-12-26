package com.mercadopago.sdk.android.threeds.data.repository

import android.app.Activity
import com.mercadopago.sdk.android.threeds.data.model.MPSeverityResponse
import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSAuthenticationParams
import com.mercadopago.sdk.android.threeds.data.model.MPThreeDSWarningResponse
import com.mercadopago.sdk.android.threeds.data.wrapper.ThreeDSWrapper
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ThreeDSRepositoryImplTest {
    private lateinit var mockThreeDSWrapper: ThreeDSWrapper
    private lateinit var threeDSRepository: ThreeDSRepositoryImpl

    @Before
    fun setUp() {
        mockThreeDSWrapper = mockk()
        threeDSRepository = ThreeDSRepositoryImpl(mockThreeDSWrapper)
    }

    @Test
    fun `initialize should call wrapper initialize method`() = runTest {
        // Arrange
        coEvery { mockThreeDSWrapper.initialize() } returns Unit

        // Act
        threeDSRepository.initialize()

        // Assert
        coVerify { mockThreeDSWrapper.initialize() }
    }

    @Test
    fun `getWarnings should return mapped warnings from wrapper`() {
        // Arrange
        val mockWarningResponses = listOf(
            MPThreeDSWarningResponse(
                id = "warning-1",
                message = "First warning",
                severity = MPSeverityResponse.HIGH,
            ),
            MPThreeDSWarningResponse(
                id = "warning-2",
                message = "Second warning",
                severity = MPSeverityResponse.LOW,
            ),
        )

        every { mockThreeDSWrapper.getWarnings() } returns mockWarningResponses

        // Act
        val actualWarnings = threeDSRepository.getWarnings()

        // Assert
        assertEquals(2, actualWarnings.size)

        assertEquals("warning-1", actualWarnings[0].id)
        assertEquals("First warning", actualWarnings[0].message)
        assertEquals(
            com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSSeverity.HIGH,
            actualWarnings[0].severity,
        )

        assertEquals("warning-2", actualWarnings[1].id)
        assertEquals("Second warning", actualWarnings[1].message)
        assertEquals(com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSSeverity.LOW, actualWarnings[1].severity)

        verify { mockThreeDSWrapper.getWarnings() }
    }

    @Test
    fun `getWarnings should return empty list when wrapper returns empty list`() {
        // Arrange
        every { mockThreeDSWrapper.getWarnings() } returns emptyList()

        // Act
        val actualWarnings = threeDSRepository.getWarnings()

        // Assert
        assertTrue(actualWarnings.isEmpty())
        verify { mockThreeDSWrapper.getWarnings() }
    }

    @Test
    fun `close should call wrapper close method`() {
        // Arrange
        every { mockThreeDSWrapper.close() } returns Unit

        // Act
        threeDSRepository.close()

        // Assert
        verify { mockThreeDSWrapper.close() }
    }

    @Test
    fun `createTransaction should call wrapper createTransaction with payment method id`() {
        // Arrange
        val paymentMethodId = "visa"
        every { mockThreeDSWrapper.createTransaction(paymentMethodId) } returns Unit

        // Act
        threeDSRepository.createTransaction(paymentMethodId)

        // Assert
        verify { mockThreeDSWrapper.createTransaction(paymentMethodId) }
    }

    @Test
    fun `createTransaction should handle different payment method ids`() {
        // Arrange
        val paymentMethods = listOf("visa", "mastercard", "amex", "debvisa")
        paymentMethods.forEach { paymentMethod ->
            every { mockThreeDSWrapper.createTransaction(paymentMethod) } returns Unit
        }

        // Act & Assert
        paymentMethods.forEach { paymentMethod ->
            threeDSRepository.createTransaction(paymentMethod)
            verify { mockThreeDSWrapper.createTransaction(paymentMethod) }
        }
    }

    @Test
    fun `getAuthenticationRequestParameters should return parameters from wrapper`() {
        // Arrange
        val expectedRequestParams = MPThreeDSRequestParams(
            sdkAppId = "app-id-123",
            deviceData = "device-data-456",
            sdkEphemeralPublicKey = "public-key-789",
            sdkReferenceNumber = "ref-number-101",
            sdkTransactionId = "transaction-id-202",
        )

        every { mockThreeDSWrapper.getAuthenticationRequestParameters() } returns expectedRequestParams

        // Act
        val actualRequestParams = threeDSRepository.getAuthenticationRequestParameters()

        // Assert
        assertEquals(expectedRequestParams, actualRequestParams)
        verify { mockThreeDSWrapper.getAuthenticationRequestParameters() }
    }

    @Test
    fun `getAuthenticationRequestParameters should return different parameters from wrapper`() {
        // Arrange
        val expectedRequestParams = MPThreeDSRequestParams(
            sdkAppId = "test-app-id",
            deviceData = "test-device-data",
            sdkEphemeralPublicKey = "test-public-key",
            sdkReferenceNumber = "test-ref-number",
            sdkTransactionId = "test-transaction-id",
        )

        every { mockThreeDSWrapper.getAuthenticationRequestParameters() } returns expectedRequestParams

        // Act
        val actualRequestParams = threeDSRepository.getAuthenticationRequestParameters()

        // Assert
        assertEquals(expectedRequestParams, actualRequestParams)
        verify { mockThreeDSWrapper.getAuthenticationRequestParameters() }
    }

    @Test
    fun `doChallenge should call wrapper with mapped authentication params and return result`() = runTest {
        // Arrange
        val mockActivity = mockk<Activity>()
        val authenticationModel = MPThreeDSAuthenticationModel(
            threeDSServerTransID = "server-trans-123",
            acsReferenceNumber = "acs-ref-456",
            dsTransID = "ds-trans-789",
            acsTransID = "acs-trans-101",
            acsSignedContent = "signed-content-abc",
        )
        val timeout = 30

        val expectedChallengeResult = MPThreeDSChallengeResult.OnSuccess(
            MPThreeDSAuthenticated(
                challengeResponse = MPThreeDSChallengeModel(
                    threeDSServerTransID = "server-trans-123",
                    acsReferenceNumber = "acs-ref-456",
                    dsTransID = "ds-trans-789",
                    acsTransID = "acs-trans-101",
                    acsSignedContent = "signed-content-abc",
                ),
                challengeCompleted = true,
            ),
        )

        coEvery {
            mockThreeDSWrapper.doChallenge(
                activity = mockActivity,
                authenticationParams = any<MPThreeDSAuthenticationParams>(),
                timeout = timeout,
            )
        } returns expectedChallengeResult

        // Act
        val actualResult = threeDSRepository.doChallenge(
            activity = mockActivity,
            authenticationResponse = authenticationModel,
            timeout = timeout,
        )

        // Assert
        assertEquals(expectedChallengeResult, actualResult)

        coVerify {
            mockThreeDSWrapper.doChallenge(
                activity = mockActivity,
                authenticationParams = match<MPThreeDSAuthenticationParams> { params ->
                    params.threeDSServerTransID == "server-trans-123" &&
                        params.acsReferenceNumber == "acs-ref-456" &&
                        params.dsTransID == "ds-trans-789" &&
                        params.acsTransID == "acs-trans-101" &&
                        params.acsSignedContent == "signed-content-abc"
                },
                timeout = timeout,
            )
        }
    }

    @Test
    fun `doChallenge should handle OnCancel result from wrapper`() = runTest {
        // Arrange
        val mockActivity = mockk<Activity>()
        val authenticationModel = MPThreeDSAuthenticationModel(
            threeDSServerTransID = "server-trans-123",
            acsReferenceNumber = "acs-ref-456",
            dsTransID = "ds-trans-789",
            acsTransID = "acs-trans-101",
            acsSignedContent = "signed-content-abc",
        )
        val timeout = 10

        coEvery {
            mockThreeDSWrapper.doChallenge(
                activity = mockActivity,
                authenticationParams = any<MPThreeDSAuthenticationParams>(),
                timeout = timeout,
            )
        } returns MPThreeDSChallengeResult.OnCancel

        // Act
        val actualResult = threeDSRepository.doChallenge(
            activity = mockActivity,
            authenticationResponse = authenticationModel,
            timeout = timeout,
        )

        // Assert
        assertEquals(MPThreeDSChallengeResult.OnCancel, actualResult)
    }

    @Test
    fun `doChallenge should handle OnTimedOut result from wrapper`() = runTest {
        // Arrange
        val mockActivity = mockk<Activity>()
        val authenticationModel = MPThreeDSAuthenticationModel(
            threeDSServerTransID = "server-trans-123",
            acsReferenceNumber = "acs-ref-456",
            dsTransID = "ds-trans-789",
            acsTransID = "acs-trans-101",
            acsSignedContent = "signed-content-abc",
        )
        val timeout = 5

        coEvery {
            mockThreeDSWrapper.doChallenge(
                activity = mockActivity,
                authenticationParams = any<MPThreeDSAuthenticationParams>(),
                timeout = timeout,
            )
        } returns MPThreeDSChallengeResult.OnTimedOut

        // Act
        val actualResult = threeDSRepository.doChallenge(
            activity = mockActivity,
            authenticationResponse = authenticationModel,
            timeout = timeout,
        )

        // Assert
        assertEquals(MPThreeDSChallengeResult.OnTimedOut, actualResult)
    }
}
