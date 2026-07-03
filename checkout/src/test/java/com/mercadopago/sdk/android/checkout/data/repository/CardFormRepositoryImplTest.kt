package com.mercadopago.sdk.android.checkout.data.repository

import com.mercadopago.sdk.android.checkout.data.remote.datasource.CardFormRemoteDataSource
import com.mercadopago.sdk.android.checkout.data.remote.request.CardBinRequest
import com.mercadopago.sdk.android.checkout.data.remote.response.CardBinResponse
import com.mercadopago.sdk.android.checkout.data.remote.response.CardFormInitResponse
import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.CardFormInitializationOutput
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
import com.mercadopago.sdk.android.checkout.domain.model.params.GetCardBinParams
import com.mercadopago.sdk.android.checkout.domain.model.params.InitializeCardFormParams
import com.mercadopago.sdk.android.checkout.domain.usecase.CardBinFilter
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

internal class CardFormRepositoryImplTest {
    private val dataSource = mockk<CardFormRemoteDataSource>()
    private val repository = CardFormRepositoryImpl(dataSource)

    private val initParams = InitializeCardFormParams(
        checkoutType = "card_payment",
    )

    private val binParams = GetCardBinParams(
        bin = "412345",
        amount = "150.00",
        checkoutType = "card_payment",
        processingMode = "aggregator",
        filter = CardBinFilter(excludedPaymentTypes = emptyList(), excludedPaymentMethods = emptyList()),
    )

    @Test
    fun `given dataSource returns success then fetchInitialization returns Result Success`() = runTest {
        val response = mockk<CardFormInitResponse>(relaxed = true)
        coEvery {
            dataSource.fetchInitialization(
                checkoutType = initParams.checkoutType,
            )
        } returns Result.Success(response)

        val result = repository.fetchInitialization(initParams)

        assertIs<Result.Success<CardFormInitializationOutput>>(result)
    }

    @Test
    fun `given dataSource returns error then fetchInitialization returns Result Error`() = runTest {
        val error = ResponseError(code = "404", message = "Not Found", httpStatus = 404)
        coEvery {
            dataSource.fetchInitialization(any())
        } returns Result.Error(error)

        val result = repository.fetchInitialization(initParams)

        assertIs<Result.Error<*>>(result)
    }

    @Test
    fun `given dataSource throws then fetchInitialization returns Result Error`() = runTest {
        coEvery {
            dataSource.fetchInitialization(any())
        } throws RuntimeException("Network failure")

        val result = repository.fetchInitialization(initParams)

        assertIs<Result.Error<*>>(result)
    }

    @Test
    fun `given fetchInitialization then delegates to dataSource with correct params`() = runTest {
        val response = mockk<CardFormInitResponse>(relaxed = true)
        coEvery {
            dataSource.fetchInitialization(
                checkoutType = initParams.checkoutType,
            )
        } returns Result.Success(response)

        repository.fetchInitialization(initParams)

        coVerify(exactly = 1) {
            dataSource.fetchInitialization(
                checkoutType = initParams.checkoutType,
            )
        }
    }

    @Test
    fun `given dataSource returns success then getCardBin returns Result Success`() = runTest {
        val response = mockk<CardBinResponse>(relaxed = true)
        coEvery { dataSource.getCardBin(any()) } returns Result.Success(response)

        val result = repository.getCardBin(binParams)

        assertIs<Result.Success<CardBinData>>(result)
    }

    @Test
    fun `given dataSource returns error then getCardBin returns Result Error`() = runTest {
        val error = ResponseError(code = "400", message = "Bad Request", httpStatus = 400)
        coEvery { dataSource.getCardBin(any()) } returns Result.Error(error)

        val result = repository.getCardBin(binParams)

        assertIs<Result.Error<*>>(result)
    }

    @Test
    fun `given dataSource throws then getCardBin returns Result Error`() = runTest {
        coEvery { dataSource.getCardBin(any()) } throws RuntimeException("Timeout")

        val result = repository.getCardBin(binParams)

        assertIs<Result.Error<*>>(result)
    }

    @Test
    fun `given empty filter then excludedPaymentTypes and excludedPaymentMethods are null`() = runTest {
        val requestSlot = slot<CardBinRequest>()
        coEvery { dataSource.getCardBin(capture(requestSlot)) } returns Result.Success(
            mockk(relaxed = true),
        )

        repository.getCardBin(binParams)

        assertNull(requestSlot.captured.excludedPaymentTypes)
        assertNull(requestSlot.captured.excludedPaymentMethods)
    }

    @Test
    fun `given getCardBin then delegates bin and amount to dataSource correctly`() = runTest {
        val requestSlot = slot<CardBinRequest>()
        coEvery { dataSource.getCardBin(capture(requestSlot)) } returns Result.Success(
            mockk(relaxed = true),
        )

        repository.getCardBin(binParams)

        assertEquals(binParams.bin, requestSlot.captured.bin)
        assertEquals(binParams.amount, requestSlot.captured.amount)
        assertEquals(binParams.checkoutType, requestSlot.captured.checkoutType)
        assertEquals(binParams.processingMode, requestSlot.captured.processingMode)
    }
}
