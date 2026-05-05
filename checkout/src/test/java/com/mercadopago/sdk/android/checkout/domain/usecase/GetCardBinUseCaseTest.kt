package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.params.GetCardBinParams
import com.mercadopago.sdk.android.checkout.domain.repository.CardFormRepository
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

internal class GetCardBinUseCaseTest {
    private val repository = mockk<CardFormRepository>()
    private val useCase = GetCardBinUseCase(repository)

    private val bin = "412345"
    private val amount = "100.00"
    private val checkoutType = "card_payment"
    private val processingMode = "aggregator"
    private val filter = CardBinFilter(cardTypes = emptyList(), cardBrands = emptyList())
    private val cardBinData = mockk<CardBinData>()
    private val error = mockk<MercadoPagoCheckoutError>()

    @Test
    fun `given repository returns success then returns Result Success`() = runTest {
        coEvery { repository.getCardBin(any()) } returns Result.Success(cardBinData)

        val result = useCase(bin, amount, checkoutType, processingMode, filter)

        assertIs<Result.Success<CardBinData>>(result)
        assertEquals(cardBinData, result.data)
    }

    @Test
    fun `given repository returns error then returns Result Error`() = runTest {
        coEvery { repository.getCardBin(any()) } returns Result.Error(error)

        val result = useCase(bin, amount, checkoutType, processingMode, filter)

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
        assertEquals(error, result.error)
    }

    @Test
    fun `given null amount then defaults to zero string`() = runTest {
        val paramsSlot = slot<GetCardBinParams>()
        coEvery { repository.getCardBin(capture(paramsSlot)) } returns Result.Success(cardBinData)

        useCase(bin, null, checkoutType, processingMode, filter)

        assertEquals("0", paramsSlot.captured.amount)
    }

    @Test
    fun `given invoke then passes correct params to repository`() = runTest {
        val paramsSlot = slot<GetCardBinParams>()
        coEvery { repository.getCardBin(capture(paramsSlot)) } returns Result.Success(cardBinData)

        useCase(bin, amount, checkoutType, processingMode, filter)

        with(paramsSlot.captured) {
            assertEquals(bin, this.bin)
            assertEquals(amount, this.amount)
            assertEquals(checkoutType, this.checkoutType)
            assertEquals(processingMode, this.processingMode)
            assertEquals(filter, this.filter)
        }
    }

    @Test
    fun `given invoke then delegates to repository exactly once`() = runTest {
        coEvery { repository.getCardBin(any()) } returns Result.Success(cardBinData)

        useCase(bin, amount, checkoutType, processingMode, filter)

        coVerify(exactly = 1) { repository.getCardBin(any()) }
    }
}
