package com.mercadopago.sdk.android.checkout.data.remote.request

import android.content.Context
import android.content.pm.ApplicationInfo
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

internal class OrderProcessRequestTest {
    private val analytics = mockk<MPAnalytics>(relaxed = true)
    private val applicationInfo = ApplicationInfo().apply { packageName = "com.merchant.checkout" }
    private val context = mockk<Context>(relaxed = true)

    @Before
    fun setup() {
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns analytics
        every { context.applicationInfo } returns applicationInfo
    }

    @After
    fun tearDown() {
        unmockkObject(MPAnalytics.Companion)
    }

    @Test
    fun `given analytics returns a session id then integration data carries it`() = runTest {
        coEvery { analytics.getSessionId() } returns "session-abc-123"

        val result = context.toIntegrationData()

        assertEquals("session-abc-123", result.melidataSessionId)
        assertEquals("payment", result.feature)
        assertEquals("android", result.platform)
        assertEquals("com.merchant.checkout", result.app)
    }

    @Test
    fun `given getSessionId throws CancellationException then it propagates`() = runTest {
        coEvery { analytics.getSessionId() } throws CancellationException("cancelled")

        assertFailsWith<CancellationException> {
            context.toIntegrationData()
        }
    }

    @Test
    fun `given getSessionId throws a regular exception then melidataSessionId is null`() = runTest {
        coEvery { analytics.getSessionId() } throws IllegalStateException("boom")

        val result = context.toIntegrationData()

        assertNull(result.melidataSessionId)
    }

    @Test
    fun `given no analytics instance then melidataSessionId is null`() = runTest {
        every { MPAnalytics.tryGetInstance() } returns null

        val result = context.toIntegrationData()

        assertNull(result.melidataSessionId)
    }
}
