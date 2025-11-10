package com.mercadopago.sdk.android.initializer.usecase

import android.content.Context
import android.util.Log
import app.cash.turbine.test
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.model.SiteId
import com.mercadopago.sdk.android.domain.usecase.GetSiteIdUseCase
import com.mercadopago.sdk.android.domain.usecase.SetSiteIdUseCase
import com.mercadopago.sdk.android.initializer.analytics.SdkInitializerAnalytics
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

internal class ReconfigureSdkUseCaseTest {

    private val context = mockk<Context>(relaxed = true)
    private val getSiteIdUseCase = mockk<GetSiteIdUseCase>(relaxed = true)
    private val setSiteIdUseCase = mockk<SetSiteIdUseCase>(relaxed = true)
    private val mpAnalytics = mockk<MPAnalytics>(relaxed = true)

    @Test
    fun `when reconfigure succeeds Then initialize analytics and track metric`() = runTest {
        // Given
        mockkStatic(MPAnalytics::class)
        mockkObject(MPAnalytics.Companion)
        mockkObject(SdkInitializerAnalytics)
        every { MPAnalytics.initialize(any(), any()) } returns Unit
        every { MPAnalytics.getInstance() } returns mpAnalytics
        every { SdkInitializerAnalytics.buildSdkInitializerEvent(any(), any(), any()) } answers { callOriginal() }

        val publicKey = "public_key"
        val country = CountryCode.ARG
        every { getSiteIdUseCase(publicKey) } returns flowOf(SiteId("MLA"))
        every { setSiteIdUseCase(publicKey, country) } returns flowOf(Unit)
        val useCase = ReconfigureSdkUseCase(getSiteIdUseCase, setSiteIdUseCase)

        // When
        useCase(
            ReconfigureSdkParams(
                context = context,
                publicKey = publicKey,
                countryCode = country,
            )
        ).test {
            awaitComplete()
        }

        // Then
        verify(exactly = 1) { MPAnalytics.initialize(context, any()) }
        verify(exactly = 1) { mpAnalytics.trackMetric(match { it.path == "/checkout_api_native/initialize" }) }
    }

    @Test
    fun `when reconfigure fails Then log error and track metric`() = runTest {
        // Given
        mockkStatic(Log::class)
        mockkStatic(MPAnalytics::class)
        mockkObject(MPAnalytics.Companion)
        mockkObject(SdkInitializerAnalytics)
        every { MPAnalytics.initialize(any(), any()) } returns Unit
        every { MPAnalytics.getInstance() } returns mpAnalytics
        every { SdkInitializerAnalytics.buildSdkInitializerEvent(any(), any(), any()) } answers { callOriginal() }
        every { Log.d(any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0

        val publicKey = "public_key"
        val country = CountryCode.ARG
        val exception = Exception("fail")
        every { getSiteIdUseCase(publicKey) } returns flowOf(SiteId("MLA"))
        every { setSiteIdUseCase(publicKey, country) } returns flow { throw exception }
        val useCase = ReconfigureSdkUseCase(getSiteIdUseCase, setSiteIdUseCase)

        // When
        useCase(
            ReconfigureSdkParams(
                context = context,
                publicKey = publicKey,
                countryCode = country,
            )
        ).test {
            awaitComplete()
        }

        // Then
        verify(exactly = 1) { Log.d(any(), any(), exception) }
        verify(exactly = 1) { mpAnalytics.trackMetric(match { it.path == "/checkout_api_native/initialize" && it.attributes["error_type"] != null }) }
    }
}
