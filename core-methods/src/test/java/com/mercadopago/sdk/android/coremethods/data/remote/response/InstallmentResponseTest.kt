package com.mercadopago.sdk.android.coremethods.data.remote.response

import org.junit.Assert.assertEquals
import kotlin.test.Test

internal class InstallmentResponseTest {
    @Test
    fun testInstallmentsResponseDefaultValues() {
        val response = InstallmentsResponse()

        assertEquals(null, response.paymentMethodId)
        assertEquals(null, response.paymentTypeId)
        assertEquals(null, response.issuer)
        assertEquals(null, response.processingMode)
        assertEquals(null, response.merchantAccountId)
        assertEquals(null, response.payerCost)
        assertEquals(null, response.agreements)
    }

    @Test
    fun testInstallmentsResponseAllProperties() {
        val issuer = IssuerResponse("issuer_id", "http://image.png")
        val payerCostList = listOf(
            PayerCostResponse(
                instalments = 3,
                installmentAmount = 1000,
                instalmentsRate = 1.5f,
                installmentRateCollector = listOf("Collective 1"),
                totalAmount = 3000f,
                minAllowedAmount = 500f,
                maxAllowedAmount = 3000f,
                discountRate = 0.0f,
                reimbursementRate = 0.0f,
                labels = listOf("Label 1"),
                paymentMethodOptionId = "option_id_1",
            ),
        )

        val agreementsList = listOf(
            AgreementsResponse(
                merchantAccount = listOf(MerchantAccountResponse("merchant_id", "option_id_2")),
                timeFrame = TimeFrameResponse("2023-01-01", "2023-12-31"),
            ),
        )

        val response = InstallmentsResponse(
            paymentMethodId = "payment_method_id",
            paymentTypeId = "payment_type_id",
            issuer = issuer,
            processingMode = "processing_mode",
            merchantAccountId = "merchant_account_id",
            payerCost = payerCostList,
            agreements = agreementsList,
        )

        assertEquals("payment_method_id", response.paymentMethodId)
        assertEquals("payment_type_id", response.paymentTypeId)
        assertEquals(issuer, response.issuer)
        assertEquals("processing_mode", response.processingMode)
        assertEquals("merchant_account_id", response.merchantAccountId)
        assertEquals(payerCostList, response.payerCost)
        assertEquals(agreementsList, response.agreements)
    }

    @Test
    fun testIssuerDefaultValues() {
        val issuer = IssuerResponse()

        assertEquals(null, issuer.id)
        assertEquals(null, issuer.thumbnail)
    }

    @Test
    fun testIssuerAllProperties() {
        val issuer = IssuerResponse("issuer_id", "http://image.png")

        assertEquals("issuer_id", issuer.id)
        assertEquals("http://image.png", issuer.thumbnail)
    }

    @Test
    fun testPayerCostDefaultValues() {
        val payerCost = PayerCostResponse()

        assertEquals(null, payerCost.instalments)
        assertEquals(null, payerCost.installmentAmount)
        assertEquals(null, payerCost.instalmentsRate)
        assertEquals(null, payerCost.installmentRateCollector)
        assertEquals(null, payerCost.totalAmount)
        assertEquals(null, payerCost.minAllowedAmount)
        assertEquals(null, payerCost.maxAllowedAmount)
        assertEquals(null, payerCost.discountRate)
        assertEquals(null, payerCost.reimbursementRate)
        assertEquals(null, payerCost.labels)
        assertEquals(null, payerCost.paymentMethodOptionId)
    }

    @Test
    fun testAgreementsDefaultValues() {
        val agreements = AgreementsResponse()

        assertEquals(null, agreements.merchantAccount)
        assertEquals(null, agreements.timeFrame)
    }

    @Test
    fun testMerchantAccountDefaultValues() {
        val merchantAccount = MerchantAccountResponse()

        assertEquals(null, merchantAccount.id)
        assertEquals(null, merchantAccount.paymentMethodOptionId)
    }

    @Test
    fun testTimeFrameDefaultValues() {
        val timeFrame = TimeFrameResponse()

        assertEquals(null, timeFrame.startDate)
        assertEquals(null, timeFrame.endDate)
    }
}
