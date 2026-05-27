package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * MPPaymentMethod class, used to determine the payment method
 * This its used to change the payment method showed in checkout
 */
sealed class MPPaymentMethod : Parcelable {
    /**
     * Card payment method
     * @param allowedTypes List of allowed card types
     * @param allowedBrands List of allowed card brands
     * @param installment MPInstallment
     */
    @Parcelize
    data class Card(
        val allowedTypes: List<MPCardType> = listOf(MPCardType.CREDIT, MPCardType.DEBIT, MPCardType.PREPAID),
        val allowedBrands: List<MPCardBrand> = MPCardBrand.default,
        val installment: MPInstallment? = MPInstallment(),
    ) : MPPaymentMethod()

    /**
     * Pix payment method
     */
    @Parcelize
    internal object Pix : MPPaymentMethod()

    /**
     * Boleto payment method
     */
    @Parcelize
    internal object Boleto : MPPaymentMethod()

    /**
     * Loan payment method
     * @param installment MPInstallment
     */
    @Parcelize
    data class Loan(
        val installment: MPInstallment,
    ) : MPPaymentMethod()

    /**
     * Default payment methods
     */
    companion object {
        /**
         * Default payment methods
         */
        val defaults: List<MPPaymentMethod> = listOf(Card(), Pix, Boleto)
    }
}
