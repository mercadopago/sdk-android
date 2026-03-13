package com.mercadopago.sdk.android.checkout.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Defines the card brand/network used in the checkout flow.
 *
 * This value represents the card issuer network and may affect validation rules,
 * available payment options, and card number formatting during the checkout.
 */
sealed class CardBrand : Parcelable {
    /**
     * The name identifier of the card brand.
     */
    abstract val name: String

    /**
     * VISA: Visa card network.
     */
    @Parcelize
    data object Visa : CardBrand() {
        override val name: String get() = VISA
    }

    /**
     * MASTERCARD: Mastercard network.
     */
    @Parcelize
    data object Mastercard : CardBrand() {
        override val name: String get() = MASTERCARD
    }

    /**
     * AMEX: American Express card network.
     */
    @Parcelize
    data object Amex : CardBrand() {
        override val name: String get() = AMEX
    }

    /**
     * ELO: Elo card network (Brazil).
     */
    @Parcelize
    data object Elo : CardBrand() {
        override val name: String get() = ELO
    }

    /**
     * HIPERCARD: Hipercard network (Brazil).
     */
    @Parcelize
    data object Hipercard : CardBrand() {
        override val name: String get() = HIPERCARD
    }

    /**
     * DINERS: Diners Club card network.
     */
    @Parcelize
    data object Diners : CardBrand() {
        override val name: String get() = DINERS
    }

    /**
     * DISCOVER: Discover card network.
     */
    @Parcelize
    data object Discover : CardBrand() {
        override val name: String get() = DISCOVER
    }

    /**
     * JCB: Japan Credit Bureau card network.
     */
    @Parcelize
    data object Jcb : CardBrand() {
        override val name: String get() = JCB
    }

    /**
     * MAESTRO: Maestro debit card network.
     */
    @Parcelize
    data object Maestro : CardBrand() {
        override val name: String get() = MAESTRO
    }

    /**
     * UNIONPAY: UnionPay card network (China).
     */
    @Parcelize
    data object UnionPay : CardBrand() {
        override val name: String get() = UNIONPAY
    }

    /**
     * CABAL: Cabal card network (Argentina).
     */
    @Parcelize
    data object Cabal : CardBrand() {
        override val name: String get() = CABAL
    }

    /**
     * NARANJA: Naranja card network (Argentina).
     */
    @Parcelize
    data object Naranja : CardBrand() {
        override val name: String get() = NARANJA
    }

    /**
     * Custom: Custom card brand not explicitly listed.
     * Use this to create instances for new or unlisted card networks.
     *
     * @param name The name identifier of the custom card brand
     */
    @Parcelize
    data class Custom(override val name: String) : CardBrand()

    /**
     * Companion object containing string constants and utility values for card brands.
     */
    companion object {
        private const val VISA = "visa"
        private const val MASTERCARD = "master"
        private const val AMEX = "amex"
        private const val ELO = "elo"
        private const val HIPERCARD = "hipercard"
        private const val DINERS = "diners"
        private const val DISCOVER = "discover"
        private const val JCB = "jcb"
        private const val MAESTRO = "maestro"
        private const val UNIONPAY = "unionpay"
        private const val CABAL = "cabal"
        private const val NARANJA = "naranja"

        /**
         * All predefined card brands.
         */
        val default: List<CardBrand> = listOf(
            Visa,
            Mastercard,
            Amex,
            Elo,
            Hipercard,
            Diners,
            Discover,
            Jcb,
            Maestro,
            UnionPay,
            Cabal,
            Naranja,
        )
    }
}
