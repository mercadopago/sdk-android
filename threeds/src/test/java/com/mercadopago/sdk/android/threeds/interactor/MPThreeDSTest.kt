package com.mercadopago.sdk.android.threeds.interactor

import android.app.Activity
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams
import com.mercadopago.sdk.android.threeds.domain.repository.ThreeDSRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.core.Koin
import kotlin.test.assertEquals
import kotlin.test.assertSame

class MPThreeDSTest {

    // Arrange - Test dependencies
    private val mockRepository: ThreeDSRepository = mockk(relaxed = true)
    private val mockKoin: Koin = mockk()
    private val mockActivity: Activity = mockk()

    private lateinit var mpThreeDS: MPThreeDS

    @Before
    fun setUp() {
        // Arrange - Mock Koin to return our mock repository
        every { mockKoin.get<ThreeDSRepository>() } returns mockRepository

        // Arrange - Create instance under test
        mpThreeDS = MPThreeDS(mockKoin)
    }

    @Test
    fun `when getWarnings is called then should delegate to repository`() = runTest {
        // Arrange
        val expectedWarnings: List<MPThreeDSWarning> = listOf(
            mockk(),
            mockk()
        )
        every { mockRepository.getWarnings() } returns expectedWarnings

        // Act
        val actualWarnings: List<MPThreeDSWarning> = mpThreeDS.getWarnings()

        // Assert
        verify(exactly = 1) { mockRepository.getWarnings() }
        assertSame(expectedWarnings, actualWarnings)
    }

    @Test
    fun `when createTransaction is called with paymentMethodId then should delegate to repository`() = runTest {
        // Arrange
        val inputPaymentMethodId = "visa"

        // Act
        mpThreeDS.createTransaction(inputPaymentMethodId)

        // Assert
        verify(exactly = 1) { mockRepository.createTransaction(inputPaymentMethodId) }
    }

    @Test
    fun `when getAuthenticationRequestParameters is called then should return repository result`() = runTest {
        // Arrange
        val expectedParams: MPThreeDSRequestParams = mockk()
        every { mockRepository.getAuthenticationRequestParameters() } returns expectedParams

        // Act
        val actualParams: MPThreeDSRequestParams? = mpThreeDS.getAuthenticationRequestParameters()

        // Assert
        verify(exactly = 1) { mockRepository.getAuthenticationRequestParameters() }
        assertSame(expectedParams, actualParams)
    }

    @Test
    fun `when getAuthenticationRequestParameters returns null then should return null`() = runTest {
        // Arrange
        every { mockRepository.getAuthenticationRequestParameters() } returns null

        // Act
        val actualParams: MPThreeDSRequestParams? = mpThreeDS.getAuthenticationRequestParameters()

        // Assert
        verify(exactly = 1) { mockRepository.getAuthenticationRequestParameters() }
        assertEquals(null, actualParams)
    }

    @Test
    fun `when doChallenge is called then should delegate to repository with correct parameters`() = runTest {
        // Arrange
        val inputAuthentication: MPThreeDSAuthenticationModel = mockk()
        val inputTimeout = 15
        val expectedResult: MPThreeDSChallengeResult = mockk()

        coEvery {
            mockRepository.doChallenge(
                activity = mockActivity,
                authenticationResponse = inputAuthentication,
                timeout = inputTimeout
            )
        } returns expectedResult

        // Act
        val actualResult: MPThreeDSChallengeResult = mpThreeDS.doChallenge(
            activity = mockActivity,
            authentication = inputAuthentication,
            timeout = inputTimeout
        )

        // Assert
        coVerify(exactly = 1) {
            mockRepository.doChallenge(
                activity = mockActivity,
                authenticationResponse = inputAuthentication,
                timeout = inputTimeout
            )
        }
        assertSame(expectedResult, actualResult)
    }

    @Test
    fun `when doChallenge is called with default timeout then should use default value`() = runTest {
        // Arrange
        val inputAuthentication: MPThreeDSAuthenticationModel = mockk()
        val expectedDefaultTimeout = 10
        val expectedResult: MPThreeDSChallengeResult = mockk()

        coEvery {
            mockRepository.doChallenge(
                activity = mockActivity,
                authenticationResponse = inputAuthentication,
                timeout = expectedDefaultTimeout
            )
        } returns expectedResult

        // Act
        val actualResult: MPThreeDSChallengeResult = mpThreeDS.doChallenge(
            activity = mockActivity,
            authentication = inputAuthentication
        )

        // Assert
        coVerify(exactly = 1) {
            mockRepository.doChallenge(
                activity = mockActivity,
                authenticationResponse = inputAuthentication,
                timeout = expectedDefaultTimeout
            )
        }
        assertSame(expectedResult, actualResult)
    }

    @Test
    fun `when close is called then should delegate to repository`() = runTest {
        // Act
        mpThreeDS.close()

        // Assert
        verify(exactly = 1) { mockRepository.close() }
    }
}
