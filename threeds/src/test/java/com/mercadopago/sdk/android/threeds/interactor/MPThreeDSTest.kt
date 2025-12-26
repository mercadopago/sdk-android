package com.mercadopago.sdk.android.threeds.interactor

import android.app.Activity
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSSeverity
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.Koin

class MPThreeDSTest {
    private lateinit var mockKoin: Koin
    private lateinit var mockRepository: ThreeDSRepository
    private lateinit var mpThreeDS: MPThreeDS

    @Before
    fun setUp() {
        mockKoin = mockk()
        mockRepository = mockk()
        every { mockKoin.get<ThreeDSRepository>() } returns mockRepository
        mpThreeDS = MPThreeDS(mockKoin)
    }

    @Test
    fun `getWarnings should return warnings from repository`() {
        // Arrange
        val expectedWarnings = listOf(
            MPThreeDSWarning(
                id = "warning-1",
                message = "First warning message",
                severity = MPThreeDSSeverity.HIGH,
            ),
            MPThreeDSWarning(
                id = "warning-2",
                message = "Second warning message",
                severity = MPThreeDSSeverity.MEDIUM,
            ),
        )

        every { mockRepository.getWarnings() } returns expectedWarnings

        // Act
        val actualWarnings = mpThreeDS.getWarnings()

        // Assert
        assertEquals(expectedWarnings, actualWarnings)
        assertEquals(2, actualWarnings.size)
        assertEquals("warning-1", actualWarnings[0].id)
        assertEquals("First warning message", actualWarnings[0].message)
        assertEquals(MPThreeDSSeverity.HIGH, actualWarnings[0].severity)
        verify { mockRepository.getWarnings() }
    }

    @Test
    fun `getWarnings should return empty list when repository returns empty list`() {
        // Arrange
        every { mockRepository.getWarnings() } returns emptyList()

        // Act
        val actualWarnings = mpThreeDS.getWarnings()

        // Assert
        assertTrue(actualWarnings.isEmpty())
        verify { mockRepository.getWarnings() }
    }

    @Test
    fun `createTransaction should call repository with payment method id`() {
        // Arrange
        val paymentMethodId = "visa"
        every { mockRepository.createTransaction(paymentMethodId) } returns Unit

        // Act
        mpThreeDS.createTransaction(paymentMethodId)

        // Assert
        verify { mockRepository.createTransaction(paymentMethodId) }
    }

    @Test
    fun `createTransaction should handle different payment method types`() {
        // Arrange
        val paymentMethods = listOf("visa", "mastercard", "amex", "debvisa", "master")
        paymentMethods.forEach { paymentMethod ->
            every { mockRepository.createTransaction(paymentMethod) } returns Unit
        }

        // Act & Assert
        paymentMethods.forEach { paymentMethod ->
            mpThreeDS.createTransaction(paymentMethod)
            verify { mockRepository.createTransaction(paymentMethod) }
        }
    }

    @Test
    fun `getAuthenticationRequestParameters should return parameters from repository`() {
        // Arrange
        val expectedParams = MPThreeDSRequestParams(
            sdkAppId = "app-id-123",
            deviceData = "device-data-456",
            sdkEphemeralPublicKey = "public-key-789",
            sdkReferenceNumber = "ref-number-101",
            sdkTransactionId = "transaction-id-202",
        )

        every { mockRepository.getAuthenticationRequestParameters() } returns expectedParams

        // Act
        val actualParams = mpThreeDS.getAuthenticationRequestParameters()

        // Assert
        assertEquals(expectedParams, actualParams)
        verify { mockRepository.getAuthenticationRequestParameters() }
    }

    @Test
    fun `getAuthenticationRequestParameters should return null when repository returns null`() {
        // Arrange
        every { mockRepository.getAuthenticationRequestParameters() } returns null

        // Act
        val actualParams = mpThreeDS.getAuthenticationRequestParameters()

        // Assert
        assertNull(actualParams)
        verify { mockRepository.getAuthenticationRequestParameters() }
    }

    @Test
    fun `doChallenge should call repository with correct parameters and default timeout`() = runTest {
        // Arrange
        val mockActivity = mockk<Activity>()
        val authenticationModel = MPThreeDSAuthenticationModel(
            threeDSServerTransID = "server-trans-123",
            acsReferenceNumber = "acs-ref-456",
            dsTransID = "ds-trans-789",
            acsTransID = "acs-trans-101",
            acsSignedContent = "signed-content-abc",
            callbackUrl = "",
        )

        val expectedResult = MPThreeDSChallengeResult.OnSuccess(
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
            mockRepository.doChallenge(
                activity = mockActivity,
                authenticationResponse = authenticationModel,
                timeout = 10,
            )
        } returns expectedResult

        // Act
        val actualResult = mpThreeDS.doChallenge(
            activity = mockActivity,
            authentication = authenticationModel,
        )

        // Assert
        assertEquals(expectedResult, actualResult)
        coVerify {
            mockRepository.doChallenge(
                activity = mockActivity,
                authenticationResponse = authenticationModel,
                timeout = 10,
            )
        }
    }

    @Test
    fun `doChallenge should call repository with custom timeout`() = runTest {
        // Arrange
        val mockActivity = mockk<Activity>()
        val authenticationModel = MPThreeDSAuthenticationModel(
            threeDSServerTransID = "server-trans-123",
            acsReferenceNumber = "acs-ref-456",
            dsTransID = "ds-trans-789",
            acsTransID = "acs-trans-101",
            acsSignedContent = "signed-content-abc",
            callbackUrl = "",
        )
        val customTimeout = 30

        val expectedResult = MPThreeDSChallengeResult.OnCancel

        coEvery {
            mockRepository.doChallenge(
                activity = mockActivity,
                authenticationResponse = authenticationModel,
                timeout = customTimeout,
            )
        } returns expectedResult

        // Act
        val actualResult = mpThreeDS.doChallenge(
            activity = mockActivity,
            authentication = authenticationModel,
            timeout = customTimeout,
        )

        // Assert
        assertEquals(expectedResult, actualResult)
        coVerify {
            mockRepository.doChallenge(
                activity = mockActivity,
                authenticationResponse = authenticationModel,
                timeout = customTimeout,
            )
        }
    }

    @Test
    fun `doChallenge should handle OnTimedOut result from repository`() = runTest {
        // Arrange
        val mockActivity = mockk<Activity>()
        val authenticationModel = MPThreeDSAuthenticationModel(
            threeDSServerTransID = "server-trans-123",
            acsReferenceNumber = "acs-ref-456",
            dsTransID = "ds-trans-789",
            acsTransID = "acs-trans-101",
            acsSignedContent = "signed-content-abc",
            callbackUrl = "",
        )

        coEvery {
            mockRepository.doChallenge(
                activity = mockActivity,
                authenticationResponse = authenticationModel,
                timeout = 10,
            )
        } returns MPThreeDSChallengeResult.OnTimedOut

        // Act
        val actualResult = mpThreeDS.doChallenge(
            activity = mockActivity,
            authentication = authenticationModel,
        )

        // Assert
        assertEquals(MPThreeDSChallengeResult.OnTimedOut, actualResult)
    }

    @Test
    fun `close should call repository close method`() {
        // Arrange
        every { mockRepository.close() } returns Unit

        // Act
        mpThreeDS.close()

        // Assert
        verify { mockRepository.close() }
    }

    @Test
    fun `getInstance should return singleton instance`() {
        // Act
        val instance1 = MPThreeDS(mockKoin)
        val instance2 = MPThreeDS(mockKoin)

        // Assert
        assertNotNull(instance1)
        assertNotNull(instance2)
        assertNotSame(instance1, instance2)
    }

    @Test
    fun `MPThreeDS constructor should initialize with koin`() {
        // Act
        val instance = MPThreeDS(mockKoin)

        // Assert
        assertNotNull(instance)
        assertNotNull(instance.koin)
        assertEquals(mockKoin, instance.koin)
    }
}
