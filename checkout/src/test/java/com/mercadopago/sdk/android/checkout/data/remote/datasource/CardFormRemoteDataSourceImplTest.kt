package com.mercadopago.sdk.android.checkout.data.remote.datasource

import com.mercadopago.sdk.android.checkout.data.remote.request.CardBinRequest
import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.data.remote.service.CardFormService
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class CardFormRemoteDataSourceImplTest {
    private val service = mockk<CardFormService>()
    private val dataSource = CardFormRemoteDataSourceImpl(service)

    private val orderId = "order-123"
    private val clientToken = "token-abc"
    private val checkoutType = "card_form"
    private val amount = "100.00"

    private val cardBinRequest = CardBinRequest(
        bin = "503175",
        amount = amount,
        checkoutType = checkoutType,
        processingMode = "aggregator",
        excludedPaymentTypes = "ticket",
        excludedPaymentMethods = "pix",
    )

    @Test
    fun `given service returns successful response then fetchInitialization returns Success`() = runTest {
        val body = mockk<CardFormInitResponse>(relaxed = true)
        coEvery { service.initialization(any(), any(), any(), any(), any()) } returns Response.success(body)

        val result = dataSource.fetchInitialization(orderId, clientToken, checkoutType, screens = null)

        assertIs<Result.Success<CardFormInitResponse>>(result)
        assertEquals(body, result.data)
    }

    @Test
    fun `given service returns error response then fetchInitialization returns Error`() = runTest {
        val errorBody = """{"message":"Not Found","code":"404"}""".toResponseBody()
        coEvery { service.initialization(any(), any(), any(), any(), any()) } returns Response.error(404, errorBody)

        val result = dataSource.fetchInitialization(orderId, clientToken, checkoutType, screens = null)

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("404", error.error.code)
        assertEquals(404, error.error.httpStatus)
    }

    @Test
    fun `given card transaction then sends authorization and orderId`() = runTest {
        val body = mockk<CardFormInitResponse>(relaxed = true)
        coEvery { service.initialization(any(), any(), any(), any(), any()) } returns Response.success(body)

        dataSource.fetchInitialization(
            orderId = orderId,
            clientToken = clientToken,
            checkoutType = checkoutType,
            screens = null,
        )

        coVerify {
            service.initialization(
                authorization = "Bearer $clientToken",
                orderId = orderId,
                checkoutType = checkoutType,
                screens = null,
            )
        }
    }

    @Test
    fun `given card save then sends no authorization and no orderId`() = runTest {
        val body = mockk<CardFormInitResponse>(relaxed = true)
        coEvery { service.initialization(any(), any(), any(), any(), any()) } returns Response.success(body)

        dataSource.fetchInitialization(orderId = null, clientToken = null, checkoutType = "card_save", screens = null)

        coVerify {
            service.initialization(
                authorization = null,
                orderId = null,
                checkoutType = "card_save",
                screens = null,
            )
        }
    }

    @Test
    fun `given service returns successful response then getCardBin returns Success`() = runTest {
        val body = mockk<CardBinResponse>(relaxed = true)
        coEvery {
            service.getCardBin(any(), any(), any(), any(), any(), any(), any())
        } returns Response.success(body)

        val result = dataSource.getCardBin(cardBinRequest)

        assertIs<Result.Success<CardBinResponse>>(result)
        assertEquals(body, result.data)
    }

    @Test
    fun `given service returns error response then getCardBin returns Error`() = runTest {
        val errorBody = """{"message":"Not Found","code":"404"}""".toResponseBody()
        coEvery {
            service.getCardBin(any(), any(), any(), any(), any(), any(), any())
        } returns Response.error(404, errorBody)

        val result = dataSource.getCardBin(cardBinRequest)

        val error = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("404", error.error.code)
        assertEquals(404, error.error.httpStatus)
    }

    @Test
    fun `given getCardBin is called then passes all request fields to service`() = runTest {
        val body = mockk<CardBinResponse>(relaxed = true)
        coEvery {
            service.getCardBin(any(), any(), any(), any(), any(), any(), any())
        } returns Response.success(body)

        dataSource.getCardBin(cardBinRequest)

        coVerify {
            service.getCardBin(
                any(),
                bin = cardBinRequest.bin,
                amount = cardBinRequest.amount,
                checkoutType = cardBinRequest.checkoutType,
                processingMode = cardBinRequest.processingMode,
                excludedPaymentTypes = cardBinRequest.excludedPaymentTypes,
                excludedPaymentMethods = cardBinRequest.excludedPaymentMethods,
            )
        }
    }
}
