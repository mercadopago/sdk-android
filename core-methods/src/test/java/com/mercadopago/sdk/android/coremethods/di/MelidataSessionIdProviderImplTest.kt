package com.mercadopago.sdk.android.coremethods.di

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.di.SessionIdProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class MelidataSessionIdProviderImplTest {
    private val analytics = mockk<MPAnalytics>()
    private val fallbackSessionIdProvider = mockk<SessionIdProvider> {
        every { getSessionId() } returns "sdk-session-id"
    }

    @Test
    fun `given analytics session id then returns it without fallback`() = runTest {
        coEvery { analytics.getSessionId() } returns "melidata-session-id"
        val provider = MelidataSessionIdProviderImpl({ analytics }, fallbackSessionIdProvider)

        assertEquals("melidata-session-id", provider.getSessionId())
        verify(exactly = 0) { fallbackSessionIdProvider.getSessionId() }
    }

    @Test
    fun `given analytics not initialized then returns fallback session id`() = runTest {
        val provider = MelidataSessionIdProviderImpl({ null }, fallbackSessionIdProvider)

        assertEquals("sdk-session-id", provider.getSessionId())
    }

    @Test
    fun `given analytics session id is empty then returns fallback session id`() = runTest {
        coEvery { analytics.getSessionId() } returns ""
        val provider = MelidataSessionIdProviderImpl({ analytics }, fallbackSessionIdProvider)

        assertEquals("sdk-session-id", provider.getSessionId())
    }

    @Test
    fun `given analytics read throws then returns fallback session id`() = runTest {
        coEvery { analytics.getSessionId() } throws IOException("datastore")
        val provider = MelidataSessionIdProviderImpl({ analytics }, fallbackSessionIdProvider)

        assertEquals("sdk-session-id", provider.getSessionId())
    }

    @Test
    fun `given analytics read is cancelled then propagates cancellation`() = runTest {
        coEvery { analytics.getSessionId() } throws CancellationException("cancelled")
        val provider = MelidataSessionIdProviderImpl({ analytics }, fallbackSessionIdProvider)

        assertFailsWith<CancellationException> { provider.getSessionId() }
    }
}
