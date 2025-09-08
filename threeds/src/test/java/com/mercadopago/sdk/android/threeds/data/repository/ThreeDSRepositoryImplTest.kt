package com.mercadopago.sdk.android.threeds.data.repository

import android.app.Activity
import com.mercadopago.sdk.android.threeds.data.wrapper.ThreeDSWrapper
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSChallengeResult
import com.mercadopago.sdk.android.threeds.domain.model.MPThreeDSWarning
import com.mercadopago.sdk.android.threeds.domain.model.params.MPThreeDSRequestParams
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ThreeDSRepositoryImplTest {

    // Arrange - Test dependencies
    private val mockWrapper: ThreeDSWrapper = mockk(relaxed = true)
    private val mockActivity: Activity = mockk()

    private lateinit var repository: ThreeDSRepositoryImpl

    @Before
    fun setUp() {
        // Arrange - Create repository instance with mock wrapper
        repository = ThreeDSRepositoryImpl(mockWrapper)
    }

    @Test
    fun `when initialize is called then should delegate to wrapper`() = runTest {
        // Arrange
        coEvery { mockWrapper.initialize() } returns Unit

        // Act
        repository.initialize()

        // Assert
        coVerify(exactly = 1) { mockWrapper.initialize() }
    }

    @Test
    fun `when getWarnings is called then should delegate to wrapper and map results`() = runTest {
        // Arrange
        val mockWrapperWarnings = listOf(mockk(), mockk())
        val expectedMappedWarnings: List<MPThreeDSWarning> = listOf(mockk(), mockk())

        every { mockWrapper.getWarnings() } returns mockWrapperWarnings
        // Note: toModel() mapper would need to be mocked or tested separately

        // Act
        val actualWarnings: List<MPThreeDSWarning> = repository.getWarnings()

        // Assert
        verify(exactly = 1) { mockWrapper.getWarnings() }
        // Additional assertion would verify mapping if toModel() is tested
    }

    @Test
    fun `when createTransaction is called then should delegate to wrapper`() = runTest {
        // Arrange
        val inputPaymentMethodId = "visa"

        // Act
        repository.createTransaction(inputPaymentMethodId)

        // Assert
        verify(exactly = 1) { mockWrapper.createTransaction(inputPaymentMethodId) }
    }

    @Test
    fun `when getAuthenticationRequestParameters is called then should delegate to wrapper`() = runTest {
        // Arrange
        val expectedParams: MPThreeDSRequestParams = mockk()
        every { mockWrapper.getAuthenticationRequestParameters() } returns expectedParams

        // Act
        val actualParams: MPThreeDSRequestParams? = repository.getAuthenticationRequestParameters()

        // Assert
        verify(exactly = 1) { mockWrapper.getAuthenticationRequestParameters() }
        assertSame(expectedParams, actualParams)
    }

    @Test
    fun `when getAuthenticationRequestParameters returns null then should return null`() = runTest {
        // Arrange
        every { mockWrapper.getAuthenticationRequestParameters() } returns null

        // Act
        val actualParams: MPThreeDSRequestParams? = repository.getAuthenticationRequestParameters()

        // Assert
        verify(exactly = 1) { mockWrapper.getAuthenticationRequestParameters() }
        assertEquals(null, actualParams)
    }

    @Test
    fun `when doChallenge is called then should delegate to wrapper with mapped parameters`() = runTest {
        // Arrange
        val inputAuthentication: MPThreeDSAuthenticationModel = mockk()
        val inputTimeout = 15
        val expectedResult: MPThreeDSChallengeResult = mockk()

        coEvery {
            mockWrapper.doChallenge(
                activity = mockActivity,
                authenticationParams = any(), // toParams() mapping would be tested
                timeout = inputTimeout
            )
        } returns expectedResult

        // Act
        val actualResult: MPThreeDSChallengeResult = repository.doChallenge(
            activity = mockActivity,
            authenticationResponse = inputAuthentication,
            timeout = inputTimeout
        )

        // Assert
        coVerify(exactly = 1) {
            mockWrapper.doChallenge(
                activity = mockActivity,
                authenticationParams = any(),
                timeout = inputTimeout
            )
        }
        assertSame(expectedResult, actualResult)
    }

    @Test
    fun `when close is called then should delegate to wrapper`() = runTest {
        // Act
        repository.close()

        // Assert
        verify(exactly = 1) { mockWrapper.close() }
    }
}
