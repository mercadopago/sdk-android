package com.mercadopago.sdk.android.checkout.statusscreen.domain.model

import com.mercadopago.sdk.android.checkout.domain.model.OrderProcessOutput
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

private const val MIN_INSTALLMENTS_SHOWN = 2
private const val INSTALLMENT_SCALE = 2
private const val DEBIT_CARD_TYPE = "debit_card"
private val INTEREST_FREE_TOLERANCE = BigDecimal("0.005")

internal data class StatusScreenFallbackInstallments(
    val count: Int,
    val perInstallment: BigDecimal,
    val interestFree: Boolean,
)

internal fun OrderProcessOutput.PaymentProcessed.paidAmount() = totalPaidAmount ?: amount ?: orderTotalAmount

internal fun OrderProcessOutput.PaymentProcessed.fallbackInstallments(): StatusScreenFallbackInstallments? {
    val count = paymentMethod?.installments?.takeIf { it >= MIN_INSTALLMENTS_SHOWN }
    val paid = totalPaidAmount
    return if (count != null && paid != null && paymentMethod?.type != DEBIT_CARD_TYPE) {
        StatusScreenFallbackInstallments(
            count = count,
            perInstallment = paid.divide(BigDecimal(count), INSTALLMENT_SCALE, RoundingMode.HALF_UP),
            interestFree = orderTotalAmount?.let { paid <= it + INTEREST_FREE_TOLERANCE } ?: false,
        )
    } else {
        null
    }
}

internal fun OrderProcessOutput.PaymentProcessed.ticketAmount() = amount ?: orderTotalAmount

private val METHOD_DISPLAY_NAMES = mapOf(
    "master" to "Mastercard",
    "amex" to "American Express",
    "diners" to "Diners Club",
    "jcb" to "JCB",
    "unionpay" to "UnionPay",
)

internal fun OrderProcessOutput.PaymentProcessed.methodDisplayName(): String? =
    paymentMethod?.id?.takeIf { it.isNotBlank() }?.let { id ->
        METHOD_DISPLAY_NAMES[id.lowercase(Locale.ROOT)]
            ?: id.replace('_', ' ').replaceFirstChar { it.titlecase(Locale.ROOT) }
    }
