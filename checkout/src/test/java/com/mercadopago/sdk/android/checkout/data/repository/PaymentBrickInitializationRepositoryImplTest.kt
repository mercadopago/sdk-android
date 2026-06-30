package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.data.remote.datasource.PaymentBrickInitializationRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickFooter
import com.mercadopago.sdk.android.checkout.data.remote.response.PaymentBrickInitializationResponse
import com.mercadopago.sdk.android.checkout.domain.model.PaymentBrickInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.FetchPaymentBrickInitializationParams
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class PaymentBrickInitializationRepositoryImplTest {
    private val dataSource = mockk<PaymentBrickInitializationRemoteDataSource>()
    private val repository = PaymentBrickInitializationRepositoryImpl(dataSource)

    private val params = FetchPaymentBrickInitializationParams(
        orderId = "ORDER_123",
        totalAmount = "500.00",
        customerId = null,
        cardIds = null,
    )

    private fun minimalResponse() = PaymentBrickInitializationResponse(
        headerTitle = "Elegí cómo pagar",
        sections = emptyList(),
        footer = PaymentBrickFooter(totalLabel = "Total", totalAmount = "$ 500"),
    )

    @Test
    fun `given dataSource returns success then returns Result Success with mapped output`() = runTest {
        coEvery { dataSource.fetch(params) } returns Result.Success(minimalResponse())

        val result = repository.fetch(params)

        val success = assertIs<Result.Success<PaymentBrickInitializationOutput>>(result)
        assertEquals("Elegí cómo pagar", success.data.headerTitle)
        assertEquals("Total", success.data.footer.totalLabel)
        assertEquals("$ 500", success.data.footer.totalAmount)
    }

    @Test
    fun `given dataSource returns error then returns Result Error with same ResponseError`() = runTest {
        val error = ResponseError(code = "500", message = "Internal Server Error", httpStatus = 500)
        coEvery { dataSource.fetch(params) } returns Result.Error(error)

        val result = repository.fetch(params)

        val resultError = assertIs<Result.Error<ResponseError>>(result)
        assertEquals("500", resultError.error.code)
        assertEquals(500, resultError.error.httpStatus)
    }

    @Test
    fun `given dataSource throws exception then withErrorHandling catches it and returns Result Error`() = runTest {
        coEvery { dataSource.fetch(params) } throws RuntimeException("Network failure")

        val result = repository.fetch(params)

        assertIs<Result.Error<ResponseError>>(result)
    }

    @Test
    fun `given fetch is called then delegates params to dataSource`() = runTest {
        coEvery { dataSource.fetch(params) } returns Result.Success(minimalResponse())

        repository.fetch(params)

        coVerify(exactly = 1) { dataSource.fetch(params) }
    }

    @Test
    fun `given dataSource returns success with sections then sections are mapped and passed through`() = runTest {
        coEvery { dataSource.fetch(params) } returns Result.Success(minimalResponse())

        val result = repository.fetch(params)

        val success = assertIs<Result.Success<PaymentBrickInitializationOutput>>(result)
        assertEquals(emptyList(), success.data.sections)
    }
}
