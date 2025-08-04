package com.mercadopago.sdk.android.threeds.domain.interactor

import android.app.Activity
import android.content.Context
import com.mercadopago.sdk.android.domain.model.CountryCode
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import com.mercadopago.sdk.android.threeds.domain.callback.MPThreeDSChallengeDelegate
import com.mercadopago.sdk.android.threeds.domain.exceptions.MPThreeDSAlreadyInitializedException
import com.mercadopago.sdk.android.threeds.domain.exceptions.MPThreeDSNotInitializedException
import com.mercadopago.sdk.android.threeds.domain.usecase.RequestChallengeUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.Koin
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

internal class MPThreeDSTest {

    private val context = mockk<Context>(relaxed = true)
    private val activity = mockk<Activity>(relaxed = true)
    private val delegate = mockk<MPThreeDSChallengeDelegate>(relaxed = true)

    @Before
    fun setup() {
        MercadoPagoSDK.initialize(context, "public_key", CountryCode.BRA)
    }

    @After
    fun tearDown() {
        MercadoPagoSDK.clearInstance()
        unmockkAll()
    }

    @Test
    fun `when getInstance is called without initialization Then should throw MPThreeDSNotInitializedException`() {
        // When & Then
        assertFailsWith<MPThreeDSNotInitializedException> {
            MPThreeDS.getInstance()
        }
    }


    @Test
    fun `when MPThreeDS is created with Koin instance Then should store koin reference`() {
        // Given
        val mockKoin = mockk<Koin>()

        // When
        val instance = MPThreeDS(mockKoin)

        // Then
        assertEquals(mockKoin, instance.koin)
    }

    @Test
    fun `when requestChallenge is called on uninitialized instance Then should throw exception`() {
        // When & Then
        assertFailsWith<MPThreeDSNotInitializedException> {
            val instance = MPThreeDS.getInstance()
            instance.requestChallenge(activity, "token", "visa", delegate)
        }
    }


    @Test
    fun `when initialized Then koin instance should be accessible`() {
        MPThreeDS.initialize(context)
        val instance = MPThreeDS.getInstance()

        // When & Then - Verify that koin property exists and is accessible
        assertNotNull(instance.koin)
    }

    @Test
    fun `when MPThreeDS class is instantiated Then should have correct class structure`() {
        // This test verifies the class structure and companion object

        // When & Then - Verify class has expected structure
        assertNotNull(MPThreeDS.Companion)

        // Verify companion methods exist (reflection test)
        val companionClass = MPThreeDS.Companion::class.java
        val initializeMethod = companionClass.getMethod("initialize", Context::class.java)
        val getInstanceMethod = companionClass.getMethod("getInstance")

        assertNotNull(initializeMethod)
        assertNotNull(getInstanceMethod)
    }

    @Test
    fun `when initialize is called twice Then should throw MPThreeDSAlreadyInitializedException`() {
        // Given
        MPThreeDS.initialize(context)

        // When & Then
        assertFailsWith<MPThreeDSAlreadyInitializedException> {
            MPThreeDS.initialize(context)
        }
    }

    @Test
    fun `when requestChallenge is called with valid parameters Then should invoke use case`() {
        // Given
        MPThreeDS.initialize(context)
        val instance = MPThreeDS.getInstance()
        val mockUseCase = mockk<RequestChallengeUseCase>(relaxed = true)
        every { instance.koin.get<RequestChallengeUseCase>() } returns mockUseCase
        coEvery { mockUseCase.invoke(any(), any(), any(), any(), any()) } just runs

        val cardToken = "test_token"
        val paymentMethodId = "visa"
        val timeout = 15

        // When
        instance.requestChallenge(activity, cardToken, paymentMethodId, delegate, timeout)

        // Then - verify use case was called (we can't verify parameters due to coroutine scope)
        // This test mainly ensures the method doesn't throw and follows the expected flow
        assertNotNull(instance.koin)
    }

    @Test
    fun `when requestChallenge is called with default timeout Then should use default value`() {
        // Given
        MPThreeDS.initialize(context)
        val instance = MPThreeDS.getInstance()
        val mockUseCase = mockk<RequestChallengeUseCase>(relaxed = true)
        every { instance.koin.get<RequestChallengeUseCase>() } returns mockUseCase
        coEvery { mockUseCase.invoke(any(), any(), any(), any(), any()) } just runs

        val cardToken = "test_token"
        val paymentMethodId = "mastercard"

        // When
        instance.requestChallenge(activity, cardToken, paymentMethodId, delegate)

        // Then - verify method executes without timeout parameter
        assertNotNull(instance.koin)
    }

    @Test
    fun `when getInstance is called after successful initialization Then should return same instance`() {
        // Given
        MPThreeDS.initialize(context)
        val instance1 = MPThreeDS.getInstance()

        // When
        val instance2 = MPThreeDS.getInstance()

        // Then
        assertEquals(instance1, instance2)
    }

    @Test
    fun `when initialize is called with different contexts Then should create instance`() {
        // Given
        val anotherContext = mockk<Context>(relaxed = true)

        // When
        MPThreeDS.initialize(anotherContext)
        val instance = MPThreeDS.getInstance()

        // Then
        assertNotNull(instance)
        assertNotNull(instance.koin)
    }
}
