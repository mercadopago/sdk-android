package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.data.remote.datasource.ReviewConfirmRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.request.ReviewConfirmRequest
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmResponse
import com.mercadopago.sdk.android.checkout.domain.model.ReviewConfirmViewData
import com.mercadopago.sdk.android.checkout.domain.model.params.ProcessOrderParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class ReviewConfirmRepositoryImplTest {
    private val dataSource = mockk<ReviewConfirmRemoteDataSource>()
    private val repository = ReviewConfirmRepositoryImpl(dataSource)

    private val processOrderParams = ProcessOrderParams(
        orderId = "order-123",
        clientToken = "token-abc",
        paymentMethodId = "visa",
        paymentMethodType = "credit_card",
        token = "card-token-xyz",
        installments = 3,
        amount = "30000",
        bin = "411111",
    )

    private val sellerInfo = MPSellerInfo(
        name = "Test Store",
        logoUrl = "https://example.com/logo.png",
    )

    @Test
    fun `given dataSource returns success then fetchReviewConfirm returns Result Success`() = runTest {
        val response = mockk<ReviewConfirmResponse>(relaxed = true)
        coEvery {
            dataSource.fetch(any(), any())
        } returns Result.Success(response)

        val result = repository.fetchReviewConfirm(processOrderParams, true, sellerInfo)

        assertIs<Result.Success<ReviewConfirmViewData>>(result)
    }

    @Test
    fun `given dataSource returns error then fetchReviewConfirm returns Result Error`() = runTest {
        val error = ResponseError(code = "404", message = "Not Found", httpStatus = 404)
        coEvery {
            dataSource.fetch(any(), any())
        } returns Result.Error(error)

        val result = repository.fetchReviewConfirm(processOrderParams, true, sellerInfo)

        assertIs<Result.Error<*>>(result)
    }

    @Test
    fun `given dataSource throws then fetchReviewConfirm returns Result Error`() = runTest {
        coEvery {
            dataSource.fetch(any(), any())
        } throws RuntimeException("Network failure")

        val result = repository.fetchReviewConfirm(processOrderParams, true, sellerInfo)

        assertIs<Result.Error<*>>(result)
    }

    @Test
    fun `fetchReviewConfirm maps orderId correctly`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, null)

        assertEquals("order-123", requestSlot.captured.orderId)
    }

    @Test
    fun `fetchReviewConfirm maps paymentMethodId correctly`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, null)

        assertEquals("visa", requestSlot.captured.paymentMethodId)
    }

    @Test
    fun `fetchReviewConfirm maps paymentMethodType correctly`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, null)

        assertEquals("credit_card", requestSlot.captured.paymentMethodType)
    }

    @Test
    fun `fetchReviewConfirm maps bin correctly`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, null)

        assertEquals("411111", requestSlot.captured.bin)
    }

    @Test
    fun `fetchReviewConfirm maps installments correctly`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, null)

        assertEquals(3, requestSlot.captured.installments)
    }

    @Test
    fun `fetchReviewConfirm maps installmentAmount from amount`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, null)

        assertEquals("30000", requestSlot.captured.installmentAmount)
    }

    @Test
    fun `fetchReviewConfirm sets issuerId to null`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, null)

        assertNull(requestSlot.captured.issuerId)
    }

    @Test
    fun `fetchReviewConfirm sets productId to null`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, null)

        assertNull(requestSlot.captured.productId)
    }

    @Test
    fun `fetchReviewConfirm sets lastFourDigits to null`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, null)

        assertNull(requestSlot.captured.lastFourDigits)
    }

    @Test
    fun `fetchReviewConfirm maps emailChangeEnabled to true`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, emailChangeEnabled = true, sellerInfo = null)

        assertEquals(true, requestSlot.captured.emailChangeEnabled)
    }

    @Test
    fun `fetchReviewConfirm maps emailChangeEnabled to false`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, emailChangeEnabled = false, sellerInfo = null)

        assertEquals(false, requestSlot.captured.emailChangeEnabled)
    }

    @Test
    fun `fetchReviewConfirm sets sellerInfo to null when sellerInfo parameter is null`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, sellerInfo = null)

        assertNull(requestSlot.captured.sellerInfo)
    }

    @Test
    fun `fetchReviewConfirm maps sellerInfo name and logoUrl when sellerInfo is provided`() = runTest {
        val requestSlot = slot<ReviewConfirmRequest>()
        coEvery {
            dataSource.fetch(any(), capture(requestSlot))
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, sellerInfo)

        assertEquals("Test Store", requestSlot.captured.sellerInfo?.name)
        assertEquals("https://example.com/logo.png", requestSlot.captured.sellerInfo?.iconUrl)
    }

    @Test
    fun `fetchReviewConfirm delegates clientToken to dataSource correctly`() = runTest {
        val clientTokenSlot = slot<String>()
        coEvery {
            dataSource.fetch(capture(clientTokenSlot), any())
        } returns Result.Success(mockk(relaxed = true))

        repository.fetchReviewConfirm(processOrderParams, true, null)

        coVerify(exactly = 1) {
            dataSource.fetch(
                clientToken = "token-abc",
                reviewConfirmRequest = any(),
            )
        }
        assertEquals("token-abc", clientTokenSlot.captured)
    }
}
