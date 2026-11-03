package com.mercadopago.sdk.android.checkout.statusscreen.domain.usecase

import com.mercadopago.sdk.android.checkout.core.model.MPSellerInfo
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.statusscreen.domain.model.StatusScreenViewData
import com.mercadopago.sdk.android.checkout.statusscreen.domain.repository.StatusScreenRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class GetStatusScreenUseCaseTest {
    private val repository = mockk<StatusScreenRepository>()
    private val useCase = GetStatusScreenUseCase(repository)

    private val sellerInfo = MPSellerInfo(name = "Test Store", logoUrl = "https://example.com/logo.png")

    @Test
    fun `given repository returns success then returns Result Success with StatusScreenViewData`() = runTest {
        // Given
        val viewData = mockk<StatusScreenViewData>(relaxed = true)
        coEvery { repository.getStatusScreen(any(), any(), any(), any(), any()) } returns Result.Success(viewData)

        // When
        val result = useCase(clientToken = "token-abc", orderId = "order-123")

        // Then
        assertEquals(viewData, assertIs<Result.Success<StatusScreenViewData>>(result).data)
    }

    @Test
    fun `given repository returns an error then returns the same ResponseError`() = runTest {
        // Given
        val error = ResponseError(code = "bad_request", message = "Invalid order", httpStatus = 400)
        coEvery { repository.getStatusScreen(any(), any(), any(), any(), any()) } returns Result.Error(error)

        // When
        val result = useCase(clientToken = "token-abc", orderId = "order-123")

        // Then
        assertEquals(error, assertIs<Result.Error<ResponseError>>(result).error)
    }

    @Test
    fun `given a client error then getStatusScreen is called exactly once — no retry`() = runTest {
        // Given
        val error = ResponseError(code = "bad_request", message = "Invalid order", httpStatus = 400)
        coEvery { repository.getStatusScreen(any(), any(), any(), any(), any()) } returns Result.Error(error)

        // When
        useCase(clientToken = "token-abc", orderId = "order-123")

        // Then
        coVerify(exactly = 1) { repository.getStatusScreen(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `given invoke is called then delegates every request parameter to repository`() =
        runTest {
            // Given
            coEvery { repository.getStatusScreen(any(), any(), any(), any(), any()) } returns
                Result.Success(mockk(relaxed = true))

            // When
            useCase(
                clientToken = "token-abc",
                orderId = "order-123",
                lastFourDigits = "1234",
                sellerInfo = sellerInfo,
                checkoutType = "payment",
            )

            // Then
            coVerify {
                repository.getStatusScreen(
                    clientToken = "token-abc",
                    orderId = "order-123",
                    lastFourDigits = "1234",
                    sellerInfo = sellerInfo,
                    checkoutType = "payment",
                )
            }
        }
}
