package com.mercadopago.sdk.android.coremethods.data.remote.mappers

import com.mercadopago.sdk.android.coremethods.data.remote.response.AgreementsResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.InstallmentsResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.IssuerResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.MerchantAccountResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.PayerCostResponse
import com.mercadopago.sdk.android.coremethods.data.remote.response.TimeFrameResponse
import org.junit.Assert.assertEquals
import kotlin.test.Test

internal class InstallmentsMapperTest {
    @Test
    fun `InstallmentsResponse toModel should convert correctly`() {
        val installmentsResponse = InstallmentsResponse(
            paymentMethodId = "payment_method_id",
            paymentTypeId = "payment_type_id",
            issuer = IssuerResponse("issuer_id", "http://image.png"),
            processingMode = "processing_mode",
            merchantAccountId = "merchant_account_id",
            payerCost = listOf(
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
            ),
            agreements = listOf(
                AgreementsResponse(
                    merchantAccount = listOf(MerchantAccountResponse("merchant_id", "option_id_2")),
                    timeFrame = TimeFrameResponse("2023-01-01", "2023-12-31"),
                ),
            ),
        )

        val model = installmentsResponse.toModel()

        assertEquals(installmentsResponse.paymentMethodId, model.paymentMethodId)
        assertEquals(installmentsResponse.paymentTypeId, model.paymentTypeId)
        assertEquals(installmentsResponse.issuer?.id, model.issuer?.id)
        assertEquals(installmentsResponse.issuer?.thumbnail, model.issuer?.thumbnail)
        assertEquals(installmentsResponse.processingMode, model.processingMode)
        assertEquals(installmentsResponse.merchantAccountId, model.merchantAccountId)

        assertEquals(1, model.payerCost?.size)
        val payerCostModel = model.payerCost?.first()
        assertEquals(3, payerCostModel?.instalments)
        assertEquals(1000, payerCostModel?.installmentAmount)
        assertEquals(1.5f, payerCostModel?.instalmentsRate)
        assertEquals(listOf("Collective 1"), payerCostModel?.installmentRateCollector)
        assertEquals(3000f, payerCostModel?.totalAmount)
        assertEquals(500f, payerCostModel?.minAllowedAmount)
        assertEquals(3000f, payerCostModel?.maxAllowedAmount)
        assertEquals(0.0f, payerCostModel?.discountRate)
        assertEquals(0.0f, payerCostModel?.reimbursementRate)
        assertEquals(listOf("Label 1"), payerCostModel?.labels)
        assertEquals("option_id_1", payerCostModel?.paymentMethodOptionId)

        assertEquals(1, model.agreements?.size)
        val agreementsModel = model.agreements?.first()
        assertEquals(1, agreementsModel?.merchantAccount?.size)
        assertEquals("merchant_id", agreementsModel?.merchantAccount?.first()?.id)
        assertEquals("option_id_2", agreementsModel?.merchantAccount?.first()?.paymentMethodOptionId)
        assertEquals("2023-01-01", agreementsModel?.timeFrame?.startDate)
        assertEquals("2023-12-31", agreementsModel?.timeFrame?.endDate)
    }

    @Test
    fun `PayerCostResponse toModel should convert correctly`() {
        val payerCostResponse = PayerCostResponse(
            instalments = 3,
            installmentAmount = 1000,
            instalmentsRate = 2.0f,
            installmentRateCollector = listOf("Collector A"),
            totalAmount = 3000f,
            minAllowedAmount = 500f,
            maxAllowedAmount = 5000f,
            discountRate = 0.0f,
            reimbursementRate = 0.0f,
            labels = listOf("Label A"),
            paymentMethodOptionId = "option_id_1",
        )

        val model = payerCostResponse.toModel()

        assertEquals(payerCostResponse.instalments, model.instalments)
        assertEquals(payerCostResponse.installmentAmount, model.installmentAmount)
        assertEquals(payerCostResponse.instalmentsRate, model.instalmentsRate)
        assertEquals(payerCostResponse.installmentRateCollector, model.installmentRateCollector)
        assertEquals(payerCostResponse.totalAmount, model.totalAmount)
        assertEquals(payerCostResponse.minAllowedAmount, model.minAllowedAmount)
        assertEquals(payerCostResponse.maxAllowedAmount, model.maxAllowedAmount)
        assertEquals(payerCostResponse.discountRate, model.discountRate)
        assertEquals(payerCostResponse.reimbursementRate, model.reimbursementRate)
        assertEquals(payerCostResponse.labels, model.labels)
        assertEquals(payerCostResponse.paymentMethodOptionId, model.paymentMethodOptionId)
    }

    @Test
    fun `AgreementsResponse toModel should convert correctly`() {
        val agreementsResponse = AgreementsResponse(
            merchantAccount = listOf(MerchantAccountResponse("merchant_id", "option_id_1")),
            timeFrame = TimeFrameResponse("2023-01-01", "2023-12-31"),
        )

        val model = agreementsResponse.toModel()

        assertEquals(1, model.merchantAccount?.size)
        assertEquals("merchant_id", model.merchantAccount?.first()?.id)
        assertEquals("option_id_1", model.merchantAccount?.first()?.paymentMethodOptionId)
        assertEquals(agreementsResponse.timeFrame?.startDate, model.timeFrame?.startDate)
        assertEquals(agreementsResponse.timeFrame?.endDate, model.timeFrame?.endDate)
    }
}
