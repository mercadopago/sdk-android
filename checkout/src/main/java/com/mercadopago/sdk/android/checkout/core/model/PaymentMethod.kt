package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * PaymentMethod class, used to determine the payment method
 * This its used to change the payment method showed in checkout
 */
sealed class PaymentMethod : Parcelable {
    /**
     * Card payment method
     * @param cardTypes List of card types
     * @param installment Installment
     */
    @Parcelize
    data class Card(
        val cardTypes: List<CardType> = listOf(CardType.CREDIT, CardType.DEBIT, CardType.PREPAID),
        val installment: Installment? = Installment(),
    ) : PaymentMethod()

    /**
     * Pix payment method
     */
    @Parcelize
    object Pix : PaymentMethod()

    /**
     * Boleto payment method
     */
    @Parcelize
    object Boleto : PaymentMethod()

    /**
     * Loan payment method
     * @param installment Installment
     */
    @Parcelize
    data class Loan(
        val installment: Installment,
    ) : PaymentMethod()

    /**
     * Default payment methods
     */
    companion object {
        /**
         * Default payment methods
         */
        val defaults: List<PaymentMethod> = listOf(Card(), Pix, Boleto)
    }
}
