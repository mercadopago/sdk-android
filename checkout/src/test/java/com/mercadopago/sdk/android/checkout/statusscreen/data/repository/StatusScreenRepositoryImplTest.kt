package com.mercadopago.sdk.android.checkout.statusscreen.data.repository

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.datasource.StatusScreenRemoteDataSource
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.request.StatusScreenRequest
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenBodyDataResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenBodyNodeResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenButtonResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenFooterResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenHeaderResponse
import com.mercadopago.sdk.android.checkout.statusscreen.data.remote.response.StatusScreenResponse
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData
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

internal class StatusScreenRepositoryImplTest {
    private val dataSource = mockk<StatusScreenRemoteDataSource>()
    private val repository = StatusScreenRepositoryImpl(dataSource)

    private val sellerInfo = MPSellerInfo(name = "Test Store", logoUrl = "https://example.com/logo.png")

    private fun validResponse() = StatusScreenResponse(
        statusType = "approved",
        header = StatusScreenHeaderResponse(title = "Pagaste ($ 900)", icon = "https://example.com/icon.png"),
        body = listOf(
            StatusScreenBodyNodeResponse(
                component = "MPListItem",
                data = StatusScreenBodyDataResponse(
                    title = "Seller name",
                    subtitle = null,
                    imageUrl = null,
                    leadingType = null,
                    leadingValue = null,
                    content = null,
                    codeFormatted = null,
                    copyLabel = null,
                    copyFeedback = null,
                    imageDataUri = null,
                ),
            ),
            StatusScreenBodyNodeResponse(
                component = "MPListItem",
                data = StatusScreenBodyDataResponse(
                    title = "Visa •••• 1234",
                    subtitle = "$ 900",
                    imageUrl = null,
                    leadingType = "icon",
                    leadingValue = "card",
                    content = null,
                    codeFormatted = null,
                    copyLabel = null,
                    copyFeedback = null,
                    imageDataUri = null,
                ),
            ),
        ),
        footer = StatusScreenFooterResponse(
            buttons = listOf(StatusScreenButtonResponse(label = "Volver", action = "back_action", receiptUrl = null)),
        ),
    )

    @Test
    fun `given dataSource returns a valid response then getStatusScreen returns Result Success`() = runTest {
        // Given
        coEvery { dataSource.fetch(any(), any(), any()) } returns Result.Success(validResponse())

        // When
        val result = repository.getStatusScreen(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = "1234",
            sellerInfo = sellerInfo,
        )

        // Then
        assertIs<Result.Success<StatusScreenViewData>>(result)
    }

    @Test
    fun `given dataSource returns an invalid response then getStatusScreen returns invalid response error`() =
        runTest {
            // Given
            val invalidResponse = validResponse().copy(statusType = "something_else")
            coEvery { dataSource.fetch(any(), any(), any()) } returns Result.Success(invalidResponse)

            // When
            val result = repository.getStatusScreen(
                clientToken = "token-abc",
                orderId = "order-123",
                lastFourDigits = null,
                sellerInfo = null,
            )

            // Then
            val error = assertIs<Result.Error<ResponseError>>(result).error
            assertEquals("INVALID_STATUS_SCREEN_RESPONSE", error.code)
        }

    @Test
    fun `given dataSource returns an error then getStatusScreen returns Result Error`() = runTest {
        // Given
        val error = ResponseError(code = "404", message = "Not Found", httpStatus = 404)
        coEvery { dataSource.fetch(any(), any(), any()) } returns Result.Error(error)

        // When
        val result = repository.getStatusScreen(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = null,
            sellerInfo = null,
        )

        // Then
        assertEquals(error, assertIs<Result.Error<ResponseError>>(result).error)
    }

    @Test
    fun `given dataSource throws then getStatusScreen returns Result Error`() = runTest {
        // Given
        coEvery { dataSource.fetch(any(), any(), any()) } throws RuntimeException("Network failure")

        // When
        val result = repository.getStatusScreen(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = null,
            sellerInfo = null,
        )

        // Then
        assertIs<Result.Error<ResponseError>>(result)
    }

    @Test
    fun `getStatusScreen maps orderId and lastFourDigits into the request body`() = runTest {
        // Given
        val requestSlot = slot<StatusScreenRequest>()
        coEvery { dataSource.fetch(any(), capture(requestSlot)) } returns Result.Success(validResponse())

        // When
        repository.getStatusScreen(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = "1234",
            sellerInfo = null,
        )

        // Then
        assertEquals("order-123", requestSlot.captured.orderId)
        assertEquals("1234", requestSlot.captured.lastFourDigits)
    }

    @Test
    fun `getStatusScreen omits lastFourDigits when it is not exactly four ASCII digits`() = runTest {
        // Given
        val requestSlot = slot<StatusScreenRequest>()
        coEvery { dataSource.fetch(any(), capture(requestSlot)) } returns Result.Success(validResponse())

        // When
        repository.getStatusScreen(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = "eyJhbGciOiJSUzI1NiJ9.not-a-card-suffix",
            sellerInfo = null,
        )

        // Then
        assertNull(requestSlot.captured.lastFourDigits)
    }

    @Test
    fun `getStatusScreen maps sellerInfo name and logoUrl into seller_info icon_url`() = runTest {
        // Given
        val requestSlot = slot<StatusScreenRequest>()
        coEvery { dataSource.fetch(any(), capture(requestSlot)) } returns Result.Success(validResponse())

        // When
        repository.getStatusScreen(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = null,
            sellerInfo = sellerInfo,
        )

        // Then
        assertEquals("Test Store", requestSlot.captured.sellerInfo?.name)
        assertEquals("https://example.com/logo.png", requestSlot.captured.sellerInfo?.iconUrl)
    }

    @Test
    fun `getStatusScreen sets sellerInfo to null when sellerInfo parameter is null`() = runTest {
        // Given
        val requestSlot = slot<StatusScreenRequest>()
        coEvery { dataSource.fetch(any(), capture(requestSlot)) } returns Result.Success(validResponse())

        // When
        repository.getStatusScreen(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = null,
            sellerInfo = null,
        )

        // Then
        assertNull(requestSlot.captured.sellerInfo)
    }

    @Test
    fun `getStatusScreen delegates clientToken to dataSource`() = runTest {
        // Given
        val clientTokenSlot = slot<String>()
        coEvery { dataSource.fetch(capture(clientTokenSlot), any()) } returns Result.Success(validResponse())

        // When
        repository.getStatusScreen(
            clientToken = "token-abc",
            orderId = "order-123",
            lastFourDigits = null,
            sellerInfo = null,
        )

        // Then
        coVerify(exactly = 1) { dataSource.fetch(clientToken = "token-abc", statusScreenRequest = any()) }
        assertEquals("token-abc", clientTokenSlot.captured)
    }
}
