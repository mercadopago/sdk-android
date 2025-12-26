package com.mercadopago.sdk.android.threeds.adapter

import android.app.Activity
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSAuthenticationModel
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSChallengeResult
import com.mercadopago.sdk.android.coremethods.domain.provider.models.ThreeDSSeverity
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticated
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSSeverity
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams
import com.mercadopago.sdk.android.threeds.interactor.MPThreeDS
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class MPThreeDSProviderAdapterTest {
    private lateinit var mpThreeDS: MPThreeDS
    private lateinit var adapter: MPThreeDSProviderAdapter

    @Before
    fun setup() {
        mpThreeDS = mockk(relaxed = true)
        adapter = MPThreeDSProviderAdapter(mpThreeDS)
    }

    @Test
    fun `createTransaction should delegate to MPThreeDS`() {
        // Arrange
        val paymentMethodId = "visa"

        // Act
        adapter.createTransaction(paymentMethodId)

        // Assert
        verify(exactly = 1) { mpThreeDS.createTransaction(paymentMethodId) }
    }

    @Test
    fun `getAuthenticationRequestParameters should return mapped params when available`() {
        // Arrange
        val mpParams = MPThreeDSRequestParams(
            sdkAppId = "app123",
            deviceData = "device_data",
            sdkEphemeralPublicKey = "public_key",
            sdkReferenceNumber = "ref123",
            sdkTransactionId = "trans123",
        )
        every { mpThreeDS.getAuthenticationRequestParameters() } returns mpParams

        // Act
        val result = adapter.getAuthenticationRequestParameters()

        // Assert
        assertNotNull(result)
        assertEquals("app123", result?.sdkAppId)
        assertEquals("device_data", result?.deviceData)
        assertEquals("public_key", result?.sdkEphemeralPublicKey)
        assertEquals("ref123", result?.sdkReferenceNumber)
        assertEquals("trans123", result?.sdkTransactionId)
    }

    @Test
    fun `getAuthenticationRequestParameters should return null when not available`() {
        // Arrange
        every { mpThreeDS.getAuthenticationRequestParameters() } returns null

        // Act
        val result = adapter.getAuthenticationRequestParameters()

        // Assert
        assertNull(result)
    }

    @Test
    fun `doChallenge should delegate to MPThreeDS and map result on success`() = runTest {
        // Arrange
        val activity: Activity = mockk()
        val authentication = ThreeDSAuthenticationModel(
            threeDSServerTransID = "server123",
            acsReferenceNumber = "acs123",
            dsTransID = "ds123",
            acsTransID = "acstrans123",
            acsSignedContent = "signed_content",
        )
        val mpResult = MPThreeDSChallengeResult.OnSuccess(
            result = MPThreeDSAuthenticated(
                challengeResponse = MPThreeDSChallengeModel(
                    threeDSServerTransID = "server123",
                    acsReferenceNumber = "acs123",
                    dsTransID = "ds123",
                    acsTransID = "acstrans123",
                    acsSignedContent = "signed_content",
                ),
                challengeCompleted = true,
            ),
        )
        coEvery {
            mpThreeDS.doChallenge(
                activity = activity,
                authentication = any(),
                timeout = 10,
            )
        } returns mpResult

        // Act
        val result = adapter.doChallenge(
            activity = activity,
            authentication = authentication,
            timeout = 10,
        )

        // Assert
        assert(result is ThreeDSChallengeResult.OnSuccess)
        val successResult = result as ThreeDSChallengeResult.OnSuccess
        assertEquals(true, successResult.result.challengeCompleted)
        assertEquals("server123", successResult.result.challengeResponse.threeDSServerTransID)
    }

    @Test
    fun `doChallenge should map OnCancel result correctly`() = runTest {
        // Arrange
        val activity: Activity = mockk()
        val authentication = ThreeDSAuthenticationModel(
            threeDSServerTransID = "server123",
            acsReferenceNumber = "acs123",
            dsTransID = "ds123",
            acsTransID = "acstrans123",
            acsSignedContent = "signed_content",
        )
        coEvery {
            mpThreeDS.doChallenge(
                activity = activity,
                authentication = any(),
                timeout = 10,
            )
        } returns MPThreeDSChallengeResult.OnCancel

        // Act
        val result = adapter.doChallenge(
            activity = activity,
            authentication = authentication,
            timeout = 10,
        )

        // Assert
        assert(result is ThreeDSChallengeResult.OnCancel)
    }

    @Test
    fun `doChallenge should map OnTimedOut result correctly`() = runTest {
        // Arrange
        val activity: Activity = mockk()
        val authentication = ThreeDSAuthenticationModel(
            threeDSServerTransID = "server123",
            acsReferenceNumber = "acs123",
            dsTransID = "ds123",
            acsTransID = "acstrans123",
            acsSignedContent = "signed_content",
        )
        coEvery {
            mpThreeDS.doChallenge(
                activity = activity,
                authentication = any(),
                timeout = 10,
            )
        } returns MPThreeDSChallengeResult.OnTimedOut

        // Act
        val result = adapter.doChallenge(
            activity = activity,
            authentication = authentication,
            timeout = 10,
        )

        // Assert
        assert(result is ThreeDSChallengeResult.OnTimedOut)
    }

    @Test
    fun `close should delegate to MPThreeDS`() {
        // Act
        adapter.close()

        // Assert
        verify(exactly = 1) { mpThreeDS.close() }
    }

    @Test
    fun `getWarnings should return mapped warnings list`() {
        // Arrange
        val mpWarnings = listOf(
            MPThreeDSWarning(
                id = "warning1",
                message = "Test warning",
                severity = MPThreeDSSeverity.MEDIUM,
            ),
            MPThreeDSWarning(
                id = "warning2",
                message = "Another warning",
                severity = MPThreeDSSeverity.HIGH,
            ),
        )
        every { mpThreeDS.getWarnings() } returns mpWarnings

        // Act
        val result = adapter.getWarnings()

        // Assert
        assertEquals(2, result.size)
        assertEquals("warning1", result[0].id)
        assertEquals("Test warning", result[0].message)
        assertEquals(ThreeDSSeverity.MEDIUM, result[0].severity)
        assertEquals("warning2", result[1].id)
        assertEquals("Another warning", result[1].message)
        assertEquals(ThreeDSSeverity.HIGH, result[1].severity)
    }

    @Test
    fun `getWarnings should return empty list when no warnings`() {
        // Arrange
        every { mpThreeDS.getWarnings() } returns emptyList()

        // Act
        val result = adapter.getWarnings()

        // Assert
        assertEquals(0, result.size)
    }
}
