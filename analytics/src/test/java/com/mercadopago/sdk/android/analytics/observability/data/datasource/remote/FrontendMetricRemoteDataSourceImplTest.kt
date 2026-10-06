package com.mercadopago.sdk.android.analytics.observability.data.datasource.remote

import com.mercadopago.sdk.android.analytics.observability.data.remote.mapper.NativeErrorRequestMapper
import com.mercadopago.sdk.android.analytics.observability.data.remote.models.request.NativeErrorRequest
import com.mercadopago.sdk.android.analytics.observability.data.remote.service.FrontendMetricService
import com.mercadopago.sdk.android.analytics.observability.domain.models.PendingNativeError
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class FrontendMetricRemoteDataSourceImplTest {
    private val service = mockk<FrontendMetricService>()
    private val mapper = mockk<NativeErrorRequestMapper>()
    private val dataSource = FrontendMetricRemoteDataSourceImpl(service, mapper)
    private val error = mockk<PendingNativeError>()
    private val request = mockk<NativeErrorRequest>()

    @Test
    fun `accepted response closes its body`() = runTest {
        val body = mockk<ResponseBody>(relaxed = true)
        every { mapper.map(error) } returns request
        coEvery { service.report(request) } returns Response.success(202, body)

        assertTrue(dataSource.report(error))

        verify(exactly = 1) { body.close() }
    }

    @Test
    fun `rejected response closes its error body`() = runTest {
        val body = mockk<ResponseBody>(relaxed = true)
        every { mapper.map(error) } returns request
        coEvery { service.report(request) } returns Response.error(500, body)

        assertFalse(dataSource.report(error))

        verify(exactly = 1) { body.close() }
    }
}
