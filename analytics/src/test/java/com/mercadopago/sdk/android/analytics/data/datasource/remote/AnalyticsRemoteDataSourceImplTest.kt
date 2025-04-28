package com.mercadopago.sdk.android.analytics.data.datasource.remote

import android.app.Application
import android.content.pm.ApplicationInfo
import app.cash.turbine.test
import com.google.gson.Gson
import com.mercadopago.sdk.android.analytics.data.remote.service.AnalyticsService
import com.mercadopago.sdk.android.analytics.mockMetric
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.test.Test

internal class AnalyticsRemoteDataSourceImplTest {

    private val service = mockk<AnalyticsService>()
    private val context = mockk<Application>(relaxed = true) {
        every { applicationInfo.packageName } returns "com.your.test.package"
    }
    private val gson = mockk<Gson>(relaxed = true)
    private val dataSource = AnalyticsRemoteDataSourceImpl(
        service = service,
        context = context,
        gson = gson,
        coroutineDispatcher = Dispatchers.Unconfined,
    )

    @Test
    fun `when trackMetric is called with success Then return unit`() = runTest {
        // Given
        val metric = mockMetric()
        val siteId = "siteId"
        val sessionId = "sessionId"
        val uid = "uid"
        mockkStatic(ApplicationInfo::class)
        val applicationInfo = mockk<ApplicationInfo>(relaxed = true)
        every {
            context.applicationInfo
        } returns applicationInfo
        coEvery {
            service.trackMetric(any())
        } returns Response.success(Unit)

        // When
        val result = dataSource.trackMetric(
            metric = metric,
            siteId = siteId,
            sessionId = sessionId,
            uid = uid,
        )

        // Then
        result.test {
            awaitItem()
            awaitComplete()
        }
    }

    @Test
    fun `when trackMetric is called with error Then return error`() = runTest {
        // Given
        val metric = mockMetric()
        val siteId = "siteId"
        val sessionId = "sessionId"
        val uid = "uid"
        coEvery {
            service.trackMetric(any())
        } returns Response.error(400, mockk<ResponseBody>(relaxed = true))

        // When
        val result = dataSource.trackMetric(
            metric = metric,
            siteId = siteId,
            sessionId = sessionId,
            uid = uid,
        )

        // Then
        result.test {
            awaitError()
        }
    }
}
