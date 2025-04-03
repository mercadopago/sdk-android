package com.mercadopago.sdk.android.coremethods.domain.interactor

import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.ResultError
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.Result
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.koin.core.Koin
import kotlin.test.Test

internal class CoreMethodsTest {

    private val koin: Koin = mockk(relaxed = true)
    private val coreMethods = CoreMethods(koin)
    private val mpAnalytics = mockk<MPAnalytics>(relaxed = true)

    @Before
    fun setup() {
        mockkObject(CoreKoinFactory)
        mockkStatic(MPAnalytics::class)
        mockkObject(MPAnalytics.Companion)

        every {
            CoreKoinFactory.createKoinApp(any(), any(), any())
        } returns koin

        every {
            MPAnalytics.getInstance()
        } returns mpAnalytics
    }

    @Test
    fun `generateCardToken should return success and track success metric`() = runTest {
        val cardNumberState = PCIFieldState()
        val expirationDateState = PCIFieldState()
        val securityCodeState = PCIFieldState()

        val expectedCardToken = CardToken("token_12345")

        val expectedResult = Result.Success(expectedCardToken)

        coEvery { koin.get<GenerateCardTokenUseCase>().invoke(any(), any(), any()) } returns expectedResult
        val result = coreMethods.generateCardToken(cardNumberState, expirationDateState, securityCodeState)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `generateCardToken should return error and track error metric`() = runTest {
        val cardNumberState = PCIFieldState()
        val expirationDateState = PCIFieldState()
        val securityCodeState = PCIFieldState()

        val expectedError = ResultError("400", "Invalid parameters")
        val expectedResult = Result.Error(expectedError)

        coEvery { koin.get<GenerateCardTokenUseCase>().invoke(any(), any(), any()) } returns expectedResult
        val result = coreMethods.generateCardToken(cardNumberState, expirationDateState, securityCodeState)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getInstallments should return success and track success metric`() = runTest {
        val bin = "123456"
        val amount = 1000L
        val expectedInstallment = Installment(paymentTypeId = "credit_card", merchantAccountId = "merchant_id")
        val expectedResult = Result.Success(expectedInstallment)

        coEvery { koin.get<GetInstallmentsUseCase>().invoke(bin, amount, any()) } returns expectedResult
        val result = coreMethods.getInstallments(bin, amount)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getInstallments should return error and track error metric`() = runTest {
        val bin = "123456"
        val amount = 1000L
        val expectedError = ResultError("404", "Installments not found")
        val expectedResult = Result.Error(expectedError)

        coEvery { koin.get<GetInstallmentsUseCase>().invoke(bin, amount, any()) } returns expectedResult
        val result = coreMethods.getInstallments(bin, amount)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getIdentificationTypes should track success metric when call is successful`() = runTest {
        val expectedTypes = listOf(
            IdentificationType(
                id = "id_1",
                name = "document",
                type = "RG",
                minLength = 6,
                maxLength = 15
            ),
            IdentificationType(
                id = "id_2",
                name = "document",
                type = "CPF",
                minLength = 5,
                maxLength = 20
            )
        )
        val expectedResult = Result.Success(expectedTypes)

        coEvery { koin.get<GetIdentificationTypesUseCase>().invoke() } returns expectedResult
        val result = coreMethods.getIdentificationTypes()

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getIdentificationTypes should track error metric when call fails`() = runTest {
        val expectedError = ResultError(code = "404", message = "Not Found")
        val expectedResult = Result.Error(expectedError)

        coEvery { koin.get<GetIdentificationTypesUseCase>().invoke() } returns expectedResult
        val result = coreMethods.getIdentificationTypes()

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getCardIssuers should return success and track success metric`() = runTest {
        val bin = 12345
        val paymentMethodId = "credit"

        val expectedCardIssuer = CardIssuer(status = "active", thumbnail = "www")
        val expectedResult = Result.Success(listOf(expectedCardIssuer))

        coEvery { koin.get<GetCardIssuersUseCase>().invoke(bin, paymentMethodId) } returns expectedResult
        val result = coreMethods.getCardIssuers(bin, paymentMethodId)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getCardIssuers should return error and track error metric`() = runTest {
        val bin = 12345
        val paymentMethodId = "credit"

        val expectedError = ResultError("404", "CardIssuer not found")
        val expectedResult = Result.Error(expectedError)

        coEvery { koin.get<GetCardIssuersUseCase>().invoke(bin, paymentMethodId) } returns expectedResult
        val result = coreMethods.getCardIssuers(bin, paymentMethodId)

        assertEquals(expectedResult, result)
    }
}
