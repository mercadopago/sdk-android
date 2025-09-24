package com.mercadopago.sdk.android.data.repository

import app.cash.turbine.test
import com.mercadopago.sdk.android.data.local.datasource.SdkInitializationLocalDataSource
import com.mercadopago.sdk.android.data.remote.datasource.SdkInitializationRemoteDataSource
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.model.SiteId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class SdkInitializationRepositoryTest {

    private val sdkInitializationLocalDataSource = mockk<SdkInitializationLocalDataSource>()
    private val sdkInitializationRemoteDataSource = mockk<SdkInitializationRemoteDataSource>()
    private val repository = SdkInitializationRepositoryImpl(
        sdkInitializationLocalDataSource = sdkInitializationLocalDataSource,
        sdkInitializationRemoteDataSource = sdkInitializationRemoteDataSource,
    )

    @Test
    fun `when fetchSiteId is called and cache is valid Then return cached siteId without remote call`() = runTest {
        // Given
        val publicKey = "public_key"
        val cachedSiteId = SiteId("cached_123")
        every {
            sdkInitializationLocalDataSource.getSiteId(publicKey)
        } returns flowOf(cachedSiteId)

        // When
        val result = repository.fetchSiteId(publicKey)

        // Then
        result.test {
            assertEquals(cachedSiteId, awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) {
            sdkInitializationLocalDataSource.getSiteId(publicKey)
        }
        verify(exactly = 0) {
            sdkInitializationRemoteDataSource.fetchSiteId(any())
        }
    }

    @Test
    fun `when fetchSiteId is called and cache is empty Then fetch from remote and save it`() = runTest {
        // Given
        val publicKey = "public_key"
        val emptySiteId = SiteId("")
        val remoteSiteId = SiteId("remote_123")
        every {
            sdkInitializationLocalDataSource.getSiteId(publicKey)
        } returns flowOf(emptySiteId)
        every {
            sdkInitializationRemoteDataSource.fetchSiteId(publicKey)
        } returns flowOf(remoteSiteId)
        every {
            sdkInitializationLocalDataSource.setSiteId(publicKey, remoteSiteId)
        } returns flowOf(Unit)

        // When
        val result = repository.fetchSiteId(publicKey)

        // Then
        result.test {
            assertEquals(remoteSiteId, awaitItem())
            awaitComplete()
        }
        verifySequence {
            sdkInitializationLocalDataSource.getSiteId(publicKey)
            sdkInitializationRemoteDataSource.fetchSiteId(publicKey)
            sdkInitializationLocalDataSource.setSiteId(publicKey, remoteSiteId)
        }
    }

    @Test
    fun `when fetchSiteId is called with empty cache and remote error Then return cached empty siteId`() = runTest {
        // Given
        val publicKey = "public_key"
        val emptySiteId = SiteId("")
        val exception = Exception("Network error")
        every {
            sdkInitializationLocalDataSource.getSiteId(publicKey)
        } returns flowOf(emptySiteId)
        every {
            sdkInitializationRemoteDataSource.fetchSiteId(publicKey)
        } returns flow { throw exception }

        // When
        val result = repository.fetchSiteId(publicKey)

        // Then
        result.test {
            assertEquals(emptySiteId, awaitItem())
            awaitComplete()
        }
        verifySequence {
            sdkInitializationLocalDataSource.getSiteId(publicKey)
            sdkInitializationRemoteDataSource.fetchSiteId(publicKey)
            sdkInitializationLocalDataSource.getSiteId(publicKey)
        }
    }

    @Test
    fun `when fetchSiteId is called and cache has null or empty siteId Then fetch from remote`() = runTest {
        // Given
        val publicKey = "public_key"
        val nullSiteId = SiteId("")
        val remoteSiteId = SiteId("remote_456")
        every {
            sdkInitializationLocalDataSource.getSiteId(publicKey)
        } returns flowOf(nullSiteId)
        every {
            sdkInitializationRemoteDataSource.fetchSiteId(publicKey)
        } returns flowOf(remoteSiteId)
        every {
            sdkInitializationLocalDataSource.setSiteId(publicKey, remoteSiteId)
        } returns flowOf(Unit)

        // When
        val result = repository.fetchSiteId(publicKey)

        // Then
        result.test {
            assertEquals(remoteSiteId, awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) {
            sdkInitializationLocalDataSource.getSiteId(publicKey)
            sdkInitializationRemoteDataSource.fetchSiteId(publicKey)
            sdkInitializationLocalDataSource.setSiteId(publicKey, remoteSiteId)
        }
    }

    @Test
    fun `when getSiteId is called with success Then emit siteId`() = runTest {
        // Given
        val publicKey = "public_key"
        val siteId = SiteId("123")
        every {
            sdkInitializationLocalDataSource.getSiteId(publicKey)
        } returns flowOf(siteId)

        // When
        val result = repository.getSiteId(publicKey)

        // Then
        result.test {
            assertEquals(siteId, awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) {
            sdkInitializationLocalDataSource.getSiteId(publicKey)
        }
    }

    @Test
    fun `when setSiteId is called with success Then emit success`() = runTest {
        // Given
        val publicKey = "public_key"
        val countryCode = CountryCode.ARG
        val siteId = SiteId("MLA")
        every {
            sdkInitializationLocalDataSource.setSiteId(publicKey, siteId)
        } returns flowOf(Unit)

        // When
        val result = repository.setSiteId(publicKey, countryCode)

        // Then
        result.test {
            assertEquals(Unit, awaitItem())
            awaitComplete()
        }
        verify(exactly = 1) {
            sdkInitializationLocalDataSource.setSiteId(publicKey, siteId)
        }
    }
}
