package com.mercadopago.sdk.android.coremethods.domain.interactor

import android.app.Application
import android.content.pm.ApplicationInfo
import com.mercadopago.sdk.android.analytics.domain.interactor.MPAnalytics
import com.mercadopago.sdk.android.core.di.CoreKoinFactory
import com.mercadopago.sdk.android.coremethods.domain.model.BuyerIdentification
import com.mercadopago.sdk.android.coremethods.domain.model.CardIssuer
import com.mercadopago.sdk.android.coremethods.domain.model.CardToken
import com.mercadopago.sdk.android.coremethods.domain.model.IdentificationType
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.MPError
import com.mercadopago.sdk.android.coremethods.domain.model.PaymentMethod
import com.mercadopago.sdk.android.coremethods.domain.usecase.GenerateCardTokenUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetCardIssuersUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetIdentificationTypesUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetInstallmentsUseCase
import com.mercadopago.sdk.android.coremethods.domain.usecase.GetPaymentMethodsUseCase
import com.mercadopago.sdk.android.coremethods.domain.utils.MPResult
import com.mercadopago.sdk.android.coremethods.ui.components.textfield.pcitextfield.PCIFieldState
import com.mercadopago.sdk.android.initializer.MercadoPagoSDK
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
        mockkObject(MercadoPagoSDK.Companion)
        mockkStatic(ApplicationInfo::class)
        val context = mockk<Application>()
        every {
            context.applicationInfo
        } returns mockk(relaxed = true)
        every {
            MercadoPagoSDK.getInstance()
        } returns mockk(relaxed = true)

        every {
            CoreKoinFactory.createKoinApp(any(), any(), any())
        } returns koin

        every {
            MPAnalytics.getInstance()
        } returns mpAnalytics
    }

    @Test
    fun `generateCardToken should return success and track success metric`() =
        runTest {
            val cardNumberState = PCIFieldState()
            val expirationDateState = PCIFieldState()
            val securityCodeState = PCIFieldState()
            val buyerIdentification = BuyerIdentification(
                name = "APRO",
                number = "012345678909",
                type = "CPF"
            )

            val expectedCardToken = CardToken("token_12345")

            val expectedMPResult = MPResult.Success(expectedCardToken)

            coEvery {
                koin.get<GenerateCardTokenUseCase>().invoke(any(), any(), any(), any())
            } returns expectedMPResult
            val result =
                coreMethods.generateCardToken(
                    cardNumberState,
                    expirationDateState,
                    securityCodeState,
                    buyerIdentification
                )

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `generateCardToken should return error and track error metric`() =
        runTest {
            val cardNumberState = PCIFieldState()
            val expirationDateState = PCIFieldState()
            val securityCodeState = PCIFieldState()
            val buyerIdentification = BuyerIdentification(
                name = "",
                number = "",
                type = ""
            )

            val expectedError = MPError.Request(code = "400", message = "Invalid parameters")
            val expectedMPResult = MPResult.Error(expectedError)

            coEvery {
                koin.get<GenerateCardTokenUseCase>().invoke(any(), any(), any(), any())
            } returns expectedMPResult
            val result =
                coreMethods.generateCardToken(
                    cardNumberState,
                    expirationDateState,
                    securityCodeState,
                    buyerIdentification
                )

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `generateCardToken by cardId should return success and track success metric`() =
        runTest {
            val expirationDateState = PCIFieldState()
            val securityCodeState = PCIFieldState()
            val buyerIdentification = BuyerIdentification(
                name = "",
                number = "",
                type = ""
            )

            val expectedCardToken = CardToken("token_12345")

            val expectedMPResult = MPResult.Success(expectedCardToken)

            coEvery {
                koin.get<GenerateCardTokenUseCase>().invoke(any(), any(), any(), any())
            } returns expectedMPResult
            val result =
                coreMethods.generateCardToken(
                    "id",
                    expirationDateState,
                    securityCodeState,
                    buyerIdentification
                )

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `generateCardToken by cardId should return error and track error metric`() =
        runTest {
            val expirationDateState = PCIFieldState()
            val securityCodeState = PCIFieldState()
            val buyerIdentification = BuyerIdentification(
                name = "",
                number = "",
                type = ""
            )

            val expectedError = MPError.Request(code = "400", message = "Invalid parameters")
            val expectedMPResult = MPResult.Error(expectedError)

            coEvery {
                koin.get<GenerateCardTokenUseCase>().invoke(any(), any(), any(), any())
            } returns expectedMPResult
            val result =
                coreMethods.generateCardToken(
                    "id",
                    expirationDateState,
                    securityCodeState,
                    buyerIdentification
                )

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `getInstallments should return success and track success metric`() =
        runTest {
            val bin = "123456"
            val amount = 1000.0.toBigDecimal()
            val expectedInstallment =
                listOf(
                    Installment(
                        paymentTypeId = "credit_card",
                        merchantAccountId = "merchant_id"
                    )
                )
            val expectedMPResult = MPResult.Success(expectedInstallment)

            coEvery {
                koin.get<GetInstallmentsUseCase>().invoke(bin, amount, any())
            } returns expectedMPResult
            val result = coreMethods.getInstallments(bin, amount)

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `getInstallments should return error and track error metric`() =
        runTest {
            val bin = "123456"
            val amount = 1000.0.toBigDecimal()
            val expectedError =
                MPError.Request(code = "404", message = "Installments not found")
            val expectedMPResult = MPResult.Error(expectedError)

            coEvery {
                koin.get<GetInstallmentsUseCase>().invoke(bin, amount, any())
            } returns expectedMPResult
            val result = coreMethods.getInstallments(bin, amount)

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `getIdentificationTypes should track success metric when call is successful`() =
        runTest {
            val expectedTypes = listOf(
                IdentificationType(
                    id = "id_1",
                    name = "document",
                    type = "RG",
                    minLength = 6,
                    maxLength = 15,
                ),
                IdentificationType(
                    id = "id_2",
                    name = "document",
                    type = "CPF",
                    minLength = 5,
                    maxLength = 20,
                ),
            )
            val expectedMPResult = MPResult.Success(expectedTypes)

            coEvery { koin.get<GetIdentificationTypesUseCase>().invoke() } returns expectedMPResult
            val result = coreMethods.getIdentificationTypes()

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `getIdentificationTypes should track error metric when call fails`() =
        runTest {
            val expectedError = MPError.Request(code = "404", message = "Not Found")
            val expectedMPResult = MPResult.Error(expectedError)

            coEvery { koin.get<GetIdentificationTypesUseCase>().invoke() } returns expectedMPResult
            val result = coreMethods.getIdentificationTypes()

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `getCardIssuers should return success and track success metric`() =
        runTest {
            val bin = "12345"
            val paymentMethodId = "credit"

            val expectedCardIssuer = CardIssuer(status = "active", thumbnail = "www")
            val expectedMPResult = MPResult.Success(listOf(expectedCardIssuer))

            coEvery {
                koin.get<GetCardIssuersUseCase>().invoke(bin, paymentMethodId)
            } returns expectedMPResult
            val result = coreMethods.getCardIssuers(bin, paymentMethodId)

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `getCardIssuers should return error and track error metric`() =
        runTest {
            val bin = "12345"
            val paymentMethodId = "credit"

            val expectedError = MPError.Request(code = "404", message = "CardIssuer not found")
            val expectedMPResult = MPResult.Error(expectedError)

            coEvery {
                koin.get<GetCardIssuersUseCase>().invoke(bin, paymentMethodId)
            } returns expectedMPResult
            val result = coreMethods.getCardIssuers(bin, paymentMethodId)

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `getPaymentMethods should return success and track success metric`() =
        runTest {
            val bin = "12345"

            val expectedPaymentMethod = PaymentMethod(status = "active", thumbnail = "www")
            val expectedMPResult = MPResult.Success(listOf(expectedPaymentMethod))

            coEvery {
                koin.get<GetPaymentMethodsUseCase>().invoke(bin)
            } returns expectedMPResult
            val result = coreMethods.getPaymentMethods(bin)

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `getPaymentMethods should return error and track error metric`() =
        runTest {
            val bin = "12345"

            val expectedError = MPError.Request(code = "404", message = "CardIssuer not found")
            val expectedMPResult = MPResult.Error(expectedError)

            coEvery {
                koin.get<GetPaymentMethodsUseCase>().invoke(bin)
            } returns expectedMPResult
            val result = coreMethods.getPaymentMethods(bin)

            assertEquals(expectedMPResult, result)
        }

    @Test
    fun `generateCardToken with string should return success and track success metric`() =
        runTest {
            val cardNumber = "510000000"
            val expirationDate = "12/25"
            val securityCode = "123"
            val buyerIdentification = BuyerIdentification(
                name = "",
                number = "",
                type = ""
            )

            val expectedCardToken = CardToken("token_12345")

            val expectedMPResult = MPResult.Success(expectedCardToken)

            coEvery {
                koin.get<GenerateCardTokenUseCase>().invoke(any(), any(), any(), any())
            } returns expectedMPResult
            val result =
                coreMethods.generateCardToken(
                    cardNumber,
                    expirationDate,
                    securityCode,
                    buyerIdentification
                )

            assertEquals(expectedMPResult, result)
        }
}
