package com.mercadopago.sdk.android.initializer

import android.content.Context
import android.util.Log
import app.cash.turbine.test
import com.mercadolibre.android.device.sdk.DeviceSDK
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.domain.model.SiteId
import com.mercadopago.sdk.android.domain.usecase.FetchSiteIdUseCase
import com.mercadopago.sdk.android.domain.usecase.GetSiteIdUseCase
import com.mercadopago.sdk.android.initializer.analytics.SdkInitializerAnalytics
import com.mercadopago.sdk.android.initializer.coroutines.SdkCoroutineProvider
import com.mercadopago.sdk.android.initializer.exceptions.EmptyPublicKeyException
import com.mercadopago.sdk.android.initializer.exceptions.SDKAlreadyInitializedException
import com.mercadopago.sdk.android.initializer.exceptions.SDKNotInitializedException
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verifyOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.koin.core.Koin
import kotlin.test.Test
import kotlin.test.assertNotNull

internal class MercadoPagoSDKTest {

    private val context = mockk<Context>(relaxed = true)
    private val koin = mockk<Koin>(relaxed = true)
    private val fetchSiteIdUseCase = mockk<FetchSiteIdUseCase>(relaxed = true)
    private val getSiteIdUseCase = mockk<GetSiteIdUseCase>(relaxed = true)
    private val mpAnalytics = mockk<MPAnalytics>(relaxed = true)
    private val deviceSDK = mockk<DeviceSDK>(relaxed = true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkObject(CoreKoinFactory)
        mockkObject(SdkInitializerAnalytics)
        mockkObject(SdkCoroutineProvider)
        mockkStatic(MPAnalytics::class)
        mockkObject(MPAnalytics.Companion)
        mockkStatic(DeviceSDK::class)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
        every {
            SdkCoroutineProvider.provideSDKCoroutineScope()
        } returns CoroutineScope(testDispatcher)
        every {
            CoreKoinFactory.createKoinApp(any(), any(), any())
        } returns koin
        every {
            koin.get<FetchSiteIdUseCase>()
        } returns fetchSiteIdUseCase
        every {
            koin.get<GetSiteIdUseCase>()
        } returns getSiteIdUseCase
        every {
            MPAnalytics.getInstance()
        } returns mpAnalytics
        every {
            DeviceSDK.getInstance()
        } returns deviceSDK
    }

    @After
    fun after() {
        MercadoPagoSDK.clearInstance()
    }

    @Test
    fun `when initialize is called Then sdkInstance is not null`() = runTest {
        // Given
        val publicKey = "public_key"
        val countryCode = CountryCode.ARG
        val siteId = SiteId("MLA")
        every {
            fetchSiteIdUseCase(publicKey, countryCode)
        } returns flowOf(siteId)
        val sdkInitializerEvent = SdkInitializerAnalytics.buildSdkInitializerEvent(context, publicKey)
        every {
            SdkInitializerAnalytics.buildSdkInitializerEvent(context, publicKey)
        } returns sdkInitializerEvent

        // When
        MercadoPagoSDK.initialize(
            context = context,
            publicKey = publicKey,
            countryCode = countryCode,
        )

        // Then
        assertNotNull(MercadoPagoSDK.getInstance())
        verifyOrder {
            fetchSiteIdUseCase(publicKey, countryCode)
            mpAnalytics.trackMetric(sdkInitializerEvent)
        }
    }

    @Test
    fun `when initialize is called and siteId fails Then log error`() = runTest {
        // Given
        val publicKey = "public_key"
        val countryCode = CountryCode.ARG
        val exception = Exception()
        every {
            fetchSiteIdUseCase(publicKey, countryCode)
        } returns flow { throw exception }
        val sdkInitializerEvent = SdkInitializerAnalytics.buildSdkInitializerEvent(
            context = context,
            publicKey = publicKey,
        )
        every {
            SdkInitializerAnalytics.buildSdkInitializerEvent(context, publicKey)
        } returns sdkInitializerEvent

        // When
        MercadoPagoSDK.initialize(
            context = context,
            publicKey = publicKey,
            countryCode = countryCode,
        )

        // Then
        assertNotNull(MercadoPagoSDK.getInstance())
        verifyOrder {
            fetchSiteIdUseCase(publicKey, countryCode)
            Log.d(any(), any(), exception)
            mpAnalytics.trackMetric(any())
        }
    }

    @Test
    fun `when initialize two times Then throw exception`() = runTest {
        // Given
        val publicKey = "public_key"
        val countryCode = CountryCode.ARG
        val siteId = SiteId("MLA")
        every {
            fetchSiteIdUseCase(publicKey, countryCode)
        } returns flowOf(siteId)
        val sdkInitializerEvent = SdkInitializerAnalytics.buildSdkInitializerEvent(context, publicKey)
        every {
            SdkInitializerAnalytics.buildSdkInitializerEvent(context, publicKey)
        } returns sdkInitializerEvent

        // When
        MercadoPagoSDK.initialize(
            context = context,
            publicKey = publicKey,
            countryCode = countryCode,
        )
        val secondInitialization = flow {
            emit(
                MercadoPagoSDK.initialize(
                    context = context,
                    publicKey = publicKey,
                    countryCode = countryCode,
                )
            )
        }

        // Then
        secondInitialization.test {
            awaitError() is SDKAlreadyInitializedException
        }
        verifyOrder {
            fetchSiteIdUseCase(publicKey, countryCode)
            mpAnalytics.trackMetric(sdkInitializerEvent)
        }
    }

    @Test
    fun `when public Key is empty Then throw exception`() = runTest {
        // Given
        val publicKey = ""

        // When
        val initialization = flow {
            emit(
                MercadoPagoSDK.initialize(
                    context = context,
                    publicKey = publicKey,
                    countryCode = CountryCode.ARG,
                )
            )
        }
        val instance = flow {
            emit(
                MercadoPagoSDK.initialize(
                    context = context,
                    publicKey = publicKey,
                    countryCode = CountryCode.ARG,
                )
            )
        }

        // Then
        initialization.test {
            awaitError() is EmptyPublicKeyException
        }
        instance.test {
            awaitError() is SDKNotInitializedException
        }
    }

    @Test
    fun `when sdk instance is called but is not initialized Then throw exception`() = runTest {
        // When
        val initialization = flow {
            emit(MercadoPagoSDK.getInstance())
        }

        // Then
        initialization.test {
            awaitError() is SDKNotInitializedException
        }
    }
}
