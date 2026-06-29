package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * MPPaymentMethod class, used to determine the payment method
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
        val excludedPaymentTypes: List<MPCardType> = emptyList(),
        val excludedPaymentMethods: List<MPCardBrand> = emptyList(),
        val installment: MPInstallment? = MPInstallment(),
    ) : MPPaymentMethodConfig()

    /**
     * Default payment methods
     */
    companion object {
        /**
         * Default payment methods
         */
        val defaults: List<MPPaymentMethodConfig> = listOf(Card())
    }
}
