package com.mercadopago.sdk.android.initializer

import android.content.Context
import android.util.Log
import app.cash.turbine.test
import com.mercadolibre.android.device.sdk.DeviceSDK
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.initializer.analytics.SdkInitializerAnalytics
import com.mercadopago.sdk.android.initializer.coroutines.SdkCoroutineProvider
import com.mercadopago.sdk.android.initializer.exceptions.EmptyPublicKeyException
import com.mercadopago.sdk.android.initializer.exceptions.SDKAlreadyInitializedException
import com.mercadopago.sdk.android.initializer.exceptions.SDKNotInitializedException
import com.mercadopago.sdk.android.initializer.usecase.ConfigureSdkUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
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
    private val setSiteIdUseCase = mockk<com.mercadopago.sdk.android.domain.usecase.SetSiteIdUseCase>(relaxed = true)
    private val getSiteIdUseCase = mockk<com.mercadopago.sdk.android.domain.usecase.GetSiteIdUseCase>(relaxed = true)
    private val mpAnalytics = mockk<MPAnalytics>(relaxed = true)
    private val deviceSDK = mockk<DeviceSDK>(relaxed = true)
    private val configureSdkUseCase = ConfigureSdkUseCase(getSiteIdUseCase, setSiteIdUseCase)

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
        every { MPAnalytics.initialize(any(), any(), any()) } returns Unit
        every { MPAnalytics.clearInstance() } returns Unit
        every { SdkInitializerAnalytics.buildSdkInitializerEvent(any(), any()) } answers { callOriginal() }
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
        every { koin.get<ConfigureSdkUseCase>() } returns configureSdkUseCase
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
        every {
            setSiteIdUseCase(publicKey, countryCode)
        } returns flowOf(Unit)

        // When
        MercadoPagoSDK.initialize(
            context = context,
            publicKey = publicKey,
            countryCode = countryCode,
        )

        // Then
        assertNotNull(MercadoPagoSDK.getInstance())
        verifyOrder {
            setSiteIdUseCase(publicKey, countryCode)
            mpAnalytics.trackMetric(any())
        }
    }

    @Test
    fun `when initialize is called and use case fails Then log error`() = runTest {
        // Given
        val publicKey = "public_key"
        val countryCode = CountryCode.ARG
        val exception = Exception()
        every { setSiteIdUseCase(publicKey, countryCode) } returns kotlinx.coroutines.flow.flow { throw exception }

        // When
        MercadoPagoSDK.initialize(
            context = context,
            publicKey = publicKey,
            countryCode = countryCode,
        )

        // Then
        assertNotNull(MercadoPagoSDK.getInstance())
        verifyOrder {
            setSiteIdUseCase(publicKey, countryCode)
            Log.d(any(), any(), exception)
        }
        verify(exactly = 0) { mpAnalytics.trackMetric(any()) }
    }

    @Test
    fun `when initialize two times Then throw exception`() = runTest {
        // Given
        val publicKey = "public_key"
        val countryCode = CountryCode.ARG
        every {
            setSiteIdUseCase(publicKey, countryCode)
        } returns flowOf(Unit)
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
            setSiteIdUseCase(publicKey, countryCode)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when setNewConfiguration is called Then update keys and reinitialize flows`() = runTest {
        // Given
        val initialPublicKey = "public_key_1"
        val initialCountryCode = CountryCode.ARG
        val newPublicKey = "public_key_2"
        val newCountryCode = CountryCode.BRA
        every { setSiteIdUseCase(initialPublicKey, initialCountryCode) } returns flowOf(Unit)
        every { setSiteIdUseCase(newPublicKey, newCountryCode) } returns flowOf(Unit)

        // When
        MercadoPagoSDK.initialize(
            context = context,
            publicKey = initialPublicKey,
            countryCode = initialCountryCode,
        )
        MercadoPagoSDK.setNewConfiguration(
            publicKey = newPublicKey,
            countryCode = newCountryCode,
        )

        advanceUntilIdle()

        // Then
        assertNotNull(MercadoPagoSDK.getInstance())
        kotlin.test.assertEquals(newPublicKey, MercadoPagoSDK.publicKey)
        kotlin.test.assertEquals(newCountryCode, MercadoPagoSDK.countryCode)
        io.mockk.verify(exactly = 1) { setSiteIdUseCase(initialPublicKey, initialCountryCode) }
        io.mockk.verify(exactly = 1) { setSiteIdUseCase(newPublicKey, newCountryCode) }
        io.mockk.verify(exactly = 2) {
            mpAnalytics.trackMetric(match { it.path == "/checkout_api_native/initialize" })
        }
    }
}
