package com.mercadopago.sdk.android.checkout.domain.usecase

import com.mercadopago.sdk.android.checkout.domain.model.CardBinData
import com.mercadopago.sdk.android.checkout.domain.model.MercadoPagoCheckoutError
import com.mercadopago.sdk.android.checkout.domain.model.ResponseError
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
    private val filter = CardBinFilter(excludedPaymentTypes = emptyList(), excludedPaymentMethods = emptyList())
    private val cardBinData = mockk<CardBinData>()
    private val error = ResponseError(code = "ERROR", message = "Error")

    private suspend fun invokeUseCase(
        orderId: String = "",
        clientToken: String? = null,
        screens: String? = null,
    ) = useCase(
        bin = bin,
        orderId = orderId,
        clientToken = clientToken,
        amount = amount,
        checkoutType = checkoutType,
        processingMode = processingMode,
        filter = filter,
        screens = screens,
    )

    @Test
    fun `given repository returns success then returns Result Success`() = runTest {
        coEvery { repository.getCardBin(any()) } returns Result.Success(cardBinData)

        val result = invokeUseCase()

        assertIs<Result.Success<CardBinData>>(result)
        assertEquals(cardBinData, result.data)
    }

    @Test
    fun `given repository returns error then returns Result Error`() = runTest {
        coEvery { repository.getCardBin(any()) } returns Result.Error(error)

        val result = invokeUseCase()

        assertIs<Result.Error<MercadoPagoCheckoutError>>(result)
    }

    @Test
    fun `given null amount then defaults to zero string`() = runTest {
        val paramsSlot = slot<GetCardBinParams>()
        coEvery { repository.getCardBin(capture(paramsSlot)) } returns Result.Success(cardBinData)

        useCase(bin = bin, amount = null, checkoutType = checkoutType, processingMode = processingMode, filter = filter)

        assertEquals("0", paramsSlot.captured.amount)
    }

    @Test
    fun `given no order id then passes an empty order id to the repository`() = runTest {
        val paramsSlot = slot<GetCardBinParams>()
        coEvery { repository.getCardBin(capture(paramsSlot)) } returns Result.Success(cardBinData)

        invokeUseCase()

        assertEquals("", paramsSlot.captured.orderId)
    }

    @Test
    fun `given order id then passes it to the repository`() = runTest {
        val paramsSlot = slot<GetCardBinParams>()
        coEvery { repository.getCardBin(capture(paramsSlot)) } returns Result.Success(cardBinData)

        invokeUseCase(orderId = "ORDER_123")

        assertEquals("ORDER_123", paramsSlot.captured.orderId)
    }

    @Test
    fun `given no client token then passes null to the repository`() = runTest {
        val paramsSlot = slot<GetCardBinParams>()
        coEvery { repository.getCardBin(capture(paramsSlot)) } returns Result.Success(cardBinData)

        invokeUseCase()

        assertEquals(null, paramsSlot.captured.clientToken)
    }

    @Test
    fun `given client token then passes it to the repository`() = runTest {
        val paramsSlot = slot<GetCardBinParams>()
        coEvery { repository.getCardBin(capture(paramsSlot)) } returns Result.Success(cardBinData)

        invokeUseCase(clientToken = "token-abc")

        assertEquals("token-abc", paramsSlot.captured.clientToken)
    }

    @Test
    fun `given invoke then passes correct params to repository`() = runTest {
        val paramsSlot = slot<GetCardBinParams>()
        coEvery { repository.getCardBin(capture(paramsSlot)) } returns Result.Success(cardBinData)

        invokeUseCase()

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

        invokeUseCase()

        coVerify(exactly = 1) { repository.getCardBin(any()) }
    }

    @Test
    fun `given screens then passes it to the repository`() = runTest {
        // Given
        val paramsSlot = slot<GetCardBinParams>()
        coEvery { repository.getCardBin(capture(paramsSlot)) } returns Result.Success(cardBinData)

        // When
        invokeUseCase(screens = "REVIEW_AND_CONFIRM")

        // Then
        assertEquals("REVIEW_AND_CONFIRM", paramsSlot.captured.screens)
    }

    @Test
    fun `given no screens then passes null to the repository`() = runTest {
        // Given
        val paramsSlot = slot<GetCardBinParams>()
        coEvery { repository.getCardBin(capture(paramsSlot)) } returns Result.Success(cardBinData)

        // When
        invokeUseCase()

        // Then
        assertEquals(null, paramsSlot.captured.screens)
    }
}
