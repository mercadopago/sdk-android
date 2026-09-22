package com.mercadopago.sdk.android.checkout.data.remote.datasource

import android.content.Context
import android.content.pm.ApplicationInfo
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.checkout.data.remote.request.OrderProcessRequest
import com.mercadopago.sdk.android.checkout.data.remote.response.OrderProcessResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.OrderService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class OrderRemoteDataSourceImplTest {
    private val service = mockk<OrderService>()
    private val analytics = mockk<MPAnalytics>(relaxed = true)
    private val applicationInfo = ApplicationInfo().apply { packageName = "com.merchant.checkout" }
    private val context = mockk<Context>(relaxed = true)
    private val dataSource = OrderRemoteDataSourceImpl(service, context)

    @Before
    fun setup() {
        mockkObject(MPAnalytics.Companion)
        every { MPAnalytics.tryGetInstance() } returns analytics
        coEvery { analytics.getSessionId() } returns "session-abc-123"
        every { context.applicationInfo } returns applicationInfo
    }

    @After
    fun tearDown() {
        unmockkObject(MPAnalytics.Companion)
    }

    private val cardParams = ProcessOrderParams(
        orderId = "ORD_789",
        clientToken = "test-order",
        paymentMethodId = "visa",
        paymentMethodType = "credit_card",
        token = "TOKEN_DEF",
        installments = 6,
        amount = "100.00",
    )

    private val debitCardParams = cardParams.copy(paymentMethodType = "debit_card")

    private val ticketParams = ProcessOrderParams(
        orderId = "ORD_789",
        clientToken = "test-order",
        paymentMethodId = "rapipago",
        paymentMethodType = "ticket",
        token = "",
        installments = 0,
        amount = "100.00",
    )

    @Test
    fun `given service returns successful response then process returns Result Success`() = runTest {
        val body = mockk<OrderProcessResponse>(relaxed = true)
        coEvery { service.process(any(), any(), any()) } returns Response.success(body)

        val result = dataSource.process(cardParams)

        assertIs<Result.Success<OrderProcessResponse>>(result)
        assertEquals(body, result.data)
    }

    @Test
    fun `given service returns error response then process returns Result Error`() = runTest {
        val errorBody = """{"message":"Not Found","code":"404"}""".toResponseBody()
        coEvery { service.process(any(), any(), any()) } returns Response.error(404, errorBody)

        val result = dataSource.process(cardParams)

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("404", error.error.code)
        assertEquals(404, error.error.httpStatus)
    }

    @Test
    fun `given process is called then passes orderId to service`() = runTest {
        coEvery { service.process(any(), any(), any()) } returns Response.success(mockk(relaxed = true))

        dataSource.process(cardParams)

        coVerify { service.process(orderId = cardParams.orderId, clientToken = any(), body = any()) }
    }

    @Test
    fun `given credit card params then body carries token and installments and no amount`() = runTest {
        val bodySlot = slot<OrderProcessRequest>()
        coEvery { service.process(any(), any(), capture(bodySlot)) } returns
            Response.success(mockk(relaxed = true))

        dataSource.process(cardParams)

        assertEquals(cardParams.paymentMethodId, bodySlot.captured.paymentMethodId)
        assertEquals(cardParams.paymentMethodType, bodySlot.captured.paymentMethodType)
        assertEquals(cardParams.token, bodySlot.captured.token)
        assertEquals(cardParams.installments, bodySlot.captured.installments)

        val json = JsonParser.parseString(Gson().toJson(bodySlot.captured)).asJsonObject
        assertFalse(json.has("amount"))
    }

    @Test
    fun `given debit card params then body carries token and installments through the same variant`() = runTest {
        val bodySlot = slot<OrderProcessRequest>()
        coEvery { service.process(any(), any(), capture(bodySlot)) } returns
            Response.success(mockk(relaxed = true))

        dataSource.process(debitCardParams)

        assertEquals(debitCardParams.paymentMethodId, bodySlot.captured.paymentMethodId)
        assertEquals("debit_card", bodySlot.captured.paymentMethodType)
        assertEquals(debitCardParams.token, bodySlot.captured.token)
        assertEquals(debitCardParams.installments, bodySlot.captured.installments)
    }

    @Test
    fun `given ticket params then body carries only payment method id and type`() = runTest {
        val bodySlot = slot<OrderProcessRequest>()
        coEvery { service.process(any(), any(), capture(bodySlot)) } returns
            Response.success(mockk(relaxed = true))

        dataSource.process(ticketParams)

        assertEquals(ticketParams.paymentMethodId, bodySlot.captured.paymentMethodId)
        assertEquals("ticket", bodySlot.captured.paymentMethodType)
        assertNull(bodySlot.captured.token)
        assertNull(bodySlot.captured.installments)

        val json = JsonParser.parseString(Gson().toJson(bodySlot.captured)).asJsonObject
        assertFalse(json.has("amount"))
        assertFalse(json.has("token"))
        assertFalse(json.has("installments"))
    }

    @Test
    fun `given process is called then integration_data carries session id feature android platform and app package`() =
        runTest {
            val bodySlot = slot<OrderProcessRequest>()
            coEvery { service.process(any(), any(), capture(bodySlot)) } returns
                Response.success(mockk(relaxed = true))

            dataSource.process(cardParams)

            val integrationData = bodySlot.captured.integrationData
            assertEquals("session-abc-123", integrationData?.melidataSessionId)
            assertEquals("payment", integrationData?.feature)
            assertEquals("android", integrationData?.platform)
            assertEquals("com.merchant.checkout", integrationData?.app)

            val json = JsonParser.parseString(Gson().toJson(bodySlot.captured)).asJsonObject
            val integrationDataJson = json.getAsJsonObject("integration_data")
            assertEquals("session-abc-123", integrationDataJson.get("melidata_session_id").asString)
            assertEquals("payment", integrationDataJson.get("feature").asString)
            assertEquals("android", integrationDataJson.get("platform").asString)
            assertEquals("com.merchant.checkout", integrationDataJson.get("app").asString)
            assertFalse(json.has("product_id"))
        }

    @Test
    fun `given analytics has no instance then melidataSessionId is omitted from integration_data`() = runTest {
        every { MPAnalytics.tryGetInstance() } returns null
        val bodySlot = slot<OrderProcessRequest>()
        coEvery { service.process(any(), any(), capture(bodySlot)) } returns
            Response.success(mockk(relaxed = true))

        dataSource.process(cardParams)

        assertNull(bodySlot.captured.integrationData?.melidataSessionId)
    }

    @Test
    fun `given host package name is empty then app is omitted from integration_data`() = runTest {
        every { context.applicationInfo } returns ApplicationInfo().apply { packageName = "" }
        val bodySlot = slot<OrderProcessRequest>()
        coEvery { service.process(any(), any(), capture(bodySlot)) } returns
            Response.success(mockk(relaxed = true))

        dataSource.process(cardParams)

        assertNull(bodySlot.captured.integrationData?.app)
    }

    @Test
    fun `given service returns 503 then process returns Result Error without a conclusive payload`() = runTest {
        val errorBody = """{"code":"service_unavailable","message":"order state is inconclusive"}"""
            .toResponseBody()
        coEvery { service.process(any(), any(), any()) } returns Response.error(503, errorBody)

        val result = dataSource.process(cardParams)

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals(503, error.error.httpStatus)
        assertEquals("service_unavailable", error.error.code)
    }
}
