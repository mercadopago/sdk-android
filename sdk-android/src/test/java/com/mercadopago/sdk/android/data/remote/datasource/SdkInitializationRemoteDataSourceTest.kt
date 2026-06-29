package com.mercadopago.sdk.android.data.remote.datasource

import app.cash.turbine.test
import com.mercadopago.sdk.android.data.remote.response.SiteIdResponse
import com.mercadopago.sdk.android.data.remote.service.SdkInitializationService
import com.mercadopago.sdk.android.domain.model.SiteId
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.lang.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class SdkInitializationRemoteDataSourceTest {

    private val sdkInitializationService = mockk<SdkInitializationService>()
    private val dataSource = SdkInitializationRemoteDataSourceImpl(
        sdkInitializationService = sdkInitializationService,
    )

    @Test
    fun `when fetchSiteId is called Then return siteId`() = runTest {
        // Given
        val siteIdResponse = SiteIdResponse("123")
        val siteId = SiteId("123")
        val publicKey = "public_key"
        coEvery {
            sdkInitializationService.fetchSiteId()
        } returns siteIdResponse

        // When
        val result = dataSource.fetchSiteId(publicKey)

        // Then
        result.test {
            assertEquals(siteId, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `when fetchSiteId returns null siteId Then throw exception`() = runTest {
        // Given
        val siteIdResponse = SiteIdResponse(siteId = null)
        val publicKey = "public_key"
        coEvery {
            sdkInitializationService.fetchSiteId()
        } returns siteIdResponse

        // When
        val result = dataSource.fetchSiteId(publicKey)

        // Then
        result.test {
            assertTrue(awaitError() is IllegalArgumentException)
        }
    }

    @Test
    fun `when fetchSiteId returns error Then throw exception`() = runTest {
        // Given
        val publicKey = "public_key"
        val error = Exception()
        coEvery {
            sdkInitializationService.fetchSiteId()
        } throws error

        // When
        val result = dataSource.fetchSiteId(publicKey)

        // Then
        result.test {
            assertEquals(error, awaitError())
        }
    }
}
