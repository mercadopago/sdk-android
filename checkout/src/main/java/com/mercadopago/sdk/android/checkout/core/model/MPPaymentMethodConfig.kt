package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * PaymentMethod class, used to determine the payment method
 * This its used to change the payment method showed in checkout
 */
sealed class MPPaymentMethodConfig : Parcelable {
    /**
     * Card payment method
     * @param excludedPaymentTypes List of excluded card types
     * @param excludedPaymentMethods List of excluded card brands
     * @param installment Installment
     */
    @Parcelize
    data class Card(
        val excludedPaymentTypes: List<CardType> = emptyList(),
        val excludedPaymentMethods: List<CardBrand> = emptyList(),
        val installment: Installment? = Installment(),
    ) : MPPaymentMethodConfig()

    /**
     * Pix payment method
     */
    @Parcelize
    internal object Pix : MPPaymentMethodConfig()

    /**
     * Boleto payment method
     */
    @Parcelize
    internal object Boleto : MPPaymentMethodConfig()

    /**
     * Loan payment method
     * @param installment Installment
     */
    @Parcelize
    data class Loan(
        val installment: Installment,
    ) : MPPaymentMethodConfig()

    /**
     * Default payment methods
     */
    companion object {
        /**
         * Default payment methods
         */
        val defaults: List<MPPaymentMethodConfig> = listOf(Card(), Pix, Boleto)
    }
}
