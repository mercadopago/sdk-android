package com.mercadopago.sdk.android.data.repository

import app.cash.turbine.test
import com.mercadopago.sdk.android.data.local.datasource.SdkInitializationLocalDataSource
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.model.SiteId
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class SdkInitializationRepositoryTest {

    private val sdkInitializationLocalDataSource = mockk<SdkInitializationLocalDataSource>()
    private val repository = SdkInitializationRepositoryImpl(
        sdkInitializationLocalDataSource = sdkInitializationLocalDataSource,
    )

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
