package com.mercadopago.sdk.android.coremethods.domain.usecase.model

import com.mercadopago.sdk.android.coremethods.domain.model.Agreements
import com.mercadopago.sdk.android.coremethods.domain.model.Installment
import com.mercadopago.sdk.android.coremethods.domain.model.Issuer
import com.mercadopago.sdk.android.coremethods.domain.model.MerchantAccount
import com.mercadopago.sdk.android.coremethods.domain.model.PayerCost
import com.mercadopago.sdk.android.coremethods.domain.model.ProcessingMode
import com.mercadopago.sdk.android.coremethods.domain.model.TimeFrame
import kotlin.test.Test
import kotlin.test.assertEquals

class InstallmentModelTest {
    @Test
    fun testInstallmentDefaultValues() {
        val installment = Installment()

        assertEquals(null, installment.paymentMethodId)
        assertEquals(null, installment.paymentTypeId)
        assertEquals(null, installment.issuer)
        assertEquals(null, installment.processingMode)
        assertEquals(null, installment.merchantAccountId)
        assertEquals(null, installment.payerCost)
        assertEquals(null, installment.agreements)
    }

    @Test
    fun testInstallmentAllProperties() {
        val issuer = Issuer("issuer_id", "http://image.png")
        val payerCost = listOf(
            PayerCost(
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

        val agreements = listOf(
            Agreements(
                merchantAccount = listOf(MerchantAccount("merchant_id", "option_id_2")),
                timeFrame = TimeFrame("2023-01-01", "2023-12-31"),
            ),
        )

        val installment = Installment(
            paymentMethodId = "payment_method_id",
            paymentTypeId = "payment_type_id",
            issuer = issuer,
            processingMode = "processing_mode",
            merchantAccountId = "merchant_account_id",
            payerCost = payerCost,
            agreements = agreements,
        )

        assertEquals("payment_method_id", installment.paymentMethodId)
        assertEquals("payment_type_id", installment.paymentTypeId)
        assertEquals(issuer, installment.issuer)
        assertEquals("processing_mode", installment.processingMode)
        assertEquals("merchant_account_id", installment.merchantAccountId)
        assertEquals(payerCost, installment.payerCost)
        assertEquals(agreements, installment.agreements)
    }

    @Test
    fun testIssuerDefaultValues() {
        val issuer = Issuer()

        assertEquals(null, issuer.id)
        assertEquals(null, issuer.thumbnail)
    }

    @Test
    fun testIssuerAllProperties() {
        val issuer = Issuer("issuer_id", "http://image.png")

        assertEquals("issuer_id", issuer.id)
        assertEquals("http://image.png", issuer.thumbnail)
    }

    @Test
    fun testPayerCostDefaultValues() {
        val payerCost = PayerCost()

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
        val agreements = Agreements()

        assertEquals(null, agreements.merchantAccount)
        assertEquals(null, agreements.timeFrame)
    }

    @Test
    fun testMerchantAccountDefaultValues() {
        val merchantAccount = MerchantAccount()

        assertEquals(null, merchantAccount.id)
        assertEquals(null, merchantAccount.paymentMethodOptionId)
    }

    @Test
    fun testTimeFrameDefaultValues() {
        val timeFrame = TimeFrame()

        assertEquals(null, timeFrame.startDate)
        assertEquals(null, timeFrame.endDate)
    }

    @Test
    fun testProcessingModeEnum() {
        assertEquals("aggregator", ProcessingMode.Aggregator.mode)
        assertEquals("gateway", ProcessingMode.Gateway.mode)
    }
}
