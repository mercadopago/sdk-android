package com.mercadopago.sdk.android.mpextended.domain.usecase

import com.mercadolibre.android.device.sdk.DeviceSDK
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
import com.mercadopago.sdk.android.mpextended.domain.model.MPDeviceSession
import com.mercadopago.sdk.android.mpextended.domain.repository.MPExtendedRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class GetDeviceSessionUseCaseTest {
    private val repository: MPExtendedRepository = mockk()
    private val useCase = GetDeviceSessionUseCase(repository)

    @Before
    fun setup() {
        mockkStatic(DeviceSDK::class)
        mockkObject(MercadoPagoSDK.Companion)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `when DeviceSDK has instance and siteId is set then returns Success`() = runBlocking {
        val mockDeviceSDK = mockk<DeviceSDK>(relaxed = true)
        val expected = MPDeviceSession(session = "session_abc")
        every { DeviceSDK.getInstance() } returns mockDeviceSDK
        every { MercadoPagoSDK.getSiteId() } returns "MLB"
        coEvery { repository.getDeviceSession(any()) } returns Result.Success(expected)

        val result = useCase()

        assertTrue(result is Result.Success)
        assertEquals(expected, (result as Result.Success).data)
    }

    @Test
    fun `when DeviceSDK returns null then device is null and delegates to repository`() = runBlocking {
        val expected = MPDeviceSession(session = "session_abc")
        every { DeviceSDK.getInstance() } returns null
        every { MercadoPagoSDK.getSiteId() } returns "MLB"
        coEvery { repository.getDeviceSession(any()) } returns Result.Success(expected)

        val result = useCase()

        assertTrue(result is Result.Success)
        assertEquals(expected, (result as Result.Success).data)
    }

    @Test
    fun `when siteId is empty then delegates to repository with empty siteId`() = runBlocking {
        val mockDeviceSDK = mockk<DeviceSDK>(relaxed = true)
        val expected = MPDeviceSession(session = "session_abc")
        every { DeviceSDK.getInstance() } returns mockDeviceSDK
        every { MercadoPagoSDK.getSiteId() } returns ""
        coEvery { repository.getDeviceSession(any()) } returns Result.Success(expected)

        val result = useCase()

        assertTrue(result is Result.Success)
        assertEquals(expected, (result as Result.Success).data)
    }

    @Test
    fun `when repository returns error then use case propagates error`() = runBlocking {
        val mockDeviceSDK = mockk<DeviceSDK>(relaxed = true)
        val error = ResultError.Request(code = "500", message = "Server Error")
        every { DeviceSDK.getInstance() } returns mockDeviceSDK
        every { MercadoPagoSDK.getSiteId() } returns "MLB"
        coEvery { repository.getDeviceSession(any()) } returns Result.Error(error)

        val result = useCase()

        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).error)
    }
}
