package com.mercadopago.sdk.android.threeds.data.repository

import app.cash.turbine.test
import com.mercadopago.sdk.android.threeds.data.datasource.remote.ThreeDSRemoteDataSource
import com.mercadopago.sdk.android.threeds.mockThreeDSAuthenticationModel
import com.mercadopago.sdk.android.threeds.mockThreeDSAuthenticationParams
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class ThreeDSRepositoryImplTest {

    private val remoteDataSource = mockk<ThreeDSRemoteDataSource>()
    private val repository = ThreeDSRepositoryImpl(remoteDataSource)

    @Test
    fun `when authenticate is called Then should call remote data source and return result`() = runTest {
        // Given
        val params = mockThreeDSAuthenticationParams()
        val mockResponse = mockThreeDSAuthenticationModel()

        every { remoteDataSource.authenticate(any()) } returns flowOf(mockResponse)

        // When
        val result = repository.authenticate(params)

        // Then
        result.test {
            assertEquals(mockResponse, awaitItem())
            awaitComplete()
        }

        verify { remoteDataSource.authenticate(any()) }
    }

    @Test
    fun `when authenticate is called with different response Then should return correct model`() = runTest {
        // Given
        val params = mockThreeDSAuthenticationParams()
        val mockResponse = mockThreeDSAuthenticationModel("CHALLENGE")

        every { remoteDataSource.authenticate(any()) } returns flowOf(mockResponse)

        // When
        val result = repository.authenticate(params)

        // Then
        result.test {
            val response = awaitItem()
            assertEquals("CHALLENGE", response.response)
            assertEquals(mockResponse.threeDSServerTransID, response.threeDSServerTransID)
            awaitComplete()
        }
    }
}
